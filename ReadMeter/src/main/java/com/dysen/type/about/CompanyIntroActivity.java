package com.dysen.type.about;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.ToastDemo;
import com.dysen.mylibrary.utils.StatusBarUtil;
import com.dysen.qj.wMeter.R;

/**
 * 公司简介
 */
public class CompanyIntroActivity extends MyActivityTools {

    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_intro);

        //透明状态栏
        StatusBarUtil.setTransparent(this);
        (findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        phone = ((TextView) findViewById(R.id.tv_tel)).getText().toString();

        findViewById(R.id.tv_tel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phone.length() != 0) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:\n" + phone));

                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    startActivity(intent);
                } else {
                    ToastDemo.myHint(CompanyIntroActivity.this, "请输入号码", 4);
                }
            }
        });
    }
}
