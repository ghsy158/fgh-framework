package fgh.weixin.message.resp;

import java.util.List;

/**
 * 获取用户列表
 * 
 * @author fgh
 * @Since 2016-4-20 下午5:22:59
 */
public class UserList {

	/**
	 * 关注该公众账号的总用户数
	 */
	private String total;
	/**
	 * 拉取的OPENID个数，最大值为10000
	 */
	private String count;
	/**
	 * 列表数据，OPENID的列表
	 */
	private OpenId data;
	/**
	 * 拉取列表的最后一个用户的OPENID
	 */
	private String next_openid;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public OpenId getData() {
		return data;
	}

	public void setData(OpenId data) {
		this.data = data;
	}

	public String getNext_openid() {
		return next_openid;
	}

	public void setNext_openid(String next_openid) {
		this.next_openid = next_openid;
	}
}
