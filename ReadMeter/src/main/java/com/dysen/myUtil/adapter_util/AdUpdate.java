package com.dysen.myUtil.adapter_util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.PercentDemo;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.dialog.SpotsDialog;
import com.dysen.myUtil.httpUtil.HttpRequest;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.mylibrary.utils.util.SharedPreUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBook;
import com.dysen.table.tMeter;
import com.dysen.type.meterSys.UserViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *  自定义Adapter
 */
public class AdUpdate extends BaseExpandableListAdapter {

    public List<tBook> father;
    private Context context;
    Intent intent;
    int grpPosition, chdPosition;
    tMeter meter;
    View v;
    private String readNumber;
    List<tMeter> listAll;

    public AdUpdate(){}

    public AdUpdate(List<tBook> faList, Context context) {  //初始化数据
        this.father = faList;
        this.context = context;
        intent = new Intent(context, UserViewActivity.class);
        readNumber = (String) SharedPreUtils.get(context, "read_id", "");
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return father.get(groupPosition).getChilds().get(childPosition);   //获取父类下面的每一个子类项
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;  //子类位置
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) { //显示子类数据的iew
        View view = null;
        grpPosition = groupPosition;
        chdPosition = childPosition;

        meter = new tMeter();
        view = LayoutInflater.from(context).inflate(
                R.layout.activity_ms_sub_book_item, null);
        final TextView tvAreaName = (TextView) view
                .findViewById(R.id.tv_area_name);

        tvAreaName.setTextColor(Color.BLUE);
        TextView tvCountAll = (TextView) view
                .findViewById(R.id.tv_count_all);
        TextView tvReadCount = (TextView) view
                .findViewById(R.id.tv_read_count);
        TextView tvReadCompleted = (TextView) view
                .findViewById(R.id.tv_read_completed);
        Button btnDl = (Button) view.findViewById(R.id.btn_dl);
        ProgressBar readProgressbar = (ProgressBar) view.findViewById(R.id.read_progressbar);

        btnDl.setVisibility(View.GONE);
        LinearLayout lJoinChildArea = (LinearLayout) view.findViewById(R.id.join_child_area);

        if (readNumber.equals("D") || readNumber.equals("Y")) {
            listAll = MyActivityTools.dbMeter.findAll(tMeter.class, "readNumber=" + "\'" + readNumber + "\'");
            if (listAll.size() > 0) {

                List<tMeter> l = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'" +
                        "AND readNumber=" + "\'" + readNumber + "\'" + "AND statusUpdate=" + "\'" + 1 + "\'");
                tvReadCount.setText("已上传数：" + l.size());
                tvReadCompleted.setText("上传比率：" + PercentDemo.getPercent((long) l.size(), (long) listAll.size()));
                readProgressbar.setVisibility(View.VISIBLE);
                readProgressbar.setMax(listAll.size());
                readProgressbar.setProgress(l.size());
//                final List<tMeter> list = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'" +
//                        "AND readNumber=" + "\'" + readNumber + "\'" + "AND statusUpdate=" + "\'" + 0 + "\'");
                final List<tMeter> list = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'" +
                        "AND readNumber=" + "\'" + readNumber + "\'");
                if (list.size() > 0) {//存在未上传的
                    btnDl.setVisibility(View.VISIBLE);
                    btnDl.setText("上传");
                    btnDl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        updateData(list);
                        }
                    });
                    lJoinChildArea.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        updateData(list);
                        }
                    });
                } else {
                    btnDl.setVisibility(View.GONE);
                }
            }else {
                readProgressbar.setVisibility(View.GONE);
            }
        }else {
            listAll = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");

            if (listAll.size() > 0) {
                final List<tMeter> l = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'"
                        + "AND statusUpdate=" + "\'" + 1 + "\'"
                        + "AND code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");

//            tvCountAll.setText("总户数："+listAll.size());
//            tvCountAll.setText("("+listAll.size()+"户)");
                tvReadCount.setText("已上传数：" + l.size());
                tvReadCompleted.setText("上传比率：" + PercentDemo.getPercent((long) l.size(), (long) listAll.size()));
                readProgressbar.setVisibility(View.VISIBLE);
                readProgressbar.setMax(listAll.size());
                readProgressbar.setProgress(l.size());
//                final List<tMeter> list = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'"
//                        + "AND statusUpdate=" + "\'" + "0" + "\'" +
//                        "AND code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");
            final List<tMeter> list = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'"+
                    "AND code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");
                if (list.size() > 0) {//存在未上传的
                    btnDl.setVisibility(View.VISIBLE);
                    btnDl.setText("上传");
                    btnDl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        updateData(list);
                        }
                    });
                    lJoinChildArea.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        updateData(list);
                        }
                    });
                } else {
                    btnDl.setVisibility(View.GONE);
//                ToastDemo.myHint(context, "无上传数据");
//                dialog("");
                }
            } else {
                readProgressbar.setVisibility(View.GONE);
//            ToastDemo.myHint(context, "无数据");
            }
        }

        tvAreaName.setText(father.get(groupPosition).getChilds().get(childPosition).getCodeName()
                +(listAll.size()>0?"("+listAll.size()+"户)":""));
        tvAreaName.setTextColor(Color.BLACK);
        return view;
    }

    public void dialog(String str){

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // 退出
                dialog.dismiss();

            }
        });
        alert.setNegativeButton("稍后", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.create().setTitle(str);
        alert.show();
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

            params = "data="+JSON.toJSONString(l);
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
                myDialogShow("上传失败 ");
            }else{

                    String t = s;

                    List<Integer> l = JSON.parseArray(t, Integer.class);
                if (l != null) {
                    if (l.size() > 0) {
                        for (int i = 0; i < l.size(); i++) {
//                            System.out.println(l.get(i)+"起码"+list.get(i).getReadStart()+"止码："+list.get(i).getReadEnd());
                            meter.setStatusUpdate("1");
                            meter.setReadStart(list.get(i).getReadStart());
                            meter.setReadEnd(list.get(i).getReadEnd());
                            meter.setReadEndTemp(list.get(i).getReadEndTemp());
                            MyActivityTools.dbMeter.update(meter, "id=" + "\'" + l.get(i) + "\'");//+"AND =" + "\'" +  + "\'"
                        }
                        ToastDemo.myHint(context, "上传成功 ", 1);
                        myDialogShow("上传成功 ");
                    }
                }
            }
            alert.cancel();
        }
    }

//    public void updateData(final List<tMeter> list) {
//
//        alert = new SpotsDialog(context, "更新表数据");
//        alert.show();
//
//        KJHttp kjt = new KJHttp();
//        HttpParams params = new HttpParams();
//
//        List<tUpdate> listU = new ArrayList<>();
//       listU = JSON.parseArray(JSON.toJSONString(list), tUpdate.class);
//
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
//                      //                         myDialogShow("上传失败 ");
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
//                        }
//                        ToastDemo.myHint(context, "上传成功", 1);
//                         myDialogShow("上传成功 ");
//                    }
//                    System.out.println("返回信息是+" + t.toString());
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
//
//            @Override
//            public void onLoading(long count, long current) {
//                super.onLoading(count, current);
//                if (current == count)
//                    System.out.println("上传进度：" + current / count);
//                myStartGif(context, v, "正在上传表数据 "+"\n上传进度：" + PercentDemo.getPercent(current, count));//	启动gif动画
//            }
//        });
//
//    }

    public void myDialogShow(String sTitle) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

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

    public static AlertDialog alert;
    /**
     * dysen 2015-7-11 上午11:45:15 info: 启动 动画 gif 播放
     */
    public static void myStartGif(Context context, View v, String str) {

        // 把该 Activity 添加到 activity列表里 方便退出时一下子退出所有activity
        // QuitAllActivity.getInstance().addActivity(this);

        alert = new AlertDialog.Builder(context).setTitle(str).setView(v).show();

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

    @Override
    public int getChildrenCount(int groupPosition) {
        return father.get(groupPosition).getChilds().size();  //子类item的总数
    }

    @Override
    public Object getGroup(int groupPosition) {   //父类数据
        return father.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return father.size();  ////父类item总数
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;   //父类位置
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_ms_book_item, null);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
        textView.setText(father.get(groupPosition).getCodeName());

        ((LinearLayout)view.findViewById(R.id.ll_title)).setBackgroundResource(MyActivityTools.icolor[groupPosition]);
//        BackgroundColor(icolor[groupPosition]);
        ImageView imgTitle = (ImageView) view.findViewById(R.id.img_title);
        imgTitle.setBackgroundResource(R.drawable.arer);
        if (isExpanded)
            imageview.setBackgroundResource(R.drawable.up);
        else
            imageview.setBackgroundResource(R.drawable.down);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {  //点击子类触发事件
//        Toast.makeText(context,
//                "第" + groupPosition + "大项，第" + childPosition + "小项被点击了",
//                Toast.LENGTH_LONG).show();
        return true;

    }

}