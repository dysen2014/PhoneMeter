package com.dysen.mylibrary.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 作者：沈迪 [dysen] on 2016-03-17 11:36.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class PercentDemo {

    /**
     * 获取百分比
     * @param x 分子
     * @param total 分母
     * @param s 保留小数点格式   如 "0.00%"
     * @return 求百分比
     */
    public static String getPercent(long x, long total, String s){
        String result="";//接受百分比的值
        double x_double = x*1.0;
        double tempresult = (double)x/(double)total;
        //NumberFormat nf = NumberFormat.getPercentInstance();     注释掉的也是一种方法
        //nf.setMinimumFractionDigits( 2 );        保留到小数点后几位
        DecimalFormat df1 = new DecimalFormat(s);    //##.00%   百分比格式，后面不足2位的用0补齐
        //result=nf.format(tempresult);
        result= df1.format(tempresult);
        System.out.println(x+"/"+total+"---百分比:"+result);
        return result;
    }

    /**
     * 获取百分比
     * @param x 分子
     * @param total 分母
     * @param i 保留小数点位数
     * @return 求百分比
     */
    public static String getPercent(long x, long total, int i){
        String result="";//接受百分比的值
        double x_double = x*1.0;
        double tempresult = (double)x/(double)total;
        NumberFormat nf = NumberFormat.getPercentInstance();     //注释掉的也是一种方法
        nf.setMinimumFractionDigits(i);        //保留到小数点后几位
//        DecimalFormat df1 = new DecimalFormat("0%");    //##.00%   百分比格式，后面不足2位的用0补齐
        result=nf.format(tempresult);
//        result= df1.format(tempresult);
        System.out.println(x+"/"+total+"---百分比:"+result);
        return result;
    }
}
