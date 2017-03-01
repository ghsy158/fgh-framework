package fgh.common.dao;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 分页对象
 * 
 * @author fgh
 * @since 2016年7月12日下午6:21:25
 */
public class Page implements Serializable {

	private static final long serialVersionUID = -2285216628802671550L;

	/**
	 * 总记录数
	 */
	private int total;

	/**
	 * 总页数
	 */
	private int totalPages;

	/**
	 * 第几页 从0开始
	 */
	private int page = 0;

	/**
	 * 开始的记录位置
	 */
	private int start;

	/**
	 * 每页多少条
	 */
	private int pagesize = 20;;

	/**
	 * 查询到的结果集
	 */
	private List<JSONObject> rows;

	/**
	 * 排序字段
	 */
	private String sort;

	/**
	 * 排序方式 asc或desc 默认desc
	 */
	private String order = "desc";

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setTotalPages() {
		if (total % pagesize == 0) {
			this.totalPages = total / pagesize;
		} else {
			this.totalPages = (total / pagesize) + 1;
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getStart() {
		return start;
	}

	public void setStart() {
		this.start = page * pagesize;
	}

	public List<JSONObject> getRows() {
		return rows;
	}

	public void setRows(List<JSONObject> rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "Page [total=" + total + ", totalPages=" + totalPages + ", page=" + page + ", start=" + start
				+ ", pagesize=" + pagesize + ", sort=" + sort + ", order=" + order + "]";
	}

}