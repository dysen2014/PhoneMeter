package com.dysen.mylibrary.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.dysen.mylibrary.R;
import com.dysen.mylibrary.utils.kjframe.KJActivity;
import com.dysen.mylibrary.utils.kjframe.KJDB;
import com.dysen.mylibrary.utils.kjframe.KJHttp;
import com.dysen.mylibrary.utils.kjframe.http.HttpParams;


/**
 * 作者：沈迪 [dysen] on 2015-12-21 16:33.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：自定义 Activity 样式
 */
public class MyActivityTools extends KJActivity {

    public static KJHttp kjt;
    public static HttpParams params;
    public static KJDB dbPayment, dbBook, dbMeter, db, dbLogin, dbBle, dbMeterId;
    public static String HTTP_IP;
    public static Context context;

    //    public static int[] icolor = new int[]{R.color.pink_, R.color.blue_, R.color.purple_, R.color.yellow_,
//            R.color.orange_, R.color.green_, R.color.darkcyan};
    public static int[] icolor = new int[]{R.drawable.img_pink, R.drawable.img_blue, R.drawable.img_purple,
            R.drawable.img_yellow, R.drawable.img_orange, R.drawable.img_green, R.drawable.img_darkcyan };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //实现淡入淡出的效果
        overridePendingTransition(android.R.anim.slide_in_left,android.
                R.anim.slide_out_right);

        //类似 iphone 的进入和退出时的效果
//        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

        HTTP_IP = this.getText(R.string.HTTP_IP).toString();
        System.out.println("ip:"+HTTP_IP);

        kjt = new KJHttp();
        params = new HttpParams();

        context = this;
    }

    public Object getDbObj(Object obj, String dbName){

        obj = KJDB.create(this, dbName);
        return obj;
    }

    private void setTile(String s) {
//        ((TextView)findViewById(R.id.tv_hint)).setText(s);
    }

    public static void initBubble(View v, String str) {
        int[] location = new int[2];
        BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(context).inflate(R.layout.layout_sample_popup_bubble, null);
        PopupWindow popupWindow = BubblePopupHelper.create(context, bubbleLayout);
        TextView tvBubble = (TextView) bubbleLayout.findViewById(R.id.txt_bubble);
        tvBubble.setText(str);
        v.getLocationInWindow(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], v.getHeight() + location[1]);
    }

    @Override
    public void setRootView() {

    }
}
