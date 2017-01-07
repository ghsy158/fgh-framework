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
import fgh.weixin.message.req.KfSendBaseMsg;
import fgh.weixin.message.resp.UserOfDept;
import fgh.weixin.pojo.AgentInfo;
import fgh.weixin.pojo.DeptList;
import fgh.weixin.pojo.JsApiTicket;
import fgh.weixin.pojo.TagUserInfo;
import fgh.weixin.pojo.Token;
import fgh.weixin.pojo.UserDetail;
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
	/**获取部门成员 URL **/
	public static final String USER_OF_DEPART = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=ACCESS_TOKEN&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD&status=STATUS";

	/** 获取部门列表 URL **/
	public static final String DEPT_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID";

	public static final String DEPARTMENT_ADD_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=ACCESS_TOKEN";
	private static final String DEPARTMENT_UPDATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token=ACCESS_TOKEN";
	private static final String DEPARTMENT_DELETE_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=ACCESS_TOKEN&id=ID";
	private static final String DEPARTMENT_QUERY_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID";

	// 通讯录管理 end

	// 标签管理 start
	public static final String TAG_GET_USERS = "https://qyapi.weixin.qq.com/cgi-bin/tag/get?access_token=ACCESS_TOKEN&tagid=TAGID";

	//企业客服接口
	public static final String KF_SEND_URL = "https://qyapi.weixin.qq.com/cgi-bin/kf/send?access_token=ACCESS_TOKEN";
	public static final String KF_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/kf/list?access_token=ACCESS_TOKEN&type=TYPE";
	
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
	
	private static String getQySessionTokenUrl() {
		return QY_TOKEN_URL.replace("APP_CORPID", PropertyUtil.getWeixinConfig("qy_CorpID")).replace("APP_SECRECT",
				PropertyUtil.getWeixinConfig("qy_sessionSecret"));
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
	 * 获取企业会话和企业客服消息token
	 * 
	 * @return
	 */
	public static String getQySessionToken() {
		// redis处理
		String tokenValue = RedisUtil.get(WeixinConstant.REDIS_CORP_SESSION_TOKEN_KEY);
		if (StringUtils.isBlank(tokenValue)) {
			Token token = null;
			String requestUrl = getQySessionTokenUrl();
			// 发起GET请求获取凭证
			String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
			token = FastJsonConvert.convertJSONToObject(resp, Token.class);
			if (null != token) {
				try {
					logger.info(
							WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取企业会话AccessToken[" + token.getAccessToken() + "]");
					RedisUtil.setExSecond(WeixinConstant.REDIS_CORP_SESSION_TOKEN_KEY, token.getAccessToken(), token.getExpiresIn() - 200);
					tokenValue = token.getAccessToken();
				} catch (JSONException e) {
					token = null;
					logger.error(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,获取企业会话token失败, errcode:{} errmsg:{}", e);
				}
			}
		} else {
			logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "从redis缓存获取企业会话AccessToken[" + tokenValue + "]");
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

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static RespMsg sendTextMsg2Agent(BaseMessage message) {
		String requestUrl = SEND_MSG.replace("ACCESS_TOKEN", getQyToken());
		String json = FastJsonConvert.convertObjectToJSON(message);
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "给指定应用发送消息,agentId="+message.getAgentid() + ",消息内容="+json);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, json);
		RespMsg respMsg = FastJsonConvert.convertJSONToObject(resp, RespMsg.class);
		return respMsg;
	}
	
	/**
	 * 给指定应用发送消息 
	 * @param message 
	 * @return
	 */
	public static RespMsg sendMsg2Agent(BaseMessage message) {
		return sendTextMsg2Agent(message);
	}

	/**
	 * 获取成员
	 * 
	 * @param userId
	 *            成员UserID。对应管理端的帐号
	 */
	public static UserDetail getUserById(String userId) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "获取成员,userId[" + userId + "]");
		String requestUrl = USER_GET_BY_ID.replace("ACCESS_TOKEN", getQyToken()).replace("USERID", userId);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		UserDetail respMsg = FastJsonConvert.convertJSONToObject(resp, UserDetail.class);
		return respMsg;
	}

	/**
	 * 通讯录 增加成员
	 * 
	 * @param user
	 * @return
	 */
	public static String addUser(JSONObject user) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "企业号通讯录,增加成员,user[" + user + "]");
		String requestUrl = USER_ADD_URL.replace("ACCESS_TOKEN", getQyToken());
		String json = FastJsonConvert.convertObjectToJSON(user);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, json);
		return resp;
	}

	/**
	 * 通讯录 更新成员
	 * 
	 * @param user
	 * @return
	 */
	public static String updateUser(JSONObject user) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "企业号通讯录,更新成员,user[" + user + "]");
		String requestUrl = USER_UPDATE_URL.replace("ACCESS_TOKEN", getQyToken());
		String json = FastJsonConvert.convertObjectToJSON(user);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, json);
		return resp;
	}

	/**
	 * 通讯录 删除成员
	 * 
	 * @param userId
	 *            成员id
	 * @return
	 */
	public static String deleteUser(String userId) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "企业号通讯录,删除成员,userId[" + userId + "]");
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

	/**
	 * 获取部门下的成员
	 * @param deptId 获取的部门id
	 * @param fetchChild 	1/0：是否递归获取子部门下面的成员
	 * @param status 0获取全部成员，1获取已关注成员列表，2获取禁用成员列表，4获取未关注成员列表。status可叠加，未填写则默认为4
	 * @return
	 */
	public static UserOfDept getUserOfDept(String deptId, String fetchChild, String status) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "获取部门下的成员,deptId[" + deptId + "],fetchChild=" + fetchChild
				+ ",status=" + status);
		String requestUrl = USER_OF_DEPART.replace("ACCESS_TOKEN", getQyToken()).replace("DEPARTMENT_ID", deptId)
				.replace("FETCH_CHILD", fetchChild).replace("STATUS", status);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		UserOfDept userOfDept = FastJsonConvert.convertJSONToObject(resp, UserOfDept.class);
		return userOfDept;
	}
	
	/**
	 * 获取部门列表
	 * 
	 * @param deptId
	 *            部门ID
	 * @return
	 */
	public static DeptList getDeptList(String deptId) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "查询部门列表,deptId[" + deptId + "]");
		String requestUrl = DEPT_LIST_URL.replace("ACCESS_TOKEN", getQyToken()).replace("ID", deptId);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		DeptList deptList = FastJsonConvert.convertJSONToObject(resp, DeptList.class);
		return deptList;
	}

	/**
	 * 通讯录 增加部门
	 * 
	 * @param dept
	 * @return
	 */
	public static String addDepartment(JSONObject dept) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "企业号通讯录,增加部门,dept[" + dept + "]");
		String requestUrl = DEPARTMENT_ADD_URL.replace("ACCESS_TOKEN", getQyToken());
		String json = FastJsonConvert.convertObjectToJSON(dept);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, json);
		return resp;
	}

	/**
	 * 通讯录 更新部门
	 * 
	 * @param dept
	 * @return
	 */
	public static String updateDepartment(JSONObject dept) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "企业号通讯录,更新部门,dept[" + dept + "]");
		String requestUrl = DEPARTMENT_UPDATE_URL.replace("ACCESS_TOKEN", getQyToken());
		String json = FastJsonConvert.convertObjectToJSON(dept);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, json);
		return resp;
	}

	/**
	 * 通讯录 删除部门
	 * 
	 * @param deptId
	 *            成员id
	 * @return
	 */
	public static String deleteDepartment(String deptId) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "企业号通讯录,删除部门,deptId[" + deptId + "]");
		String requestUrl = DEPARTMENT_DELETE_URL.replace("ACCESS_TOKEN", getQyToken()).replace("ID", deptId);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		return resp;
	}
	
	/**
	 * 查询部门
	 * @param deptId
	 * @return
	 */
	public static String queryDepartment(String deptId) {
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "企业号通讯录,查询部门,deptId[" + deptId + "]");
		String requestUrl = DEPARTMENT_QUERY_URL.replace("ACCESS_TOKEN", getQyToken()).replace("ID", deptId);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		return resp;
	}

	public static RespMsg sendKfMsg(KfSendBaseMsg msg) {
		String requestUrl = KF_SEND_URL.replace("ACCESS_TOKEN", getQySessionToken());
		String json = FastJsonConvert.convertObjectToJSON(msg);
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "企业客服接口,向企业号客服发送客服消息=" + json);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.POST, json);
		RespMsg respMsg = FastJsonConvert.convertJSONToObject(resp, RespMsg.class);
		return respMsg;
	}
	
	/**
	 * 企业客服接口,获取客服列表
	 * @param type 客服类型
	 * @return
	 */
	public static void getKfList(String type) {
		String requestUrl = KF_LIST_URL.replace("ACCESS_TOKEN", getQyToken()).replace("TYPE", type);
		logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "企业客服接口,获取客服列表,type=" + type);
		String resp = HttpClientUtil.httpsRequest(requestUrl, WeixinConstant.requestMethod.GET, null);
		System.out.println(resp);
	}
	
	public static void main(String[] args) {

		// RespMsg resp = sendTextMsg2Agent(message);
		// TextMessage message = new TextMessage();
		// message.setAgentid(5);//应用ID 固定5
		// message.setMsgtype(WeixinConstant.QY_MSG_TYPE_TEXT);//文本消息
		// message.setSafe("0");//固定0
		// message.setTouser("2615");//要发送给的用户 多个用户用|分隔
		// message.setContent("111");//发送的内容
		// QyWeixinApiUtil.sendTextMsg2Agent(message);//调用发送方法

		// 8256,8257,8276
//		QyWeixinApiUtil.getUserById("2019");
//		 QyWeixinApiUtil.getDeptList("8151");
		 
//		KfSendMsgText text = new KfSendMsgText();
//		 
//		KfSendUserInfo  receiver = new KfSendUserInfo();
//		receiver.setType("kf");
//		receiver.setId("2615");
//		
//		KfSendUserInfo  sender = new KfSendUserInfo();
//		sender.setType("openid");
//		sender.setId("okccKv-zxIj3VPyZZyjap9t8CZ0s");
//		
//		text.setMsgtype("text");
//		
//		text.setSender(sender);
//		text.setReceiver(receiver);
//		text.setContent("企业客服消息测试");
//		QyWeixinApiUtil.sendKfMsg(text);
//		
//		QyWeixinApiUtil.getKfList("external");

//		JSONObject sendObj = new JSONObject();
//		sendObj.put("id", "8348");
//		sendObj.put("name", "金融风控组");
//		sendObj.put("parentid", "8270");
//		sendObj.put("order","8348");
		
//		String json = QyWeixinApiUtil.updateDepartment(sendObj);
//		String json = QyWeixinApiUtil.addDepartment(sendObj);
//		System.out.println(json);
//		QyWeixinApiUtil.getUserOfDept("8155",WeixinConstant.YES,WeixinConstant.USER_ALL);
		
		JSONObject sendObj = new JSONObject();
//		sendObj.put("id","6666");
//		sendObj.put("name", "顾问2");
//		sendObj.put("parentid", "8026");
//		sendObj.put("order", "1");
		sendObj.put("id","8364");
		sendObj.put("name", "成都财务");
		sendObj.put("parentid", "8174");
		sendObj.put("order", "8364");
		QyWeixinApiUtil.addDepartment(sendObj);
		
//		QyWeixinApiUtil.queryDepartment("8364");
		
	}

}