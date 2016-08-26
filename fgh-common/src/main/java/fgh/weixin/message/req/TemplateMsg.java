package fgh.weixin.message.req;

import java.util.Map;

public class TemplateMsg {

	private String toUser;
	private String templateId;
	private String url;

	private Map<String, Template> data;

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Template> getData() {
		return data;
	}

	public void setData(Map<String, Template> data) {
		this.data = data;
	}

}
