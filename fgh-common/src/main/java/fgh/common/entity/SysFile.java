package fgh.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统文件实体类
 * @author fgh
 * @since 2016年9月24日下午4:12:51
 */
public class SysFile implements Serializable {

	private static final long serialVersionUID = 68897810750144991L;

	/** 文件键值 **/
	private String key;

	/** 文件分类 **/
	private String type;

	/** 文件名称 **/
	private String name;
	/** 文件扩展名 **/
	private String ext;
	/** 文件大小 **/
	private long bytes;
	/** 文件存放路径 **/
	private String dataPath;
	/** 文件数据组 **/
	private String dataGroup;
	/** 过期时间 **/
	private Date expired;
	/** 描述信息 **/
	private String descInfo;
	/** 更新人 **/
	private String updateBy;
	/** 更新时间 **/
	private Date updateTime;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public long getBytes() {
		return bytes;
	}

	public void setBytes(long bytes) {
		this.bytes = bytes;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getDataGroup() {
		return dataGroup;
	}

	public void setDataGroup(String dataGroup) {
		this.dataGroup = dataGroup;
	}

	public Date getExpired() {
		return expired;
	}

	public void setExpired(Date expired) {
		this.expired = expired;
	}

	public String getDescInfo() {
		return descInfo;
	}

	public void setDescInfo(String descInfo) {
		this.descInfo = descInfo;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
