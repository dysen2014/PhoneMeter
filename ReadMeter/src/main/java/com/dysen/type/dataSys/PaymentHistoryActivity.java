package com.dysen.type.dataSys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.PercentDemo;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.adapter_util.AdPaymentHistory;
import com.dysen.myUtil.dialog.SpotsDialog;
import com.dysen.myUtil.httpUtil.HttpExcTint;
import com.dysen.myUtil.listView.MyListView;
import com.dysen.mylibrary.utils.kjframe.http.HttpCallBack;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tPaymentHistory;
import java.util.List;

/**
 * 用户缴费记录
 */
public class PaymentHistoryActivity extends MyActivityTools implements AdapterView.OnItemClickListener {

    TextView tvHint;
    String userName, meterNum;
    List<tPaymentHistory> lMeter;
    MyListView lv;
    Context context ;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_payment_history);
        context = this;
        //透明状态栏
        StatusBarUtil.setTransparent(this);

        (this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        meterNum = getIntent().getExtras().getString("meter_num");

        tvHint = (TextView) this.findViewById(R.id.tv_hint);

        tvHint.setTextSize(14);

        if (meterNum.equals("")){
            ToastDemo.myHint(this, "无记录", 2);
//            lMeter = dbMeter.findAllByWhere(tMeter.class, "id=" + "\'" + 1 + "\'");
        }else {
            getMeter();
        }
    }

    private void entryValue(List<tPaymentHistory> lMeter) {

        final AdPaymentHistory adapter = new AdPaymentHistory(this, lMeter);

        lv = (MyListView) findViewById(R.id.lv_history);
        lv.setAdapter(adapter);

        //  下拉刷新
        lv.setonRefreshListener(
                new MyListView.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new AsyncTask<Void, Void, Void>() {
                            protected Void doInBackground(Void... params) {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Void result) {

                                getMeter();
                                adapter.notifyDataSetChanged();
                                lv.onRefreshComplete();
                            }
                        }.execute(null, null, null);
                    }
                }
        );

        lv.setOnItemClickListener(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        final Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                lv.setSelection(0);

                fab.setVisibility(View.GONE);
            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    int position = lv.getFirstVisiblePosition();
                    if (position > 0) {
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                fab.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 获得表数据(数据库中)
     */
    public void getMeter(){
//        lMeter = dbPayment.findAllByWhere(tPaymentHistory.class, "meterID=" + "\'" + meterNum + "\'");
//        if (lMeter.size() >0) {
//
//            for (int i = 0; i < lMeter.size(); i++) {
//
//                System.out.println("第"+i+"用户：\t"+lMeter.get(i).getTimeAccount());
//                entryValue(lMeter);//填充数据
//            }
//        }else {//"http://duzhaohui.f3322.net:8080/admin/search/findFlowByCode?code="+code

            dlBook();
//        }
    }

    public static AlertDialog alert;
    /**
     * dysen 2015-7-11 上午11:45:15 info: 启动 动画 gif 播放
     */
    public static void myStartGif(Context context, View v, String str) {

        // 把该 Activity 添加到 activity列表里 方便退出时一下子退出所有activity
        // QuitAllActivity.getInstance().addActivity(this);

        alert = new AlertDialog.Builder(context).create();
        alert.setTitle(str);
        alert.getWindow().setContentView(v);

//            // 从xml中得到GifView的句柄
//            GifView gf1 = (GifView) v.findViewById(R.id.gif1);
//            // 设置Gif图片源
//            gf1.setGifImage(R.drawable.loading);
//
//            // 设置显示的大小，拉伸或者压缩
//            gf1.setShowDimension(300, 300);
//            // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
//            gf1.setGifImageType(GifView.GifImageType.COVER);

    }


    /**
     * 下载数据
     */
    public void dlBook() {

//        v = LayoutInflater.from(this).inflate(
//                R.layout.uber_progress, null);
//        v.setBackgroundColor(Color.argb(0,0,0,0));
//       alert = MyActivityTools.myDialog(this, v, "正在查询数据 ", Gravity.CENTER);//	启动gif动画

        alert = new SpotsDialog(this, "查询数据");
        alert.show();
        params.put("meterID", meterNum);
//        kjt.post(HTTP_IP+"tPaymentHistory.json", null, new HttpCallBack() {
        kjt.post(HTTP_IP+"admin/search/payRecord?", params, new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                alert.cancel();
                if ("".equals(t)) {
                    System.out.println("查询数据为空");
                    return;
                } else {
                    System.out.println("查询数据：" + t);
                    lMeter = JSON.parseArray(t, tPaymentHistory.class);
                    if (lMeter.size()>0) {
                        tvHint.setText(lMeter.get(0).getUserName() + "\n(" + lMeter.get(0).getMeterID() + ")");
//                    entryValue(lMeter);
//                    dbPayment.deleteByWhere(tPaymentHistory.class, "id>0");
//                    for (tPaymentHistory tm : lMeter) {
//
//                        dbPayment.save(tm);//把下载的数据存入数据库
//                    }
                        entryValue(lMeter);
//                    Intent intent = new Intent(PaymentHistoryActivity.this, PaymentHistoryActivity.class);
//                    intent.putExtra("meter_num", meterNum);
//                    finish();
//                    startActivity(intent);
                    }else{
                        finish();
                        ToastDemo.myHint(getApplicationContext(), "查询，无记录",1);
                    }
                }
                ToastDemo.myHint(PaymentHistoryActivity.this, "查询成功", 1);
        }

            @Override
            public void onFailure(int t, String strMsg) {
                super.onFailure(t, strMsg);
                //加载失败的时候回调
//                ToastDemo.myHint(context, "http访问异常\n" + strMsg);
                System.out.println("http访问异常：" + strMsg);

                if (HttpExcTint.hTimeout.equals(strMsg)){
                    ToastDemo.myHint(PaymentHistoryActivity.this, "访问超时",2);
                }else if(HttpExcTint.hNoConnectServer.equals(strMsg)){
                    ToastDemo.myHint(PaymentHistoryActivity.this, "访问失败", 2);
                }else {
                    ToastDemo.myHint(PaymentHistoryActivity.this, "访问异常", 2);
                }
                alert.cancel();
                finish();
            }

            @Override
            public void onLoading(long count, long current) {
                super.onLoading(count, current);
                if (current == count)
                    System.out.println("进度：" + current / count);

                myStartGif(PaymentHistoryActivity.this, v, "正在查询数据 "+"\n查询进度：" +PercentDemo.getPercent(current, count));//	启动gif动画
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i;

        switch (position) {
            case 0:
//                i = new Intent(this, LineChartActivity1.class);
//                startActivity(i);
                ToastDemo.myHint(this, "asddddddafqwf", 5);
                break;
        }
    }
}
