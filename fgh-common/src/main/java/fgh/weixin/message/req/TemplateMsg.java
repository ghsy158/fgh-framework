package fgh.weixin.message.req;

import java.util.List;
import java.util.Map;

public class TemplateMsg {

	private String touser;
	private String template_id;
	private String url;

	private Map<String, List<Template>> data;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, List<Template>> getData() {
		return data;
	}

	public void setData(Map<String, List<Template>> data) {
		this.data = data;
	}

}
