package fgh.common.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;

import fgh.common.dao.SysFileComDao;
import fgh.common.entity.SysFile;
import fgh.common.util.FastDFSClientUtils;

/**
 * 
 * @author fgh
 * @since 2016年9月24日下午4:56:56
 */
@Service
@PropertySource("classpath:fastdfs.properties")
public class SysFileComService {

	@Autowired
	private SysFileComDao sysFileComDao;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Value("${fastdfs.baseurl}")
	public String FASTDFS_BASEURL;

	public String getKey() {
		return this.sysFileComDao.generateKey();
	}

	/**
	 * 上传文件 传参MultipartFile
	 * 
	 * @param key
	 * @param type
	 * @param userId
	 * @param mf
	 * @param expired
	 * @param descinfo
	 * @return
	 * @throws IOException
	 */
	public JSONObject put(String key, String type, String userId, MultipartFile mf, Date expired, String descinfo)
			throws IOException {
		String fileName = mf.getOriginalFilename();
		String extName = getExtName(fileName);
		String fileId = FastDFSClientUtils.upload(mf.getBytes(), extName);
		String dataGroup = fileId.substring(0, fileId.indexOf("/"));
		long bytes = mf.getSize();

		SysFile file = new SysFile();
		file.setKey(key);
		file.setName(fileName);
		file.setType(type);
		file.setExt(extName);
		file.setBytes(bytes);
		file.setDataPath(FASTDFS_BASEURL + fileId);
		file.setDataGroup(dataGroup);
		file.setExpired(expired);
		file.setDescInfo(descinfo);
		file.setUpdateBy(userId);
		sysFileComDao.insert(file);

		JSONObject ret = new JSONObject();
		ret.put("success", true);
		ret.put("key", key);
		ret.put("type", type);
		ret.put("name", mf.getOriginalFilename());
		ret.put("bytes", mf.getSize());
		ret.put("dataPath", FASTDFS_BASEURL + fileId);
		ret.put("dataGroup", dataGroup);
		ret.put("expired", expired);
		return ret;

	}

	/**
	 * 上传文件 传参byte
	 * 
	 * @param key
	 * @param type
	 * @param userId
	 * @param fileName
	 * @param data
	 * @param expired
	 * @param descinfo
	 * @return
	 * @throws IOException
	 */
	public String put(String key, String type, String userId, String fileName, byte[] data, Date expired,
			String descinfo) throws IOException {
		if (StringUtils.isBlank(fileName)) {
			return "未指定文件名称";
		}
		if (data == null || data.length < 1) {
			return "未指定文件数据";
		}
		String extName = getExtName(fileName);
		String fileId = FastDFSClientUtils.upload(data, extName);
		String dataGroup = fileId.substring(0, fileId.indexOf("/"));

		SysFile file = new SysFile();
		file.setKey(key);
		file.setName(fileName);
		file.setType(type);
		file.setExt(extName);
		file.setBytes(data.length);
		file.setDataPath(FASTDFS_BASEURL + fileId);
		file.setDataGroup(dataGroup);
		file.setExpired(expired);
		file.setDescInfo(descinfo);
		file.setUpdateBy(userId);
		sysFileComDao.insert(file);

		return null;

	}

	/**
	 * 删除文件
	 * 
	 * @param keys
	 */
	public void delete(String... keys) {
		if (keys == null || keys.length < 1) {
			return;
		}
		if (keys.length == 1) {
			keys = keys[0].split(",");
		}
		for (String key : keys) {
			SysFile file = sysFileComDao.get(key);
			String dataPath = file.getDataPath();
			String fileId = dataPath.substring(FASTDFS_BASEURL.length());
			String groupName = file.getDataGroup();
			FastDFSClientUtils.delete(groupName, fileId);

			sysFileComDao.delete(key);
		}
	}

	/**
	 * 
	 * @param keys
	 */
	public void expire(String... keys) {
		if (keys == null || keys.length < 1) {
			return;
		}
		if (keys.length == 1) {
			keys = keys[0].split(",");
		}
		for (String key : keys) {
			sysFileComDao.expire(key, null);
		}
	}

	/**
	 * 清除过期时间
	 * 
	 * @param keys
	 */
	public void clearExpired(String... keys) {
		if (keys == null || keys.length < 1) {
			return;
		}
		if (keys.length == 1) {
			keys = keys[0].split(",");
		}
		for (String key : keys) {
			sysFileComDao.clearExpired(key);
		}
	}

	/**
	 * 系统文件过期
	 * 
	 * @param key
	 * @param expired
	 */
	public void expire(String key, Date expired) {
		if (StringUtils.isBlank(key)) {
			return;
		}
		String[] keys = key.split(",");
		for (String k : keys) {
			sysFileComDao.expire(k, expired);
		}
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	private String getExtName(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		int i = fileName.indexOf(".");
		if (i >= 0) {
			return fileName.substring(i + 1).trim().toLowerCase();
		}
		return null;
	}

	/**
	 * 获取文件信息
	 * 
	 * @param key
	 * @return
	 */
	public SysFile getInfo(String key) {
		return sysFileComDao.get(key);
	}

	/**
	 * 获取文件列表
	 * 
	 * @param keys
	 * @return
	 */
	public List<SysFile> getInfoList(String... keys) {
		return sysFileComDao.getList(keys);
	}
}
