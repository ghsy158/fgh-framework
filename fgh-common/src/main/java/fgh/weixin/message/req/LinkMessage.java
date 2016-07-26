package fgh.weixin.message.req;

/**
 * 
 * @Description: 链接消息
 * @author Administrator
 * @2014年4月20日下午6:37:21
 * @version V1.0
 */
public class LinkMessage extends BaseMessage {

	/**
	 * 消息标题
	 */
	private String Title;

	/**
	 * 消息描述
	 */
	private String Description;

	/**
	 * 消息链接
	 */
	private String Url;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}
}
