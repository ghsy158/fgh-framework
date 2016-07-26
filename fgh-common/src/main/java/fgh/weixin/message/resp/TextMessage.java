package fgh.weixin.message.resp;

/**
 * @Description: 响应消息 - 文本消息
 * @author ghfeng
 * @2014年4月20日下午7:06:12
 * @version V1.0
 */
public class TextMessage extends BaseMessage {

	// 回复的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
