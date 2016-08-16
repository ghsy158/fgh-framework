package fgh.weixin.util;

/**
 * 微信中用到的常量
 * @author fgh
 * @since 2016年8月12日下午5:19:20
 */
public class WeixinConstant {

	/**
	 * 成员点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取成员基本信息接口结合，获得成员基本信息。
	 */
	public static final String BUTTON_TYPE_VIEW="view";
	/**
	 * 成员点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event	的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与成员进行交互；
	 */
	public static final String BUTTON_TYPE_CLICK="click";
	/**
	 * 成员点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
	 */
	public static final String BUTTON_TYPE_SCANCODE_PUSH="scancode_push";
	/**
	 * 成员点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
	 */
	public static final String BUTTON_TYPE_SCANCODE_WAITMSG="scancode_waitmsg";
	/**
	 * 成员点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
	 */
	public static final String BUTTON_TYPE_PIC_SYSPHOTO="pic_sysphoto";
	/**
	 * 成员点击按钮后，微信客户端将弹出选择器供成员选择“拍照”或者“从手机相册选择”。成员选择后即走其他两种流程。
	 */
	public static final String BUTTON_TYPE_PIC_PHOTO_OR_ALBUM="pic_photo_or_album";
	/**
	 * 成员点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。
	 */
	public static final String BUTTON_TYPE_PIC_WEIXIN="pic_weixin";
	/**
	 * 成员点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息。
	 */
	public static final String BUTTON_TYPE_LOCATION_SELECT="location_select";
}
