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
 * ��ʱû�õ�
 * @author fgh
 * @since 2017��3��15������11:02:50
 */
public class ResponseHandler {

	private static final String appkey = null;

	/** ��Կ */
	private String key;

	/** Ӧ��Ĳ��� */
	private SortedMap parameters;

	/** debug��Ϣ */
	private String debugInfo;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private String uriEncoding;
	
	 private Hashtable xmlMap;

	private String k;

	/**
	 * ���캯��
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
	 *��ȡ��Կ
	 */
	public String getKey() {
		return key;
	}

	/**
	 *������Կ
	 */
	public void setKey(String key) {
		this.key = key;
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

	/**
	 * ���ò���ֵ
	 * 
	 * @param parameter
	 *            ��������
	 * @param parameterValue
	 *            ����ֵ
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if (null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}

	/**
	 * �������еĲ���
	 * 
	 * @return SortedMap
	 */
	public SortedMap getAllParameters() {
		return this.parameters;
	}
	public void doParse(String xmlContent) throws JDOMException, IOException {
		this.parameters.clear();
		//����xml,�õ�map
		Map m = WeixinPayUtil.doXMLParse(xmlContent);
		
		//���ò���
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			this.setParameter(k, v);
		}
	}
	/**
	 * �Ƿ�Ƹ�ͨǩ��,������:����������a-z����,������ֵ�Ĳ������μ�ǩ����
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

		// ���ժҪ
		String sign = MD5Util.MD5Encode(sb.toString(), this.uriEncoding).toLowerCase();

		String ValidSign = this.getParameter("sign").toLowerCase();

		// debug��Ϣ
		this.setDebugInfo(sb.toString() + " => sign:" + sign + " ValidSign:"
				+ ValidSign);

		return ValidSign.equals(sign);
	}
	/**
	 * �ж�΢��ǩ��
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
	//�ж�΢��άȨǩ��
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
	 * ���ش��������Ƹ�ͨ��������
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
	 * ��ȡuri����
	 * 
	 * @return String
	 */
	public String getUriEncoding() {
		return uriEncoding;
	}

	/**
	 *��ȡdebug��Ϣ
	 */
	public String getDebugInfo() {
		return debugInfo;
	}
	/**
	 *����debug��Ϣ
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
