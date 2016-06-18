package fgh.common.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 
 * <b>系统名称：</b><br>
 * <b>模块名称：</b>fastJson转换类<br>
 * <b>中文类名：</b><br>
 * <b>概要说明：</b><br>
 * 
 * @author fgh
 * @since 2016年6月4日上午11:32:25
 */
public class FastJsonConvert {

	private static final SerializerFeature[] featuresWithNullValue = { SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty,
			SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty };

	/**
	 * 
	 * <b>方法名称：</b>JSON字符串转换成对象<br>
	 * <b>概要说明：</b><br>
	 */
	public static <T> T convertJSONToObject(String data, Class<T> clazz) {
		try {
			T t = JSON.parseObject(data, clazz);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * <b>方法名称：</b>JSON对象转换成对象<br>
	 * <b>概要说明：</b><br>
	 */
	public static <T> T convertJSONToObject(JSONObject data, Class<T> clazz) {
		try {
			T t = JSONObject.toJavaObject(data, clazz);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * <b>方法名称：</b>将JSON字符串数组转换成List集合对象<br>
	 * <b>概要说明：</b><br>
	 * @param data JSOn字符串数组
	 * @param clazz 转换对象
	 * @return List<T> 集合对象
	 */
	public static <T> List<T> convertJSONToArray(String data, Class<T> clazz) {
		try {
			List<T> t = JSON.parseArray(data,clazz);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 
	 * <b>方法名称：</b>将JSON字符串数组转换成List集合对象<br>
	 * <b>概要说明：</b><br>
	 * @param data List<JSONObject>
	 * @param clazz 转换对象
	 * @return List<T> 集合对象
	 */
	public static <T> List<T> convertJSONToArray(List<JSONObject> data, Class<T> clazz) {
		try {
			List<T> t = new ArrayList<T>();
			for (JSONObject jsonObject : data) {
				t.add(convertJSONToObject(jsonObject, clazz));
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * <b>方法名称：</b>将对象转换成JSON字符串<br>
	 * <b>概要说明：</b><br>
	 */
	public static String convertObjectToJSON(Object obj) {
		try {
			String text = JSON.toJSONString(obj);
			return text;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * <b>方法名称：</b>将对象转换成JSONObject对象<br>
	 * <b>概要说明：</b><br>
	 */
	public static JSONObject convertObjectToJSONObject(Object obj) {
		try {
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(obj);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * <b>方法名称：</b><br>
	 * <b>概要说明：</b><br>
	 */
	public static String convertObjectToJSONWithNullValue(Object obj) {
		try {
			String text = JSON.toJSONString(obj, featuresWithNullValue);
			return text;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
