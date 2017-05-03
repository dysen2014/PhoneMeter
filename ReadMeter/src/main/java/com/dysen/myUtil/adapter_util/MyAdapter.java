package com.dysen.myUtil.adapter_util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dysen.qj.wMeter.R;

import java.util.List;

/**
 *  自定义Adapter
 */
public class MyAdapter extends ArrayAdapter<ContentItem> {

//    private Typeface mTypeFaceLight;
//    private Typeface mTypeFaceRegular;

    public MyAdapter(Context context, List<ContentItem> objects) {
        super(context, 0, objects);

//        mTypeFaceLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
//        mTypeFaceRegular = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContentItem c = getItem(position);

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_ms_mlist_item, null);
            holder.tvReadMDate2 = (TextView) convertView.findViewById(R.id.tv_readM_date2);
            holder.tvReadMStatus = (TextView) convertView.findViewById(R.id.tv_readM_status);
            holder.tvReadMAddr2 = (TextView) convertView.findViewById(R.id.tv_readM_addr2);
            holder.tvSum = (TextView) convertView.findViewById(R.id.tv_sum);
            holder.tvCompleted = (TextView) convertView.findViewById(R.id.tv_completed);
            holder.tvPercent = (TextView) convertView.findViewById(R.id.tv_percent);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvReadMDate2.setText(c.readMDate2);
        holder.tvReadMStatus.setText("状态："+c.readMStatus);
        holder.tvReadMAddr2.setText(c.readMAddr2);
        holder.tvSum.setText("总户数："+c.sum+"");
        holder.tvCompleted.setText("已抄数："+c.completed+"");
        holder.tvPercent.setText(c.percent);

        return convertView;
    }

    private class ViewHolder {

        TextView tvReadMAddr2, tvReadMStatus, tvReadMDate2, tvSum, tvCompleted;
        TextView tvPercent;
    }
}
