package fgh.common.constant;

public final class Const {

	private Const() {

	}

	public static final String TRUE = "1";

	public static final String FALSE = "0";

	/** 通用字符集编码**/
	public static final String CHARSET_UTF8 = "UTF-8";
	
	/** 中文字符编码**/
	public static final String CHARSET_CHINESE = "GBK";

	/**
	 * 英文字符编码
	 */
	public static final String CHARSET_LATIN = "ISO-8859-1";

	/**
	 * 根节点
	 */
	public static final String ROOT_ID = "root";
	/**
	 * 空字符串
	 */
	public static final String NULL = "null";

	/** 日期格式 yyyy-MM-dd **/
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	
	/** 日期格式 yyyyMMdd **/
	public static final String FORMAT_yyyyMMdd = "yyyyMMdd";
	
	/** 日期时间格式  yyyy-MM-dd HH:mm:ss**/
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	
	/** 时间戳格式  yyyy-MM-dd HH:mm:ss.SSS**/
	public static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
	
	/** 时间格式  MM-dd HH:mm**/
	public static final String FORMAT_DATETIME2 = "MM-dd HH:mm";
	
	/** JSON成功标记 **/
	public static final String JSON_SUCCESS = "success";
	
	/** JSON数据 **/
	public static final String JSON_DATA= "data";
	
	/** JSON数据列表 **/
	public static final String JSON_ROWS = "rows";
	
	/** JSON总数 **/
	public static final String JSON_TOTAL = "total";
	
	/** JSON消息文本 **/
	public static final String JSON_MESSAGE = "message";
	
	public static final String TAG_SYS = "sys";
	
	/**基础信息**/
	public static final String TAG_MST = "mst";
	
	public static final String TAG_MQ = "mq";
	
	/**数据报送**/
	public static final String TAG_DAT = "dat";
	
	/**统计分析**/
	public static final String TAG_STA = "sta";
	
	public static final String TAG_INT = "int";
	
	public static final String[] TAGS = {TAG_SYS,TAG_MST,TAG_MQ,TAG_DAT,TAG_STA,TAG_INT};
	
	/** cookie键值：验证键值 **/
	public static final String COOKIE_VALIDATE_KEY = "VALIDATE_KEY";
	
	/** cookie键值：验证键值分隔符 **/
	public static final String COOKIE_VALIDATE_KEY_SPLIT = "$_";
	
	/** 请求属性键值，当前项目标识**/
	public static final String REQ_CUR_TAG = "REQ_CUR_TAG";
	
	/** 请求属性键值，当前项目标识**/
	public static final String REQ_CUR_USER_ID = "CUR_USER_ID";
	
	/** 请求属性键值，当前用户名称**/
	public static final String REQ_CUR_USER_NAME= "CUR_USER_NAME";
	
	/** 请求属性键值，当前机构标识**/
	public static final String REQ_CUR_ORG_ID = "CUR_ORG_ID";
	
	/** 请求属性键值，当前角色名称**/
	public static final String REQ_CUR_ROLE_CODE = "CUR_ROLE_CODE";
	
	/**
	 * <b>系统名称：</b><br>
	 * <b>模块名称：</b>数据库类型<br>
	 * <b>中文类名：</b><br>
	 * <b>概要说明：</b><br>
	 * 
	 * @author fgh
	 * @since 2016年6月26日上午10:41:52
	 */
	public static class DatabaseType {
		/** MySQL **/
		public static final String MYSQL = "MYSQL";
		/** oracle **/
		public static final String ORACLE = "ORACLE";
		/** Sqlserver **/
		public static final String SQLSERVER = "SQLSERVER";
	}

}
