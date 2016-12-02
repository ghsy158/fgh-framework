package fgh.weixin.pojo;

import java.util.List;

import fgh.weixin.message.resp.BaseRespMsg;

/**
 * 部门列表
 * 
 * @author fgh
 * @since 2016年11月22日下午5:51:44
 */
public class DeptList extends BaseRespMsg {

	private List<Depart> department;

	public class Depart {
		private String id;
		private String name;
		private String parentid;
		private String order;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getParentid() {
			return parentid;
		}

		public void setParentid(String parentid) {
			this.parentid = parentid;
		}

		public String getOrder() {
			return order;
		}

		public void setOrder(String order) {
			this.order = order;
		}

	}

	public List<Depart> getDepartment() {
		return department;
	}

	public void setDepartment(List<Depart> department) {
		this.department = department;
	}

}
