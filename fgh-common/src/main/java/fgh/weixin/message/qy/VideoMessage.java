package fgh.weixin.message.qy;

/**
 * video消息
 * @author fgh
 * @since 2016年8月16日下午3:12:38
 */
public class VideoMessage extends BaseMessage {

	/**
	 * 视频媒体文件id，可以调用上传临时素材或者永久素材接口获取
	 */
	private String mediaId;
	
	/**
	 * 视频消息的标题，不超过128个字节，超过会自动截断
	 */
	private String title;
	/**
	 * 视频消息的描述，不超过512个字节，超过会自动截断
	 */
	private String description;
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
