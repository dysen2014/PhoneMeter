package com.dysen.nfcdemo;

import com.dysen.myUtil.MyStringConversion;
import com.dysen.myUtil.MyUtils;

/**
 * 作者：沈迪 [dysen] on 2016-05-04 15:36.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class MeterIdUtil {

    public static void main(String[] args) {

        setMeterIdFormat(11110301l);
        getMeterId(setMeterIdFormat(8208l));

    }

    /**
     * 设置 meterId 号 (如 00	 00	00	00	20	10	FF	FF	FF	FF	DF	EF	00	00	72	6D)
     * @param l
     * @return
     */
    public static String setMeterIdFormat(Long l){

        String strMeterId = Long.toHexString(l).toUpperCase();
        System.out.println(strMeterId.toUpperCase());
        String str = MyStringConversion.myInverseStr(strMeterId, 12).toUpperCase();
//        System.out.println("******1*********"+str);
        char[] ch = MyUtils.Byte2Hex(str);
        for (int i = 0; i < ch.length; i++) {
//            System.out.println(Long.toHexString(ch[i])+"------------------------"+ Long.toHexString(0xff - ch[i]));
            str += Long.toHexString(0xff - ch[i]).toUpperCase();
        }

        str+= "000072";
//        System.out.println("*******2********"+str);
        String checkSum = MyUtils.HexSUM(str).toUpperCase();
        str += checkSum;
        System.out.println("********3*******"+str.toLowerCase());
        return str.toLowerCase();
    }

    /**
     * 获得 meterId 号 (如 10010)
     * info 格式1 (反码)
     * @param s
     * @return
     */
    public static Long getMeterId(String s){

        String str = s.substring(0, 12).toUpperCase();

        long l = Long.valueOf(str, 16);
//        System.out.println("********l*******"+l);
        return l;
    }

    /**
     * 获得 meterId 号 (如 31313033)
     * info 格式2
     * @param s
     * @return
     */
    public static Long getMeterId2(String s){

        String str = s.substring(0, 14).toUpperCase();
        String ss = "";

        for (int i=0; i<str.length()-1; i+=2){
            ss += Integer.parseInt(str.substring(i, i+2))-30;
//            System.out.println("ss:"+ss);
        }

//        System.out.println("********l*******"+ss);
        return Long.parseLong(ss);
    }
}
