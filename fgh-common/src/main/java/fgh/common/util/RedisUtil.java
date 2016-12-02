package fgh.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * Redis工具类
 * 
 * @author fgh
 * @Since 2016-7-22 下午3:16:46
 */
public class RedisUtil {
	private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	// 定义连接池
	public static JedisSentinelPool pool = null;

	private static final Properties redisProp = new Properties();

	private static final String LOG_MAIN = "【redis】";

	static {
		logger.info(LOG_MAIN + "load redis.properties...");
		InputStream fis = RedisUtil.class.getClassLoader().getResourceAsStream("redis.properties");
		try {
			redisProp.load(fis);
		} catch (IOException e) {
			logger.error("读取redis配置文件失败", e);
		}
	}

	/**
	 * 获取链接资源
	 * 
	 * @return
	 */
	public static synchronized JedisSentinelPool getJedisPool() {
		if (pool == null || pool.isClosed()) {
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxTotal(getMaxTotal());
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(getMaxIdle());
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(getMaxWaitMillis());
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(getTestOnBorrow());
			// pool = new JedisPool(config, getRedisHost(), getRedisPort());//
			// 创建连接池

			Set<String> sentinel = new HashSet<String>();
			String sentinelHost = redisProp.getProperty("redis.sentinel.host");
			String masterName = redisProp.getProperty("redis.sentinel.masterName");
			logger.info(
					LOG_MAIN + "getJedisPool,redis.sentinel.host【" + sentinelHost + "】,masterName【" + masterName + "】");
			sentinel.add(sentinelHost);
			pool = new JedisSentinelPool(masterName, sentinel, config);
		}

		return pool;
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public static void returnResource(Jedis redis) {
		logger.info(LOG_MAIN + "redis returnResource...");
		if (redis != null) {
			redis.close();
		}
	}

	/**
	 * 获取redis实例
	 * 
	 * @return
	 */
	public static Jedis getJedis() {
		JedisSentinelPool pool = getJedisPool();
		if (logger.isInfoEnabled()) {
			logger.info(LOG_MAIN + "getJedis,currentHostMaster[" + pool.getCurrentHostMaster() + "],NumActive["
					+ pool.getNumActive() + "],NumIdle[" + pool.getNumIdle() + "],NumWaiters[" + pool.getNumWaiters()
					+ "]");
		}
		return pool.getResource();
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		logger.info(LOG_MAIN + "redis get,key[" + key + "]");
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			logger.error(LOG_MAIN + "redis get,key[" + key + "] error", e);
			returnResource(jedis);
		} finally {
			// 返还到连接池
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 
	 * 为指定的key赋值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String set(String key, String value) {
		logger.info(LOG_MAIN + "redis set,key[" + key + "],value[" + value + "]");
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.set(key, value);
		} catch (Exception e) {
			logger.error(LOG_MAIN + "redis set,key[" + key + "],value[" + value + "] error", e);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置key并设置超时时间秒数
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            超时秒数
	 * @return
	 */
	public static String setExSecond(String key, String value, int seconds) {
		logger.info(LOG_MAIN + "redis setExSecond,key[" + key + "],value[" + value + "],expire[" + seconds + "]");
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.setex(key, seconds, value);
		} catch (Exception e) {
			logger.error(LOG_MAIN + "redis setExSecond error ,key[" + key + "],value[" + value + "],expire[" + seconds
					+ "]");
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置key并设置超时时间 小时
	 * 
	 * @param key
	 * @param value
	 * @param hour
	 *            小时数
	 * @return
	 */
	public static String setExHour(String key, String value, int hour) {
		int seconds = hour * 60 * 60;
		return setExSecond(key, value, seconds);
	}

	/**
	 * 设置超时时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public static long expire(String key, int seconds) {
		logger.info(LOG_MAIN + "redis expire,key[" + key + "],value[" + seconds + "]");
		Jedis jedis = null;
		long result = 0L;
		try {
			jedis = getJedis();
			jedis.expire(key, seconds);
		} catch (Exception e) {
			logger.error(LOG_MAIN + "redis expire error,key[" + key + "],value[" + seconds + "]", e);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置set集合，并设置超时时间,如果不超时,senonds传-1
	 * 
	 * @param key
	 * @param seconds
	 *            超时时间,如果不超时,设置为-1
	 * @param members
	 * @return
	 */
	public static boolean addSet(final String key, int seconds, final String... members) {
		logger.info(LOG_MAIN + "redis addSet,key[" + key + "],members[" + members + "],seconds[" + seconds + "]");
		Jedis jedis = null;
		boolean result = false;
		try {
			jedis = getJedis();
			long addCount = jedis.sadd(key, members);
			long exCount = 0;
			// 如果超时时间>0，设置超时时间，否则不设置
			if (seconds > 0) {
				exCount = jedis.expire(key, seconds);
			}
			if (addCount > 0 && exCount > 0) {
				result = true;
			}
		} catch (Exception e) {
			logger.error(
					LOG_MAIN + "redis addSet error,key[" + key + "],members[" + members + "],seconds[" + seconds + "]",
					e);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取set集合
	 * 
	 * @param key
	 * @return
	 */
	public static Set<String> getSet(final String key) {
		logger.info(LOG_MAIN + "redis getSet,key[" + key + "]");
		Jedis jedis = null;
		Set<String> result = null;
		try {
			jedis = getJedis();
			result = jedis.smembers(key);
		} catch (Exception e) {
			logger.error(LOG_MAIN + "redis getSet error,key[" + key + "]", e);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 添加hash set
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return long
	 */
	public static long addHashSet(String key, String field, String value) {
		logger.info(LOG_MAIN + "redis addHashSet,key:" + key + ",field:" + field + ",value:" + value);
		Jedis jedis = null;
		long result = 0L;
		try {
			jedis = getJedis();
			result = jedis.hset(key, field, value);
		} catch (Exception e) {
			logger.error(LOG_MAIN + "redis addHashSet error,key:" + key + ",field:" + field + ",value:" + value);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 通过 key field 获取 Hash set
	 * 
	 * @param key
	 * @param field
	 * @return String
	 */
	public static String getHashSetByKey(String key, String field) {
		logger.info(LOG_MAIN + "redis getHashSetByKey,key:" + key + ",field:" + field);
		if (StringUtils.isBlank(key) || StringUtils.isBlank(field)) {
			return null;
		}
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.hget(key, field);
		} catch (Exception e) {
			logger.error(LOG_MAIN + "redis getHashSetByKey error,key:" + key + ",field:" + field);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取hash set 全部数据
	 * 
	 * @param key
	 * @return Map<String, String>
	 */
	public static Map<String, String> getHashSetAll(String key) {
		logger.info(LOG_MAIN + "redis getHashSetAll,key:" + key);
		Jedis jedis = null;
		Map<String, String> result = null;
		try {
			jedis = getJedis();
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			logger.error(LOG_MAIN + "redis getHashSetAll error,key:" + key);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static List<String> getHmList(String key,String field) {
		logger.info(LOG_MAIN + "redis getHmList,key=" + key+",field="+field);
		Jedis jedis = null;
		List<String> result = null;
		try {
			jedis = getJedis();
			result = jedis.hmget(key, field);
		} catch (Exception e) {
			logger.error(LOG_MAIN + "redis getHmList,key=" + key+",field="+field);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 通过key 删除hash set
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public static long delHashSetByKey(String key, String field) {
		logger.info(LOG_MAIN + "redis delHashSetByKey,key:" + key + ",field:" + field);
		Jedis jedis = null;
		long result = 0L;
		try {
			jedis = getJedis();
			result = jedis.hdel(key, field);
		} catch (Exception e) {
			logger.error(LOG_MAIN + "redis delHashSetByKey error,key:" + key + ",field:" + field);
			returnResource(jedis);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	// private static String getRedisHost() {
	// return redisProp.getProperty("redis.host");
	// }
	//
	// private static int getRedisPort() {
	// return Integer.valueOf(redisProp.getProperty("redis.port", "6379"));
	// }

	private static int getMaxTotal() {
		return Integer.valueOf(redisProp.getProperty("redis.MaxTotal", "100"));
	}

	private static int getMaxIdle() {
		return Integer.valueOf(redisProp.getProperty("redis.MaxIdle", "10"));
	}

	private static int getMaxWaitMillis() {
		return Integer.valueOf(redisProp.getProperty("redis.MaxWaitMillis", "10000"));
	}

	private static boolean getTestOnBorrow() {
		return Boolean.valueOf(redisProp.getProperty("redis.TestOnBorrow", "true"));
	}

	/**
	 * 释放链接资源
	 * 
	 * @param jedis
	 */
	public static void releaseJedis(Jedis jedis) {
		pool.close();
	}

	public static void main(String[] args) throws InterruptedException {
		// Set<String> sentinel = new HashSet<String>();
		// sentinel.add("172.16.88.169:26379");
		// JedisSentinelPool pool = new JedisSentinelPool("mymaster", sentinel);
		// HostAndPort currentMaster = pool.getCurrentHostMaster();
		// System.out.println(currentMaster);
		// pool.getResource();
		// pool.close();
		// Thread.sleep(110000000);

		RedisUtil.set("test8", "test8");

	}
}
