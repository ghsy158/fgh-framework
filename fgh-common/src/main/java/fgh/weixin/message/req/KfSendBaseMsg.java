package fgh.weixin.message.req;

/**
 * 发送消息给企业号客服对象
 * 
 * @author fgh
 * @since 2016年11月27日下午5:52:36
 */
public class KfSendBaseMsg {

	private KfSendUserInfo sender;
	private KfSendUserInfo receiver;
	private String msgtype;

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public KfSendUserInfo getSender() {
		return sender;
	}

	public void setSender(KfSendUserInfo sender) {
		this.sender = sender;
	}

	public KfSendUserInfo getReceiver() {
		return receiver;
	}

	public void setReceiver(KfSendUserInfo receiver) {
		this.receiver = receiver;
	}

}
