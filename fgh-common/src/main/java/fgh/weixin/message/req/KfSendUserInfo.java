package fgh.weixin.message.req;

/**
 * 向企业号客服发送客服消息 ---对象
 * @author fgh
 * @since 2016年11月27日下午6:13:14
 */
public class KfSendUserInfo {

	private String type;
	private String id;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
