package com.dysen.myUtil.test;

import com.dysen.myUtil.MyUtils;
import com.dysen.mylibrary.utils.MyIpConvert;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dysen_000 on 2016-06-21.
 * Info：
 */
public class Test {

    public static void main(String[] args) {

        String s = "Photographs of villages like Pescara del Tronto, one of the closest villages to the fault, show the odd semi-intact house sticking up like a bad tooth from piles of rubble.\n" +
                "Yet this was an earthquake of magnitude 6.2 -- a strong event, but by no means a great one in global terms. Comparing it to the Tohoku earthquake in Japan five years ago, this is smaller in energy terms by about 27,000 times. This is why the familiar magnitude scale (conventionally miscalled \"the Richter Scale,\" which is not a scientific term) has no units -- the numbers get astronomical too quickly. In fact, on average, earthquakes this size happen somewhere in the world about every three days.\n";
//        System.out.println(s.getBytes().length);
//        System.out.println(MyUtils.HexSUM("0001000400000022000803EAAE01"));
//        getTel();
//        System.out.println(new Date().toString()+"\n"+new Date().toLocaleString()+"\n"+new Date().toGMTString());

//        getPkgHeager("3CDF010011223C020100DD03");

        System.out.println((float) 8/10+""+8*0.1);
    }

    public static String getPkgHeager(String str) {

        Pattern p=Pattern.compile("3C");
        Matcher m=p.matcher(str);
        while(m.find()){
            System.out.println(m.group()+""+m.groupCount());
            return m.group();
        }
        return "";
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
