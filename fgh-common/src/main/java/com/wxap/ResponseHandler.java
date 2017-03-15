package com.wxap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;

import com.wxap.util.MD5Util;
import com.wxap.util.WeixinPayUtil;

/**
 * 暂时没用到
 * @author fgh
 * @since 2017年3月15日上午11:02:50
 */
public class ResponseHandler {

	private static final String appkey = null;

	/** 密钥 */
	private String key;

	/** 应答的参数 */
	private SortedMap parameters;

	/** debug信息 */
	private String debugInfo;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private String uriEncoding;
	
	 private Hashtable xmlMap;

	private String k;

	/**
	 * 构造函数
	 * 
	 * @param request
	 * @param response
	 */
	public ResponseHandler(HttpServletRequest request,
			HttpServletResponse response) {
		this.request = request;
		this.response = response;

		this.key = "";
		this.parameters = new TreeMap();
		this.debugInfo = "";

		this.uriEncoding = "";

		Map m = this.request.getParameterMap();
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String k = (String) it.next();
			String v = ((String[]) m.get(k))[0];
			this.setParameter(k, v);
		}

	}

	/**
	 *获取密钥
	 */
	public String getKey() {
		return key;
	}

	/**
	 *设置密钥
	 */
	public void setKey(String key) {
		this.key = key;
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

	/**
	 * 设置参数值
	 * 
	 * @param parameter
	 *            参数名称
	 * @param parameterValue
	 *            参数值
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if (null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}

	/**
	 * 返回所有的参数
	 * 
	 * @return SortedMap
	 */
	public SortedMap getAllParameters() {
		return this.parameters;
	}
	public void doParse(String xmlContent) throws JDOMException, IOException {
		this.parameters.clear();
		//解析xml,得到map
		Map m = WeixinPayUtil.doXMLParse(xmlContent);
		
		//设置参数
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			this.setParameter(k, v);
		}
	}
	/**
	 * 是否财付通签名,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * 
	 * @return boolean
	 */
	public boolean isValidSign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}

		sb.append("key=" + this.getKey());

		// 算出摘要
		String sign = MD5Util.MD5Encode(sb.toString(), this.uriEncoding).toLowerCase();

		String ValidSign = this.getParameter("sign").toLowerCase();

		// debug信息
		this.setDebugInfo(sb.toString() + " => sign:" + sign + " ValidSign:"
				+ ValidSign);

		return ValidSign.equals(sign);
	}
	/**
	 * 判断微信签名
//	 */
//	public boolean isWXsign(){
//			
//		StringBuffer sb = new StringBuffer();
//		String keys="";
//		SortedMap<String, String> signParams = new TreeMap<String, String>();
//		 Hashtable signMap = new Hashtable();
//		 Set es = this.parameters.entrySet();
//		 Iterator it = es.iterator();
//		 while (it.hasNext()){
//			 	Map.Entry entry = (Map.Entry) it.next();
//				String k = (String) entry.getKey();
//				String v = (String) entry.getValue();
//			 if (k != "SignMethod" && k != "AppSignature"){
//				 
//				 sb.append(k + "=" + v + "&");
//			 }
//		 }
//		 signMap.put("appkey", this.appkey);
//		 //ArrayList akeys = new ArrayList();
//         //akeys.sort();
//         while (it.hasNext()){ 
//             String v = k;
//             if (sb.length() == 0)
//             {
//                 sb.append(k + "=" + v);
//             }
//             else
//             {
//                 sb.append("&" + k + "=" + v);
//             }
//         }
//
//         String sign = Sha1Util.getSha1(sb.toString()).toString().toLowerCase();
//
//         this.setDebugInfo(sb.toString() + " => SHA1 sign:" + sign);
//
//         return sign.equals("AppSignature");
//       
//	}
	//判断微信维权签名
//	public boolean isWXsignfeedback(){
//		
//		StringBuffer sb = new StringBuffer();
//		 Hashtable signMap = new Hashtable();
//		 Set es = this.parameters.entrySet();
//		 Iterator it = es.iterator();
//		 while (it.hasNext()){
//			 	Map.Entry entry = (Map.Entry) it.next();
//				String k = (String) entry.getKey();
//				String v = (String) entry.getValue();
//			 if (k != "SignMethod" && k != "AppSignature"){
//				 
//				 sb.append(k + "=" + v + "&");
//			 }
//		 }
//		 signMap.put("appkey", this.appkey);
//		 
//		// ArrayList akeys = new ArrayList();
//        // akeys.Sort();
//         while (it.hasNext()){ 
//             String v = k;
//             if (sb.length() == 0)
//             {
//                 sb.append(k + "=" + v);
//             } 
//             else
//             {
//                 sb.append("&" + k + "=" + v);
//             }
//         }
//
//         String sign = Sha1Util.getSha1(sb.toString()).toString().toLowerCase();
//
//         this.setDebugInfo(sb.toString() + " => SHA1 sign:" + sign);
//
//         return sign.equals("App    Signature");
//     }	
		
	/**
	 * 返回处理结果给财付通服务器。
	 * 
	 * @param msg
	 * Success or fail
	 * @throws IOException
	 */
	public void sendToCFT(String msg) throws IOException {
		String strHtml = msg;
		PrintWriter out = this.getHttpServletResponse().getWriter();
		out.println(strHtml);
		out.flush();
		out.close();

	}

	/**
	 * 获取uri编码
	 * 
	 * @return String
	 */
	public String getUriEncoding() {
		return uriEncoding;
	}

	/**
	 *获取debug信息
	 */
	public String getDebugInfo() {
		return debugInfo;
	}
	/**
	 *设置debug信息
	 */
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}

	protected HttpServletRequest getHttpServletRequest() {
		return this.request;
	}

	protected HttpServletResponse getHttpServletResponse() {
		return this.response;
	}

}
