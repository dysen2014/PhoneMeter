package com.dysen.myUtil.adapter_util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dysen.qj.wMeter.R;
import com.dysen.table.tMeter;

import java.util.List;

/**
 * 作者：沈迪 [dysen] on 2016-03-17 12:26.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：用户浏览页 自定义 Adapter
 */
public class AdUserView extends ArrayAdapter<tMeter> {

//    private Typeface mTypeFaceLight;
//    private Typeface mTypeFaceRegular;
    public List<tMeter> lMeter;
    private Context context;

    public AdUserView(Context context, List<tMeter> list) {
        super(context, 0 , list);
        this.lMeter = list;
        this.context = context;
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.activity_ms_user_view_item, null);
        TextView tvUserViewName, tvUserViewPhone, tvUserViewAddr, tvUserViewStatus, tvUserViewItem,
                tvUserViewNum, tvUserViewLast, tvUserViewThis, tvUserViewWaterVolume,
                tvUserViewInfo, tvUserViewMeterNum;

            tvUserViewItem = (TextView) convertView.findViewById(R.id.tv_user_view_item);
            tvUserViewName = (TextView) convertView.findViewById(R.id.tv_user_view_name);
            tvUserViewNum = (TextView) convertView.findViewById(R.id.tv_user_view_num);
//            tvUserViewPhone = (TextView) convertView.findViewById(R.id.tv_user_view_phone);
            tvUserViewMeterNum = (TextView) convertView.findViewById(R.id.tv_user_view_meter_num);
            tvUserViewAddr = (TextView) convertView.findViewById(R.id.tv_user_view_addr);
            tvUserViewLast = (TextView) convertView.findViewById(R.id.tv_user_view_last);
            tvUserViewThis = (TextView) convertView.findViewById(R.id.tv_user_view_this);
            tvUserViewStatus = (TextView) convertView.findViewById(R.id.tv_user_view_status);
            tvUserViewInfo = (TextView) convertView.findViewById(R.id.tv_user_view_info);

            tvUserViewWaterVolume = (TextView) convertView.findViewById(R.id.tv_user_view_water_volume);

        if (lMeter.size() > 0){

            int index = Integer.valueOf(lMeter.get(position).getAreaId().substring(3));
            tvUserViewItem.setText("第 "+index+" 户");
            tvUserViewName.setText("姓名："+lMeter.get(position).getUserName());
            tvUserViewNum.setText("用户编号："+lMeter.get(position).getAmrID());
            tvUserViewMeterNum.setText("表号："+lMeter.get(position).getMeterID());
            tvUserViewAddr.setText("地址："+lMeter.get(position).getContactAddr());
            tvUserViewLast.setText("本月起码："+lMeter.get(position).getReadEndTemp());
            tvUserViewThis.setText("本月止码："+lMeter.get(position).getReadEnd());
            tvUserViewInfo.setText("抄表详情："+lMeter.get(position).getReadInfo());
            System.out.println("抄表stustas："+lMeter.get(position).getStatusRead());
            if (lMeter.get(position).getReadEnd() < lMeter.get(position).getReadStart()) {
                if ("1".equals(lMeter.get(position).getStatusRead())) {
//            c.uViewInfo = "已抄";
                    tvUserViewStatus.setText("抄表状态：" + "已抄"+("\t(异常)"));
                } else {
//            c.uViewInfo = "未抄";
                    tvUserViewStatus.setText("抄表状态：" + "未抄"+("\t(异常)"));
                }
                tvUserViewStatus.setTextColor(Color.BLUE);
                tvUserViewWaterVolume.setTextColor(Color.BLUE);
            }else{
                if ("1".equals(lMeter.get(position).getStatusRead())) {//

                    tvUserViewStatus.setText("抄表状态：" + "已抄");
                    tvUserViewStatus.setTextColor(Color.GREEN);
                }else {
                    tvUserViewStatus.setText("抄表状态：" + "未抄");
                    tvUserViewStatus.setTextColor(Color.RED);

                }

                if (lMeter.get(position).getReadEnd() - lMeter.get(position).getReadStart() > 50){
                    tvUserViewWaterVolume.setTextColor(Color.RED);
                }
            }
            tvUserViewWaterVolume.setText("用量：" + (lMeter.get(position).getReadEnd() - lMeter.get(position).getReadEndTemp())+"\tm³");

        }else {

        }
        return convertView;
    }

}
