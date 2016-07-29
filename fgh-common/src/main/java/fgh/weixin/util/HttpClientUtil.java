package fgh.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求工具类
 * 
 * @author fgh
 * @since 2016年7月28日上午10:04:00
 */
public class HttpClientUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * 发送https请求
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		logger.info("请求报文:" + requestUrl);
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		HttpsURLConnection conn = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);

			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 从输入流读取返回内容
			is = conn.getInputStream();
<<<<<<< HEAD
			isr = new InputStreamReader(is, "UTF-8");
=======
			isr = new InputStreamReader(is, "GBK");
>>>>>>> branch 'master' of git@github.com:ghsy158/fgh-framework.git
			br = new BufferedReader(isr);
			String str = null;
			StringBuffer resp = new StringBuffer();
			while ((str = br.readLine()) != null) {
				resp.append(str);
			}

			logger.info("返回报文:" + resp);

			return resp.toString();
		} catch (ConnectException ce) {
			logger.error("连接超时：{}", ce);
		} catch (Exception e) {
			logger.error("https请求异常：{}", e);
		} finally {
			// 释放资源
			try {
				if (null != br) {
					br.close();
				}
				if (null != isr) {
					isr.close();
				}
				if (null != is) {
					is.close();
					is = null;
				}
				if (null != conn) {
					conn.disconnect();
				}
			} catch (IOException e) {
				logger.error("释放资源出错", e);
			}
		}
		return null;
	}
}
