package fgh.weixin.util;

/**
 * 微信中用到的常量
 * 
 * @author fgh
 * @since 2016年8月12日下午5:19:20
 */
public class WeixinConstant {

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

	/**
	 * jsticket api
	 * @author fgh
	 * @since 2016年8月29日下午6:08:16
	 */
	public class jsApiType{
		/**企业号**/
		public static final String CORP = "corp";
		/**服务号**/
		public static final String MP = "mp";
	}
	
	/** 企业 token redis key **/
	public static final String REDIS_CORP_TOKEN_KEY = "corpToken";

	/** 服务号token redis key **/
	public static final String REDIS_MP_TOKEN_KEY = "mpToken";

	/** 企业 jsapi_ticket redis key **/
	public static final String REDIS_CORP_JSAPI_TICKET_KEY = "corpJsTicket";

	/** 服务号 jsapi_ticket redis key **/
	public static final String REDIS_MP_JSAPI_TICKET_KEY = "mpJsTicket";

	public static final String REDIS_CORP_TOKEN_EXPIRE_KEY = "corpTokenExpire";

	public static final String LOG_MAIN_WEIXIN = "【wexin】";

	/**
	 * 成功返回报文编号
	 */
	public static final String SUCCESS_CODE = "0";

	public static final String SUCCESS_MSG = "ok";

	// BUTTON_TYPE start
	/**
	 * 成员点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取成员基本信息接口结合，获得成员基本信息。
	 */
	public static final String BUTTON_TYPE_VIEW = "view";
	/**
	 * 成员点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event
	 * 的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与成员进行交互；
	 */
	public static final String BUTTON_TYPE_CLICK = "click";
	/**
	 * 成员点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
	 */
	public static final String BUTTON_TYPE_SCANCODE_PUSH = "scancode_push";
	/**
	 * 成员点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
	 */
	public static final String BUTTON_TYPE_SCANCODE_WAITMSG = "scancode_waitmsg";
	/**
	 * 成员点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
	 */
	public static final String BUTTON_TYPE_PIC_SYSPHOTO = "pic_sysphoto";
	/**
	 * 成员点击按钮后，微信客户端将弹出选择器供成员选择“拍照”或者“从手机相册选择”。成员选择后即走其他两种流程。
	 */
	public static final String BUTTON_TYPE_PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";
	/**
	 * 成员点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。
	 */
	public static final String BUTTON_TYPE_PIC_WEIXIN = "pic_weixin";
	/**
	 * 成员点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息。
	 */
	public static final String BUTTON_TYPE_LOCATION_SELECT = "location_select";
	// BUTTON_TYPE end

	// QY_MSG_TYPE start
	/**
	 * text （支持消息型应用跟主页型应用）
	 */
	public static final String QY_MSG_TYPE_TEXT = "text";
	/**
	 * image（不支持主页型应用）
	 */
	public static final String QY_MSG_TYPE_IMAGE = "image";
	/**
	 * voice （不支持主页型应用）
	 */
	public static final String QY_MSG_TYPE_VOICE = "voice";
	/**
	 * video （不支持主页型应用）
	 */
	public static final String QY_MSG_TYPE_VIDEO = "video";
	/**
	 * file （不支持主页型应用）
	 */
	public static final String QY_MSG_TYPE_FILE = "file";
	/**
	 * news （不支持主页型应用）
	 */
	public static final String QY_MSG_TYPE_NEWS = "news";
	/**
	 * mpnews（不支持主页型应用）
	 */
	public static final String QY_MSG_TYPE_MPNEWS = "mpnews";
	// QY_MSG_TYPE end
}
