package fgh.weixin.message.qy;

/**
 * text消息
 * @author fgh
 * @since 2016年8月16日下午3:08:40
 */
public class TextMessage  extends BaseMessage{

	/**
	 * 
	 */
	private Text text;
	
	class Text{
		/**
		 * 消息内容，最长不超过2048个字节，注意：主页型应用推送的文本消息在微信端最多只显示20个字（包含中英文）
		 */
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
	}

	public void setContent(String content){
		Text text = new Text();
		text.setContent(content);
		this.setText(text);
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	
	
}
