package fgh.weixin.message.req;

/**
 * 
 * @Description:文本消息
 * @author Administrator
 * @2014年4月20日下午6:37:42
 * @version V1.0
 */
public class TextMessage extends BaseMessage {

	/**
	 * 文本消息内容
	 */
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
