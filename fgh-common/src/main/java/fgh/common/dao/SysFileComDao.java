package fgh.common.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import fgh.common.entity.SysFile;

/**
 * 文件处理DAO
 * 
 * @author fgh
 * @since 2016年9月24日下午4:31:58
 */
@Repository
public class SysFileComDao extends BaseJdbcDao {

	/**
	 * 系统文件映射器
	 */
	private static final SysFileRowMapper SYS_FILE_ROW_MAPPER = new SysFileRowMapper();

	public int insert(SysFile file) {
		if (file == null) {
			return 0;
		}

		String sql = "INSERT INTO SYS_FILE(KEY,TYPE,NANE,EXT,BYTES,DATA_PATH,DATA_GROUP,EXPIRED,DESC_INFO,UPDATE_BY) VALUES(?,?,?,?,?,?,?,?,?,?)";
		Object[] args = new Object[10];
		args[0] = file.getKey();
		args[1] = file.getType();
		args[2] = file.getName();
		args[3] = file.getExt();
		args[4] = file.getBytes();
		args[5] = file.getDataPath();
		args[6] = file.getDataGroup();
		args[7] = file.getExpired();
		args[8] = file.getDescInfo();
		args[9] = file.getUpdateBy();

		return super.getJdbcTemplate().update(sql, args);
	}

	/**
	 * 清除过期时间
	 * 
	 * @param key
	 * @return
	 */
	public int clearExpired(String key) {
		if (StringUtils.isBlank(key)) {
			return 0;
		}
		return super.getJdbcTemplate().update("UPDATE SYS_FILE SET EXPIRED = NULL WHERE KEY = ?", key);
	}

	/**
	 * 更新过期时间
	 * 
	 * @param key
	 * @param expired
	 * @return
	 */
	public int expire(String key, Date expired) {
		if (StringUtils.isBlank(key)) {
			return 0;
		}
		if (expired == null) {
			return super.getJdbcTemplate().update("UPDATE SYS_FILE SET EXPIRED = SYSTIMESTAMP WHERE KEY = ?", key);
		}
		return super.getJdbcTemplate().update("UPDATE SYS_FILE SET EXPIRED = ? WHERE KEY = ?", expired, key);
	}

	/**
	 * 删除文件
	 * 
	 * @param key
	 *            文件key值
	 * @return
	 */
	public int delete(String key) {
		if (StringUtils.isBlank(key)) {
			return 0;
		}
		return super.getJdbcTemplate().update("DELETE FROM SYS_FILE WHERE KEY = ? ", key);

	}

	/**
	 * 获取文件
	 * 
	 * @param key
	 * @return
	 */
	public SysFile get(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		List<SysFile> fileList = getList(key.split(","));
		if (fileList == null || fileList.size() < 1) {
			return null;
		}
		return fileList.get(0);
	}

	/**
	 * 获取文件列表
	 * 
	 * @param keys
	 * @return
	 */
	public List<SysFile> getList(String[] keys) {
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT KEY,TYPE,NAME,EXT,BYTES,DATA_PATH,DATA_GROUP,EXPIRED,DESC_INFO,UPDATE_BY,UPDATE_TIME ");
		sql.append(" FROM SYS_FILE WHERE KEY IN ");
		super.appendSqlIn(sql, args, keys);
		sql.append(" ORDER BY TYPE,NAME ");
		return super.getJdbcTemplate().query(sql.toString(), SYS_FILE_ROW_MAPPER, args.toArray());

	}

	/**
	 * 清空文件缓存方法
	 * 
	 * @return
	 */
	public List<String> clearFileKeys() {
		SimpleJdbcCall call = new SimpleJdbcCall(this.getJdbcTemplate()).withCatalogName("PKG_SYS")
				.withProcedureName("CLEAR_FILES").declareParameters(new SqlOutParameter("P_RET_REFCUR", 0));
		Map<String, Object> m = call.execute();
		if (m.values().size() > 0) {
			String temp = m.values().toArray()[0].toString().replace("[", "").replace("]", "");
			String[] keys = temp.split(",");
			List<String> ret = new ArrayList<String>();
			for (String key : keys) {
				String temp1 = key.replace("{", "").replace("}", "");
				if (!StringUtils.isBlank(temp1)) {
					ret.add(temp1.split("\\=")[1]);
				}
			}
			return ret;
		}
		return Collections.emptyList();
	}
}
