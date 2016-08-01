package fgh.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fgh.weixin.util.WeixinApiUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis工具类
 * 
 * @author fgh
 * @Since 2016-7-22 下午3:16:46
 */
public class RedisUtil {
	private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	// 定义连接池
	public static JedisPool pool = null;

	private static final Properties redisProp = new Properties();
	
	private static final String LOG_MAIN="【redis】";

	static {
		logger.info(LOG_MAIN+"load redis.properties...");
		InputStream fis = WeixinApiUtil.class.getClassLoader().getResourceAsStream("redis.properties");
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
	public static synchronized JedisPool getJedisPool() {
		if (pool == null) {
			logger.info(LOG_MAIN+"getJedisPool...");
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
			pool = new JedisPool(config, getRedisHost(), getRedisPort());// 创建连接池
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
		logger.info(LOG_MAIN+"redis returnResource...");
		if (redis != null) {
			redis.close();
			pool.close();
		}
	}

	/**
	 * 获取redis实例
	 * @return
	 */
	public static Jedis getJedis() {
		JedisPool pool = getJedisPool();
		if(logger.isInfoEnabled()){
			logger.info(LOG_MAIN+"getJedis,NumActive["+pool.getNumActive()+"],NumIdle["+pool.getNumIdle()+"],NumWaiters["+pool.getNumWaiters()+"]");
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
		logger.info(LOG_MAIN+"redis get,key["+key+"]");
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			logger.error(LOG_MAIN+"redis get,key["+key+"] error",e);
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
		logger.info(LOG_MAIN+"redis set,key["+key+"],value["+value+"]");
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getJedis();
			result = jedis.set(key, value);
		}catch (Exception e) {
			logger.error(LOG_MAIN+"redis set,key["+key+"],value["+value+"] error",e);
			returnResource(jedis);
		}  finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置超时时间
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public static long expire(String key, int seconds) {
		logger.info(LOG_MAIN+"redis expire,key["+key+"],value["+seconds+"]");
		Jedis jedis = null;
		long result = 0L;
		try {
			jedis = getJedis();
			jedis.expire(key, seconds);
		}catch (Exception e) {
			logger.info(LOG_MAIN+"redis expire,key["+key+"],value["+seconds+"] error",e);
			returnResource(jedis);
		}  finally {
			returnResource(jedis);
		}
		return result;
	}

	private static String getRedisHost() {
		return redisProp.getProperty("redis.host");
	}

	private static int getRedisPort() {
		return Integer.valueOf(redisProp.getProperty("redis.port", "6379"));
	}

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
}
