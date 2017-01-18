package fgh.weixin.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;

import fgh.common.util.FastJsonConvert;
import fgh.common.util.PropertyUtil;
import fgh.common.util.RedisUtil;
import fgh.weixin.message.req.TemplateMsg;
import fgh.weixin.message.resp.OauthAccessToken;
import fgh.weixin.message.resp.TemplateRespMsg;
import fgh.weixin.pojo.JsApiTicket;
import fgh.weixin.pojo.KfOnlineList;
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

	/** 发送模板消息 url **/
	public final static String send_template_msg_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

	/** 通过code换取网页授权access_token url **/
	public static final String oauth2_access_token = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	/** 获取jsapi_ticket **/
	public static final String GET_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	//客服接口stat
	public static final String SERVICE_ONLINE_KF_LIST="https://api.weixin.qq.com/cgi-bin/customservice/getonlinekflist?access_token=ACCESS_TOKEN";
	//客服接口end
	
	
	public static final String APPID = PropertyUtil.getWeixinConfig("mp_APPID");

	public static final String SECRET = PropertyUtil.getWeixinConfig("mp_APPSECRET");

	/**
	 * 获取接口访问凭证
	 * 
	 * @return
	 */
	public static String getToken() {
		// redis处理
		String tokenValue = RedisUtil.get(WeixinConstant.REDIS_MP_TOKEN_KEY);
		if (StringUtils.isBlank(tokenValue)) {
			Token token = null;
			String requestUrl = token_url.replace("APPID", PropertyUtil.getWeixinConfig("mp_APPID"))
					.replace("APPSECRET", PropertyUtil.getWeixinConfig("mp_APPSECRET"));
			// 发起GET请求获取凭证
			String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
			token = FastJsonConvert.convertJSONToObject(resp, Token.class);
			if (null != token) {
				try {
					logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取token[" + token.getAccessToken() + "]");
					RedisUtil.setExSecond(WeixinConstant.REDIS_MP_TOKEN_KEY, token.getAccessToken(),
							token.getExpiresIn() - 200);
					tokenValue = token.getAccessToken();
				} catch (JSONException e) {
					token = null;
					logger.error(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取token失败, errcode:{} errmsg:{}", e);
				}
			}
		} else {
			logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "从redis缓存获取token[" + tokenValue + "]");
		}
		return tokenValue;
	}

	/**
	 * 发送模板消息
	 * 
	 * @param msg
	 * @return
	 */
	public static TemplateRespMsg sendTemplateMsg(TemplateMsg msg) {
		String requestUrl = send_template_msg_url.replace("ACCESS_TOKEN", getToken());
		String data = FastJsonConvert.convertObjectToJSON(msg);
		logger.info("发送模板消息:" + data);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, data);
		TemplateRespMsg respMsg = FastJsonConvert.convertJSONToObject(resp, TemplateRespMsg.class);
		return respMsg;
	}

	/**
	 * 通过code换取网页授权access_token
	 * 
	 * @param code
	 */
	public static OauthAccessToken getOauthAccessToken(String code) {
		logger.info("获取oauth2的access token,code[" + code + "]");
		String requestUrl = oauth2_access_token.replace("APPID", APPID).replace("SECRET", SECRET).replace("CODE", code);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		OauthAccessToken respMsg = FastJsonConvert.convertJSONToObject(resp, OauthAccessToken.class);
		return respMsg;
	}

	/**
	 * 获取jsapi_ticket
	 * 
	 * @return
	 */
	public static String getJsApiTicket() {
		// redis获取
		String ticket = RedisUtil.get(WeixinConstant.REDIS_MP_JSAPI_TICKET_KEY);
		if (StringUtils.isBlank(ticket)) {
			String requestUrl = GET_JSAPI_TICKET.replace("ACCESS_TOKEN", getToken());
			String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
			JsApiTicket jsApiTicket = FastJsonConvert.convertJSONToObject(resp, JsApiTicket.class);

			if (null != jsApiTicket) {
				try {
					logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取服务号jsapi_ticket[" + jsApiTicket.getTicket() + "]");
					if (null != jsApiTicket && WeixinConstant.SUCCESS_CODE.equals(jsApiTicket.getErrCode())) {
						RedisUtil.setExSecond(WeixinConstant.REDIS_MP_JSAPI_TICKET_KEY, jsApiTicket.getTicket(),
								jsApiTicket.getExpiresIn() - 200);
					} else {
						logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取服务号jsapi_ticket失败,errorCode["
								+ jsApiTicket.getErrCode() + "],errorMsg[" + jsApiTicket.getErrMsg() + "]");
					}
					ticket = jsApiTicket.getTicket();
				} catch (JSONException e) {
					ticket = null;
					logger.error(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取服务号jsapi_ticket失败, errcode:{} errmsg:{}", e);
				}
			}
		} else {
			logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "从redis缓存获取服务号jsapi_ticket[" + ticket + "]");
		}
		return ticket;

	}

	/**
	 * 获取在线客服列表
	 * @return KfOnlineList
	 */
	public static KfOnlineList getOnlineKfList() {
		logger.info("获取在线客服列表...");
		String requestUrl = SERVICE_ONLINE_KF_LIST.replace("ACCESS_TOKEN", getToken());
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		KfOnlineList respMsg = FastJsonConvert.convertJSONToObject(resp, KfOnlineList.class);
		return respMsg;
	}

	
	public static void main(String[] args) {
//		MpWeixinApiUtil.getOnlineKfList();
		String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=";
		String requestUrl = url.replace("ACCESS_TOKEN", getToken());
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		System.out.println(resp);
		
	}
}
