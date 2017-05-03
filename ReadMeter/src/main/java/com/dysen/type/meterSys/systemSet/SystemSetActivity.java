package com.dysen.type.meterSys.systemSet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dysen.login_register.ForgetPwdDemo;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyDateUtils;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.db.DBHelper;
import com.dysen.myUtil.excel.ExcelDemo;
import com.dysen.myUtil.excel.ExcelUtils;
import com.dysen.mylibrary.utils.StatusBarUtil;
import com.dysen.mylibrary.utils.kjframe.KJDB;
import com.dysen.mylibrary.utils.kjframe.database.SqlInfo;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBook;
import com.dysen.table.tExcel;
import com.dysen.table.tMeter;
import com.dysen.type.user.UserActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SystemSetActivity extends MyActivityTools {

    private SqlInfo sqlInfo;

    private static ArrayList<ArrayList<String>> bill2List;
    KJDB dbExcel ;
    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_set);

        //透明状态栏
        StatusBarUtil.setTransparent(this);
        (this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        ((TextView) this.findViewById(R.id.tv_hint)).setText("系统设置");

        dbExcel = KJDB.create(this, "tExcel");
    }

    public void systemInfo(View v){

        startActivity(new Intent(this, SysInfoActivity.class));
    }

    public void btnUpdatePwd(View v){
        // 修改密码
        Intent it = new Intent(this, ForgetPwdDemo.class);

        startActivity(it);
    }

    public void btnUser(View v) {

            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);

    }

    public void proofingTime(View v){

        ToastDemo.myHint(this, "当前时间:"+new Date().toLocaleString(), 5);
//        startActivity(new Intent(this, SystemSetActivity.class));
    }

    public void clearData(View v){

//        startActivity(new Intent(this, SystemSetActivity.class));
        myDialog("清空抄表数据");
    }
    public void exPortReadMeter(View v){

        List<tMeter> lm = dbMeter.findAllByWhere(tMeter.class, "statusRead="+"\'"+ 1 +"\'");
        LogUtils.i("lm.seise="+lm.size());
        List<tExcel> lExcel = new ArrayList<>();

        if (lm != null) {
            lExcel = JSON.parseArray(JSON.toJSONString(lm), tExcel.class);
            for (int i = 0; i < lm.size(); i++) {

                lExcel.get(i).setMeterId(lm.get(i).getMeterID().equals("") ? "0" : lm.get(i).getMeterID());
                lExcel.get(i).setAmrId(lm.get(i).getAmrID().equals("") ? "0" : lm.get(i).getAmrID());
                lExcel.get(i).setUserName(lm.get(i).getUserName());
                lExcel.get(i).setMonth(MyDateUtils.formatDate(MyDateUtils.addDate(new Date(), "M", 1), "yyMM"));
                lExcel.get(i).setReadTime(lm.get(i).getTimeAccount());
                lExcel.get(i).setReadStart(lm.get(i).getReadStart());
                lExcel.get(i).setReadEnd(lm.get(i).getReadEnd());
                lExcel.get(i).setAddr(lm.get(i).getContactAddr());
                lExcel.get(i).setReadType("32");
                lExcel.get(i).setReadName(lm.get(i).getReadName());
                lExcel.get(i).setReadNumber(lm.get(i).getReadNumber());
                lExcel.get(i).setReadInfo("手机抄表");

                LogUtils.i(i+"************************"+lExcel.get(i).getUserName());

//                for (tExcel e : lExcel){
//                    dbExcel.save(e);
//
//                    LogUtils.i("表号："+e.getMeterId()+"\t电子表号："+e.getAmrId()+"\t用户名："+e.getUserName()+"\t月份："+e.getMonth()+"\t抄表时间"+e.getReadTime()
//                            +"\t起码："+e.getReadStart()+"\t止码："+ e.getReadEnd()+"\t地址："+e.getAddr()+"\t操作类型："+e.getReadType()
//                            +"\t抄表员："+e.getReadName()+"\t工位号："+e.getReadNumber()+"\t备注："+e.getReadInfo());
//                }
            }

            if (lExcel != null) {
//                lExcel.clear();
//                new ExcelDemo().export2Excel(lExcel, "潜江抄表流水", this);
                String dirName = new ExcelDemo().initData("潜江抄表系统/潜江抄表流水");

                ExcelUtils.writeObjListToExcel(lExcel, dirName, this);
            }
        }
    }

//    public void systemSet(View v){
//
//        startActivity(new Intent(this, SystemSetActivity.class));
//    }

    public void myDialog(String sTitle) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // 退出
                dialog.dismiss();
                dbBook.deleteByWhere(tBook.class, "id>0");
                dbMeter.deleteByWhere(tMeter.class, "id>0");
                ToastDemo.myHint(SystemSetActivity.this, "数据已清空", 4);
            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setTitle(sTitle);
        alert.setMessage("是否确认"+ "" +"?");
        alert.create().show();
    }
}
