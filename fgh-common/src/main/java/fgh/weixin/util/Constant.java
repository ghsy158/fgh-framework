package fgh.weixin.util;

/**
 * 常量类
 * 
 * @author fgh
 * @since 2016年7月28日上午11:20:06
 */
public class Constant {

	/**
	 * 请求方法
	 * 
	 * @author fgh
	 * @since 2016年7月28日上午11:21:41
	 */
	public class requestMethod {
		public static final String GET = "GET";
		public static final String POST = "POST";
	}

	/**企业 token redis key**/
	public static final String REDIS_CORP_TOKEN_KEY = "corpToken";
	/**企业 jsapi_ticket redis key**/
	public static final String REDIS_CORP_JSAPI_TICKET_KEY = "corpJsTicket";
	public static final String REDIS_CORP_TOKEN_EXPIRE_KEY = "corpTokenExpire";
	
	public static final String ERROR_CODE_OK="0";
}
