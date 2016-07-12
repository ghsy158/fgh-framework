package fgh.common.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.fastjson.JSONObject;

import fgh.common.constant.Const;
import fgh.common.datasource.MultipleDataSource;
import fgh.common.util.FastJsonConvert;

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

	private Logger logger = Logger.getLogger(BaseJdbcDao.class);
	
	/** JSON数据行映射器 **/
	private static final JsonRowMapper JSON_ROW_MAPPER = new JsonRowMapper();

	/** JDBC调用模板 */
	private JdbcTemplate jdbcTemplate = null;

	/** 启动时间 */
	private static Date startTime;

	/**多数据源支持  通过调用MultipleDataSource.setDataSourceKey("sqlServerDataSource")手动切换数据源**/
	private DataSource multipleDataSource;
	
	/**
	 * 
	 * <b>方法名称：初始化JDBC调用模板</b><br>
	 * <b>概要说明：</b><br>
	 */
	@Autowired
	public void initJdbcTemplate(DataSource multipleDataSource) {
		//这里要传入参数dataSource，否则启动时会报错 No DataSource specified
		this.jdbcTemplate = new JdbcTemplate(multipleDataSource);
		if (startTime == null) {
			startTime = new Date(System.currentTimeMillis());
//			startTime = getCurrentTime();
		}
	}

	/**
	 * 
	 * <b>方法名称：当前时间</b><br>
	 * <b>概要说明：</b><br>
	 */
	public Date getCurrentTime() {
		logger.info("获取当前时间...");
//		this.multipleDataSource.getConnection().getMetaData().getDatabaseProductName();
		String databaseName=null;
		try {
			databaseName = this.jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName();
			if(null != databaseName ){
				if(Const.DatabaseType.MYSQL.equals(databaseName.toUpperCase())){
					return this.getJdbcTemplate().queryForObject("SELECT NOW() FROM DUAL", Date.class);//mysql
				}else if(Const.DatabaseType.SQLSERVER.equals(databaseName.toUpperCase())){
					return this.getJdbcTemplate().queryForObject("SELECT GETDATE()", Date.class);//oracle
				}else if(Const.DatabaseType.ORACLE.equals(databaseName.toUpperCase())){
					return this.getJdbcTemplate().queryForObject("SELECT sysdate() FROM DUAL", Date.class);//oracle
				}
			}
		} catch (SQLException e) {
			logger.error("获取当前数据库类型失败，取应用服务器时间",e);
		}
		java.util.Date date;
		try {
			date = new SimpleDateFormat( Const.FORMAT_DATETIME).parse(getSysemTime());
			return new Date(date.getTime());
		} catch (ParseException e) {
			logger.error("解析当前时间异常",e);
		}
		return null;
	}

	
	/**
	 * <b>方法名称：</b>获取当前使用的数据库名称<br>
	 * <b>概要说明：</b><br>
	 */
	protected String getCurrentDatabaseName() {
		try {
			return null == this.jdbcTemplate ? ""
					: this.jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName();
		} catch (SQLException e) {
			logger.error("获取当前使用的数据库名称失败",e);
		}
		return null;
	}
	
	/**
	 * 
	 * <b>方法名称：</b>设置目标数据源<br>
	 * <b>概要说明：</b><br>
	 */
	protected void setTargetDataSource(String targetDataSource,String dataBaseType){
		logger.info("setTargetDataSource[targetDataSource="+targetDataSource+",dataBaseType="+dataBaseType+"]");
		String currentDatabaseName = this.getCurrentDatabaseName();
		//判断是目标数据源,如果是直接返回
		if(null!= currentDatabaseName && dataBaseType.equals(currentDatabaseName.toUpperCase())){
			return;
		}
		MultipleDataSource.setDataSourceKey(targetDataSource);
	}
	
	/**
	 * 
	 * <b>方法名称：</b>获取当前应用服务器时间<br>
	 * <b>概要说明：</b><br>
	 */
	public String  getSysemTime(){
		return DateFormatUtils.format(Calendar.getInstance().getTime(), Const.FORMAT_DATETIME);
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
	 * <b>方法名称：查询json列表string json</b><br>
	 * <b>概要说明：</b><br>
	 * @param sql sql语句
	 * @param args 参数
	 * @return List<JSONObject> JSON列表
	 */
	public String queryForJsonListString(String sql,Object... args){
		List<JSONObject> list = queryForJsonList(sql, args);
		return FastJsonConvert.convertObjectToJSON(list);
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
	public String appendPageSql(StringBuffer sql,int start,int limit){
		sql.insert(0, "SELECT * FROM (SELECT PAGE_VIEW.*,ROWNUM AS ROW_SEQ_NO FROM (");
		sql.append(") PAGE_VIEW WHERE ROWNUM<=").append(start +limit);
		sql.append(") WHERE ROW_SEQ_NO > ").append(start);
		return sql.toString();
	}
	
	
	/**
	 * 
	 * <b>方法名称：</b>查询总条数sql<br>
	 * <b>概要说明：</b><br>
	 */
	protected String getCountSql(StringBuffer sql){
		sql.insert(0, "SELECT count(1) FROM (");
		sql.append(")");
		return sql.toString();
	}
	
	
	/**
	 * 
	 * <b>方法名称：</b>查询总记录数<br>
	 * <b>概要说明：</b><br>
	 */
	protected int  queryCount(String sql,Object... args){
		String countSql = getCountSql(new StringBuffer(sql));
		return this.jdbcTemplate.queryForObject(countSql, Integer.class);
	}

	/**
	 * <b>方法名称：</b>分页查询<br>
	 * <b>概要说明：</b><br>
	 */
	public String pageQueryForJSONString(String sql,int start,int limit,Object... args){
		String pageSQL  = getPageSql(sql, start, limit);
		System.out.println("分页查询SQL："+pageSQL);
		List<JSONObject> list = queryForJsonList(pageSQL, args);
//		JSONObject page = new JSONObject();
//		page.put("total", queryCount(sql, args));
//		page.put("pagesie", list.size());
		
		Page page = new Page();
		int total = queryCount(sql, args);
		page.setTotalRows(total);
		page.setRows(list);
		page.setNumPerPage(start);
		String result = FastJsonConvert.convertObjectToJSON(page);
//		String result = FastJsonConvert.convertObjectToJSON(page) + FastJsonConvert.convertObjectToJSON(list) ;
		return result;
	}
	
	private String getPageSql(String sql,int start,int limit){
		return this.appendPageSql(new StringBuffer(sql), start, limit);
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
	 * <b>方法名称：</b>更新方法<br>
	 * <b>概要说明：</b><br>
	 * @param tableName 表名
	 * @param data 要更新的数据
	 * @param where where条件
	 */
	protected int update(String tableName,JSONObject data,JSONObject where){
		if(data.size()<=0){
			return 0;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE ");
		sql.append(tableName);
		sql.append(" SET ");
		
		Set<Entry<String,Object>> set = data.entrySet();
		List<Object> sqlArgs = new ArrayList<Object>();
		
		for(Iterator<Entry<String,Object>> iterator = set.iterator();iterator.hasNext();){
			Entry<String,Object> entry = iterator.next();
			sql.append(entry.getKey()+"=?,");
			sqlArgs.add(entry.getValue());
		}
		
		sql.delete(sql.length()-1, sql.length());
		
		sql.append(" WHERE ");
		Set<Entry<String,Object>> whereSet = where.entrySet();
		
		for(Iterator<Entry<String,Object>> iterator = whereSet.iterator();iterator.hasNext();){
			Entry<String,Object> entry = iterator.next();
			sql.append(entry.getKey()+"=?,");
			sqlArgs.add(entry.getValue());
		}
		sql.delete(sql.length()-1, sql.length());
		return this.getJdbcTemplate().update(sql.toString(),sqlArgs.toArray());
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
	 * <b>方法名称：</b>通过表名批量插入,根据list中map的key,自动拼接insert语句，<br>
	 * <b>概要说明：</b><br>
	 */
	protected void batchInsertByTableName(String tableName,final List<LinkedHashMap<String, Object>> list){
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
		
		batchInsert(sql.toString(), list);
	}
	
	/**
	 * 
	 * <b>方法名称：</b>执行批量插入<br>
	 * <b>概要说明：</b><br>
	 */
	private void batchInsert(String sql, final List<LinkedHashMap<String, Object>> list) {
		this.getJdbcTemplate().batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				LinkedHashMap<String, Object> map = list.get(i);
				Object[] valueSet = map.values().toArray(new Object[map.size()]);
				int len = map.keySet().size();
				for (int j = 0; j < len; j++) {
					ps.setObject(j + 1, valueSet[j]);
				}
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}
	
	/**
	 * 
	 * <b>方法名称：</b>通过insert sql语句，批量插入，从list中取值<br>
	 * <b>概要说明：</b><br>
	 */
	public void batchInsertBySql(String sql,final List<LinkedHashMap<String, Object>> list){
		batchInsert(sql.toString(), list);
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
		return multipleDataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.multipleDataSource = dataSource;
	}

}
