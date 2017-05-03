package com.dysen.type.meterSys.systemSet;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.PercentDemo;
import com.dysen.mylibrary.utils.StatusBarUtil;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.mylibrary.utils.util.SharedPreUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tMeter;

import java.util.List;

public class SysInfoActivity extends MyActivityTools {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_info);

        //透明状态栏
        StatusBarUtil.setTransparent(this);
        (this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        ((TextView) this.findViewById(R.id.tv_hint)).setText("系统信息");

        initControl();
    }

    private void initControl() {

        List<tMeter> lMeter, lMeterRead;

        EditText etReadName = bindView(R.id.et_sys_read_name);
        EditText etCount = bindView(R.id.et_sys_count);
        EditText etReadM = bindView(R.id.et_sys_readM);
        EditText etReadmPercent = bindView(R.id.et_sys_readM_percent);

        lMeter = dbMeter.findAll(tMeter.class);
        lMeterRead = dbMeter.findAllByWhere(tMeter.class, "statusRead="+"\'"+ 1 + "\'");

        LogUtils.i(lMeter.size()+"***"+lMeterRead.size());
        etReadName.setText((String)SharedPreUtils.get(this, "read_name", ""));
        LogUtils.i((String)SharedPreUtils.get(this, "read_name", ""));
        if (lMeter.size() > 0 && lMeterRead.size() > 0) {

            etCount.setText(lMeter.size() + "");
            etReadM.setText(lMeterRead.size() + "");
            etReadmPercent.setText(PercentDemo.getPercent(lMeterRead.size(), lMeter.size()));
        }
    }
}
