package fgh.weixin.menu;

/**
 * view类型的按钮
 * 
 * @author fgh
 * @date 2013-10-14
 */
public class ViewButton extends Button {
	private String type;
	private String url;

	private String key;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
