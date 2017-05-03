package com.dysen.myUtil;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 作者：沈迪 [dysen] on 2016-04-09 11:19.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class ScreenUtils extends MyActivityTools{
    /**
     * 获取屏幕的宽 sen dy 2015-1-28 下午12:34:17
     */
    public int myScreenWidth(Context context) {

        // 要获取屏幕的宽和高等参数，首先需要声明一个DisplayMetrics对象，屏幕的宽高等属性存放在这个对象中
        DisplayMetrics DM = new DisplayMetrics();
        // 获取窗口管理器,获取当前的窗口,调用getDefaultDisplay()后，其将关于屏幕的一些信息写进DM对象中,最后通过getMetrics(DM)获取
        getWindowManager().getDefaultDisplay().getMetrics(DM);

        float scale, pxValue;

        pxValue = context.getResources().getDisplayMetrics().widthPixels;
        scale = context.getResources().getDisplayMetrics().density;
        int wdip = (int) (pxValue / scale + 0.5f);

        return wdip;//(int) pxValue;
    }

    /**
     * 获取屏幕的高 sen dy 2015-1-28 下午12:34:17
     */
    public int myScreenHeight(Context context) {

        float scale, pxValue;

        pxValue = context.getResources().getDisplayMetrics().heightPixels;
        scale = context.getResources().getDisplayMetrics().density;
        int hdip = (int) (pxValue / scale + 0.5f);

        return hdip;//(int) pxValue;
    }

}
