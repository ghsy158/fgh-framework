package fgh.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import fgh.common.entity.SysFile;

/**
 * 
 * @author fgh
 * @since 2016年9月24日下午4:23:37
 */
public class SysFileRowMapper implements RowMapper<SysFile> {

	/**
	 * 
	 * <b>方法描述：</b>映射行数据
	 * 
	 * @param @param
	 *            rs 结果集
	 * @param @param
	 *            rowNum 行号
	 * @param @return
	 * @param @throws
	 *            SQLException
	 */
	@Override
	public SysFile mapRow(ResultSet rs, int rowNum) throws SQLException {
		SysFile sysFile = new SysFile();
		sysFile.setKey(rs.getString("KEY"));
		sysFile.setType(rs.getString("TYPE"));
		sysFile.setName(rs.getString("NAME"));
		sysFile.setExt(rs.getString("EXT"));
		sysFile.setBytes(rs.getLong("BYTES"));
		sysFile.setDataPath(rs.getString("DADA_PATH"));
		sysFile.setDataGroup(rs.getString("DATA_GROUP"));
		sysFile.setExpired(rs.getDate("EXPIRED"));
		sysFile.setDescInfo(rs.getString("DESC_INFO"));
		sysFile.setUpdateBy(rs.getString("UPDATE_BY"));
		sysFile.setUpdateTime(rs.getDate("UPDATE_TIME"));
		return sysFile;
	}

}
