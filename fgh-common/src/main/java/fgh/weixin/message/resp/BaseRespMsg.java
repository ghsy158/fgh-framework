package fgh.weixin.message.resp;

/**
 * 返回报文
 * @author fgh
 * @since 2016年8月25日下午2:50:06
 */
public class BaseRespMsg {

	private String errCode;
	
	private String errMsg;

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
	
	
}
