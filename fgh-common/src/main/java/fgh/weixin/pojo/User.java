package fgh.weixin.pojo;

/**
 * 获取企业号应用信息<br>授权的用户信息
 * @author fgh
 * @since 2016年7月29日下午12:53:01
 */
public class User {
	private String userid;
	private String status;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}