package fgh.weixin.message.qy;

/**
 * news消息
 * 
 * @author fgh
 * @since 2016年8月16日下午3:14:58
 */
public class NewsMessage extends BaseMessage {

	/**
	 * 图文消息，一个图文消息支持1到8条图文
	 */
	private String articles;
	/**
	 * 标题，不超过128个字节，超过会自动截断
	 */
	private String title;
	/**
	 * 描述，不超过512个字节，超过会自动截断
	 */
	private String description;
	/**
	 * 点击后跳转的链接。
	 */
	private String url;
	/**
	 * 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80。如不填，在客户端不显示图片
	 */
	private String picurl;

	public String getArticles() {
		return articles;
	}

	/**
	 * 图文消息，一个图文消息支持1到8条图文
	 * 
	 * @param articles
	 */
	public void setArticles(String articles) {
		this.articles = articles;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * 标题，不超过128个字节，超过会自动截断
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * 描述，不超过512个字节，超过会自动截断
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * 点击后跳转的链接。
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public String getPicurl() {
		return picurl;
	}

	/**
	 * 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80。如不填，在客户端不显示图片
	 * 
	 * @param picurl
	 */
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}

}
