package com.dysen.mylibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateUtils {

	/**
	 * 返回当月的第一天 YYYY-MM-DD
	 * 
	 * @param date
	 * @return
	 */
	public static String getFristDateOfMonth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		Calendar c = Calendar.getInstance();
		String s = null;
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		date = c.getTime();
		s = sdf.format(date);
		// System.out.println(s + "--");
		return s;
	}

	/**
	 * 返回下月的第一天yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getNextDateOfMonth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String s = null;
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
		date = c.getTime();
		s = sdf.format(date);
		// System.out.println(s);
		return s;
	}

	/**
	 * 将时间转成字指定格式符串
	 * 时间格式 yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @param s
	 * @return
	 */
	public static String formatDate(Date date, String s) {
		SimpleDateFormat sdf = new SimpleDateFormat(s);
		return sdf.format(date);
	}

	/**
	 * 将字符串转时间
	 * 
	 * @param str
	 * @return
	 */
	public static Date parseDate(String str, String format) {
		Date d = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			d = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("时间格式不对");
		}
		return d;
	}

	/**
	 * 将星期转成01、02、03、04、05、06、07
	 * 
	 * @param date
	 *            format后的字符串
	 * @return
	 */
	public static String weekToNumber(String date) {
		if (date.contains("星期一"))
			date = date.replace("星期一", "01");

		else if (date.contains("星期二"))
			date = date.replace("星期二", "02");

		else if (date.contains("星期三"))
			date = date.replace("星期三", "03");

		else if (date.contains("星期四"))
			date = date.replace("星期四", "04");

		else if (date.contains("星期五"))
			date = date.replace("星期五", "05");

		else if (date.contains("星期六"))
			date = date.replace("星期六", "06");

		else if (date.contains("星期日"))
			date = date.replace("星期日", "07");

		return date;
	}

	/**
	 * 比较两个时间
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long compareDateByDay(String date1, String date2) {
		String format = "yyyy-MM-dd";
		long l = parseDate(date2, format).getTime() - parseDate(date2, format).getTime();
		l = l / (3600 * 24 * 1000);
		// System.out.println(l);
		return l;
	}

	/**
	 * 对时候进行加减操作
	 * 
	 * @param date
	 * @param field
	 *            y,M,d,H,m,s
	 * @param val
	 * @return
	 */
	public static Date addDate(Date date, String field, int val) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		switch (field) {
		case "y":
			c.set(Calendar.YEAR, c.get(Calendar.YEAR) + val);
		case "M":
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + val);
		case "d":
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) + val);
		case "H":
			c.set(Calendar.HOUR, c.get(Calendar.HOUR_OF_DAY) + val);
		case "m":
			c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + val);
		case "s":
			c.set(Calendar.SECOND, c.get(Calendar.SECOND) + val);
		}
		return c.getTime();
	}

	/**
	 * 获取时间对象的年、月、日、时、分、秒
	 * 
	 * @param date
	 * @param field y,M,d,H,m,s
	 * @return
	 */
	public static int getDateField(Date date, String field) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		switch (field) {
		case "y":
			return c.get(Calendar.YEAR);
		case "M":
			return c.get(Calendar.MONTH) + 1;
		case "d":
			return c.get(Calendar.DATE);
		case "H":
			return c.get(Calendar.HOUR_OF_DAY);
		case "m":
			return c.get(Calendar.MINUTE);
		case "s":
			return c.get(Calendar.SECOND);
		}
		return 0;
	}

	public static void main(String[] args) {
		 Date da = new Date();
		 Date d = addDate(da, "M", 12);
		System.out.println(da.toLocaleString()+"----------------"+d.toLocaleString());
		// System.out.println(formatDate(da, "yyyy MM dd HH:mm:ss:sss"));
		// System.out.println(formatDate(d, "yyyy MM dd HH:mm:ss:sss"));
//		System.out.println(getDateField(new Date(), "s"));

	}
}
