package com.dysen.myUtil;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.dysen.mylibrary.utils.kjframe.KJActivity;
import com.dysen.mylibrary.utils.kjframe.KJDB;
import com.dysen.mylibrary.utils.kjframe.KJHttp;
import com.dysen.mylibrary.utils.kjframe.http.HttpParams;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBleInfo;
import com.dysen.table.tBook;
import com.dysen.table.tLogin;
import com.dysen.table.tMeter;
import com.dysen.table.tMeterId;
import com.dysen.table.tPaymentHistory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作者：沈迪 [dysen] on 2015-12-21 16:33.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：自定义 Activity 样式
 */
public class MyActivityTools extends KJActivity {

    public static tLogin login;
    public static tMeter meter;
    public static tBook book;
    public static tPaymentHistory payment;
    public static tBleInfo bleInfo;
    public static tMeterId meterId;
    public static tLogin loginT;

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
        login = new tLogin();
        meter = new tMeter();
        book = new tBook();
        payment = new tPaymentHistory();
        bleInfo = new tBleInfo();
        meterId = new tMeterId();

        context = this;
        dbBook = KJDB.create(this, "tBook");
        dbMeter = KJDB.create(this, "tMeter");
//        db = FinalDb.create(this, "TempLogin");
        dbPayment = KJDB.create(this, "tPayment");
        dbLogin = KJDB.create(this, "tLogin");

        dbBle = KJDB.create(this, "tBleInfo");
        dbMeterId = KJDB.create(this, "tMeterId");

    }

    private void setTile(String s) {
        ((TextView)findViewById(R.id.tv_hint)).setText(s);
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
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
