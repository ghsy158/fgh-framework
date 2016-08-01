package fgh.weixin.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 签名工具类
 * 
 * @author fgh
 * @date 2013-09-01
 */
public class SignUtil {

	private static Logger logger = LoggerFactory.getLogger(SignUtil.class);

	// 与开发模式接口配置信息中的Token保持一致
	private static String token = "weixin";

	/**
	 * 校验签名
	 * 
	 * @param signature
	 *            微信加密签名
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机数
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		logger.info("weixin checkSignature start...");
		// 对token、timestamp和nonce按字典排序
		String[] paramArr = new String[] { token, timestamp, nonce };
		Arrays.sort(paramArr);

		// 将排序后的结果拼接成一个字符串
		String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);

		String ciphertext = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			// 对接后的字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			ciphertext = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			logger.error("weixin checkSignature error", e);
		}
		boolean result = ciphertext != null ? ciphertext.equals(signature.toUpperCase()) : false;
		logger.info("weixin checkSignature end ,result[" + result + "]...");

		// 将sha1加密后的字符串与signature进行对比
		return result;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}

	/**
	 * 企业jsApi 签名
	 * 
	 * @param url
	 *            要签名的url
	 * @return
	 */
	public static JSONObject getCorpJsTicketSign(String url) {
		logger.info("getCorpJsTicketSign,url["+url+"]");
		String nonceStr = createNonceStr();
		String timestamp = createTimestamp();
		String signature = "";

		String jsApiTicket = WeixinApiUtil.getQyJsApiTicket();//ticket
		
		// 注意这里参数名必须全部小写，且必须有序
		StringBuffer signStr = new StringBuffer(64);
		signStr.append("jsapi_ticket=");
		signStr.append(jsApiTicket);
		signStr.append("&noncestr=");
		signStr.append(nonceStr);
		signStr.append("&timestamp=");
		signStr.append(timestamp);
		signStr.append("&url=");
		signStr.append(url);

		logger.info(signStr.toString());
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(signStr.toString().getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error("企业 js ticket 签名失败", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("企业 js ticket 签名失败", e);
		}

		JSONObject result = new JSONObject();
		result.put("url", url);
		result.put("jsApiTicket", jsApiTicket);
		result.put("nonceStr", nonceStr);
		result.put("timestamp", timestamp);
		result.put("signature", signature);

		logger.info("getCorpJsTicketSign,"+result+"");
		return result;
	}

	/**
	 * 
	 * @param hash
	 * @return
	 */
	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 生成签名的随机串
	 * 
	 * @return
	 */
	protected static String createNonceStr() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 生成签名的时间戳
	 * 
	 * @return
	 */
	protected static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

}
