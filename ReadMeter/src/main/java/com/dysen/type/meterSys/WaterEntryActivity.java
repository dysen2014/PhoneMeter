package com.dysen.type.meterSys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyDateUtils;
import com.dysen.myUtil.MyTools;
import com.dysen.myUtil.SoundPoolUtil;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.dialog.SpotsDialog;
import com.dysen.myUtil.httpUtil.HttpRequest;
import com.dysen.myUtil.picpopupwindow.SelectPicPopupWindow;
import com.dysen.mylibrary.utils.IsNumeric;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.mylibrary.utils.util.SharedPreUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBook;
import com.dysen.table.tMeter;
import com.dysen.type.dataSys.PaymentHistoryActivity;
import com.dysen.type.dataSys.StatisticsActivity;
import com.dysen.type.dataSys.UsewAnalysisActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class  WaterEntryActivity extends MyActivityTools {

    TextView tvHint;
    TextView tvUp, tvNext;
    EditText edtUserName, edtUserNum, edtUserPhone, edtUserAddr, edtMeterNum, edtLast, edtThis, edtWaterVolume, edtReadMStatus, edtNum, edtReadInfo;
    Button btnUp, btnNext, btnOperation, btnDel;
    Button btnEntryOk;

    long num;//当前止码
    int  i=0;
    String areaName;
    List<tMeter> list;
    Intent intent;
    int lastNum, thisNum,  readMtSum, readMtCompleted, readStartTemp, readEndTemp;
    String meterNum, userNum;
    int id;

    SoundPool sp;//声明一个SoundPool
    int musicId;//定义一个整型用load（）；来设置suondID
    int resId;
    //自定义的弹出框类
    SelectPicPopupWindow menuWindow;
    private String code;
    private String statusRead;
    private LinearLayout entryDel;
    private String readNumber;
    private EditText edtLastWaterVolume;
    int[] musics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_water_entry);
        //透明状态栏
        StatusBarUtil.setTransparent(this);

        (bindView(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        sp = SoundPoolUtil.initSP(sp);
        musics = new int[]{sp.load(this, R.raw.s0, 1), sp.load(this, R.raw.s1, 1), sp.load(this, R.raw.s2, 1), sp.load(this, R.raw.s3, 1), sp.load(this, R.raw.s4, 1), sp.load(this, R.raw.s5, 1)
                , sp.load(this, R.raw.s6, 1), sp.load(this, R.raw.s7, 1), sp.load(this, R.raw.s8, 1), sp.load(this, R.raw.s9, 1), sp.load(this, R.raw.s_clear, 1), sp.load(this, R.raw.s_operation, 1),
                sp.load(this, R.raw.s_other, 1), sp.load(this, R.raw.s_up, 1), sp.load(this, R.raw.s_next, 1)};

//        sp = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
//        musicId = sp.load(WaterEntryActivity.this, R.raw.s7, 1);
//
        initBtn();

        readNumber = (String)SharedPreUtils.get(this, "read_id", "");
        i = getIntent().getExtras().getInt("item");//传过来的item 为1
        code = getIntent().getStringExtra("code");
        statusRead = getIntent().getStringExtra("statusRead");
        System.out.println("i:"+i);
        getEntryData(i);
    }

    /**
     * 实例化SelectPicPopupWindow
     */
    private void initSelectPicPopupWindow() {

        //实例化SelectPicPopupWindow
        menuWindow = new SelectPicPopupWindow(this, new View.OnClickListener(){//为弹出窗口实现监听类

            public void onClick(View v) {
                menuWindow.dismiss();

                if (v.getId() == R.id.btn_entry_ok) {//数据录入

                    myEntryData();
                } else if(v.getId() == R.id.btn_payment_history){//查看历史

                    intent = new Intent(WaterEntryActivity.this, PaymentHistoryActivity.class);
                    String sMeterId = edtMeterNum.getText().toString().trim();

                    if (!TextUtils.isEmpty(sMeterId) ) {
                        intent.putExtra("meter_num", sMeterId);
                        startActivity(intent);
                    }else {
                        ToastDemo.myHint(getApplicationContext(), "表号可能有误！",2 );
                    }
                }else if(v.getId() == R.id.btn_data_update){//数据上传

                    List<tMeter> l = new ArrayList<>();
                    l.add(list.get(i));
                    updateData(l);
//                    startActivity(new Intent(WaterEntryActivity.this, UpdAppActivity.class));
                }else if (v.getId() == R.id.btn_read_statist){//统计

                    startActivity(new Intent(WaterEntryActivity.this, StatisticsActivity.class));
                }else if (v.getId() == R.id.btn_usew_analysis){

                    String sMeterId = edtMeterNum.getText().toString().trim();
                    intent = new Intent(WaterEntryActivity.this, UsewAnalysisActivity.class);
                    intent.putExtra("meter_num", sMeterId);
                    startActivity(intent);
                }else if (v.getId() == R.id.btn_read_anomaly){

                    intent = new Intent(WaterEntryActivity.this, PhotoShowActivity.class);
                    String sMeterId = edtMeterNum.getText().toString().trim();
                    intent.putExtra("meter_num", sMeterId);
                    startActivity(intent);
                }else if(v.getId() == R.id.btn_entry_localtion){

                    intent = new Intent(WaterEntryActivity.this, SelectMIdActivity.class);
                    intent.putExtra("type", "location");
                    startActivity(intent);
                }
            }
        });
        //显示窗口
        menuWindow.showAtLocation(bindView(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private void myEntryData(){

        List<tMeter> list = dbMeter.findAllByWhere(tMeter.class, "id=" + "\'" + id + "\'");
        if ("".equals(edtThis.getText().toString())) {
//            ToastDemo.myHint(WaterEntryActivity.this, "录入的数据不能为空，请重新录入");

            thisNum = lastNum = list.get(0).getReadEnd();//上次起码也是(当次起码)
            myDialog("录入的数据可能有误！！，是否继续录入");
        }else {

            if (list != null) {
                if ("1".equals(list.get(0).getStatusUpdate())){//已上传
//                ToastDemo.myHint(WaterEntryActivity.this, "数据已更新！不能重复抄表！", 2);
                    lastNum = list.get(0).getReadStart();//上次起码也是(当次起码)
                    thisNum = Integer.valueOf(edtThis.getText().toString());
                    myDialog("数据已上传！是否重复抄表！");

                }else {//未上传
                    if("1".equals(list.get(0).getStatusRead())){//已抄表

                        lastNum = list.get(0).getReadStart();//上次起码也是(当次起码)
                        thisNum = Integer.valueOf(edtThis.getText().toString());
                        LogUtils.i("dysen", "起码："+lastNum+"\t止码："+thisNum+"\t已抄");
                        if (thisNum < lastNum) {//起码大于止码时
                            thisNum = lastNum = list.get(0).getReadStart();
                            myDialog("本户已抄表，重新录入的数据可能有误！是否重新录入？");//提示操作员是否更新数据
                        }else if (thisNum == lastNum) {//起码等于止码时

                            thisNum = lastNum = list.get(0).getReadStart();//上次起码也是(当次起码)
                            myDialog("本户已抄表，重新录入的数据可能有误！，是否重新录入");
                        }else if (thisNum - lastNum > 50) {//用量大于50提示抄表员确认

                            myDialog("本户已抄表，重新录入的数据过大！"+(thisNum - lastNum)+"m³\n是否重新录入？");//提示操作员是否更新数据
                        }else if((thisNum - lastNum) < 5 && (thisNum - lastNum) > 0){

                            thisNum = lastNum = list.get(0).getReadStart();//上次起码也是(当次起码)
//                        myDialog("当前用户用水量过小！"+(thisNum - lastNum)+"m³\n是否录入？");//提示操作员是否更新数据
                            myDialog("本户已抄表，重新录入的数据可能有误！，是否继续录入");
                        }else {//正常录入数据
                            myDialog("本户已抄表，数据已经录入，是否继续重新录入");
                        }
                    }else{//未抄
                        lastNum = list.get(0).getReadEnd();//上次起码也是(当次起码)
                        thisNum = Integer.valueOf(edtThis.getText().toString());//当次止码
                        LogUtils.i("dysen", "起码："+lastNum+"\t止码："+thisNum+"\t未抄");
                        System.out.println("上月止码："+lastNum+"\t本月止码："+thisNum);
                        if (thisNum < lastNum) {//起码大于止码时
                            thisNum = lastNum = list.get(0).getReadEnd();
                            myDialog("录入的数据可能有误！是否继续录入？");//提示操作员是否更新数据
                        } else if (thisNum == lastNum) {//起码等于止码时

//                        ToastDemo.myHint(WaterEntryActivity.this, "该用户本月无用水记录！");//提示操作员是否更新数据
//                        updateEntry();
                            thisNum = lastNum = list.get(0).getReadEnd();//上次起码也是(当次起码)
                            myDialog("录入的数据可能有误！，是否继续录入");
                        }else if (thisNum - lastNum > 50) {//用量大于50提示抄表员确认

                            myDialog("录入的数据过大！"+(thisNum - lastNum)+"m³\n是否继续录入？");//提示操作员是否更新数据
                        }else if((thisNum - lastNum) < 5 && (thisNum - lastNum) > 0){
                            thisNum = lastNum = list.get(0).getReadEnd();//上次起码也是(当次起码)
//                        myDialog("当前用户用水量过小！"+(thisNum - lastNum)+"m³\n是否录入？");//提示操作员是否更新数据
                            myDialog("录入的数据可能有误！，是否继续录入");
                        }else {//正常录入数据

                            updateEntry();
                        }
                    }
                }
            }
        }
    }

    /**
     * 更新录入数据
     */
    private void updateEntry() {
        LogUtils.i("止码:"+thisNum +"起码："+lastNum +"临时码："+readEndTemp);
        meter.setReadEnd(thisNum);
        meter.setMeterID(meterNum);
        meter.setReadStart(readEndTemp);
        meter.setTimeAccount(MyDateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
//        meter.setTimeAccount(new Date());
        meter.setStatusRead("1");//标志已抄
        meter.setStatusUpdate("0");//标志未上传
        String sReadInfo = edtReadInfo.getText().toString();
        Log.i("dysen", sReadInfo);
        if (thisNum < lastNum){
            meter.setReadInfo((sReadInfo.equals("") | sReadInfo.equals("正常")) ? "异常" : sReadInfo);
        }else {
            meter.setReadInfo((sReadInfo.equals("") | sReadInfo.equals("异常")) ? "正常" : sReadInfo);
        }
        meter.setAccountFeeAll(BigDecimal.valueOf(thisNum - lastNum));
        meter.setUsed(String.valueOf(Integer.valueOf(thisNum - lastNum)));
        meter.setContactTel(edtUserPhone.getText().toString().trim());
        meter.setReadEndTemp(readEndTemp);
//        meter.setUserNum(userNum);
//        meter.setReadMtCompleted(readMtCompleted);
//        List<tMeter> l = dbMeter.findAllByWhere(tMeter.class, "id=" + "\'" + id + "\'");
//        long lThis1 = l.get(0).getReadEnd();
//        long lLast1 = l.get(0).getReadStart();
        dbMeter.update(meter, "id=" + "\'" + id + "\'");
//        l = dbMeter.findAllByWhere(tMeter.class, "id=" + "\'" + id + "\'");
//        long lThis2 = l.get(0).getReadEnd();
//        long lLast2 = l.get(0).getReadStart();
//        System.out.println(lThis1 + "******" + lThis2);
//        if (lThis1 == lThis2 && lLast1 == lLast2)
//            ToastDemo.myHint(WaterEntryActivity.this, "录入失败");
//        else {
//            ToastDemo.myHint(WaterEntryActivity.this, "录入成功");
            // 添加震动效果，提示用户删除完成
            Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            mVibrator.vibrate(200);
            ++i;
            getEntryData(i);
//        }
    }

    public void initBtn(){

        btnOperation = bindView(R.id.btn_entry_operation);
        btnDel = bindView(R.id.btn_entry_del);
        entryDel = bindView(R.id.entry_del);
        btnUp = bindView(R.id.btn_entry_up);
        btnNext = bindView(R.id.btn_entry_next);
        tvHint = bindView(R.id.tv_hint);
        tvUp = bindView(R.id.tv_entry_up);
        tvNext = bindView(R.id.tv_entry_next);

        edtUserName = bindView(R.id.et_entry_user_name);
        edtReadMStatus = bindView(R.id.et_read_meter_status);
        edtUserNum = bindView(R.id.et_entry_user_num);
        edtUserPhone = bindView(R.id.et_entry_user_phone);
        edtUserAddr = bindView(R.id.et_entry_user_addr);
        edtMeterNum = bindView(R.id.et_entry_meter_num);
        edtLast = bindView(R.id.et_entry_last);
        edtThis = bindView(R.id.et_entry_this);
        edtReadInfo = bindView(R.id.et_entry_info);

        edtWaterVolume = bindView(R.id.et_entry_this_volume);
        edtLastWaterVolume = bindView(R.id.et_entry_last_volume);
        edtNum = bindView(R.id.et_entry_num);

        bindView(R.id.btn_entry_1).setOnClickListener(new onClick());
        bindView(R.id.btn_entry_2).setOnClickListener(new onClick());
        bindView(R.id.btn_entry_3).setOnClickListener(new onClick());
        bindView(R.id.btn_entry_4).setOnClickListener(new onClick());
        bindView(R.id.btn_entry_5).setOnClickListener(new onClick());
        bindView(R.id.btn_entry_6).setOnClickListener(new onClick());
        bindView(R.id.btn_entry_7).setOnClickListener(new onClick());
        bindView(R.id.btn_entry_8).setOnClickListener(new onClick());
        bindView(R.id.btn_entry_9).setOnClickListener(new onClick());
        bindView(R.id.btn_entry_0).setOnClickListener(new onClick());

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myEntryData();
            }
        });

        btnOperation.setOnClickListener(new onClick());
        btnDel.setOnClickListener(new onClick());
        entryDel.setOnClickListener(new onClick());
        btnUp.setOnClickListener(new onClick());
        btnNext.setOnClickListener(new onClick());

        btnDel.setOnLongClickListener(new onLongClick());
        entryDel.setOnLongClickListener(new onLongClick());
    }

    private void delData() {
        // 添加震动效果，提示用户删除完成
        Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mVibrator.vibrate(200);
        edtNum.setText("");
        edtThis.setText("");
//        edtWaterVolume.setText("月使用量");
        entryUsedData();
    }

    /**
     * 长按监听
     */
    private class onLongClick implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {

            switch (v.getId()){
                case R.id.entry_del:
//                    delData();
//                break;
                case R.id.btn_entry_del:
                    musicId = musics[10];
                    delData();
                break;
            }
            sp.play(musicId, 1, 1, 0, 0, 1);//id, 左声道， 右声道， 优先级，循环，速率
            return true;
        }
    }

    /**
     * 点击监听
     */
    private class onClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.btn_entry_0){

                keyPressed(KeyEvent.KEYCODE_0);
                resId = R.raw.s0;
                musicId = musics[0];
            }else if(v.getId() == R.id.btn_entry_1){

                keyPressed(KeyEvent.KEYCODE_1);
                resId = R.raw.s1;
                musicId = musics[1];

            }else if(v.getId() == R.id.btn_entry_2){

                keyPressed(KeyEvent.KEYCODE_2);
                resId = R.raw.s2;
                musicId = musics[2];
            }else if(v.getId() == R.id.btn_entry_3){

                keyPressed(KeyEvent.KEYCODE_3);
                resId = R.raw.s3;
                musicId = musics[3];
            }else if(v.getId() == R.id.btn_entry_4){

                keyPressed(KeyEvent.KEYCODE_4);
                resId = R.raw.s4;
                musicId = musics[4];
            }else if(v.getId() == R.id.btn_entry_5){

                keyPressed(KeyEvent.KEYCODE_5);
                resId = R.raw.s5;
                musicId = musics[5];
            }else if(v.getId() == R.id.btn_entry_6){

                keyPressed(KeyEvent.KEYCODE_6);
                resId = R.raw.s6;
                musicId = musics[6];
            }else if(v.getId() == R.id.btn_entry_7){

                keyPressed(KeyEvent.KEYCODE_7);
                resId = R.raw.s7;
                musicId = musics[7];
            }else if(v.getId() == R.id.btn_entry_8){

                keyPressed(KeyEvent.KEYCODE_8);
                resId = R.raw.s8;
                musicId = musics[8];
            }else if(v.getId() == R.id.btn_entry_9){

                keyPressed(KeyEvent.KEYCODE_9);
                resId = R.raw.s9;
                musicId = musics[9];
            }else if(v.getId() == R.id.entry_del){
                resId = R.raw.s_clear;
                musicId = musics[10];
                keyPressed(KeyEvent.KEYCODE_DEL);
            }else if(v.getId() == R.id.btn_entry_del){
                resId = R.raw.s_clear;
                musicId = musics[10];
                keyPressed(KeyEvent.KEYCODE_DEL);
            }else if(v.getId() == R.id.btn_entry_operation){
                resId = R.raw.s_operation;
                musicId = musics[11];
                initSelectPicPopupWindow();

            }else if(v.getId() == R.id.btn_entry_up){
                resId = R.raw.s_up;
                musicId = musics[13];
                --i;
                getEntryData(i);

            }else if(v.getId() == R.id.btn_entry_next){
                resId = R.raw.s_next;
                musicId = musics[14];
                ++i;
                getEntryData(i);
            }

//            musicId = sp.load(WaterEntryActivity.this, resId, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
            System.out.println("resId=" + resId + "**********musicId=" + musicId);

            sp.play(musicId, 1, 1, 0, 0, 1);//id, 左声道， 右声道， 优先级，循环，速率
            entryUsedData();
        }
    }

    private void entryUsedData() {

        String ss = edtThis.getHint().toString();
        String s = edtNum.getText().toString();

        if(!s.equals("")){
            edtWaterVolume.setText(String.valueOf((Integer.valueOf(s) - readEndTemp)));
            entryDel.setEnabled(true);
//                    entryDel.setBackgroundResource(R.drawable.btn_enable);
        }else {
            if (!"".equals(ss)) {
                edtWaterVolume.setText(Integer.valueOf(ss)==0?"0":(String.valueOf((Integer.valueOf(ss) - readEndTemp))));
                entryDel.setEnabled(true);
//                    entryDel.setBackgroundResource(R.drawable.btn_enable);
            }else {
                edtWaterVolume.setText(String.valueOf((0 - lastNum)));
                entryDel.setEnabled(false);
//                entryDel.setBackgroundResource(R.drawable.btn_dis_enable);
            }
        }
        //监控输入的数据是否异常
        String used = edtWaterVolume.getText().toString();
        if (Integer.parseInt(IsNumeric.isNumeric(used)? used : "-1" ) < 0){
            edtWaterVolume.setTextColor(Color.RED);
        }else {
            if (Integer.parseInt(IsNumeric.isNumeric(used)? used : "-1" )<5){
                edtWaterVolume.setTextColor(Color.parseColor("#B8860B"));
            }else if (Integer.parseInt(IsNumeric.isNumeric(used)? used : "-1" )>50){
                edtWaterVolume.setTextColor(Color.parseColor("#B8860B"));
            }else {
                edtWaterVolume.setTextColor(Color.BLACK);
            }
        }
    }

    private void keyPressed(int keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);

        edtThis.onKeyDown(keyCode, event);
        edtNum.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    /**
     * 从库中获取用户相关信息，方便 操作员录入数据
     * @param p0
     */
    private void getEntryData(int p0) {
        List<tBook> lB =  null;
        i = p0;
        edtThis.setText("");
        edtNum.setText("");

        if (readNumber.equals("D") || readNumber.equals("Y")){
            lB = dbBook.findAllByWhere(tBook.class, "readNumber=" + "\'" + readNumber + "\'");
            list = dbMeter.findAllByWhere(tMeter.class, "readNumber=" + "\'" + readNumber + "\'");
        }else {
            lB = dbBook.findAllByWhere(tBook.class, "code=" + "\'" + code + "\'");
            list = dbMeter.findAllByWhere(tMeter.class, "code=" + "\'" + code + "\'");
        }
        if (lB.size() > 0) {
            areaName = lB.get(0).getCodeName();
        }

        if (i < list.size() && i >= 0){
            if (!TextUtils.isEmpty(statusRead))//statusRead 为空时 不需要过滤已抄表用户，不为空时列表只显示未抄表用户
                list = dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + statusRead + "\'"+
                        "AND code=" + "\'" + code + "\'");
            tvHint.setText(areaName);
            if (i != 0) {
                tvUp.setText("上一户\t\t\t" + list.get(i - 1).getUserName() + "\t(" + list.get(i - 1).getMeterID() + ")");
                btnUp.setEnabled(true);
//                btnUp.setBackgroundResource(R.drawable.btn_enable);
            }else {
                btnUp.setEnabled(false);
//                btnUp.setBackgroundResource(R.drawable.btn_dis_enable);
                tvUp.setText("");
                i = 0;
            }
            edtUserName.setText(list.get(i).getUserName());
            edtUserNum.setText(list.get(i).getAmrID());
            edtUserPhone.setText(list.get(i).getContactTel());
            edtUserAddr.setText(list.get(i).getContactAddr());
            edtMeterNum.setText(list.get(i).getMeterID());
            if (list.get(i).getReadInfo().equals("")) {
                edtReadInfo.setHint("在此处 输入 抄表状况说明");
                edtReadInfo.setHintTextColor(Color.parseColor("#0000ff"));
                edtReadInfo.setAlpha((float)0.6);
                edtReadInfo.setText("");//清空编辑框，防止显示上次内容
            }else {
                edtReadInfo.setText(list.get(i).getReadInfo());
            }
            lastNum = Integer.valueOf(list.get(i).getReadStart());//上月止码
            /**
             * 1月 起码(0) --- 止码(a)
             * 2月 起码(a) --- 止码(b)
             * 3月 起码(b) --- 止码(c)
             * 上月止码变成本月起码 本月止码变成下月起码
             */
            lastNum = list.get(i).getReadStart();//本月起码
            thisNum = list.get(i).getReadEnd();
            readStartTemp = list.get(i).getReadStartTemp();
            readEndTemp = list.get(i).getReadEndTemp();//保留上月止码
            edtWaterVolume.setText((thisNum - readEndTemp)+"");
            edtLastWaterVolume.setText((readEndTemp - readStartTemp)+"m³");

            if (thisNum < readEndTemp){
//                edtLast.setText(thisNum+ "("+readEndTemp+")");
                edtLast.setText(String.valueOf(readEndTemp));
            }else{
                lastNum = thisNum;
                edtLast.setText(String.valueOf(readEndTemp));
            }

            if (list.get(i).getStatusRead().equals("0")){
                edtThis.setText("");
                edtThis.setHint(String.valueOf(list.get(i).getReadEnd()));
                edtReadMStatus.setText("未抄");
                edtReadMStatus.setTextColor(Color.RED);

            }else{

                edtThis.setHint(String.valueOf(list.get(i).getReadEnd()));
//                edtThis.setText(String.valueOf(list.get(i).getReadEnd()));

                if (list.get(i).getStatusUpdate().equals("0")){
                    edtReadMStatus.setText("已抄(未上传)");
                    // 颜色选择时 必须Color。(而  R.color. 是无效的)
                    edtReadMStatus.setText(MyTools.setStrhHighlighted("已抄(未上传)", Color.RED, 2, 7));
                    edtReadMStatus.setTextColor(Color.GREEN);
                }else{
                    edtReadMStatus.setText("已抄(已上传)");
                    edtReadMStatus.setTextColor(Color.GREEN);
                }
            }

            meterNum = list.get(i).getMeterID();//表编号
            userNum = list.get(i).getAmrID();//用户编号
            readMtSum = Integer.valueOf(list.size());//总户数
//            readMtCompleted = list.get(i).getReadMtCompleted();//已抄户数
            id = list.get(i).getId();

            if (i != list.size()-1) {
                tvNext.setText("下一户\t\t\t" + list.get(i + 1).getUserName() + "\t(" + list.get(i + 1).getMeterID() + ")");
                btnNext.setEnabled(true);
//                btnNext.setBackgroundResource(R.drawable.btn_enable);
            }else {
                tvNext.setText("录入最后一户数据");
                i = list.size()-1;
                btnNext.setEnabled(false);
//                btnNext.setBackgroundResource(R.drawable.btn_dis_enable);
            }
        } else
        if(i < 0) {
            ToastDemo.myHint(this, "当前已是第一个用户", 2);
            i = 0;
        }else {
            ToastDemo.myHint(this, "当前已是最后一个用户", 2);
            i = list.size() - 1;
        }
    }

//    public void btn(View v){
//
//    }

    AlertDialog alert;

    /**
     * 提示操作员(防止数据录入无效数据)
     */
    public void myDialog(String str) {
        View v = LayoutInflater.from(this).inflate(R.layout.login_register,
                null);
        TextView tvHintTitle;
        Button btnCancel, btnOkPress;

        tvHintTitle = (TextView) v.findViewById(R.id.tv_hint_title);
        btnCancel = (Button) v.findViewById(R.id.btn_cancel_normal);
        btnOkPress = (Button) v.findViewById(R.id.btn_ok_press);

        tvHintTitle.setText(str);
        btnOkPress.setText("是");
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alert.dismiss();
            }
        });
        btnOkPress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                alert.dismiss();
                updateEntry();
            }
        });

        alert = new AlertDialog.Builder(this).setTitle("提示 ").setIcon(R.drawable.hint).setView(v).show();
    }

    public void updateData(List<tMeter> l){
        //实例化异步任务
        myAsyncTask = new MyAsyncTask(l);
        //开始执行异步任务
        myAsyncTask.execute();
    }

    MyAsyncTask myAsyncTask;
    class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
        String str = "";
        String url = MyActivityTools.HTTP_IP+"admin/search/uploadFlowData";
        String params = "";
        List<tMeter> list = new ArrayList<>();

        MyAsyncTask(List<tMeter> l){
            LogUtils.i(url+params+"\n"+l);
            params = "data="+com.alibaba.fastjson.JSON.toJSONString(l);
            list = l;
        }
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alert = new SpotsDialog(context, "更新表数据");
            alert.show();
            System.out.println("开始执行异步线程");
        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(Integer... ii) {
            str = HttpRequest.sendPost(url, params);
            System.out.println("***************************收到的数据："+str);
            SystemClock.sleep(3000);
            return str;
        }
        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println("异步操作执行结束"+s);
            System.out.println("-------------------------="+s);
            if (s.equals("")){
//                    ToastDemo.myHint(UserViewActivity.this, "************************异常"+s, 5);
                ToastDemo.myHint(context, "上传失败 ", 3);
                myDialogShow("上传失败");
            }else{

                String t = s;

                List<Integer> l = com.alibaba.fastjson.JSON.parseArray(t, Integer.class);
                if (l.size()>0) {
                    for (int i=0; i<l.size(); i++){
//                            System.out.println(l.get(i)+"起码"+list.get(i).getReadStart()+"止码："+list.get(i).getReadEnd());
                        meter.setStatusUpdate("1");
                            meter.setReadStart(list.get(i).getReadStart());
                            meter.setReadEnd(list.get(i).getReadEnd());
                            meter.setReadEndTemp(list.get(i).getReadEndTemp());
                            MyActivityTools.dbMeter.update(meter, "id=" + "\'" + l.get(i) + "\'");//+"AND =" + "\'" +  + "\'"
                    }
                    ToastDemo.myHint(context, "上传成功 ", 1);
                    getEntryData(i);
                    myDialogShow("上传成功");
                }
            }
            alert.cancel();
        }
    }

//    private void updateData(final List<tMeter> list) {
//
//        alert = new SpotsDialog(context, "更新表数据");
//        alert.show();
//
//        KJHttp kjt = new KJHttp();
//        HttpParams params = new HttpParams();
//        List<tUpdate> listU = new ArrayList<>();
//       listU = JSON.parseArray(JSON.toJSONString(list), tUpdate.class);
//        params.put("data", JSON.toJSONString(listU));
//        LogUtils.i("update:"+MyActivityTools.HTTP_IP+"admin/search/uploadFlowData?"+"data="+JSON.toJSONString(list));
//        kjt.post(MyActivityTools.HTTP_IP+"admin/search/uploadFlowData?", params, new HttpCallBack() {
//            @Override
//            public void onSuccess(String t) {
//                super.onSuccess(t);
//
//                alert.cancel();
//                if ("".equals(t)) {
//                    System.out.println("上传数据返回为空");
//                         myDialogShow("上传失败 ");
//                    return;
//                } else if (t.equals("[]")) {
//
//                    System.out.println("无当前下载的数据");
//                    ToastDemo.myHint(context, "上传失败", 3);
    //                         myDialogShow("上传失败 ");
//                }else {
//                    List<Integer> l = JSON.parseArray(t, Integer.class);
//                    if (l.size()>0) {
//                        for (int i=0; i<l.size(); i++){
//                            System.out.println(l.get(i)+"起码"+list.get(i).getReadStart()+"止码："+list.get(i).getReadEnd());
//                            meter.setStatusUpdate("1");
//                            meter.setReadStart(list.get(i).getReadEndTemp());
//                            meter.setReadEnd(list.get(i).getReadEnd());
//                            meter.setReadEndTemp(list.get(i).getReadEndTemp());
//                            MyActivityTools.dbMeter.update(meter, "id=" + "\'" + list.get(i).getId() + "\'");//+"AND =" + "\'" +  + "\'"
//                            System.out.println("上传返回id="+list.get(i).getId());
//
//                        }
//                        ToastDemo.myHint(context, "上传成功 ", 1);
//                        getEntryData(i);
    //                         myDialogShow("上传成功 ");
//                    }
//                    System.out.println("返回信息是+" + t);
//                }
//            }
//            @Override
//            public void onFailure(int t, String strMsg) {
//                super.onFailure(t, strMsg);
//                //加载失败的时候回调
//                System.out.println("http访问异常：" + strMsg);
////
//                if (HttpExcTint.hTimeout.equals(strMsg)){
//                    ToastDemo.myHint(context, "访问超时", 2);
//                }else if(HttpExcTint.hNoConnectServer.equals(strMsg)){
//                    ToastDemo.myHint(context, "访问失败", 2);
//                }else {
//                    ToastDemo.myHint(context, "访问异常", 2);
//                }
////                tv.setText("数据，上传失败！\n请检查网络或服务器是否开启！！！");
//                alert.cancel();
//            }
//        });
//    }

    public void myDialogShow(String sTitle) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // 退出
                dialog.dismiss();
            }
        });

        alert.setTitle(sTitle);
//        alert.setMessage("是否确认"+ "" +"?");
        alert.create().show();
    }
}
