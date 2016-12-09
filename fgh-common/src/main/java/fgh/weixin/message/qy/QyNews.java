package fgh.weixin.message.qy;

import java.util.List;

/**
 * 
 * @author fgh
 * @since 2016年12月1日下午7:04:25
 */
public class QyNews {

	/**
	 * 图文消息，一个图文消息支持1到8条图文
	 */
	private List<QyArticle> articles;

	public List<QyArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<QyArticle> articles) {
		this.articles = articles;
	}
}
