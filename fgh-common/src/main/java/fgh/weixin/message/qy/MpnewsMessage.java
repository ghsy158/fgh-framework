package fgh.weixin.message.qy;

/**
 * mpnews消息
 * @author fgh
 * @since 2016年8月16日下午3:18:07
 */
public class MpnewsMessage extends BaseMessage {
	/**
	 * 
	参数	必须	说明
	touser	否	成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	toparty	否	部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数
	totag	否	标签ID列表，多个接收者用‘|’分隔。当touser为@all时忽略本参数
	msgtype	是	消息类型，此时固定为：mpnews （不支持主页型应用）
	agentid	是	企业应用的id，整型。可在应用的设置页面查看
	articles	是	图文消息，一个图文消息支持1到8个图文
	title	是	图文消息的标题，不超过128个字节，超过会自动截断
	thumb_media_id	是	图文消息缩略图的media_id, 可以在上传多媒体文件接口中获得。此处thumb_media_id即上传接口返回的media_id
	author	否	图文消息的作者，不超过64个字节
	content_source_url	否	图文消息点击“阅读原文”之后的页面链接
	content	是	图文消息的内容，支持html标签，不超过666 K个字节
	digest	否	图文消息的描述，不超过512个字节，超过会自动截断
	show_cover_pic	否	是否显示封面，1为显示，0为不显示
	safe	否	表示是否是保密消息，0表示否，1表示是，默认0
	 */
	
	/**
	 * articles 是 图文消息，一个图文消息支持1到8个图文
	 */
	private String articles;
	/**
	 * 图文消息的标题，不超过128个字节，超过会自动截断
	 */
	private String title;
	/**
	 * 图文消息缩略图的media_id, 可以在上传多媒体文件接口中获得。此处thumb_media_id即上传接口返回的media_id
	 */
	private String thumbMediaId;
	/**
	 * 图文消息的作者，不超过64个字节
	 */
	private String author;
	/**
	 * 图文消息点击“阅读原文”之后的页面链接
	 */
	private String contentSourceUrl;
	/**
	 * 图文消息的内容，支持html标签，不超过666 K个字节
	 */
	private String content;
	/**
	 * 图文消息的描述，不超过512个字节，超过会自动截断
	 */
	private String digest;
	/**
	 * 是否显示封面，1为显示，0为不显示
	 */
	private String showCoverPic;

	public String getArticles() {
		return articles;
	}

	/**
	 * articles	是	图文消息，一个图文消息支持1到8个图文
	 * @param articles
	 */
	public void setArticles(String articles) {
		this.articles = articles;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * title	是	图文消息的标题，不超过128个字节，超过会自动截断
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	/**
	 * thumb_media_id	是	图文消息缩略图的media_id, 可以在上传多媒体文件接口中获得。此处thumb_media_id即上传接口返回的media_id
	 * @param thumbMediaId
	 */
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getAuthor() {
		return author;
	}

	/**
	 * author	否	图文消息的作者，不超过64个字节
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContentSourceUrl() {
		return contentSourceUrl;
	}

	/**
	 * content_source_url	否	图文消息点击“阅读原文”之后的页面链接
	 * @param contentSourceUrl
	 */
	public void setContentSourceUrl(String contentSourceUrl) {
		this.contentSourceUrl = contentSourceUrl;
	}

	public String getContent() {
		return content;
	}

	/**
	 * content	是	图文消息的内容，支持html标签，不超过666 K个字节
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public String getDigest() {
		return digest;
	}

	/**
	 * digest	否	图文消息的描述，不超过512个字节，超过会自动截断
	 * @param digest
	 */
	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getShowCoverPic() {
		return showCoverPic;
	}

	/**
	 * show_cover_pic	否	是否显示封面，1为显示，0为不显示
	 * @param showCoverPic
	 */
	public void setShowCoverPic(String showCoverPic) {
		this.showCoverPic = showCoverPic;
	}

}
