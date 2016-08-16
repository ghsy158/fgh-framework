package fgh.weixin.message.qy;

/**
 * image消息
 * @author fgh
 * @since 2016年8月16日下午3:09:32
 */
public class ImageMessage extends BaseMessage {

	/**
	 * 图片媒体文件id，可以调用上传临时素材或者永久素材接口获取,永久素材media_id必须由发消息的应用创建
	 */
	private String mediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	
}
