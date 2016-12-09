package fgh.weixin.message.req;

/**
 * 向企业号客服发送客服消息 --文本
 * 
 * @author fgh
 * @since 2016年11月27日下午5:59:48
 */
public class KfSendMsgText extends KfSendBaseMsg {

	private Content text;

	public class Content {
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}

	public Content getText() {
		return text;
	}

	public void setText(Content text) {
		this.text = text;
	}
	
	/**
	 * 设置发送的消息内容
	 * @param content
	 */
	public void setContent(String content) {
		Content text = new Content();
		text.setContent(content);
		this.setText(text);
	}

}
