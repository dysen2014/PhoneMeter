package com.dysen.load;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dysen.gifView.util.GifAction;
import com.dysen.gifView.util.GifView;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.qj.wMeter.R;

public class WelcomeActivity extends MyActivityTools implements AnimationListener {
    private ImageView imageView = null;
    private Animation alphaAnimation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //实现淡入淡出的效果
        overridePendingTransition(android.R.anim.slide_in_left,android.
                R.anim.slide_out_right);


        //类似 iphone 的进入和退出时的效果
//        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        setContentView(R.layout.activity_welcome);
        //透明状态栏
        StatusBarUtil.setTransparent(this);

        imageView = (ImageView) findViewById(R.id.welcome_image_view);
        alphaAnimation = AnimationUtils.loadAnimation(this,
                R.anim.welcome_alpha);
        alphaAnimation.setFillEnabled(true); // 启动Fill保持
        alphaAnimation.setFillAfter(true); // 设置动画的最后一帧是保持在View上面
        imageView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(this); // 为动画设置监听

        myStartGif();
    }

    /**
     *	dysen
     *	2015-7-11 上午11:45:15
     *	info: 启动 动画 gif 播放
     */
    public void myStartGif() {

        // 把该 Activity 添加到 activity列表里 方便退出时一下子退出所有activity
        // QuitAllActivity.getInstance().addActivity(this);

        // 从xml中得到GifView的句柄
        GifView gf1 = (GifView) findViewById(R.id.gif1);
        // 设置Gif图片源
        gf1.setGifImage(R.drawable.xr);

        // 设置显示的大小，拉伸或者压缩
        System.out.println("屏幕宽度:"+myScreenWidth()+"\n屏幕高度："+myScreenHeight());
        gf1.setShowDimension(300, 300);

        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        gf1.setGifImageType(GifView.GifImageType.SYNC_DECODER);

        new GifAction() {

            @Override
            public void parseOk(boolean arg0, int arg1) {
                System.out.println(arg0+"__________________"+arg1);

                if (arg1 == -1 || arg0 == true) {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                }
            }
        };
    }

    /**
     * 获取屏幕的宽 sen dy 2015-1-28 下午12:34:17
     */
    public int myScreenWidth() {

        // 要获取屏幕的宽和高等参数，首先需要声明一个DisplayMetrics对象，屏幕的宽高等属性存放在这个对象中
        DisplayMetrics DM = new DisplayMetrics();
        // 获取窗口管理器,获取当前的窗口,调用getDefaultDisplay()后，其将关于屏幕的一些信息写进DM对象中,最后通过getMetrics(DM)获取
        getWindowManager().getDefaultDisplay().getMetrics(DM);

        float scale, pxValue;

        pxValue = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        scale = this.getResources().getDisplayMetrics().density;
        int wdip = (int) (pxValue / scale + 0.5f);

        return wdip;//(int) pxValue;
    }

    /**
     * 获取屏幕的高 sen dy 2015-1-28 下午12:34:17
     */
    public int myScreenHeight() {

        float scale, pxValue;

        pxValue = getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        scale = this.getResources().getDisplayMetrics().density;
        int hdip = (int) (pxValue / scale + 0.5f);

        return hdip;//(int) pxValue;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return false;
    }

}
