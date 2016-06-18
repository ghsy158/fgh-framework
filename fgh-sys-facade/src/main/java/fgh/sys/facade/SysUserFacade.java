package fgh.sys.facade;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSONObject;

import fgh.sys.entity.SysUser;

@Path("/sysUserService")
@Consumes({MediaType.APPLICATION_JSON,MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8,ContentType.TEXT_XML_UTF_8})
public interface SysUserFacade {

	@GET
	@Path("/testGet")
	public void testGet();
	
	@GET
	@Path("/getUser")
	public SysUser getUser();
	
	@GET
	@Path("/get/{id:\\d+}")
	public SysUser getUser(@PathParam(value="id") Integer id) ;
	
	@GET
	@Path("/get/{id:\\d+}{name}")
	public SysUser getUser(@PathParam(value="id") Integer id,@PathParam(value="name") String name) ;
	
	@POST
	@Path("/testPost")
	public void testPost();
	
	@POST
	@Path("/postUser")
	public SysUser postUser(SysUser user);
	
	//内部使用，不对外暴露服务
	@POST
	public String generateKey() throws Exception;
	
	@GET
	@Path("/getById/{id}")
	public JSONObject getById(@PathParam(value="id") String id) throws Exception;
	
	@POST
	@Path("/getList")
	public List<JSONObject> getList() throws Exception;
	
	@POST
	public int insert(JSONObject jsonObject) throws Exception;
}
