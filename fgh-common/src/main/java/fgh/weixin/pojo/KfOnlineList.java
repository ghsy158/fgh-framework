package fgh.weixin.pojo;

import java.util.List;

/**
 * 在线客服对象
 * @author fgh
 * @since 2016年10月27日下午6:08:23
 */
public class KfOnlineList {
	
	private List<KfInfo> kfOnlineList;
	
	public class KfInfo{
		/**完整客服帐号，格式为：帐号前缀@公众号微信号**/
		private String kfAccount;
		/**客服在线状态，目前为：1、web 在线**/
		private String status;
		/**客服编号**/
		private String kfId;
		/**客服当前正在接待的会话数**/
		private String acceptedCase;
		public String getKfAccount() {
			return kfAccount;
		}
		public void setKfAccount(String kfAccount) {
			this.kfAccount = kfAccount;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getKfId() {
			return kfId;
		}
		public void setKfId(String kfId) {
			this.kfId = kfId;
		}
		public String getAcceptedCase() {
			return acceptedCase;
		}
		public void setAcceptedCase(String acceptedCase) {
			this.acceptedCase = acceptedCase;
		}
		@Override
		public String toString() {
			return "KfInfo [kfAccount=" + kfAccount + ", status=" + status + ", kfId=" + kfId + ", acceptedCase="
					+ acceptedCase + "]";
		}
		
	}

	public List<KfInfo> getKfOnlineList() {
		return kfOnlineList;
	}

	public void setKfOnlineList(List<KfInfo> kfOnlineList) {
		this.kfOnlineList = kfOnlineList;
	}
	
	
}
