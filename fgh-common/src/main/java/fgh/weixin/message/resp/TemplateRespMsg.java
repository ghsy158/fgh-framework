package fgh.weixin.message.resp;

/**
 * 发送模板消息返回报文
 * @author fgh
 * @since 2016年8月25日下午2:52:09
 */
public class TemplateRespMsg extends BaseRespMsg{

	String msgId;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
