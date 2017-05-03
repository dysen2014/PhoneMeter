package com.dysen.mylibrary.utils.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.mylibrary.R;
import com.dysen.mylibrary.utils.tastytoast.TastyToast;

/**
 * Created by dy on 2016-08-26.
 * Toast统一管理类
 */
public class ToastUtils
{

    private ToastUtils()
    {
		/* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    /**
     *  TastyToast  不一样的 Toast
     * @param context
     * @param message   要显示的toast内容
     * @param type  要显示的toast类型
     */
    public static void showShort(Context context, String message, int type){

        if (isShow)
            TastyToast.makeText(context, message, 0, type);
    }

    /**
     *  TastyToast  不一样的 Toast
     * @param context
     * @param message   要显示的toast内容
     * @param type  要显示的toast类型
     */
    public static void showLong(Context context, String message, int type){

        if (isShow)
        TastyToast.makeText(context, message, 1, type);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message)
    {
        if (isShow)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration)
    {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration)
    {
        if (isShow)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * Adding an Image to the Standard Toast
     * 在标准显示方式基础上添加图片
     * @param context
     * @param message 标题
     * @param duration 显示长短(1/0)
     * @param iconId 图片
     */
    public static void show(Context context, CharSequence message, int duration, int iconId) {

        if (isShow) {
            Toast toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastView = (LinearLayout) toast.getView();
            ImageView imageCodeProject = new ImageView(context);
            imageCodeProject.setImageResource(iconId);
            toastView.setBackgroundColor(Color.parseColor("#00000000"));
//        toastView.setBackgroundColor(Color.parseColor(bgColor));
            toastView.addView(imageCodeProject, 0);
            toast.show();
        }
    }

    /**
     * Adding an Image to the Standard Toast
     * 在标准显示方式基础上添加图片
     * @param context
     * @param message 标题
     * @param duration 显示长短(1/0)
     * @param iconId 图片
     */
    public static void show(Context context, int message, int duration, int iconId) {

        if (isShow) {
            Toast toast = Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastView = (LinearLayout) toast.getView();
            ImageView imageCodeProject = new ImageView(context);
            imageCodeProject.setImageResource(iconId);
            toastView.setBackgroundColor(Color.parseColor("#00000000"));
//        toastView.setBackgroundColor(Color.parseColor(bgColor));
            toastView.addView(imageCodeProject, 0);
            toast.show();
        }
    }

    /**
     * Creating a Toast with Custom Layout
     * 创建自定义的提示信息方式
     * @param context
     * @param title 标题
     * @param str 内容
     */
    public static void show(Context context, String title, String str ) {

        View v = LayoutInflater.from(context).inflate(R.layout.activity_toast, null);
//        ImageView image = (ImageView) layout.findViewById(R.id.tvImageToast);
//        image.setImageResource(R.drawable.page);
        WebView wv = (WebView) v.findViewById(R.id.wb);
        wv.getSettings().setJavaScriptEnabled(true);
    	/* Prevent WebView from Opening the Browser */
        wv.setWebViewClient(new InsideWebViewClient());

        new InsideWebViewClient().shouldOverrideUrlLoading(wv, "http://img14.poco.cn/mypoco/myphoto/20130302/17/3926359420130302172458075_640.jpg");

        TextView txtTitle = (TextView) v.findViewById(R.id.tv_title);
        txtTitle.setText(title);
        TextView text = (TextView) v.findViewById(R.id.tv_text);
        text.setText(str);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.RIGHT | Gravity.TOP, 12, 40);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(v);
        toast.show();
    }

    /* Class that prevents opening the Browser */
    private static class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
