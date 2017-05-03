package com.dysen.myUtil.adapter_util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dysen.table.tBook;
import com.dysen.table.tMeter;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyColorArcProgressBar;
import com.dysen.myUtil.PercentDemo;
import com.dysen.qj.wMeter.R;
import com.dysen.type.meterSys.UserViewActivity;

import java.util.List;

/**
 *  自定义Adapter
 */
public class AdStatistics extends BaseExpandableListAdapter {

    public List<tBook> father;
    private Context context;
    Intent intent;
    int grpPosition, chdPosition;
    tMeter meter;

    TextView tvUpdateStatus;

    public AdStatistics(List<tBook> faList, Context context) {  //初始化数据
        this.father = faList;
        this.context = context;
        intent = new Intent(context, UserViewActivity.class);
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
                R.layout.activity_statistics_item, null);
        final TextView textView = (TextView) view
                .findViewById(R.id.textview1);
        tvUpdateStatus = (TextView) view
                .findViewById(R.id.tv_update_status);
        textView.setTextColor(Color.BLUE);
        TextView tvCountAll = (TextView) view
                .findViewById(R.id.tv_count_all);
        TextView tvReadCount = (TextView) view
                .findViewById(R.id.tv_read_count);
        TextView tvReadCompleted = (TextView) view
                .findViewById(R.id.tv_read_completed);
        Button btnDl = (Button) view.findViewById(R.id.btn_dl);
        ProgressBar statisticsProgressbar = (ProgressBar) view.findViewById(R.id.statistics_progressbar);

        btnDl.setVisibility(View.GONE);
        MyColorArcProgressBar pbReadM = (MyColorArcProgressBar) view.findViewById(R.id.bar1);
        MyColorArcProgressBar pbUsed = (MyColorArcProgressBar) view.findViewById(R.id.bar2);
        final List<tMeter> listAll = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");
        final List<tMeter> listUsed = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");

        if (listAll.size() > 0) {
            List<tMeter> list = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'"
                    + "AND code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");
            int i = -1;
            if (list.size() >0){
                i = list.size();
            }else {
               i = 0;
            }

            long l = 0;
            if (listUsed.size()>0){
                for (tMeter t : listUsed){
                  l +=  Long.valueOf(t.getReadEnd() - t.getReadEndTemp());
                }
                System.out.println("l"+l);
            }
            System.out.println("总户数："+listAll.size()+"\t已抄数："+i);
//            tvCountAll.setText("总户数："+listAll.size());
            tvReadCount.setText("已抄数："+i);
            tvReadCompleted.setText("抄表比率："+ PercentDemo.getPercent((long)i, (long)listAll.size()));
            statisticsProgressbar.setMax(listAll.size());
            statisticsProgressbar.setVisibility(View.VISIBLE);
            statisticsProgressbar.setProgress(i);
            pbReadM.setMaxValues(listAll.size());//设置最大值
            if (l>10000)
                pbUsed.setMaxValues(l);
            else
            pbUsed.setMaxValues(10000);
            pbUsed.setCurrentValues(l);
            pbReadM.setCurrentValues(i);

        }else {
//            ToastDemo.myHint(context, "无数据");
            statisticsProgressbar.setVisibility(View.GONE);
        }

        textView.setText(father.get(groupPosition).getChilds().get(childPosition).getCodeName()
                +(listAll.size()>0?"("+listAll.size()+"户)":""));
        textView.setTextColor(Color.BLACK);
        return view;
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