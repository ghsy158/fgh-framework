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
 * ΢��֧��ǩ��
 * 
 * @author fgh
 * @since 2017��3��12������9:48:14
 */
public class RequestHandler {

	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	/** �̻����� */
	private String appId; // ���ںŵ�ID
	private String partnerKey;// �̻�ƽ̨��API��Կ
	private String appSecret;// ���ںŵ���Կ
	/** ����Ĳ��� */
	private SortedMap<String, String> parameters;
	/** ���� **/
	private String charSet;
	private HttpServletRequest request;

	private HttpServletResponse response;

	/**
	 * ��ʼ���캯����
	 * 
	 * @return
	 */
	public RequestHandler(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.parameters = new TreeMap();
	}

	/**
	 * ��ʼ��������
	 */
	public void init(String appid, String appSecret, String partnerKey, String charSet) {
		this.appId = appid;
		this.partnerKey = partnerKey;
		this.appSecret = appSecret;
		this.charSet = charSet;
	}

	/**
	 * ��ȡ����ֵ
	 * 
	 * @param parameter
	 *            ��������
	 * @return String
	 */
	public String getParameter(String parameter) {
		String s = (String) this.parameters.get(parameter);
		return (null == s) ? "" : s;
	}

	// �����ַ�����
	public String UrlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, this.getCharSet()).replace("+", "%20");
	}

	/**
	 * ��ȡpackage��ǩ����
	 * 
	 * @param packageParams
	 *            �������
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String genPackage(SortedMap<String, String> packageParams) throws UnsupportedEncodingException {
		logger.info("΢��֧��,����package����:" + packageParams);
		String signValue = createSign(packageParams);

		logger.info("΢��֧��,ƴ�Ӳ����ַ���...");
		StringBuffer sb = new StringBuffer(64);
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k).append("=").append(UrlEncode(v)).append("&");
		}

		// ȥ�����һ��&
		String packageValue = sb.append("sign=").append(signValue).toString();
		logger.info("΢��֧��,����package����,packageValue=" + packageValue);
		return packageValue;
	}

	/**
	 * ����md5ժҪ,������:����������a-z����,������ֵ�Ĳ������μ�ǩ����
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
		logger.info("΢��֧��,MD5ǩ���ַ���===" + sb);
		String sign = MD5Util.MD5Encode(sb.toString(), this.getCharSet()).toUpperCase();
		logger.info("΢��֧��,����MD5ǩ��,value===" + sign);
		return sign;
	}

	// ���XML
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
