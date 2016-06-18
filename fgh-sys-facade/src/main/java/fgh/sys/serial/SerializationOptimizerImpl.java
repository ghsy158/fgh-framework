package fgh.sys.serial;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
import com.alibaba.fastjson.JSONObject;

public class SerializationOptimizerImpl implements SerializationOptimizer {

	@Override
	public Collection<Class> getSerializableClasses() {
		List<Class> classes = new LinkedList<Class>();
		// 这里可以把所有需要序列化的类进行添加
//		classes.add(SysUser.class);
		
		//不用实体，都用json对象
		classes.add(JSONObject.class);
		return classes;
	}

	public void dtoUser(){
		//把扩展好的SysUser添加到序列化中
	}
}
