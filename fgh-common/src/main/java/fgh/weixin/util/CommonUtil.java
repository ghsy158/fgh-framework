package fgh.weixin.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fgh.common.util.RedisUtil;
import fgh.weixin.pojo.AgentInfo;
import fgh.weixin.pojo.TagUserInfo;
import fgh.weixin.pojo.TagUserInfo.UserList;
import fgh.weixin.pojo.User;

/**
 * 微信处理工具类
 * 
 * @author fgh
 * @since 2016年8月17日上午10:04:54
 */
public class CommonUtil {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	/**
	 * 查询指定应用的授权用户
	 * 
	 * @param key
	 *            缓存key名称
	 * @param agentId
	 *            应用ID
	 * @return
	 */
	public static Set<String> getAllowUserIds(String key, String agentId) {
		Set<String> allowUsers = RedisUtil.getSet(key);
		if (allowUsers.size() <= 0) {
			AgentInfo agentInfo = QyWeixinApiUtil.getAgentfo(agentId);
			List<User> allowUserInfos = agentInfo.getAllowUserInfos();
			String[] users = new String[allowUserInfos.size()];
			String userId = null;
			for (int i = 0; i < allowUserInfos.size(); i++) {
				userId = allowUserInfos.get(i).getUserid();
				users[i] = userId;
				allowUsers.add(userId);
			}
			RedisUtil.addSet(key, 4 * 60 * 60, users);// 缓存4个小时
			logger.info(
					WeixinConstant.LOG_MAIN_WEIXIN + "调用api,查询授权的用户,agentId[" + agentId + "],allowUsers:" + allowUsers);
		} else {
			logger.info(
					WeixinConstant.LOG_MAIN_WEIXIN + "从缓存中获取授权的用户,agentId[" + agentId + "],allowUsers:" + allowUsers);
		}
		return allowUsers;
	}

	/**
	 * 查询指定标签的用户
	 * 
	 * @param key
	 *            redis key
	 * @param tagId
	 *            企业微信号标签ID
	 * @return
	 */
	public static Set<String> getTagUserSet(String key, String tagId) {
		Set<String> tagUsers = RedisUtil.getSet(key);
		if (tagUsers.size() <= 0) {
			TagUserInfo tagUserInfo = QyWeixinApiUtil.getTagUsers(tagId);
			List<UserList> tagUserList = tagUserInfo.getUserList();
			String[] users = new String[tagUserList.size()];
			String userId = null;
			for (int i = 0; i < tagUserList.size(); i++) {
				userId = tagUserList.get(i).getUserid();
				users[i] = userId;
				tagUsers.add(userId);
			}
			RedisUtil.addSet(key, 4 * 60 * 60, users);// 缓存4个小时
			logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "调用api,查询指定标签的用户,tagId[" + tagId + "],tagUsers:" + tagUsers);
		} else {
			logger.info(WeixinConstant.LOG_MAIN_WEIXIN + "从缓存中获取指定标签的用户,tagId[" + tagId + "],tagUsers:" + tagUsers);
		}
		return tagUsers;
	}

	/**
	 * 发送消息,获取授权的用户ID，多个用户用"|"分隔
	 */
	public static String getUserIds(String key, String agentId) {
		Set<String> allowUserSet = getAllowUserIds(key, agentId);
		StringBuffer allowUsers = new StringBuffer();
		for (String userId : allowUserSet) {
			allowUsers.append(userId);
			allowUsers.append("|");
		}
		if (allowUsers.length() > 0) {
			return allowUsers.substring(0, allowUsers.lastIndexOf("|"));
		} else {
			return "";
		}
	}

	/**
	 * 发送消息，获取授的标签的用户ID，多个用户用"|"分隔
	 */
	public static String getTagUserIds(String key, String tagId) {
		Set<String> allowUserSet = getTagUserSet(key, tagId);
		StringBuffer allowUsers = new StringBuffer();
		for (String userId : allowUserSet) {
			allowUsers.append(userId);
			allowUsers.append("|");
		}
		if (allowUsers.length() > 0) {
			return allowUsers.substring(0, allowUsers.lastIndexOf("|"));
		} else {
			return "";
		}
	}

	/**
	 * 格式化金额，添加千位分隔符,保留两位小数
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatCurrency(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat("#,###.00");
		return df.format(amount);
	}

	/**
	 * 格式化金额
	 * 
	 * @param amount
	 * @return
	 */
	public static String getAmount(Object amount) {
		DecimalFormat df = new DecimalFormat("#,###");
		return df.format(amount);
	}

	public static void main(String[] args) {
		System.out.println(getAmount("10"));
	}
}
