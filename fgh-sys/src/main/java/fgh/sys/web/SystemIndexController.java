package fgh.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;

import fgh.common.util.FastJsonConvert;
import fgh.sys.entity.SysUser;
import fgh.sys.facade.SysUserFacade;

/**
 * <b>系统名称：</b><br>
 * <b>模块名称：</b><br>
 * <b>中文类名：</b><br>
 * <b>概要说明：</b><br>
 * @author fgh
 * @since 2016年5月29日下午9:32:49
 */
@Controller
public class SystemIndexController {

	@Autowired
	private SysUserFacade sysUserFacade;
	
	/**
	 * 
	 * <b>方法名称：</b><br>
	 * <b>概要说明：</b><br>
	 * @throws Exception 
	 */
	@RequestMapping("/sysindex.html")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView ret = new ModelAndView();
		System.out.println(this.sysUserFacade);
		
		SysUser sysUser = this.sysUserFacade.getUser();
		System.out.println(sysUser.getName());
		
//		ret.addObject("sysUsser", sysUser);
		
		List<JSONObject> list = this.sysUserFacade.getList();
		for(JSONObject jsonObject:list){
			System.out.println(jsonObject);
		}
		System.out.println("getById="+this.sysUserFacade.getById("admin"));
		System.out.println("generateKey="+this.sysUserFacade.generateKey());
		return ret;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getList.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String getList(HttpServletRequest request,HttpServletResponse response) throws Exception {
		List<JSONObject> list = this.sysUserFacade.getList();
		for(JSONObject jsonObject:list){
			System.out.println(jsonObject);
		}
		
		String json = FastJsonConvert.convertObjectToJSON(list);
		System.out.println(json);
		return json;
	}
	
}
