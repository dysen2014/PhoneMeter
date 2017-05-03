package com.dysen.myUtil;

/**
 * sen dy 2015-1-29 上午11:51:27 info: 字符转换(高地位)
 */
public class MyStringConversion {

	/**
	 * sen dy 2015-1-29 上午11:51:27
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 把普通(十六进制)字符串 以十六进制显示 sen dy 2014-12-2 下午5:10:12
	 */
	public static String myStr(String str) {
		String s = "";
		// String str = //"3C0001000100000022000803EAAE01";
		// "3C000000000000007900080901000550380260205018";
		for (int i = 0; i < str.length() - 1; i++, i++) {
			s += str.substring(i, i + 2) + " ";
		}
		// System.out.println(s);
		return s;
	}

	/**
	 * 变长字符串 00 sen dy 2015-2-11 上午8:54:51
	 */
	public static String myVariableStr(int l) {
		String str = "";
		for (int i = 0; i < l; i++) {
			str += 0;
		}
		return str;
	}

	/**
	 * 通过把十进制的1转换成01， 0001 等 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverseStr(String id, int len) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(len);
		String str = "";
		char[] ch = new char[len];

		if ("".equals(id)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strId = id;

			str = strSet1.substring(0, strSet1.length() - strId.length())
					+ strId;
			// ch = str.toCharArray();
			// str = StringHandler.myConver(ch).toUpperCase();
		}
		return str;
	}

	/**
	 * 通过把十进制的1转换成01， 0100 等 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverseStrConver(String id, int len) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(len);
		String str = "";
		char[] ch = new char[len];

		if ("".equals(id)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strId = String.valueOf(id);

			str = strSet1.substring(0, strSet1.length() - strId.length())
					+ strId;
			ch = str.toCharArray();
			str = myConver(ch).toUpperCase();
		}
		return str;
	}

	/**
	 * 通过把十进制的1转换成01， 00 01 等 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverse(long l, int len) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(len);
		String str = "";
		char[] ch = new char[len];

		if ("".equals(l)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strId = Long.toHexString(Long.parseLong(l + ""));

			ch = (strSet1.substring(0, strSet1.length() - strId.length()) + strId)
					.toCharArray();

			str = new String(ch);
			// StringHandler.myConver(ch).toUpperCase();
		}
		return str;
	}

	public static String myInverseConver2(String l, int len) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(len);
		String strNet = "";
		char[] ch = new char[len];

		if ("".equals(l)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strNetId = l;

			ch = (strSet1.substring(0, strSet1.length() - strNetId.length()) + strNetId)
					.toCharArray();

			strNet = myConver(ch).toUpperCase();
		}
		return strNet;
	}

	/**
	 * 通过把十进制的1转换成01, 01 00, 01 00 00 00 等 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverseConver(String id, int len) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(len);
		String strNet = "";
		char[] ch = new char[len];

		if ("".equals(id)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strNetId = id;

			ch = (strSet1.substring(0, strSet1.length() - strNetId.length()) + strNetId)
					.toCharArray();

			strNet = myConver(ch).toUpperCase();
		}
		return strNet;
	}

	/**
	 * 通过把十进制的网号逆置转换 sen dy 2015-1-29 下午3:35:35
	 */
	public static String myInverseNetId(long mNetId, int l) {

		String strSet1 = "0000";
		strSet1 = myVariableStr(l);
		String strNet = "";
		char[] chNetId = new char[4];

		if ("".equals(mNetId)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strNetId = Long.toHexString(Long.parseLong(mNetId + ""));

			chNetId = (strSet1.substring(0,
					strSet1.length() - strNetId.length()) + strNetId)
					.toCharArray();

			strNet = myConver(chNetId).toUpperCase();
		}
		return strNet;
	}

	/**
	 * 通过把十进制的表号逆置转换 sen dy 2015-1-29 下午3:35:19
	 */
	public static String myInverseMeterId(long mMeterId, int l) {

		String strSet2 = "00000000";
		strSet2 = myVariableStr(l);
		String strMeter = "";
		char[] chMeterNum = new char[8];

		if ("".equals(mMeterId)) {
			// alertDemo("参数不能为空").show();
		} else {
			String strMeterNum = Long
					.toHexString(Long.parseLong(mMeterId + ""));
			chMeterNum = (strSet2.substring(0,
					strSet2.length() - strMeterNum.length()) + strMeterNum)
					.toCharArray();

			strMeter = myConver(chMeterNum).toUpperCase();
		}
		return strMeter;
	}

	/**
	 * 逆置
	 */
	public static String myConver(char[] ch) {
		// char[] ch = {'1','2','3','4','5','6','7','8'};
		char[] temp = new char[1];

		for (int i = 0; i < ch.length / 2; i++) {
			temp[0] = ch[i];
			ch[i] = ch[ch.length - i - 1];
			ch[ch.length - i - 1] = temp[0];

		}
		for (int i = 0; i < ch.length; i++) {
			if (i % 2 == 0) {
				temp[0] = ch[i];
				ch[i] = ch[i + 1];
				ch[i + 1] = temp[0];
			}
		}

		System.out.println("...." + new String(ch));
		return new String(ch);
	}
}
