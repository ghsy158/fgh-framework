package com.wxap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wxap.util.MD5Util;

/**
 * 微信支付签名
 * 
 * @author fgh
 * @since 2017年3月12日下午9:48:14
 */
public class RequestHandler {

	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	/** 商户参数 */
	private String appId; // 公众号的ID
	private String partnerKey;// 商户平台的API密钥
	private String appSecret;// 公众号的密钥
	/** 请求的参数 */
	private SortedMap<String, String> parameters;
	/** 编码 **/
	private String charSet;
	private HttpServletRequest request;

	private HttpServletResponse response;

	/**
	 * 初始构造函数。
	 * 
	 * @return
	 */
	public RequestHandler(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.parameters = new TreeMap();
	}

	/**
	 * 初始化函数。
	 */
	public void init(String appid, String appSecret, String partnerKey, String charSet) {
		this.appId = appid;
		this.partnerKey = partnerKey;
		this.appSecret = appSecret;
		this.charSet = charSet;
	}

	/**
	 * 获取参数值
	 * 
	 * @param parameter
	 *            参数名称
	 * @return String
	 */
	public String getParameter(String parameter) {
		String s = (String) this.parameters.get(parameter);
		return (null == s) ? "" : s;
	}

	// 特殊字符处理
	public String UrlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, this.getCharSet()).replace("+", "%20");
	}

	/**
	 * 获取package的签名包
	 * 
	 * @param packageParams
	 *            输入参数
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String genPackage(SortedMap<String, String> packageParams) throws UnsupportedEncodingException {
		logger.info("微信支付,生成package参数:" + packageParams);
		String signValue = createSign(packageParams);

		logger.info("微信支付,拼接参数字符串...");
		StringBuffer sb = new StringBuffer(64);
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k).append("=").append(UrlEncode(v)).append("&");
		}

		// 去掉最后一个&
		String packageValue = sb.append("sign=").append(signValue).toString();
		logger.info("微信支付,生成package参数,packageValue=" + packageValue);
		return packageValue;
	}

	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public String createSign(SortedMap<String, String> packageParams) {
		StringBuffer sb = new StringBuffer(64);
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k).append("=").append(v).append("&");
			}
		}
		sb.append("key=").append(this.getPartnerkey());
		logger.info("微信支付,MD5签名字符串===" + sb);
		String sign = MD5Util.MD5Encode(sb.toString(), this.getCharSet()).toUpperCase();
		logger.info("微信支付,生成MD5签名,value===" + sign);
		return sign;
	}

	// 输出XML
	public String parseXML() {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"appkey".equals(k)) {
				sb.append("<" + k + ">" + getParameter(k) + "</" + k + ">\n");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public void setPartnerkey(String partnerkey) {
		this.partnerKey = partnerkey;
	}

	public String getAppid() {
		return appId;
	}

	public void setAppid(String appid) {
		this.appId = appid;
	}

	public String getAppsecret() {
		return appSecret;
	}

	public void setAppsecret(String appsecret) {
		this.appSecret = appsecret;
	}

	public SortedMap getParameters() {
		return parameters;
	}

	public void setParameters(SortedMap parameters) {
		this.parameters = parameters;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPartnerKey() {
		return partnerKey;
	}

	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public String getPartnerkey() {
		return partnerKey;
	}

}
