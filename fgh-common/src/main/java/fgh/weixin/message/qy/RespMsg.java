package fgh.weixin.message.qy;

/**
 * 企业微信发送消息 返回报文
 * 
 * @author fgh
 * @since 2016年8月16日下午3:02:43
 */
public class RespMsg {

	private String errCode;
	private String errMsg;
	private String invalidUser;
	private String invalidParty;
	private String invalidTag;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getInvalidUser() {
		return invalidUser;
	}

	public void setInvalidUser(String invalidUser) {
		this.invalidUser = invalidUser;
	}

	public String getInvalidParty() {
		return invalidParty;
	}

	public void setInvalidParty(String invalidParty) {
		this.invalidParty = invalidParty;
	}

	public String getInvalidTag() {
		return invalidTag;
	}

	public void setInvalidTag(String invalidTag) {
		this.invalidTag = invalidTag;
	}

}
