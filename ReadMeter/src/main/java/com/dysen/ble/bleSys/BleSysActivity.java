package com.dysen.ble.bleSys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dysen.ble.BleReadMeterActivity;
import com.dysen.ble.BleWirelessDebugActivity;
import com.dysen.ble.LoraReadMeterActivity;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.mylibrary.utils.StatusBarUtil;
import com.dysen.qj.wMeter.R;

public class BleSysActivity extends MyActivityTools {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_sys);

        //透明状态栏
        StatusBarUtil.setTransparent(this);
        (this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        ((TextView) this.findViewById(R.id.tv_hint)).setText("BLE抄表系统");
    }

    public void testReadMeter(View v){

//        ToastDemo.myHint(this, "", 5);
        startActivity(new Intent(this, BleReadMeterActivity.class));
    }

    public void wirelesDdebug (View v){

//        ToastDemo.myHint(this, "", 5);
        startActivity(new Intent(this, BleWirelessDebugActivity.class));
    }

    public void loraReadMeter(View v){

//        ToastDemo.myHint(this, "", 5);
        startActivity(new Intent(this, LoraReadMeterActivity.class));
    }

//    public void testReadMeter(View v){
//
//        startActivity(new Intent(this, .class));
//    }
}
