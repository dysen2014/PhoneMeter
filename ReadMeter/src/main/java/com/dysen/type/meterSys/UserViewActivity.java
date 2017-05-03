package com.dysen.type.meterSys;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyStringConversion;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.adapter_util.AdUserView;
import com.dysen.myUtil.dialog.SpotsDialog;
import com.dysen.myUtil.httpUtil.HttpExcTint;
import com.dysen.mylibrary.utils.kjframe.KJHttp;
import com.dysen.mylibrary.utils.kjframe.http.HttpCallBack;
import com.dysen.mylibrary.utils.kjframe.http.HttpParams;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.mylibrary.utils.util.SharedPreUtils;
import com.dysen.mylibrary.utils.util.ToastUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBook;
import com.dysen.table.tMeter;

import java.util.ArrayList;
import java.util.List;

public class UserViewActivity extends MyActivityTools implements AdapterView.OnItemClickListener {

    TextView tvHint;
    String code, pid, statusRead;
    List<tMeter> lMeter;
    String data = "";
    Intent intent;
    ListView lv;
        View v;
        private String areaName;
        Button btnRefresh;
        FloatingActionButton btnUp;
        public static AlertDialog alert;
        String readNumber, readName;
        private TextView tv;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ms_user_view);
        //透明状态栏
        StatusBarUtil.setTransparent(this);

        (this.bindView(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

            (this.bindView(R.id.ll_btn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getMeter();
                }
            });

        tvHint = bindView(R.id.tv_hint);
            tv = bindView(R.id.tv_);
        code = getIntent().getStringExtra("code");
        pid = getIntent().getStringExtra("pid");
        statusRead = getIntent().getStringExtra("statusRead");

//            dbMeter = KJDB.create(this, "tMeter");

            readNumber = (String)SharedPreUtils.get(this, "read_id", "");
            readName = (String)SharedPreUtils.get(this, "read_name", "");
        System.out.println("pid="+pid+"\ncode="+code);
        intent = new Intent(this, UserViewActivity.class);
            btnRefresh  = bindView(R.id.btn_);
//            btnRefresh.setText("刷新");
            btnRefresh.setBackgroundResource(R.drawable.ic_refresh);
            btnRefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMeter();
                }
            });
        if (code != null && !"".equals(code))
            getMeter();
        else
            ;
//            ToastDemo.myHint(this, "code 有误", 2);
//            getMeter();

    }

        /**
         * 填充数据
         * @param lMeter
         */
        private void entryValue(List<tMeter> lMeter){

            final AdUserView adapter = new AdUserView(this, lMeter);

            lv = (ListView) findViewById(R.id.lv_user_view);
            lv.setAdapter(adapter);

              //下拉刷新
//            lv.setonRefreshListener(
//                    new MyListView.OnRefreshListener() {
//                        @Override
//                        public void onRefresh() {
//                            new AsyncTask<Void, Void, Void>() {
//                                protected Void doInBackground(Void... params) {
//                                    try {
//                                        Thread.sleep(1000);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    return null;
//                                }
//                                @Override
//                                protected void onPostExecute(Void result) {
//
//                                    getMeter();
//                                    adapter.notifyDataSetChanged();
//                                    lv.onRefreshComplete();
//                                }
//                            }.execute(null, null, null);
//                        }
//                    }
//            );

            lv.setOnItemClickListener(this);

            btnUp = bindView(R.id.btn_up);
//        final Button btnUp = (Button) findViewById(R.id.btnUp);
            btnUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                    lv.setSelection(0);

                    btnUp.setVisibility(View.GONE);
                }
            });
            lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // 不滚动时保存当前滚动到的位置
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                        int position = lv.getFirstVisiblePosition();
//                        System.out.println("position:"+position);
                        if (position > 10) {
                            btnUp.setVisibility(View.VISIBLE);
                        } else {
                            btnUp.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    btnUp.setVisibility(View.GONE);
                }
            });
        }

        /**
         * 获得表数据(数据库中)
         */
        public void getMeter(){

            if (readNumber.equals("D") || readNumber.equals("Y")){
                lMeter = dbMeter.findAllByWhere(tMeter.class, "readNumber=" + "\'" + readNumber + "\'");
            }else {
                lMeter = dbMeter.findAllByWhere(tMeter.class, "code=" + "\'" + code + "\'" + "AND readNumber=" + "\'" + readNumber + "\'");
            }
            if (lMeter == null){//当前集合为 null
                lMeter = new ArrayList<>();
            }
            if (lMeter.size() >0) {
                mMeterHandler();
                tv.setVisibility(View.GONE);
            }else {
                tv.setVisibility(View.VISIBLE);
                dlBook();
            }
        }

        private void mMeterHandler() {
            if (!TextUtils.isEmpty(statusRead))//statusRead 为空时 不需要过滤已抄表用户，不为空时列表只显示未抄表用户
            lMeter = dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + statusRead + "\'"+
                    "AND code=" + "\'" + code + "\'"+"AND readNumber="+"\'"+ readNumber + "\'");
            entryValue(lMeter);//填充数据

            List<tBook> lB = dbBook.findAllByWhere(tBook.class, "code=" + "\'" + code + "\'"+"AND readNumber="+"\'"+ readNumber + "\'");
            if (lB.size() > 0) {
                areaName = lB.get(0).getCodeName();
            }
            for (int i = 0; i < lMeter.size(); i++) {
                tvHint.setText(areaName + "\n(" + lMeter.get(i).getTimeAccount() + ")");
                tvHint.setTextSize(12);
            }
        }

        /**
     * 下载数据
     */
    public void dlBook() {

        //实例化异步任务
        myAsyncTask = new MyAsyncTask();
        //开始执行异步任务
        myAsyncTask.execute();

    }

        MyAsyncTask myAsyncTask;
        class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
            String str = "";

            //onPreExecute方法用于在执行后台任务前做一些UI操作
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                alert = new SpotsDialog(UserViewActivity.this, "下载子片区数据");
                alert.show();
                System.out.println("开始执行异步线程");
            }

            //doInBackground方法内部执行后台任务,不可在此方法内修改UI
            @Override
            protected String doInBackground(Integer... ii) {

                kjt = new KJHttp();
                params = new HttpParams();
                params.put("code",code );
                params.put("readNumber", readNumber);
//                params.put("readNumber", (int)SharedPreUtils.get(UserViewActivity.this, "read_id", 0));
                System.out.println("code:"+code+"\treadNumber:"+readNumber);
               kjt.post(HTTP_IP+"admin/search/findFlowByCode?", params, new HttpCallBack() {
//              kjt.post(HTTP_IP+"tMeter.json", null, new HttpCallBack() {

                    Intent intent = new Intent(UserViewActivity.this, UserViewActivity.class);

                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        LogUtils.i("下载数据为:\n"+t);
                        if ("".equals(t)) {
                            System.out.println("下载数据为空");
                            str = "下载数据为空";
                        } else if (t.equals("[]")) {

                            System.out.println("无当前下载的数据");
//
                            str = "无当前下载的数据";
                        } else {
                            System.out.println("****下载数据:"+t);
                            lMeter = JSON.parseArray(t, tMeter.class);
                            dbMeter.deleteByWhere(tMeter.class, "code=" + "\'" + code + "\'");
                            int i = 1;

                            for (tMeter tm : lMeter) {
//                                LogUtils.i("download code="+tm.getCode()+"***code="+(code)+"boolean:"+"".equals(code));
                                if (tm.getCode().equals(code) || readNumber.equals("Y") || readNumber.equals("D")) {

                                    i = 1;
                                }else{
                                    System.out.println("无当前下载的数据");
                                    i = 2;
                                }
                            }
                            if (i == 1) {
                                str = "数据，下载成功";
                            }else if(i == 2) {
                                str = "无当前下载的数据";
                            }
                        }
                    }

                    @Override
                    public void onFailure(int t, String strMsg) {
                        super.onFailure(t, strMsg);
                        LogUtils.i("下载失败:\n"+strMsg);
                        if (HttpExcTint.hTimeout.equals(strMsg)) {
//                        ToastDemo.myHint(BookActivity.this, "访问超时");
                            str = "访问超时";
                        } else if (HttpExcTint.hNoConnectServer.equals(strMsg)) {
//                        ToastDemo.myHint(BookActivity.this, "访问失败");
                            str = "访问失败";
                        } else {
//                        ToastDemo.myHint(BookActivity.this, "访问异常");
                            str = "访问异常";
                        }
                        System.out.println(str+"http访问异常：" + strMsg);
//                    tv.setText("数据，下载失败！\n请检查网络或服务器是否开启！！！");
                    }
                });
                System.out.println("正在执行异步线程"+str);
                SystemClock.sleep(5000);
                alert.dismiss();
                return str;
            }

            //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                System.out.println("异步操作执行结束"+s);
                System.out.println("-------------------------="+s);
                if (s.equals("")){
                    ToastDemo.myHint(UserViewActivity.this, "请求异常"+s, 4);
                    finish();
                }else{
                    if (s.equals("数据，下载成功")){
                        System.out.println("数据，下载成功。正在处理数据");
                        alert = new SpotsDialog(UserViewActivity.this, "片区数据处理中");
                        alert.show();

                        ToastUtils.showLong(UserViewActivity.this, "正在处理数据", 4);
                        saveData(lMeter);
                    }else {
                        ToastDemo.myHint(UserViewActivity.this, s, 4);
                        finish();
                    }
                }
            }
        }

        Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int index = msg.what;

                LogUtils.i("==================index="+index);
                switch (index) {
                    case 1:
                        getMeter();
                        break;
                    case 0:
                        finish();
                        break;
                }
                alert.dismiss();
            }
        };

        int index = 0, count;
        private void saveData(final List<tMeter> lMeter) {

            new Thread(new Runnable() {

                @Override
                public void run() {
                    for (tMeter tm : lMeter) {
                        if (tm.getCode().equals(code) || readNumber.equals("Y") || readNumber.equals("D")) {

                            tm.setReadNumber(readNumber);
                            tm.setStatusRead("0");
                            tm.setStatusUpdate("0");
                            tm.setReadInfo("");
                            tm.setReadName(readName);

                            tm.setAreaId(tm.getCode()  + MyStringConversion.myInverseStr(String.valueOf(count), 4));
                            count++;
                            tm.setReadStartTemp(tm.getReadStart());
                            tm.setReadEndTemp(tm.getReadEnd());
                            tm.setReadStart(tm.getReadEndTemp());
                            try {
                                dbMeter.save2(tm);//把下载的数据存入数据库
                                index = 1;
                            }catch (Exception e){
                                ToastUtils.showLong(UserViewActivity.this, "数据库异常，请卸载重新安装", 4);
                                LogUtils.i("数据库异常，请卸载重新安装");
                                index = 0;
                            }
                    }else{
                        System.out.println("无当前下载的数据");
                        index = 0;
                    }
                }

                Message msg = handler.obtainMessage();
                msg.what = index;
                msg.sendToTarget();
                }
            }).start();
        }


        @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(this, WaterEntryActivity.class);
            String readNumber = (String)SharedPreUtils.get(UserViewActivity.this, "read_id", "");

        i.putExtra("item", position);
        i.putExtra("code", code);
        i.putExtra("statusRead", statusRead);


            if (readNumber.equals("ICK")){//卡表
                ToastDemo.myHint(UserViewActivity.this, "卡表 暂仅供查看数据", 2);
//            }else if (readNumber.equals("Y")){//远传表
//                ToastDemo.myHint(UserViewActivity.this, "远传表 暂仅供查看数据", 2);
//            }else if (readNumber.equals("D")){//光电表
//                ToastDemo.myHint(UserViewActivity.this, "光电表 暂仅供查看数据", 2);
            }else if (readNumber.equals("999999")){//管理员 稽查
                ToastDemo.myHint(UserViewActivity.this, "稽查 暂仅供查看数据", 2);
            }else {
                startActivity(i);
            }

//       ToastDemo.myHint(this, "");

    }
}
