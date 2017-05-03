package com.dysen.myUtil;

import java.util.Random;

/**
 * Created by dysen on 2015-10-29.
 */
public class MyRandom {

    public static void main(String[] args) {

        while (true){
//            System.out.println(random2Int(65535) + "----------------" + random2Hex(65535));
            System.out.println(random2Int(2, 10) + "----------------" + random2Hex(2, 10));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取随机数 int类型
     * @param i
     * @return
     */
    public static int random2Int(int i){
        Random random = new Random();
        int randomData =  random.nextInt(i);

        return randomData;
    }

    /**
     * 获取 i~j 的随机数 int类型
     * @param i
     * @param j
     * @return
     */
    public static int random2Int(int i, int j){
        Random random = new Random();
        int randomData = random.nextInt(j);
        if (i >= j)
            ;
        else if(randomData < i)
           randomData = randomData + i;
        return randomData;
    }

    /**
     * 获取随机数 返回String类型 Hex字符
     * @param i
     * @return
     */
    public static String random2Hex(int i){
        Random random = new Random();
        int randomData =  random.nextInt(i);
        String rd = (Integer.toHexString(randomData)).toUpperCase();
        if (rd.length() < 2)
            rd = "0" + rd;
        System.out.println("随机数："+rd);
        return rd;
    }

    /**
     * 获取 i~j 的随机数 返回String类型 Hex字符
     * @param i
     * @return
     */
    public static String random2Hex(int i, int j){
        Random random = new Random();
        int randomData =  random.nextInt();
        if (i >= j)
            ;
        else if(randomData < i)
            randomData = randomData + i;
        String rd = (Integer.toHexString(randomData)).toUpperCase();
        if (rd.length() < 2)
            rd = "0" + rd;
//        System.out.println("随机数："+rd);
        return rd;
    }
}
