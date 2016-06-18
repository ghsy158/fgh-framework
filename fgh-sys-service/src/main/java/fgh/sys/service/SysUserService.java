package fgh.sys.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import fgh.sys.dao.SysUserDao;
import fgh.sys.entity.SysUser;
import fgh.sys.facade.SysUserFacade;

@Service("sysUserService")
@com.alibaba.dubbo.config.annotation.Service(interfaceClass = fgh.sys.facade.SysUserFacade.class, protocol = { "rest",
		"dubbo" })
public class SysUserService implements SysUserFacade {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Override
	public void testGet() {
		System.out.println("测试...get");
	}

	@Override
	public SysUser getUser() {
		System.out.println("getUser测试...get");
		SysUser user = new SysUser();
		user.setId("1001");
		user.setName("张三");
		return user;
	}

	@Override
	public SysUser getUser(Integer id) {
		System.out.println("getUser,id=" + id);
		SysUser user = new SysUser();
		user.setId("1002");
		user.setName("李四");
		return user;
	}

	// http://localhost:8888/provider/userService/get/3/c6
	@Override
	public SysUser getUser(Integer id, String name) {
		System.out.println("getUser,id=" + id + ",name=" + name);
		SysUser user = new SysUser();
		user.setId("1002");
		user.setName("李四");
		return user;
	}

	@Override
	public void testPost() {
		System.out.println("测试...post");
	}

	@Override
	public SysUser postUser(SysUser user) {
		System.out.println("postUser," + user.getName());
		SysUser user1 = new SysUser();
		user.setId("1002");
		user1.setName("李四");
		return user1;
	}

	@Override
	public String generateKey() throws Exception {
		return this.sysUserDao.generateKey();
	}

	@Override
	public JSONObject getById(String id) throws Exception {
		return this.sysUserDao.getById(id);
	}

	@Override
	public List<JSONObject> getList() throws Exception {
		List<JSONObject> list = this.sysUserDao.getList();
		if(list.isEmpty()){
			return Collections.emptyList();
		}else{
			return list;
		}
	}

	@Override
	public int insert(JSONObject jsonObject) throws Exception {
		return this.sysUserDao.insert(jsonObject);
	}

}
