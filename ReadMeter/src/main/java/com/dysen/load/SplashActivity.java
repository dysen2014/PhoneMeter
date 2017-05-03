package com.dysen.load;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.dysen.myUtil.StatusBarUtil;
import com.dysen.qj.wMeter.R;

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = View.inflate(this, R.layout.start, null);
        setContentView(view);
        //透明状态栏
        StatusBarUtil.setTransparent(this);

        //实现淡入淡出的效果
//		overridePendingTransition(android.R.anim.slide_in_left,android.
//				R.anim.slide_out_right);

        //类似 iphone 的进入和退出时的效果
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.1f,0.9f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}

        });
    }

    /**
     * 跳转到...
     */
    private void redirectTo(){
        Intent intent = new Intent(this, UserDemo.class);
        startActivity(intent);
        finish();
    }

}
