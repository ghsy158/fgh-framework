package fgh.weixin.message.req;


/**
 * 
 * @Description: 事件推送
 * @author Administrator
 * @2014年4月20日下午6:36:15
 * @version V1.0
 */
public class EventMessage extends BaseMessage {

	/**
	 * 事件类型，subscribe(订阅)、unsubscribe(取消订阅)、CLICK(自定义菜单点击事件)
	 */
	private String Event;

	/**
	 * 事件KEY值，与自定义菜单接口中KEY值对应
	 */
	private String EventKey;

}
