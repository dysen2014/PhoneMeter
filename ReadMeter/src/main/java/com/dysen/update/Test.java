package com.dysen.update;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Created by dy on 2016-11-22.
 */

public class Test extends Activity implements View.OnClickListener{

    String url= "http://192.168.0.12:8080";
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btn = new Button(this);
        setContentView(btn);
        checkVersion();
//        setContentView(R.layout.test);
//        init();
    }

    private void init(){
//        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVersion();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
//        if (i == R.id.btn) {
//            checkVersion();
//        }
    }

    /**
     * 检查是否有限版本
     */
    private void checkVersion() {
//        CommonDialog commonDialog = new CommonDialog(this, getString(R.string.update_new_version), getString(R.string.update_title),
//                getString(R.string.update_install), getString(R.string.update_cancel), new CommonDialog.DialogClickListener(){
//
//            @Override
//            public void onDialogClick(){
                Intent intent = new Intent(Test.this, UpdateService.class);
//                intent.putExtra("lastVersion", "4.2.2");
                intent.putExtra("apkUrl", url);
                startService(intent);
//            }
//        });
//        commonDialog.show();
    }
}
