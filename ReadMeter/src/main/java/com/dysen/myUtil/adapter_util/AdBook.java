package com.dysen.myUtil.adapter_util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.PercentDemo;
import com.dysen.mylibrary.utils.util.SharedPreUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBook;
import com.dysen.table.tMeter;
import com.dysen.type.meterSys.UserViewActivity;

import java.util.List;

/**
 *  自定义Adapter
 */
public class AdBook extends BaseExpandableListAdapter {

    public List<tBook> father;
    private Context context;
    Intent intent;
    int grpPosition, chdPosition;
//    int[] icolor = new int[]{R.color.pink_, R.color.blue_, R.color.purple_, R.color.yellow_,
//            R.color.orange_, R.color.green_, R.color.darkcyan};
    int[] icolor = new int[]{R.drawable.img_pink, R.drawable.img_blue, R.drawable.img_purple,
        R.drawable.img_yellow, R.drawable.img_orange, R.drawable.img_green, R.drawable.img_darkcyan };
    private String readNumber;

    public AdBook(List<tBook> faList, Context context) {  //初始化数据
        this.father = faList;
        this.context = context;
        intent = new Intent(context, UserViewActivity.class);
        readNumber = (String)SharedPreUtils.get(context, "read_id", "");
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

        view = LayoutInflater.from(context).inflate(
                R.layout.activity_ms_sub_book_item, null);
        TextView tvAreaName = (TextView) view
                .findViewById(R.id.tv_area_name);
        TextView tvCountAll = (TextView) view
                .findViewById(R.id.tv_count_all);
        TextView tvCode = (TextView) view.findViewById(R.id.tv_read_code);
        TextView tvReadCount = (TextView) view
                .findViewById(R.id.tv_read_count);
        TextView tvReadCompleted = (TextView) view
                .findViewById(R.id.tv_read_completed);
        Button btnDl = (Button) view.findViewById(R.id.btn_dl);
        ProgressBar readProgressbar = (ProgressBar) view.findViewById(R.id.read_progressbar);

        LinearLayout lJoinChildArea = (LinearLayout) view.findViewById(R.id.join_child_area);

        List<tMeter> listAll = null;
        if (readNumber.equals("D") || readNumber.equals("Y")){
                listAll = MyActivityTools.dbMeter.findAll(tMeter.class, "readNumber="+"\'"+ readNumber + "\'");
            if (listAll.size() > 0) {

                List<tMeter> list = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'" +
                        "AND readNumber="+"\'"+ readNumber + "\'");
//                tvCountAll.setText("总户数："+listAll.size());

                tvReadCount.setText("已抄数：" + list.size());
                tvReadCompleted.setText("抄表比率：" + PercentDemo.getPercent((long) list.size(), (long) listAll.size()));
                readProgressbar.setVisibility(View.VISIBLE);
                readProgressbar.setMax(listAll.size());
                readProgressbar.setProgress(list.size());

                if (list.size() <= listAll.size()) {
                    btnDl.setText("查看");
                    lJoinChildArea.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent.putExtra("code", "");
                            intent.putExtra("pid", "");
                            intent.putExtra("statusRead", "");
                            context.startActivity(intent);
                        }
                    });
                    btnDl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent.putExtra("code", "");
                            intent.putExtra("pid", "");
                            intent.putExtra("statusRead", "");
                            context.startActivity(intent);
                        }
                    });
                }
            } else {
                    btnDl.setText("下载");
                readProgressbar.setVisibility(View.GONE);
                    lJoinChildArea.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent.putExtra("code", "");
                            intent.putExtra("pid", "");
                            context.startActivity(intent);
                        }
                    });
                    btnDl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent.putExtra("code", "");
                            intent.putExtra("pid", "");
                            context.startActivity(intent);
                        }
                    });
                }
        }else{
            listAll = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");

        if (listAll.size() > 0) {
            List<tMeter> list = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "statusRead=" + "\'" + 1 + "\'" +
                    "AND code=" + "\'" + father.get(groupPosition).getChilds().get(childPosition).getCode() + "\'");
//            tvCountAll.setText("总户数："+listAll.size());
//            tvCountAll.setText("("+listAll.size()+"户)");
            tvReadCount.setText("已抄数：" + list.size());
            tvReadCompleted.setText("抄表比率：" + PercentDemo.getPercent((long) list.size(), (long) listAll.size()));
            readProgressbar.setVisibility(View.VISIBLE);
            readProgressbar.setMax(listAll.size());
            readProgressbar.setProgress(list.size());
            if (list.size() <= listAll.size()) {
                btnDl.setText("查看");
                lJoinChildArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("code", father.get(groupPosition).getChilds().get(childPosition).getCode());
                        intent.putExtra("pid", father.get(groupPosition).getChilds().get(childPosition).getPid());
                        intent.putExtra("statusRead", "");
                        context.startActivity(intent);
                    }
                });
                btnDl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("code", father.get(groupPosition).getChilds().get(childPosition).getCode());
                        intent.putExtra("pid", father.get(groupPosition).getChilds().get(childPosition).getPid());
                        intent.putExtra("statusRead", "");
                        context.startActivity(intent);
                    }
                });
            }
        } else {
            btnDl.setText("下载");
            readProgressbar.setVisibility(View.GONE);
            lJoinChildArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("code", father.get(groupPosition).getChilds().get(childPosition).getCode());
                    intent.putExtra("pid", father.get(groupPosition).getChilds().get(childPosition).getPid());
                    context.startActivity(intent);
                }
            });
            btnDl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("code", father.get(groupPosition).getChilds().get(childPosition).getCode());
                    intent.putExtra("pid", father.get(groupPosition).getChilds().get(childPosition).getPid());
                    context.startActivity(intent);
                }
            });
        }
    }

        tvAreaName.setText(father.get(groupPosition).getChilds().get(childPosition).getCodeName()
        +(listAll.size()>0 ? "\t("+listAll.size()+"户)" : ""));
//        tvAreaName.setTextColor(Color.BLACK);
        tvCode.setText("区编码：" + father.get(groupPosition).getChilds().get(childPosition).getCode());
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

        List<tMeter> listAll = MyActivityTools.dbMeter.findAllByWhere(tMeter.class, "code LIKE" + "\'" + father.get(groupPosition).getCode() + "%\'");
        textView.setText(father.get(groupPosition).getCodeName()+"(" + listAll.size() + ")");
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