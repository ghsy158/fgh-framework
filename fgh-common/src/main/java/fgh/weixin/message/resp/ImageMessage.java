package fgh.weixin.message.resp;

/**
 * 图片消息
 * 
 * @author fgh
 * @date 2013-09-11
 */
public class ImageMessage extends BaseMessage {
	// 图片
	private Image Image;

	public Image getImage() {
		return Image;
	}

	public void setImage(Image image) {
		Image = image;
	}
}
