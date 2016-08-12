package fgh.weixin.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import fgh.common.util.FastJsonConvert;
import fgh.common.util.RedisUtil;
import fgh.weixin.pojo.AgentInfo;
import fgh.weixin.pojo.JsApiTicket;
import fgh.weixin.pojo.Token;
import fgh.weixin.pojo.UserInfo;

/**
 * 	企业微信API工具类
 * 
 * @author fgh
 * @since 2016年7月28日上午10:49:13
 */
public class QyWeixinApiUtil {

	private static Logger logger = LoggerFactory.getLogger(QyWeixinApiUtil.class);

	// 公众号凭证获取（GET）
	public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 企业api start
	/** 获取企业token **/
	public static final String QY_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=APP_CORPID&corpsecret=APP_SECRECT";

	/** 根据code获取成员信息 **/
	public static final String GET_USER_INFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";

	/** 获取企业号应用 **/
	public static final String GET_AGENT_INFO = "https://qyapi.weixin.qq.com/cgi-bin/agent/get?access_token=ACCESS_TOKEN&agentid=AGENTID";

	/** 获取jsapi_ticket **/
	public static final String GET_JSAPI_TICKET = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";

	// 企业api end

	//发送消息
	public static final String SEND_MSG = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
	
	//创建菜单
	public static final String CREATE_MENU="https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN&agentid=AGENTID";
	
	private static final Properties weixinProp = new Properties();
	
	static {
		InputStream fis = QyWeixinApiUtil.class.getClassLoader().getResourceAsStream("weixin.properties");
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
	public static String getQyCorpID() {
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
	 * 获取微信配置
	 * 
	 * @param key
	 * @return
	 */
	public static String getWeixinConfig(String key) {
		return weixinProp.getProperty(key);
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
		String requestUrl = token_url.replace("APPID", appid).replace("APPSECRET", appsecret);
		// 发起GET请求获取凭证
		String resp = HttpClientUtil.httpsRequest(requestUrl, "GET", null);
		return FastJsonConvert.convertJSONToObject(resp, Token.class);
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
			String resp = HttpClientUtil.httpsRequest(requestUrl, Constant.requestMethod.GET, null);
			token = FastJsonConvert.convertJSONToObject(resp, Token.class);
			if (null != token) {
				try {
					logger.info("调用api,获取企业AccessToken[" + token.getAccessToken() + "]");
					setRedisCorpToken(token);
					tokenValue = token.getAccessToken();
				} catch (JSONException e) {
					token = null;
					logger.error("调用api,获取企业token失败, errcode:{} errmsg:{}", e);
				}
			}
		} else {
			logger.info("从redis缓存获取企业AccessToken[" + tokenValue + "]");
		}
		return tokenValue;

	}

	/**
	 * 企业API<br>
	 * 根据code获取成员信息
	 * 
	 * @param code
	 */
	public static UserInfo getUserInfo(String code) {
		String requestUrl = GET_USER_INFO_URL.replace("ACCESS_TOKEN", getQyToken()).replace("CODE", code);
		String result = HttpClientUtil.httpsRequest(requestUrl, Constant.requestMethod.GET, null);
		UserInfo userInfo = FastJsonConvert.convertJSONToObject(result, UserInfo.class);
		logger.info("微信oauth获取用户信息code[" + code + "],userInfo[" + userInfo + "]");
		return userInfo;
	}

	/**
	 * 企业api<br>
	 * 获取企业号应用
	 * 
	 * @param agentId
	 *            应用ID
	 */
	public static AgentInfo getAgentfo(String agentId) {
		String requestUrl = GET_AGENT_INFO.replace("ACCESS_TOKEN", getQyToken()).replace("AGENTID", agentId);
		String resp = HttpClientUtil.httpsRequest(requestUrl, Constant.requestMethod.GET, null);
		AgentInfo agentInfo = FastJsonConvert.convertJSONToObject(resp, AgentInfo.class);
		return agentInfo;
	}

	/**
	 * 设置redis缓存
	 * 
	 * @param token
	 */
	public static void setRedisCorpToken(Token token) {
		logger.info("setRedisCorpToken...");
		RedisUtil.setExSecond(Constant.REDIS_CORP_TOKEN_KEY, token.getAccessToken(), token.getExpiresIn() - 200);
	}

	/**
	 * 获取企业jsapi_ticket
	 * 
	 * @return
	 */
	public static String getQyJsApiTicket() {
		// redis获取
		String ticket = RedisUtil.get(Constant.REDIS_CORP_JSAPI_TICKET_KEY);
		if (StringUtils.isBlank(ticket)) {
			String requestUrl = GET_JSAPI_TICKET.replace("ACCESS_TOKEN", getQyToken());
			String resp = HttpClientUtil.httpsRequest(requestUrl, Constant.requestMethod.GET, null);
			JsApiTicket jsApiTicket = FastJsonConvert.convertJSONToObject(resp, JsApiTicket.class);

			if (null != jsApiTicket) {
				try {
					logger.info("调用api,获取企业jsapi_ticket[" + jsApiTicket.getTicket() + "]");
					if (null != jsApiTicket && Constant.ERROR_CODE_OK.equals(jsApiTicket.getErrCode())) {
						RedisUtil.set(Constant.REDIS_CORP_JSAPI_TICKET_KEY, jsApiTicket.getTicket());
						RedisUtil.expire(Constant.REDIS_CORP_JSAPI_TICKET_KEY, jsApiTicket.getExpiresIn() - 200);
					}else{
						logger.info("调用api,获取企业jsapi_ticket失败,errorCode["+jsApiTicket.getErrCode()+"],errorMsg["+jsApiTicket.getErrMsg()+"]");
					}
					ticket = jsApiTicket.getTicket();
				} catch (JSONException e) {
					ticket = null;
					logger.error("调用api,获取企业jsapi_ticket失败, errcode:{} errmsg:{}", e);
				}
			}
		} else {
			logger.info("从redis缓存获取企业jsapi_ticket[" + ticket + "]");
		}
		return ticket;

	}

	public static void createMenu(){
		
	}
	
	public static void main(String[] args) {
		// Token token = null;
		// String requestUrl = getQyTokenUrl();
		// // 发起GET请求获取凭证
		// String resp = HttpClientUtil.httpsRequest(requestUrl,
		// Constant.requestMethod.GET, null);
		// token = FastJsonConvert.convertJSONToObject(resp, Token.class);
		// System.out.println(token);
		// getQyToken();
		// getAgentfo("14");
		// getUserInfo("223234");
//		getQyJsApiTicket();
//		SignUtil.getCorpJsTicketSign("http://localhost:8088/hx-sales-web/");
		JSONObject json = new JSONObject();
		json.put("touser", "2615");
		json.put("msgtype", "text");
		json.put("agentid", "11");
		json.put("safe", "1");
		
		JSONObject content = new JSONObject();
		content.put("content", "主页型发送通知测试-保密");
		
		json.put("text", content);
		String requestUrl = SEND_MSG.replace("ACCESS_TOKEN", getQyToken());
		System.out.println("发消息");
		String resp = HttpClientUtil.httpsRequest(requestUrl, Constant.requestMethod.POST, json.toJSONString());
		System.out.println(resp);
	}
}