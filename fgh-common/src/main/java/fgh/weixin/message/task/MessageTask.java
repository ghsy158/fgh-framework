package fgh.weixin.message.task;

import java.util.Date;
import java.util.TimerTask;

import fgh.weixin.message.resp.TextMessage;
import fgh.weixin.util.MessageUtil;


public class MessageTask extends TimerTask {

	@Override
	public void run() {
		// 回复文本消息
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName("fenggh326");
		textMessage.setFromUserName("");
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
		textMessage.setFuncFlag(0);

	}

}
