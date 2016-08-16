package fgh.weixin.message.qy;

/**
 * 
 * @author fgh
 * @since 2016年8月16日下午3:14:07
 */
public class FileMessage extends BaseMessage{

	/**
	 * 媒体文件id，可以调用上传临时素材或者永久素材接口获取
	 */
	private String mediaId;

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
		
	
}
