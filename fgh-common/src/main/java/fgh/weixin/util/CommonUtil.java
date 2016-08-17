package fgh.weixin.util;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fgh.common.util.RedisUtil;
import fgh.weixin.pojo.AgentInfo;
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
			for (int i = 0; i < allowUserInfos.size(); i++) {
				users[i] = allowUserInfos.get(i).getUserid();
			}
			RedisUtil.addSet(key, 4 * 60 * 60, users);// 缓存4个小时
			logger.info(Constant.LOG_MAIN_WEIXIN + "调用api,查询授权的用户,agentId[" + agentId + "],allowUserInfos:"
					+ allowUserInfos);
		} else {
			logger.info(Constant.LOG_MAIN_WEIXIN + "从缓存中获取授权的用户,agentId[" + agentId + "],allowUsers:" + allowUsers);
		}
		return allowUsers;
	}

	/**
	 * 发送消息,获取授权的用户ID，多个用户用"|"分隔
	 */
	public static String getUserIds(String key, String agentId) {
		Set<String> allowUserSet  = getAllowUserIds(key, agentId);
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

}
