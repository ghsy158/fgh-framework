package fgh.common.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * <b>系统名称：</b>日期工具类<br>
 * <b>模块名称：</b><br>
 * <b>中文类名：</b><br>
 * <b>概要说明：</b><br>
 * @author fgh
 * @since 2016年6月29日下午3:57:40
 */
public class DateUtil {
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat simpleyyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat simpleHHmmTimeFormat = new SimpleDateFormat("HH:mm");
	private static SimpleDateFormat simpleDateFormatYM = new SimpleDateFormat("yyyy-MM");
	private static SimpleDateFormat simpleDateFormatYMZH = new SimpleDateFormat("yyyy年MM月");
	private static SimpleDateFormat simpleDateFormatY = new SimpleDateFormat("yyyy");

	/**
	 * format date just only for MQ generate insert SQL sentences。
	 * 
	 * @param
	 * @return
	 */
	public static String formatForMQ(Object date) {
		if (null == date) {
			return "null";
		} else {
			return String.format("'%s'", format(date));
		}
	}

	public static String format(Object d) {
		if (d == null) {
			return "";
		}

		if (d instanceof Timestamp) {
			return simpleDateFormat.format((Timestamp) d);
		} else if (d instanceof Time) {
			return simpleTimeFormat.format((Time) d);
		} else if (d instanceof Date) {
			return simpleyyyyMMddFormat.format((Date) d);
		}

		return d.toString();
	}

	public static String format(Object d, String partten) {
		if (d == null) {
			return "";
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(partten);

		return simpleDateFormat.format(d);
	}

	/**
	 * 返回 HH:mm:ss
	 * 
	 * @param d
	 * @return
	 */
	public static String formatTime(Object d) {
		if (d == null) {
			return "";
		}

		return simpleTimeFormat.format(d);
	}

	public static Date parseDate(String s) {
		if (StringUtils.isEmpty(s)) {
			return null;
		}

		try {
			return simpleyyyyMMddFormat.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前日期 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return Timestamp
	 */
	public static Timestamp currentTimestamp() {
		String str = simpleDateFormat.format(new Date());
		try {
			return new Timestamp(simpleDateFormat.parse(str).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据长度格式化日期 yyyy-MM-dd
	 * 
	 * @return Timestamp
	 */
	public static Timestamp limit6Timestamp() {
		String str = simpleyyyyMMddFormat.format(new Date());
		try {
			return new Timestamp(simpleyyyyMMddFormat.parse(str).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据格式返回Timestamp,如果格式为空返回yyyy-MM-dd HH:mm:ss
	 * 
	 * @param s
	 * @param format
	 * @return Timestamp
	 */
	public static Timestamp formatTimestamp(String s, String format) {
		if (s == null) {
			return null;
		}

		try {
			if (StringUtils.isEmpty(format)) {
				return new Timestamp(simpleDateFormat.parse(s).getTime());
			} else if ("yyyy-MM-dd HH:mm:ss".equalsIgnoreCase(format)) {
				return new Timestamp(simpleDateFormat.parse(s).getTime());
			} else if ("yyyy-MM-dd".equalsIgnoreCase(format)) {
				return new Timestamp(simpleyyyyMMddFormat.parse(s).getTime());
			} else {
				return new Timestamp(System.currentTimeMillis());
			}
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 返回带格式的Timestamp，格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param s
	 * @return Timestamp
	 */
	public static Timestamp formatTimestamp(String s) {
		if (s == null) {
			return null;
		}

		try {
			return new Timestamp(simpleDateFormat.parse(s).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	public static Timestamp parseTimestamp(String s) {
		if (s == null) {
			return null;
		}

		try {
			return new Timestamp(simpleDateFormat.parse(s).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	public static Time parseTime(String s) {
		if (s == null) {
			return null;
		}

		try {
			return new Time(simpleTimeFormat.parse(s).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 传入yyyy-MM-dd HH:mm:ss，返回date
	 * 
	 * @param s
	 * @return
	 */
	public static Date parseDateAll(String s) {
		if (s == null) {
			return null;
		}

		try {
			return simpleDateFormat.parse(s);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 比较两个时间点的大小
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static int timeCompare(String t1, String t2) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(formatter.parse(t1));
			c2.setTime(formatter.parse(t2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int result = c1.compareTo(c2);
		return result;
	}

	/**
	 * 返回本周的开始和结束时间
	 * 
	 * @return
	 */
	public static Date[] getCurrentWeek() {
		Calendar ca = Calendar.getInstance();
		int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
		// 中国习惯：周一是一周的开始
		if (dayOfWeek == 1) {
			dayOfWeek = 7;
		} else {
			dayOfWeek--;
		}
		Calendar cal = (Calendar) ca.clone();

		cal.add(Calendar.DATE, 1 - dayOfWeek);
		Date date1 = cal.getTime();
		cal = (Calendar) ca.clone();
		cal.add(Calendar.DATE, 7 - dayOfWeek);
		Date date2 = cal.getTime();
		return new Date[] { date1, date2 };
	}

	/**
	 * 判断当前日期是星期几<br>
	 * <br>
	 * 
	 * @param pTime
	 *            修要判断的时间<br>
	 * @return dayForWeek 判断结果<br>
	 * @Exception 发生异常<br>
	 */
	public static int dayForWeek(String pTime) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 得到当前日期 YYYY-MM-DD
	 */
	public static String getNowDateYYDDMM() {
		return simpleyyyyMMddFormat.format(new Date());

	}

	/**
	 * 得到当前年 YYYY
	 */
	public static String getNowDateY() {
		return simpleDateFormatY.format(new Date());

	}

	/**
	 * 得到下一年 YYYY
	 */
	public static int getNextY() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);// 获取年份
		return year + 1;
	}

	/**
	 * 得到当前日期 YYYY-MM-DD HH:MM:SS
	 */
	public static String getNowDateYYDDMMHHMMSS() {
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 得到当前HH:mm
	 */
	public static String getNowDateHHMM() {
		return simpleHHmmTimeFormat.format(new Date());
	}

	/**
	 * 得到传入的date得到HH:mm
	 */
	public static String getDateHHMM(Date date) {
		return simpleHHmmTimeFormat.format(date);
	}

	/**
	 * 转换HHmm字符串
	 */
	public static Date parseDateHHMM(String hhmm) throws Exception {
		return simpleHHmmTimeFormat.parse(hhmm);
	}

	public static String getNowHHMMSS() throws Exception {
		return simpleTimeFormat.format(new Date());
	}

	public static Time getNowHHMMSSDate() throws Exception {
		return new Time(simpleTimeFormat.parse(simpleTimeFormat.format(new Date())).getTime());
	}

	public static Date parseHHMMSSDate(String hhmmss) throws Exception {
		return simpleTimeFormat.parse(hhmmss);
	}

	public static String formatDateHHMM(Time time) throws Exception {
		return simpleHHmmTimeFormat.format(time);
	}

	public static Time addTime(Time time, int minutes) throws Exception {
		return null;
	}

	/**
	 * 判断字符串是否是日期格式
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDate(String date) {
		try {
			return java.text.DateFormat.getDateInstance().parse(date) != null;
		} catch (java.text.ParseException e) {
			return false;
		}
	}

	/**
	 * 返回当前日期0点的毫秒数
	 * 
	 * @return Long
	 */
	public static Long getTodayTime() {
		Date date1 = new Date();

		@SuppressWarnings("deprecation")
		Date date2 = new Date(date1.getYear(), date1.getMonth(), date1.getDate());

		return date2.getTime();
	}

	/**
	 * 返回昨天日期0点的毫秒数
	 * 
	 * @return Long
	 */
	public static Long getYesterdayTime() {
		Date date1 = new Date();

		@SuppressWarnings("deprecation")
		Date date2 = new Date(date1.getYear(), date1.getMonth(), date1.getDate());

		return date2.getTime() - (24 * 60 * 60 * 1000);
	}

	public static Time parseTimeBystr(String param) {
		if (param == null) {
			return null;
		}

		try {
			return new Time(simpleHHmmTimeFormat.parse(param).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 判断两个日期是否是不同年份的同一天
	 * 
	 * @param date1
	 *            date1
	 * @param date2
	 *            date2
	 * @return
	 */
	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		boolean isSameMonth = cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}

	/**
	 * 将分钟时间格式化为**天**小时**分钟
	 * 
	 * @param minute
	 * @return
	 */
	public static String formatTime(int paramMinute) {
		long days = paramMinute / (60 * 24);
		long hours = (paramMinute % (60 * 24)) / 60;
		long minutes = paramMinute % 60;
		String result = "";
		if (days > 0) {
			result += days + "天";
		}
		if (hours > 0) {
			result += hours + "小时";
		}
		if (minutes > 0) {
			result += minutes + "分";
		}
		return result;
	}

	/**
	 * 格式化日期类型的参数
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(String time) {
		try {
			Date date = DateUtil.parseDateAll(time);
			return simpleDateFormat.format(date);
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// Time t = new Time(System.currentTimeMillis());
		//
		// System.out.println(format(t));
		// int a = timeCampare("","");
		System.out.println(DateUtil.getCurrentDateByDiff(600));
	}

	/**
	 * 格式化日期类型的参数
	 * 
	 * @param time
	 * @return month年月 month_in传入 无则返回本月 month_in_next下月
	 */
	@SuppressWarnings("deprecation")
	public static JSONObject getNextMonth(String yyyyMM) {
		JSONObject result = new JSONObject();
		Date now1 = new Date();
		try {
			Date now = new Date();
			String nowds = simpleDateFormatYM.format(now);
			now1 = simpleDateFormatYM.parse(nowds);
			if (yyyyMM != null && yyyyMM.length() > 0) {
				Date indate = simpleDateFormatYM.parse(yyyyMM);

				result.put("month", simpleDateFormatYMZH.format(indate));
				result.put("month_in", simpleDateFormat.format(indate));
				indate.setMonth(indate.getMonth() + 1);
				result.put("month_in_next", simpleDateFormat.format(indate));
				return result;

			}
		} catch (ParseException e) {

			result.put("month", simpleDateFormatYMZH.format(now1));
			result.put("month_in", simpleDateFormat.format(now1));
			now1.setMonth(now1.getMonth() + 1);
			result.put("month_in_next", simpleDateFormat.format(now1));
			return result;
		}

		result.put("month", simpleDateFormatYMZH.format(now1));
		result.put("month_in", simpleDateFormat.format(now1));
		now1.setMonth(now1.getMonth() + 1);
		result.put("month_in_next", simpleDateFormat.format(now1));
		return result;
	}

	@SuppressWarnings("deprecation")
	public static JSONObject getThisMonth(String yyyyMM) {
		JSONObject result = new JSONObject();
		Date now1 = new Date();
		try {
			Date now = new Date();
			String nowds = simpleDateFormatYM.format(now);
			now1 = simpleDateFormatYM.parse(nowds);
			if (yyyyMM != null && yyyyMM.length() > 0) {
				Date indate = simpleDateFormatYM.parse(yyyyMM);

				result.put("month", simpleDateFormatYMZH.format(indate));
				result.put("month_c", yyyyMM);
				result.put("month_first", yyyyMM + "-01");
				Calendar a = Calendar.getInstance();
				a.set(Calendar.YEAR, indate.getYear());
				a.set(Calendar.MONTH, indate.getMonth());
				a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
				a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
				result.put("month_last", yyyyMM + "-" + a.get(Calendar.DATE));
				result.put("days", a.get(Calendar.DATE));
				return result;

			}
		} catch (ParseException e) {
			String nd = simpleDateFormatYM.format(now1);
			result.put("month", simpleDateFormatYMZH.format(now1));
			result.put("month_c", nd);
			result.put("month_first", nd + "-01");
			Calendar a = Calendar.getInstance();
			a.set(Calendar.YEAR, now1.getYear());
			a.set(Calendar.MONTH, now1.getMonth());
			a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
			a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
			result.put("month_last", nd + "-" + a.get(Calendar.DATE));
			result.put("days", a.get(Calendar.DATE));
			return result;
		}

		String nd = simpleDateFormatYM.format(now1);
		result.put("month", simpleDateFormatYMZH.format(now1));
		result.put("month_c", nd);
		result.put("month_first", nd + "-01");
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, now1.getYear());
		a.set(Calendar.MONTH, now1.getMonth());
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		result.put("month_last", nd + "-" + a.get(Calendar.DATE));
		result.put("days", a.get(Calendar.DATE));
		return result;
	}

	/**
	 * 得到这个月的第一天和今天
	 * 
	 * @param time
	 * @return month年月 month_in传入 无则返回本月 month_in_next下月
	 */
	public static JSONObject getMonthDate() {
		JSONObject result = new JSONObject();
		Date now1 = new Date();
		String from = simpleDateFormatYM.format(now1) + "-01";
		String to = simpleyyyyMMddFormat.format(now1);
		result.put("month", from + " -- " + to);
		result.put("from", from);
		result.put("to", to);
		return result;
	}

	/**
	 * 计算两个日期相差天数
	 * 
	 * @param date
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date date, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	public static String getCurrentDateByDiff(int seconds) {
		Date now = new Date();
		long times = now.getTime() - seconds * 1000;
		Date result = new Date(times);
		return DateUtil.format(result, "yyyy-MM-dd HH:mm:ss");
	}
}
