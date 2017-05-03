package com.dysen.mylibrary.utils;

/**
 * 
 * 邮箱: dysen@outlook.com | 2207922029@qq.com
 * 
 * 作者: 沈迪 [ sendy ]
 * 
 * 日期: 2015-5-6 下午5:59:37
 * 
 * 描述:是否是全数字
 * 
 */
public class IsNumeric {

	/**
	 * dysen 2015-5-6 下午5:59:37 info:
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * dysen 2015-5-6 下午6:00:08 info: 判断该字符串 里是否含有字符(是否是全数字)
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {

			if (!Character.isDigit(str.charAt(i))) {

				return false;
			}
		}
		return true;
	}
}
