package fgh.weixin.message.resp;

import java.util.List;

/**
 * 
 * @author fgh
 * @since 2016年12月13日下午5:14:53
 */
public class UserListOfDept {

	private String userId;
	private String name;
	private List<String> department;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDepartment() {
		return department;
	}

	public void setDepartment(List<String> department) {
		this.department = department;
	}

}
