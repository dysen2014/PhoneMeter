package com.dysen.type.about;

import android.os.Bundle;
import android.view.View;

import com.dysen.myUtil.MyActivityTools;
import com.dysen.mylibrary.utils.StatusBarUtil;
import com.dysen.qj.wMeter.R;

/**
 * 应用介绍
 */
public class AppIntroActivity extends MyActivityTools {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        //透明状态栏
        StatusBarUtil.setTransparent(this);
        (this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
