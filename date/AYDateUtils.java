package com.sunsheen.cns.edt.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

public class AYDateUtils {

	public static String YMD = "yyyy-MM-dd";
	public static String YMDHM = "yyyy-MM-dd HH:mm";
	public static String YMDHMS = "yyyy-MM-dd HH:mm:ss";

	public static String CHINESE_YMD = "yyyy年MM月dd日";
	public static String FORWARDSLASH_YMD = "yyyy/MM/dd";

	/**
	 * 开始时间结束时间比较
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Boolean startEndTimeCompare(Map<String, Object> params) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDate = sdf.parse(params.get("starttime").toString());// 任务开始时间
		Date endDate = sdf.parse(params.get("endtime").toString());// 任务结束时间

		if (startDate.getTime() >= endDate.getTime()) {
			return false;
		} else {
			return true;
		}
	}

	/***
	 * 功能描述：日期转换cron表达式
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateByPattern(Date date) {
		String dateFormat = "ss mm HH dd MM ? yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	/**
	 * 字符串转日期
	 * 
	 * @param date
	 *            日期
	 * @param type
	 *            日期的格式 例如："yyyy-MM-dd HH:mm:ss" yyyy-MM-dd
	 */
	public static Date strToDate(String str, String type) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Date date = null;
		date = sdf.parse(str);
		return date;
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 *            日期
	 * @param type
	 *            日期的格式 例如："yyyy-MM-dd HH:mm:ss"
	 */
	public static String getFisrtDayOfMonth(Date date, String type) {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String firstDayOfMonth = sdf.format(date.getTime());

		return firstDayOfMonth;
	}

	/**
	 * 验证日期格式是否满足要求
	 * 
	 * @param str
	 *            需要验证的日期格式
	 * @param formatString
	 *            验证的标准格式，如：（yyyy/MM/dd HH:mm:ss）
	 * @return 返回验证结果
	 */
	public static boolean isValidDate(String str, String formatString) {
		// 指定日期格式，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
		return true;
	}

}
