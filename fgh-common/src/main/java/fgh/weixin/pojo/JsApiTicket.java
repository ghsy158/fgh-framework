package fgh.weixin.pojo;

/**
 * jsapi_ticket
 * 
 * @author fgh
 * @since 2016年7月29日下午5:18:40
 */
public class JsApiTicket {

	private String errCode;
	private String errMsg;
	private String ticket;
	private int expiresIn;

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

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

}
