package fgh.common.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * <b>系统名称：</b><br>
 * <b>模块名称：</b><br>
 * <b>中文类名：JSON数据行映射器</b><br>
 * <b>概要说明：</b><br>
 * @author fgh
 * @since 2016年6月3日下午10:51:48
 */
public class JsonRowMapper implements RowMapper<JSONObject> {

	/**
	 * 
	 * <b>方法名称：</b><br>
	 * <b>概要说明：</b><br>
	 */
	@Override
	public JSONObject mapRow(ResultSet rs, int row) throws SQLException {
		String key = null;
		Object obj = null;
		JSONObject json = new JSONObject();
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		for (int i = 1; i <= count; i++) {
			key = JdbcUtils.lookupColumnName(rsmd, i);
			obj = JdbcUtils.getResultSetValue(rs, i);
			try {
				json.put(key, obj);
			} catch (JSONException e) {

			}
		}
		return json;

	}

}
