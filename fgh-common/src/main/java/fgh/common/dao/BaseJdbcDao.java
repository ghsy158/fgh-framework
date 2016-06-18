package fgh.common.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSONObject;

import fgh.common.constant.Const;

/**
 * 
 * <b>系统名称：</b><br>
 * <b>模块名称：</b><br>
 * <b>中文类名：</b><br>
 * <b>概要说明：</b><br>
 * 
 * @author fgh
 * @since 2016年6月3日下午10:26:43
 */
public class BaseJdbcDao {

	/** JSON数据行映射器 **/
	private static final JsonRowMapper JSON_ROW_MAPPER = new JsonRowMapper();

	/** JDBC调用模板 */
	private JdbcTemplate jdbcTemplate;

	/** 启动时间 */
	private static Date startTime;

	private DataSource dataSource;
	
	/**
	 * 
	 * <b>方法名称：初始化JDBC调用模板</b><br>
	 * <b>概要说明：</b><br>
	 */
	@Autowired
	public void initJdbcTemplate(DataSource dataSource) {
		//这里要传入参数dataSource，否则启动时会报错 No DataSource specified
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		if (startTime == null) {
			startTime = getCurrentTime();
		}
	}

	/**
	 * 
	 * <b>方法名称：当前时间</b><br>
	 * <b>概要说明：</b><br>
	 */
	public Date getCurrentTime() {
		return this.getJdbcTemplate().queryForObject("SELECT NOW() FROM DUAL", Date.class);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * 
	 * <b>方法名称：查询json列表</b><br>
	 * <b>概要说明：</b><br>
	 * @param sql sql语句
	 * @param args 参数
	 * @return List<JSONObject> JSON列表
	 */
	public List<JSONObject> queryForJsonList(String sql,Object... args){
		return this.jdbcTemplate.query(sql, JSON_ROW_MAPPER,args);
	}
	
	/**
	 * 
	 * <b>方法名称：查询一条JSON数据</b><br>
	 * <b>概要说明：</b><br>
	 * @param sql sql语句
	 * @param args 参数
	 * @return JSONObject JSON数据
	 */
	public JSONObject queryForJsonObject(String sql,Object... args){
		List<JSONObject> jsonList = queryForJsonList(sql, args);
		if(jsonList ==null || jsonList.size()<1){
			return null;
		}
		return jsonList.get(0);
	}
	
	/**
	 * 
	 * <b>方法名称：查询一条文本</b><br>
	 * <b>概要说明：</b><br>
	 * @param sql sql语句
	 * @param args 参数
	 * @return JSONObject JSON数据
	 */
	public String queryForString(String sql,Object... args){
		List<String> dataList = this.jdbcTemplate.queryForList(sql, args,String.class);
		if(dataList ==null || dataList.size()<1){
			return null;
		}
		return dataList.get(0);
	}
	
	
	/**
	 * 
	 * <b>方法名称：拼接分页语句</b><br>
	 * <b>概要说明：</b><br>
	 */
	public void appendPageSql(StringBuffer sql,int start,int limit){
		sql.insert(0, "SELECT * FROM (SELECT PAGE_VIEW.*,ROWNUM AS ROW_SEQ_NO FROM (");
		sql.append(") PAGE_VIEW WHERE ROWNUM<=").append(start +limit);
		sql.append(") WHERE ROW_SEQ_NO > ").append(start);
	}
	
	/**
	 * 
	 * <b>方法名称：拼接管理子机构查询语句</b><br>
	 * <b>概要说明：</b><br>
	 */
	public void appendOrgSubQuerySql(StringBuffer sql,List<Object> params,String orgId){
		sql.append("SELECT ORG_ID FROM MST_ORG_REF WHERE PARENT_ID = ?");
		sql.append(orgId);
	}
	
	/**
	 * 
	 * <b>方法名称：获取唯一键值</b><br>
	 * <b>概要说明：</b><br>
	 */
	public String generateKey(){
		String sql = "SELECT CONCAT('0000',DATE_FORMAT(SYSDATE(),'%Y%m%d'))  FROM DUAL";//mysql
//		String sql = "SELECT '0000' || TO_CHAR(SYSTIMESTAMP,'YYYYMMDD') FROM DUAL";//oracle
		String pre = this.getJdbcTemplate().queryForObject(sql, String.class);//业务规范
		String uuid  = UUID.randomUUID().toString().replaceAll("-", "");
		return pre+uuid.substring(12);
	}
	
	/**
	 * 
	 * <b>方法名称：用户 In 通配符(?)的拼接</b><br>
	 * <b>概要说明：</b><br>
	 */
	public void appendSqlIn(StringBuffer sql,List<Object> sqlArgs,String[] params){
		if(params!=null && params.length>0){
			sql.append(" (");
			for(int i=0;i<params.length;i++){
				if(i==0){
					sql.append("?");
				}else{
					sql.append(",?");
				}
				sqlArgs.add(params[i]);
			}
			sql.append(")");
		}
	}
	
	/**
	 * 
	 * <b>方法名称：</b>适应sql列名 大写<br>
	 * <b>概要说明：</b><br>
	 */
	public static String c(String c){
		if(StringUtils.isBlank(c)){
			return null;
		}
		return c.trim().toUpperCase();
	}
	
	/**
	 *
	 * <b>方法名称：</b> 防止sql注入<br>
	 * <b>概要说明：</b><br>
	 */
	public static String v(String v){
		if(StringUtils.isBlank(v)){
			return null;
		}
		return v.trim().replaceAll("'", "''");
	}
	
	/**
	 * 
	 * <b>方法名称：</b>获取日期文本值<br>
	 * <b>概要说明：</b><br>
	 */
	public String getDate(ResultSet rs,String column) throws SQLException{
		Date date = rs.getDate(column);
		if(null == date){
			return null;
		}
		return DateFormatUtils.format(date, Const.FORMAT_DATE);
			
	}
	
	/**
	 * 
	 * <b>方法名称：</b>获取日期时间文本值<br>
	 * <b>概要说明：</b><br>
	 */
	public String getDateTime(ResultSet rs,String column) throws SQLException{
		Date date = rs.getDate(column);
		if(null == date){
			return null;
		}
		return DateFormatUtils.format(date, Const.FORMAT_DATETIME);
		
	}
	
	/**
	 * 
	 * <b>方法名称：</b>获取时间戳文本值<br>
	 * <b>概要说明：</b><br>
	 */
	public String getTimestamp(ResultSet rs,String column) throws SQLException{
		Date date = rs.getDate(column);
		if(null == date){
			return null;
		}
		return DateFormatUtils.format(date, Const.FORMAT_TIMESTAMP);
	}
	
	/**
	 * 
	 * <b>方法名称：</b>单表insert<br>
	 * <b>概要说明：</b><br>
	 */
	protected int insert(String tableName,JSONObject data){
		if(data.size()<=0){
			return 0;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO ");
		sql.append(tableName);
		sql.append("(");
		
		Set<Entry<String,Object>> set = data.entrySet();
		List<Object> sqlArgs = new ArrayList<Object>();
		
		for(Iterator<Entry<String,Object>> iterator = set.iterator();iterator.hasNext();){
			Entry<String,Object> entry = iterator.next();
			sql.append(entry.getKey()+",");
			sqlArgs.add(entry.getValue());
		}
		sql.delete(sql.length()-1, sql.length());
		sql.append(" ) VALUES (");
		for(int i=0;i<set.size();i++){
			sql.append("?,");
		}
		sql.delete(sql.length()-1, sql.length());
		sql.append(" ) ");
		
		return this.getJdbcTemplate().update(sql.toString(),sqlArgs.toArray());
	}
	
	/**
	 * 
	 * <b>方法名称：</b>批量插入<br>
	 * <b>概要说明：</b><br>
	 */
	protected void insertBatch(String tableName,final List<LinkedHashMap<String, Object>> list){
		if(list.size()<=0){
			return;
		}
		LinkedHashMap<String, Object> linkedHashMap = list.get(0);
		StringBuffer sql = new StringBuffer();
		sql.append(" INSERT INTO ");
		sql.append(tableName + " ( ");
		
		final String[] keySet = (String[])linkedHashMap.keySet().toArray(new String[linkedHashMap.size()]);
		
		for(int i=0;i<linkedHashMap.size();i++){
			sql.append(keySet[i]+",");
		}
		
		sql.delete(sql.length()-1, sql.length());
		sql.append(" ) VALUES ( ");
		
		for(int i=0;i<linkedHashMap.size();i++){
			sql.append("?,");
		}
		sql.delete(sql.length()-1, sql.length());
		sql.append(" )");
		
		this.getJdbcTemplate().batchUpdate(sql.toString(),new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				LinkedHashMap<String, Object> map = list.get(i);
				Object[] valueSet = map.values().toArray(new Object[map.size()]);
				int len = map.keySet().size();
				for(int j=0;i<len;j++){
					ps.setObject(j+1, valueSet[i]);
				}
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
		
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public static Date getStartTime() {
		return startTime;
	}

	public static void setStartTime(Date startTime) {
		BaseJdbcDao.startTime = startTime;
	}

	public static JsonRowMapper getJsonRowMapper() {
		return JSON_ROW_MAPPER;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
