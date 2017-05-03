package com.dysen.type.dataSys;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.dysen.table.tBook;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.adapter_util.AdStatistics;
import com.dysen.qj.wMeter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计查询
 */
public class StatisticsActivity extends MyActivityTools {

    Button btnBook;
    AdStatistics updateAdapter;//绑定数据的adpter
    ExpandableListView listView; //控件
    List<tBook> fatherList;   //放置父类数据
    TextView tv;
    Handler handler;
    List<tBook> mBook;

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


        idInit();
        fatherList = new ArrayList<tBook>();

        getBook();
    }

    private void idInit() {

        tv = (TextView) this.findViewById(R.id.tv_);

        TextView tvHint = (TextView) this.findViewById(R.id.tv_hint);
        tvHint.setText("数据统计");
        btnBook = (Button) this.findViewById(R.id.btn_);
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

    }

    private void getBook() {
        mBook = dbBook.findAll(tBook.class);
        if (mBook.size()>0){

            mBookHandle(mBook);
            tv.setVisibility(View.GONE);
        }else {
            tv.setVisibility(View.VISIBLE);
            tv.setText("无数据");
        }
    }

    /**
     * 给 listview 填充数据
     */
    private void entryValue(List<tBook> fatherList){

        //绑定数据
        if (updateAdapter == null) {
            updateAdapter = new AdStatistics(fatherList, StatisticsActivity.this);

//            for(int i=0;i<updateAdapter.getGroupCount();i++) {
//                System.out.println(updateAdapter.father.get(i).getCodeName()+"***********&&&&&&&local&&&&&&&&&****************" + updateAdapter.father.get(i).getChilds().get(0).getCodeName());
//            }

            listView.setAdapter(updateAdapter);
        }
    }

    /**
     * 表册数据处理
     * @param mBook
     */
    private void mBookHandle(List<tBook> mBook) {

//            System.out.println(mBook.size()+"集合处理前："+JSON.toJSONString(mBook))

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
}
