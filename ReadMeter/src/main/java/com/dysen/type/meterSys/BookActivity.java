package com.dysen.type.meterSys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.adapter_util.AdBook;
import com.dysen.myUtil.dialog.SpotsDialog;
import com.dysen.myUtil.httpUtil.HttpExcTint;
import com.dysen.mylibrary.utils.kjframe.KJHttp;
import com.dysen.mylibrary.utils.kjframe.http.HttpCallBack;
import com.dysen.mylibrary.utils.kjframe.http.HttpParams;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.mylibrary.utils.util.SharedPreUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBook;
import com.dysen.table.tMeter;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends MyActivityTools implements View.OnClickListener {

    Button btnBook, btnIcon;
    AdBook bookAdapter;//绑定数据的adpter
    ExpandableListView listView; //控件
    List<tBook> fatherList;   //放置父类数据
    TextView tv;
    Handler handler;
    List<tBook> mBook;
    FloatingActionButton fab;
    int[] icolor = new int[]{R.color.pink_, R.color.blue_, R.color.purple_, R.color.yellow_,
            R.color.orange_, R.color.green_, R.color.colorAccent};
    private SpotsDialog alert;

    String readNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ms_book);

        //透明状态栏
        StatusBarUtil.setTransparent(this);

        (this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        (this.bindView(R.id.ll_btn)).setOnClickListener(this);

//        initViews();
        idInit();
//        dbBook = KJDB.create(this, "tBook");

        fatherList = new ArrayList<tBook>();
        readNumber = (String)SharedPreUtils.get(BookActivity.this, "read_id", "");

        getBook();
    }

    private void getBook() {

        mBook = dbBook.findAllByWhere(tBook.class, "readNumber="+"\'"+ readNumber + "\'");
        if (mBook == null){//当前集合为 null
            mBook = new ArrayList<>();
        }
        LogUtils.i(readNumber+"mBook:"+mBook.size());
        if (mBook.size()>0){
            for (tBook t :mBook)
            LogUtils.i("readMeter="+t.getReadNumber());
//            if (readNumber.equals("D") || readNumber.equals("Y")){
//                entryValue(mBook);
//            }else {
                mBookHandle(mBook);
//            }
            tv.setVisibility(View.GONE);
        }else {
            tv.setVisibility(View.VISIBLE);
            dbBook.deleteByWhere(tBook.class, "id>0");
            dbMeter.deleteByWhere(tMeter.class, "id>0");
//            fatherList.removeAll(fatherList);
        }
    }

    /**
     * 给 listview 填充数据
     */
    private void entryValue(List<tBook> fatherList){

        //绑定数据
        if (bookAdapter == null) {
            bookAdapter = new AdBook(fatherList, this);

            listView.setAdapter(bookAdapter);

        }
    }

    /**
     * 表册数据处理
     * @param mBook
     */
    private void mBookHandle(List<tBook> mBook) {

//            System.out.println(mBook.size()+"集合处理前："+JSON.toJSONString(mBook));

            if (mBook != null && mBook.size() > 0) {
                for (tBook l : mBook) {

                    if (l.getPid() == null || l.getPid().equals("")) {

                        fatherList.add(l);
                    }
                }
                for (int i = 0; i < fatherList.size(); i++) {
                    List<tBook> cls = new ArrayList<tBook>();
                    for (tBook l : mBook) {
                        if (fatherList.get(i).getCode().equals(l.getPid())) {
                            cls.add(l);
                        }
                    }
                    fatherList.get(i).setChilds(cls);
                }
//                System.out.println("集合处理后:"+JSON.toJSONString(fatherList));

                entryValue(fatherList);
            }else {
                ToastDemo.myHint(this, "mBook 数据为空", 4);
                fatherList.clear();
                entryValue(fatherList);
            }
    }

    String t = "";
    /**
     * 获得表册数据
     */
    public String dlBook() {

        //实例化异步任务
        myAsyncTask = new MyAsyncTask();
        //开始执行异步任务
        myAsyncTask.execute();

        return t;
    }

    MyAsyncTask myAsyncTask;
    class MyAsyncTask extends AsyncTask<Integer, Integer, String> {
    String str = "";

        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alert = new SpotsDialog(BookActivity.this, "下载父片区数据");
            alert.show();
            System.out.println("开始执行异步线程");
        }

        @Override
        protected String doInBackground(Integer... i) {

            kjt = new KJHttp();
            params = new HttpParams();
            params.put("readNumber", readNumber);
            System.out.println("readNember:"+(String)SharedPreUtils.get(BookActivity.this, "read_id", ""));
//        kjt.post(HTTP_IP + "local.json", null, new HttpCallBack() {
            kjt.post(HTTP_IP+"admin/search/findLocalById?", params, new HttpCallBack() {

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);

                    if ("".equals(t)) {
                        System.out.println("下载数据为空");
                        str = "下载数据为空";
                        alert.dismiss();
                    } else if (t.equals("[]")) {

                        System.out.println("无当前下载的数据");
//                        ToastDemo.myHint(getApplicationContext(), "无当前下载的数据");
                        str = "无当前下载的数据";
                        alert.dismiss();
                    } else {
                        System.out.println("下载数据:" + t);
                        mBook = JSON.parseArray(t, tBook.class);
                        dbBook.deleteByWhere(tBook.class, "id>0");

                        for (tBook tb : mBook) {

                            tb.setReadNumber(readNumber);
                            dbBook.save(tb);//把下载的数据存入数据库
                        }
                        str = "数据，下载成功";
//                        ToastDemo.myHint(BookActivity.this, "数据，下载成功");
                    }
                }

                @Override
                public void onFailure(int t, String strMsg) {
                    super.onFailure(t, strMsg);

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
                    System.out.println("http访问异常：" + strMsg);
//                    tv.setText("数据，下载失败！\n请检查网络或服务器是否开启！！！");
                }
            });
            SystemClock.sleep(5000);
            return str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("数据，下载成功")){
                finish();
                startActivity(new Intent(BookActivity.this, BookActivity.class));
            }else {
                ToastDemo.myHint(BookActivity.this, str, 4);
            }
            alert.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_ || v.getId() == R.id.ll_btn){

            tv.setVisibility(View.VISIBLE);
            if (readNumber.equals("D")){

                setBookData("光电表");
            }else if(readNumber.equals("Y")){

                setBookData("远传表");
            }else {
                dlBook();
            }
        }
    }

    private void setBookData(String codeName) {

        dbBook.deleteByWhere(tBook.class, "id>0");
        book.setReadNumber(readNumber);
        book.setCodeName(codeName);
        book.setPid("");
        book.setCode("");

        dbBook.save(book);

        finish();
        startActivity(new Intent(BookActivity.this, BookActivity.class));
    }

    private void idInit() {

        tv = (TextView) this.findViewById(R.id.tv_);
        btnBook = (Button) this.findViewById(R.id.btn_);
        TextView tvHint = (TextView) this.findViewById(R.id.tv_hint);
        tvHint.setText("潜江抄表片区");
        listView = (ExpandableListView) this.findViewById(R.id.listView);
        book = new tBook();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                listView.setSelection(0);

                fab.setVisibility(View.GONE);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                    int position = listView.getFirstVisiblePosition();
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

        btnBook.setBackgroundResource(R.drawable.ic_download);
//        btnBook.setText("下载");
        btnBook.setOnClickListener(this);
    }
}
