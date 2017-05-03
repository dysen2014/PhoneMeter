package com.dysen.mylibrary.utils;

/**
 * Created by dysen_000 on 2016-07-15 10:28.
 * Email：dysen@outlook.com
 * Info：自定义工具类
 */
public class Utils {

    public static void main(String[] args) {
        System.out.println(retainRedix(3d, 2));
    }

    /**
     * info 保留小数点
     * @param d
     * @param i
     * @return
     */
    public static String retainRedix(Double d, int i){

        String result="";

        System.out.println(String.format(d.toString(), "2d%"));
        return result;
    }
}
