package fgh.weixin.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;

import fgh.common.util.FastJsonConvert;
import fgh.common.util.PropertyUtil;
import fgh.common.util.RedisUtil;
import fgh.weixin.pojo.Token;

/**
 * 微信服务号API工具类
 * 
 * @author fgh
 * @since 2016年8月24日下午4:46:50
 */
public class MpWeixinApiUtil {

	private static Logger logger = LoggerFactory.getLogger(MpWeixinApiUtil.class);

	// 公众号凭证获取（GET）
	public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/**
	 * 获取接口访问凭证
	 * 
	 * @return
	 */
	public static String getToken() {
		// redis处理
		String tokenValue = RedisUtil.get(Constant.REDIS_MP_TOKEN_KEY);
		if (StringUtils.isBlank(tokenValue)) {
			Token token = null;
			String requestUrl = token_url.replace("APPID", PropertyUtil.getWeixinConfig("mp_APPID"))
					.replace("APPSECRET", PropertyUtil.getWeixinConfig("mp_APPSECRET"));
			// 发起GET请求获取凭证
			String resp = HttpClientUtil.httpsRequest(requestUrl, Constant.requestMethod.GET, null);
			token = FastJsonConvert.convertJSONToObject(resp, Token.class);
			if (null != token) {
				try {
					logger.info(Constant.LOG_MAIN_WEIXIN + "调用api,获取token[" + token.getAccessToken() + "]");
					RedisUtil.setExSecond(Constant.REDIS_MP_TOKEN_KEY, token.getAccessToken(),
							token.getExpiresIn() - 200);
					tokenValue = token.getAccessToken();
				} catch (JSONException e) {
					token = null;
					logger.error(Constant.LOG_MAIN_WEIXIN + "调用api,获取token失败, errcode:{} errmsg:{}", e);
				}
			}
		} else {
			logger.info(Constant.LOG_MAIN_WEIXIN + "从redis缓存获取token[" + tokenValue + "]");
		}
		return tokenValue;
	}

}
