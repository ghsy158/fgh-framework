package fgh.common.dao;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 分页对象
 * @author fgh
 * @since 2016年7月12日下午6:21:25
 */
public class Page implements Serializable {

	private static final long serialVersionUID = -2285216628802671550L;

	private int numPerPage = 500;

	private int totalRows;

	private int totalPages;

	private int currentPage = 1;

	private int startIndex;

	private List<JSONObject> rows;

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public void setTotalPages() {
		if (totalRows % numPerPage == 0) {
			this.totalPages = totalRows / numPerPage;
		} else {
			this.totalPages = (totalRows / numPerPage) + 1;
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex() {
		this.startIndex = (currentPage - 1) * numPerPage;
	}

	public List<JSONObject> getRows() {
		return rows;
	}

	public void setRows(List<JSONObject> rows) {
		this.rows = rows;
	}

}