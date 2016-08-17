package fgh.weixin.message.qy;

/**
 * 企业消息对象，基本属性
 * 
 * @author fgh
 * @since 2016年8月16日下午3:02:00
 */
public class BaseMessage {

	/**
	 * 
	 * 参数 必须 说明 touser 否
	 * 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送 toparty
	 * 否 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数 totag 否
	 * 标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数 msgtype 是 消息类型，此时固定为：text
	 * （支持消息型应用跟主页型应用） agentid 是 企业应用的id，整型。可在应用的设置页面查看 content 是
	 * 消息内容，最长不超过2048个字节，注意：主页型应用推送的文本消息在微信端最多只显示20个字（包含中英文） safe 否
	 * 表示是否是保密消息，0表示否，1表示是，默认0
	 */

	/**
	 * 成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 */
	private String touser;
	/**
	 * 部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	 */
	private String toparty;
	/**
	 * 标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数
	 */
	private String totag;

	/**
	 * 消息类型，此时固定为：text （支持消息型应用跟主页型应用）
	 */
	private String msgtype;
	/**
	 * 企业应用的id，整型。可在应用的设置页面查看
	 */
	private int agentid;
	/**
	 * 表示是否是保密消息，0表示否，1表示是，默认0
	 */
	private String safe;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(String toparty) {
		this.toparty = toparty;
	}

	public String getTotag() {
		return totag;
	}

	public void setTotag(String totag) {
		this.totag = totag;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public int getAgentid() {
		return agentid;
	}

	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}

	public String getSafe() {
		return safe;
	}

	public void setSafe(String safe) {
		this.safe = safe;
	}

}
