package com.dysen.mylibrary.test;

import com.dysen.mylibrary.utils.MyUtils;

import java.util.List;

/**
 * Created by dysen_000 on 2016-06-21.
 * Info：
 */
public class Test {

    public static void main(String[] args) {

        System.out.println(MyUtils.HexSUM("0001000400000022000803EAAE01"));
        getTel();
    }

    private static void getTel() {

        //联系方式，请阅读代码
        int[] arr = new int[]{0, 1, 2, 3, 4, 5, 6, 9};
        int[] index = new int[]{1, 3, 2, 7, 4, 1, 6, 2, 5, 0, 1};
        String tel = "";

        String[] reData = null;

        for (int i : index){
            tel+=arr[i];
        }
        System.out.println("联系方式："+tel);

        String ss = "3C0001003C000100060000003C2C330099";
        List<Integer> l = MyUtils.seachString(ss, "3C");

        for (int i=0; i<l.size()-1;i++){
            System.out.println("three:"+ss.substring(l.get(i), l.get(i+1)));
            reData[i] = ss.substring(l.get(i), l.get(i+1))+"\n";

        }
        System.out.println("three:"+ss.substring(l.get(l.size()-1)));
        ss="";
        for (int i =0; i<reData.length;i++){
            ss+= reData[i]+"\n";
        }
        ss+=ss.substring(l.get(l.size()-1));

        System.out.println("reData:"+ss);
    }
}
