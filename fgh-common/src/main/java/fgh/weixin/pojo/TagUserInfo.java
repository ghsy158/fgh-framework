package fgh.weixin.pojo;

import java.util.List;

/**
 * 标签用户
 * 
 * @author fgh
 * @since 2016年10月19日下午3:11:21
 */
public class TagUserInfo {

	private String errCode;
	private String errMsg;

	/** 成员列表 **/
	private List<UserList> userList;
	private String[] partyList;

	public class UserList {
		private String userid;
		private String name;

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

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

	public List<UserList> getUserList() {
		return userList;
	}

	public void setUserList(List<UserList> userList) {
		this.userList = userList;
	}

	public String[] getPartyList() {
		return partyList;
	}

	public void setPartyList(String[] partyList) {
		this.partyList = partyList;
	}

}
