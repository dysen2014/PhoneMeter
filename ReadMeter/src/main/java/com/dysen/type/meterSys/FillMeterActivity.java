package com.dysen.type.meterSys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.dysen.myUtil.adapter_util.AdFill;
import com.dysen.myUtil.httpUtil.HttpExcTint;
import com.dysen.mylibrary.utils.kjframe.KJHttp;
import com.dysen.mylibrary.utils.kjframe.http.HttpCallBack;
import com.dysen.mylibrary.utils.kjframe.http.HttpParams;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBook;

import java.util.ArrayList;
import java.util.List;

/**
 * 补抄 漏抄
 */
public class FillMeterActivity  extends MyActivityTools implements View.OnClickListener{

    Button btnBook, btnIcon;
    AdFill bookAdapter;//绑定数据的adpter
    ExpandableListView listView; //控件
    List<tBook> fatherList;   //放置父类数据
    TextView tv;
    Handler handler;
    List<tBook> mBook;
    FloatingActionButton fab;

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

    //        initViews();
    idInit();
    fatherList = new ArrayList<tBook>();

    getBook();
}

    private void getBook() {

        mBook = dbBook.findAll(tBook.class);
        if (mBook.size()>0){

            mBookHandle(mBook);
            tv.setVisibility(View.GONE);
        }else {
            tv.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 给 listview 填充数据
     */
    private void entryValue(List<tBook> fatherList){

        //绑定数据
        if (bookAdapter == null) {
            bookAdapter = new AdFill(fatherList, FillMeterActivity.this);

//            for(int i=0;i<bookAdapter.getGroupCount();i++) {
//                System.out.println(bookAdapter.father.get(i).getCodeName()+"***********&&&&&&&local&&&&&&&&&****************" + bookAdapter.father.get(i).getChilds().get(0).getCodeName());
//            }

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
            ToastDemo.myHint(this, "mBook 数据为空", 2);
        }
    }

    String t = "";
    /**
     * 获得表册数据
     */
    public String dlBook() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                t = HttpRequest.sendPost(HTTP_IP+"local.json", "");
//                System.out.println("t:" + t);
//                if ("".equals(t)) {
//                    System.out.println("下载数据为空");
//
//                    return ;
//                } else {
//                    mBook = JSON.parseArray(t, tBook.class);
//                    dbBook.deleteByWhere(tBook.class, "id>0");
//                    for (tBook tb : mBook)  {
//                        dbBook.save(tb);//把下载的数据存入数据库
//                    }
//
//                    finish();
//                    startActivity(new Intent(FillMeterActivity.this, BookActivity.class));
//                }
//            }
//        }).start();

        KJHttp kjt = new KJHttp();
	HttpParams params = new HttpParams();
	params.put("data", "");
	kjt.post(HTTP_IP+"local.json", null, new HttpCallBack() {
		@Override
		public void onSuccess(String t) {
			super.onSuccess(t);
            if ("".equals(t)) {
                System.out.println("下载数据为空");
                return;
            } else {
                mBook = JSON.parseArray(t, tBook.class);
                dbBook.deleteByWhere(tBook.class, "id>0");
                for (tBook tb : mBook) {
                    dbBook.save(tb);//把下载的数据存入数据库
                }

                finish();
                startActivity(new Intent(FillMeterActivity.this, FillMeterActivity.class));
            }
		}

		@Override
		public void onFailure(int t, String strMsg) {
			super.onFailure(t, strMsg);
			//加载失败的时候回调
			System.out.println("http访问异常：" + strMsg);

            if (HttpExcTint.hTimeout.equals(strMsg)){
                ToastDemo.myHint(FillMeterActivity.this, "访问超时",3);
            }else if(HttpExcTint.hNoConnectServer.equals(strMsg)){
                ToastDemo.myHint(FillMeterActivity.this, "访问失败", 3);
            }else {
                ToastDemo.myHint(FillMeterActivity.this, "访问异常", 3);
            }
                tv.setText("数据，下载失败！\n请检查网络或服务器是否开启！！！");
		}

		@Override
		public void onLoading(long count, long current) {
			super.onLoading(count, current);
			if (current == count)
				System.out.println("进度：" + current / count);
		}
	});
        return t;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_){

//            tv.setVisibility(View.VISIBLE);
//            dlBook();
            if (t.isEmpty()){
//                tv.setText("数据，下载失败！\n请检查网络或服务器是否开启！！！");
            }
        }
    }

    private void idInit() {

        tv = (TextView) this.findViewById(R.id.tv_);
        btnBook = (Button) this.findViewById(R.id.btn_);
        TextView tvHint = (TextView) this.findViewById(R.id.tv_hint);
        tvHint.setText("补抄漏抄");
        listView = (ExpandableListView) this.findViewById(R.id.listView);

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

//        btnBook.setBackgroundResource(android.R.drawable.ic_menu_search);
//        btnBook.setText("下载");
//        btnBook.setOnClickListener(this);
    }
}
