package fgh.weixin.pojo;

/**
 * 企业号oauth2获取用户信息
 * 
 * @author fgh
 * @since 2016年7月28日下午3:28:18
 */
public class UserInfo {

	/** 成员UserID <br>企业成员授权时返回**/
	private String userId;
	/** 非企业成员的标识，对当前企业号唯一<br>非企业成员授权时返回 **/
	private String openId;
	/** 手机设备号(由微信在安装时随机生成，删除重装会改变，升级不受影响) **/
	private String deviceId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", openId=" + openId + ", deviceId=" + deviceId + "]";
	}

	
}
