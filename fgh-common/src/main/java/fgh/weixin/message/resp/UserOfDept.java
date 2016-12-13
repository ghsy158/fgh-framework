package fgh.weixin.message.resp;

import java.util.List;

/**
 * 获取部门成员
 * 
 * @author fgh
 * @since 2016年12月13日下午5:16:32
 */
public class UserOfDept extends BaseRespMsg {

	private List<UserListOfDept> userList;

	public List<UserListOfDept> getUserList() {
		return userList;
	}

	public void setUserList(List<UserListOfDept> userList) {
		this.userList = userList;
	}

}
