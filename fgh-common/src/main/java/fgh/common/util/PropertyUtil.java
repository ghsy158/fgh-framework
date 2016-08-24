package fgh.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取配置文件工具类
 * 
 * @author fgh
 * @since 2016年8月24日下午5:00:52
 */
public class PropertyUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
	
	private static final Properties weixinProp = new Properties();
	
	static {
		InputStream fis = PropertyUtil.class.getClassLoader().getResourceAsStream("weixin.properties");
		try {
			weixinProp.load(fis);
		} catch (IOException e) {
			logger.error("读取微信的配置文件失败", e);
		}
	}

	/**
	 * 获取微信配置
	 * 
	 * @param key
	 * @return
	 */
	public static String getWeixinConfig(String key) {
		return weixinProp.getProperty(key);
	}
}
