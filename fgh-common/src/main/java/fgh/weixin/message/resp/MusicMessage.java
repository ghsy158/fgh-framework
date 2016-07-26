package fgh.weixin.message.resp;

/**
 * 
 * @Description: 响应消息之音乐消息
 * @author ghfeng
 * @2014年4月20日下午7:07:28
 * @version V1.0
 */
public class MusicMessage extends BaseMessage {
	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}
}
