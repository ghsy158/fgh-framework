package fgh.weixin.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取企业号应用信息
 * 
 * @author fgh
 * @since 2016年7月28日下午5:17:01
 */
public class AgentInfo {

	private String errCode;
	private String errMsg;
	/** 企业应用id **/
	private String agentid;
	/** 企业应用名称 **/
	private String name;
	/** 企业应用方形头像 **/
	private String squareLogoUrl;
	/** 企业应用圆形头像 **/
	private String roundLogourl;
	/** 企业应用详情 **/
	private String description;
	/** 企业应用可见范围（人员），其中包括userid和关注状态state **/
	private AllowUserInfos allowUserInfos;
	/** 企业应用可见范围（部门） **/
	private AllowPartys allowPartys;
	/** 企业应用可见范围（标签） **/
	private AllowTags allowTags;
	/** 企业应用是否被禁用 **/
	private String close;
	/** 企业应用可信域名 **/
	private String redirectDomain;
	/** 企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；2：持续上报 **/
	private String reportLocationFlag;
	/** 是否接收用户变更通知。0：不接收；1：接收 **/
	private String isReportUser;
	/** 是否上报用户进入应用事件。0：不接收；1：接收 **/
	private String isReportEnter;
	/** 关联会话url **/
	private String chatExtensionUrl;
	/** 应用类型。1：消息型；2：主页型 **/
	private String type;

	public class AllowPartys {
		private String[] partyid;

		public String[] getPartyid() {
			return partyid;
		}

		public void setPartyid(String[] partyid) {
			this.partyid = partyid;
		}

	}

	public class AllowUserInfos {
		private List<User> user;

		public List<User> getUser() {
			return user;
		}

		public void setUser(List<User> user) {
			this.user = user;
		}

	}

	public List<User> getAllowUserInfos() {
		return this.allowUserInfos == null ? new ArrayList<User>() : this.allowUserInfos.getUser();
	}

	class AllowTags {
		private String[] tagid;

		public String[] getTagid() {
			return tagid;
		}

		public void setTagid(String[] tagid) {
			this.tagid = tagid;
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

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSquareLogoUrl() {
		return squareLogoUrl;
	}

	public void setSquareLogoUrl(String squareLogoUrl) {
		this.squareLogoUrl = squareLogoUrl;
	}

	public String getRoundLogourl() {
		return roundLogourl;
	}

	public void setRoundLogourl(String roundLogourl) {
		this.roundLogourl = roundLogourl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAllowUserInfos(AllowUserInfos allowUserInfos) {
		this.allowUserInfos = allowUserInfos;
	}

	public AllowPartys getAllowPartys() {
		return allowPartys;
	}

	public void setAllowPartys(AllowPartys allowPartys) {
		this.allowPartys = allowPartys;
	}

	public AllowTags getAllowTags() {
		return allowTags;
	}

	public void setAllowTags(AllowTags allowTags) {
		this.allowTags = allowTags;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getRedirectDomain() {
		return redirectDomain;
	}

	public void setRedirectDomain(String redirectDomain) {
		this.redirectDomain = redirectDomain;
	}

	public String getReportLocationFlag() {
		return reportLocationFlag;
	}

	public void setReportLocationFlag(String reportLocationFlag) {
		this.reportLocationFlag = reportLocationFlag;
	}

	public String getIsReportUser() {
		return isReportUser;
	}

	public void setIsReportUser(String isReportUser) {
		this.isReportUser = isReportUser;
	}

	public String getIsReportEnter() {
		return isReportEnter;
	}

	public void setIsReportEnter(String isReportEnter) {
		this.isReportEnter = isReportEnter;
	}

	public String getChatExtensionUrl() {
		return chatExtensionUrl;
	}

	public void setChatExtensionUrl(String chatExtensionUrl) {
		this.chatExtensionUrl = chatExtensionUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
