package com.dysen.myUtil;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

/**
 * Created by dysen on 2016-05-21.
 * 工具类
 */
public class MyTools {

    MyTools(){

    }

    /**
     * 设置字符 高亮显示
     * @param str 字符
     * @param intColor   颜色(id)Color.RED
     * @param start 字符开始 索引
     * @param end   字符结束 索引
     * @return
     */
    public static SpannableStringBuilder setStrhHighlighted(String str, int intColor, int start, int end) {

        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(intColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return style;
    }
}
