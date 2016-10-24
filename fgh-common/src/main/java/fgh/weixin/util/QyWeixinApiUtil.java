package fgh.weixin.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import fgh.common.util.FastJsonConvert;
import fgh.common.util.PropertyUtil;
import fgh.common.util.RedisUtil;
import fgh.weixin.message.qy.BaseMessage;
import fgh.weixin.message.qy.RespMsg;
import fgh.weixin.pojo.AgentInfo;
import fgh.weixin.pojo.JsApiTicket;
import fgh.weixin.pojo.TagUserInfo;
import fgh.weixin.pojo.Token;
import fgh.weixin.pojo.UserInfo;

/**
 * 企业微信API工具类
 * 
 * @author fgh
 * @since 2016年7月28日上午10:49:13
 */
public class QyWeixinApiUtil {

	private static Logger logger = LoggerFactory.getLogger(QyWeixinApiUtil.class);

	/** 获取企业token **/
	public static final String QY_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=APP_CORPID&corpsecret=APP_SECRECT";

	/** 根据code获取成员信息 **/
	public static final String GET_USER_INFO_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE";

	/** 获取企业号应用 **/
	public static final String GET_AGENT_INFO = "https://qyapi.weixin.qq.com/cgi-bin/agent/get?access_token=ACCESS_TOKEN&agentid=AGENTID";

	/** 获取jsapi_ticket **/
	public static final String GET_JSAPI_TICKET = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN";

	// 发送消息
	public static final String SEND_MSG = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";

	// 创建菜单
	public static final String CREATE_MENU = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN&agentid=AGENTID";

	// 通讯录管理 start
	/** 获取成员 URL **/
	public static final String USER_GET_BY_ID = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";

	/** 增加成员 URL **/
	public static final String USER_ADD_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=ACCESS_TOKEN";
	/** 更新成员 URL **/
	public static final String USER_UPDATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=ACCESS_TOKEN";
	/** 删除成员 URL **/
	public static final String USER_DELETE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=ACCESS_TOKEN&userid=USERID";

	// 通讯录管理 end

	// 标签管理 start
	public static final String TAG_GET_USERS = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token=ACCESS_TOKEN&tagid=TAGID";

	// 标签管理 end
	/**
	 * 获取企业token 请求URL
	 * 
	 * @return
	 */
	private static String getQyTokenUrl() {
		return QY_TOKEN_URL.replace("APP_CORPID", PropertyUtil.getWeixinConfig("qy_CorpID")).replace("APP_SECRECT",
				PropertyUtil.getWeixinConfig("qy_Secret"));
	}

	/**
	 * 获取企业token
	 * 
	 * @return
	 */
	public static String getQyToken() {
		// redis处理
		String tokenValue = RedisUtil.get(WeixinConstant.REDIS_CORP_TOKEN_KEY);
		if (StringUtils.isBlank(tokenValue)) {
			Token token = null;
			String requestUrl = getQyTokenUrl();
			// 发起GET请求获取凭证
			String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
			token = FastJsonConvert.convertJSONToObject(resp, Token.class);
			if (null != token) {
				try {
					logger.info(
							WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取企业AccessToken[" + token.getAccessToken() + "]");
					setRedisCorpToken(token);
					tokenValue = token.getAccessToken();
				} catch (JSONException e) {
					token = null;
					logger.error(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取企业token失败, errcode:{} errmsg:{}", e);
				}
			}
		} else {
			logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "从redis缓存获取企业AccessToken[" + tokenValue + "]");
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
		String result = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		UserInfo userInfo = FastJsonConvert.convertJSONToObject(result, UserInfo.class);
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "微信oauth获取用户信息code[" + code + "],userInfo[" + userInfo + "]");
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
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "获取应用信息[" + agentId + "]");
		String requestUrl = GET_AGENT_INFO.replace("ACCESS_TOKEN", getQyToken()).replace("AGENTID", agentId);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
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
		RedisUtil.setExSecond(WeixinConstant.REDIS_CORP_TOKEN_KEY, token.getAccessToken(), token.getExpiresIn() - 200);
	}

	/**
	 * 获取企业jsapi_ticket
	 * 
	 * @return
	 */
	public static String getQyJsApiTicket() {
		// redis获取
		String ticket = RedisUtil.get(WeixinConstant.REDIS_CORP_JSAPI_TICKET_KEY);
		if (StringUtils.isBlank(ticket)) {
			String requestUrl = GET_JSAPI_TICKET.replace("ACCESS_TOKEN", getQyToken());
			String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
			JsApiTicket jsApiTicket = FastJsonConvert.convertJSONToObject(resp, JsApiTicket.class);

			if (null != jsApiTicket) {
				try {
					logger.info(
							WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取企业jsapi_ticket[" + jsApiTicket.getTicket() + "]");
					if (null != jsApiTicket && WeixinConstant.SUCCESS_CODE.equals(jsApiTicket.getErrCode())) {
						RedisUtil.set(WeixinConstant.REDIS_CORP_JSAPI_TICKET_KEY, jsApiTicket.getTicket());
						RedisUtil.expire(WeixinConstant.REDIS_CORP_JSAPI_TICKET_KEY, jsApiTicket.getExpiresIn() - 200);
					} else {
						logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取企业jsapi_ticket失败,errorCode["
								+ jsApiTicket.getErrCode() + "],errorMsg[" + jsApiTicket.getErrMsg() + "]");
					}
					ticket = jsApiTicket.getTicket();
				} catch (JSONException e) {
					ticket = null;
					logger.error(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取企业jsapi_ticket失败, errcode:{} errmsg:{}", e);
				}
			}
		} else {
			logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "从redis缓存获取企业jsapi_ticket[" + ticket + "]");
		}
		return ticket;

	}

	/**
	 * 创建菜单
	 * 
	 * @param menu
	 *            菜单内容 json串
	 * @param agentId
	 *            应用ID
	 * @return
	 */
	public static String createMenu(String menu, String agentId) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "创建菜单,menu:" + menu + ",agentid[" + agentId + "]");
		String requestUrl = CREATE_MENU.replace("ACCESS_TOKEN", getQyToken()).replace("AGENTID", agentId);
		return HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, menu);
	}

	public static RespMsg sendTextMsg2Agent(BaseMessage message) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "发送消息" + message);
		String requestUrl = SEND_MSG.replace("ACCESS_TOKEN", getQyToken());
		String json = FastJsonConvert.convertObjectToJSON(message);
		System.out.println("发消息," + json);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, json);
		RespMsg respMsg = FastJsonConvert.convertJSONToObject(resp, RespMsg.class);
		return respMsg;
	}

	/**
	 * 获取成员
	 * 
	 * @param userId
	 *            成员UserID。对应管理端的帐号
	 */
	public static void getUserById(String userId) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "获取成员,userId[" + userId + "]");
		String requestUrl = USER_GET_BY_ID.replace("ACCESS_TOKEN", getQyToken()).replace("USERID", userId);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		System.out.println(resp);
	}

	/**
	 * 通讯录 增加成员
	 * @param user
	 * @return
	 */
	public static String addUser(JSONObject user) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "通讯录,增加成员,user[" + user + "]");
		String requestUrl = USER_ADD_URL.replace("ACCESS_TOKEN", getQyToken());
		String json = FastJsonConvert.convertObjectToJSON(user);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, json);
		return resp;
	}
	/**
	 * 通讯录 更新成员
	 * @param user
	 * @return
	 */
	public static String updateUser(JSONObject user) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "通讯录,更新成员,user[" + user + "]");
		String requestUrl = USER_UPDATE_URL.replace("ACCESS_TOKEN", getQyToken());
		String json = FastJsonConvert.convertObjectToJSON(user);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, json);
		return resp;
	}
	
	/**
	 *  通讯录 删除成员
	 * @param userId 成员id
	 * @return
	 */
	public static String deleteUser(String userId) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "通讯录,删除成员,userId[" + userId + "]");
		String requestUrl = USER_DELETE_URL.replace("ACCESS_TOKEN", getQyToken()).replace("USERID", userId);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		return resp;
	}

	/**
	 * 查询标签成员
	 * 
	 * @param tagId
	 */
	public static TagUserInfo getTagUsers(String tagId) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "查询标签成员,tagId[" + tagId + "]");
		String requestUrl = TAG_GET_USERS.replace("ACCESS_TOKEN", getQyToken()).replace("TAGID", tagId);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		TagUserInfo tagUserInfo = FastJsonConvert.convertJSONToObject(resp, TagUserInfo.class);
		return tagUserInfo;
	}

	public static void main(String[] args) {
		// Token token = null;
		// String requestUrl = getQyTokenUrl();
		// // 发起GET请求获取凭证
		// String resp = HttpClientUtil.httpsRequest(requestUrl,
		// WeixinConstant.requestMethod.GET, null);
		// token = FastJsonConvert.convertJSONToObject(resp, Token.class);
		// System.out.println(token);
		// getQyToken();
		// getAgentfo("14");
		// getUserInfo("223234");
		// getQyJsApiTicket();
		// SignUtil.getCorpJsTicketSign("http://localhost:8088/hx-sales-web/");
		// JSONObject json = new JSONObject();
		// json.put("touser", "2615");
		// json.put("msgtype", "text");
		// json.put("agentid", "29");
		// json.put("safe", "0");
		//
		// JSONObject content = new JSONObject();
		// content.put("content", "测试");
		//
		// json.put("text", content);
		//
		//
		// String requestUrl = SEND_MSG.replace("ACCESS_TOKEN", getQyToken());
		// System.out.println("发消息");
		// String resp = HttpClientUtil.httpsRequest(requestUrl,
		// WeixinConstant.requestMethod.POST, json.toJSONString());
		// System.out.println(resp);

		// TextMessage message = new TextMessage();
		// message.setAgentid(29);
		// message.setMsgtype(WeixinConstant.QY_MSG_TYPE_TEXT);
		// message.setSafe("0");
		// message.setTouser("2615");
		// String price = "3-5万";
		// String pawnageName = "真力时计时自动男表真力时计时自动男表";
		// String storeName = "北京回龙观华联一店";
		// String amount = "46844";
		// String salesMan = "姚魁";
		// String saleTime = "15:07:24";
		// StringBuffer detail = new StringBuffer();
		// detail.append("****有大单了****\r\n");
		// detail.append("价格：");
		// detail.append(price);
		// detail.append("\r\n当物：");
		// detail.append(pawnageName);
		// detail.append("\r\n门店：");
		// detail.append(storeName);
		// detail.append("\r\n金额：");
		// detail.append(amount);
		// detail.append("\r\n业务员：");
		// detail.append(salesMan);
		// detail.append("\r\n销售时间：");
		// detail.append(saleTime);
		// message.setContent(detail.toString());
		// String json = FastJsonConvert.convertObjectToJSON(message);
		// System.out.println(json);

		// QyWeixinApiUtil.getUserById("2444");

		// RespMsg resp = sendTextMsg2Agent(message);
		QyWeixinApiUtil.deleteUser("9999");
	}
}