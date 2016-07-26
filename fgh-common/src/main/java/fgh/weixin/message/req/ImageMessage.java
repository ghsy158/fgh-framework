package fgh.weixin.message.req;

/**
 * 
 * @Description: 图片消息
 * @author Administrator
 * @2014年4月20日下午6:37:11
 * @version V1.0
 */
public class ImageMessage extends BaseMessage {

	/**
	 * 图片链接
	 */
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

}
