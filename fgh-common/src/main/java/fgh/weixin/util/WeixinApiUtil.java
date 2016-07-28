package fgh.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import fgh.common.util.RedisUtil;
import fgh.weixin.pojo.Token;

/**
 * 微信API工具类
 * 
 * @author fgh
 * @since 2016年7月28日上午10:49:13
 */
public class WeixinApiUtil {

	private static Logger logger = LoggerFactory.getLogger(WeixinApiUtil.class);

	// 公众号凭证获取（GET）
	public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	public static final String QY_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=APP_CORPID&corpsecret=APP_SECRECT";

	private static final Properties weixinProp = new Properties();

	static {
		InputStream fis = WeixinApiUtil.class.getClassLoader().getResourceAsStream("weixin.properties");
		try {
			weixinProp.load(fis);
		} catch (IOException e) {
			logger.error("读取微信的配置文件失败", e);
		}
	}

	/**
	 * 获取企业token 请求URL
	 * 
	 * @return
	 */
	private static String getQyTokenUrl() {
		return QY_TOKEN_URL.replace("APP_CORPID", getQyCorpID()).replace("APP_SECRECT", getQySecret());
	}

	/**
	 * QyCorpID
	 * 
	 * @return
	 */
	private static String getQyCorpID() {
		return weixinProp.getProperty("qy_CorpID");
	}

	/**
	 * QySecret
	 * 
	 * @return
	 */
	private static String getQySecret() {
		return weixinProp.getProperty("qy_Secret");
	}

	/**
	 * 获取接口访问凭证
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static Token getToken(String appid, String appsecret) {
		Token token = null;
		String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		// 发起GET请求获取凭证
		JSONObject jsonObject = HttpClientUtil.httpsRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			try {
				token = new Token();
				token.setAccessToken(jsonObject.getString("access_token"));
				token.setExpiresIn(jsonObject.getIntValue("expires_in"));
			} catch (JSONException e) {
				token = null;
				// 获取token失败
				logger.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"),
						jsonObject.getString("errmsg"));
			}
		}
		return token;
	}

	/**
	 * 获取企业token
	 * 
	 * @return
	 */
	public static String getQyToken() {
		// redis处理
		String tokenValue = RedisUtil.get(Constant.REDIS_CORP_TOKEN_KEY);
		if (StringUtils.isBlank(tokenValue)) {
			Token token = null;
			String requestUrl = getQyTokenUrl();
			// 发起GET请求获取凭证
			JSONObject jsonObject = HttpClientUtil.httpsRequest(requestUrl, Constant.requestMethod.GET, null);

			if (null != jsonObject) {
				try {
					token = new Token();
					token.setAccessToken(jsonObject.getString("access_token"));
					token.setExpiresIn(jsonObject.getIntValue("expires_in"));
					logger.info("获取企业AccessToken[" + token.getAccessToken() + "]");
					setRedisCorpToken(token);
					tokenValue = token.getAccessToken();
				} catch (JSONException e) {
					token = null;
					// 获取token失败
					logger.error("获取企业token失败 errcode:{} errmsg:{}", jsonObject.getIntValue("errcode"),
							jsonObject.getString("errmsg"));
				}
			}
		}
		return tokenValue;

	}

	/**
	 * 设置redis缓存
	 * 
	 * @param token
	 */
	public static void setRedisCorpToken(Token token) {
		if (null != token) {
			RedisUtil.set(Constant.REDIS_CORP_TOKEN_KEY, token.getAccessToken());
			RedisUtil.set(Constant.REDIS_CORP_TOKEN_EXPIRE_KEY, String.valueOf(token.getExpiresIn()));
			RedisUtil.expire(Constant.REDIS_CORP_TOKEN_KEY, token.getExpiresIn() - 200);
		}
	}
}