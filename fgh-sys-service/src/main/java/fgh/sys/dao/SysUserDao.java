package fgh.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

import fgh.common.dao.BaseJdbcDao;

@Repository("sysUserDao")
public class SysUserDao extends BaseJdbcDao {

	private static final String SQL_TABLE_NAME = "SYS_USER";
	private static final String SQL_SELECT_SYS_USER = "SELECT * FROM SYS_USER";
	private static final String SQL_SELECT_SYS_USER_BY_ID = "SELECT * FROM SYS_USER WHERE USER_ID = ?";

	public List<JSONObject> getList() throws Exception {
		return super.queryForJsonList(SQL_SELECT_SYS_USER);
	}
	
	public JSONObject getById(String id) {
		return super.queryForJsonObject(SQL_SELECT_SYS_USER_BY_ID, id);
	}

	public int insert(JSONObject jsonObject) throws Exception {
		return super.insert(SQL_TABLE_NAME, jsonObject);
	}

}
