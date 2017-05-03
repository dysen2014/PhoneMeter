package com.dysen.myUtil.adapter_util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dysen.table.tPaymentHistory;
import com.dysen.qj.wMeter.R;

import java.util.List;

/**
 * 作者：沈迪 [dysen] on 2016-03-17 15:13.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class AdPaymentHistory extends ArrayAdapter<tPaymentHistory> {

    public List<tPaymentHistory> lMeter;
    private Context context;

        public AdPaymentHistory(Context context, List<tPaymentHistory> list) {
            super(context, 0 , list);
            this.lMeter = list;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.activity_ms_payment_history_item, null);
            TextView tvHistoryDate, tvLast, tvThis, tvInfo, tvWaterVolume,
                    tvPrice, tvLateFees, tvWaterFees, tvPaymentStatus;
                tvHistoryDate = (TextView) convertView.findViewById(R.id.tv_history_date);
                tvLast = (TextView) convertView.findViewById(R.id.tv_history_last);
                tvThis = (TextView) convertView.findViewById(R.id.tv_history_this);
                tvInfo = (TextView) convertView.findViewById(R.id.tv_history_info);
                tvWaterVolume = (TextView) convertView.findViewById(R.id.tv_history_water_volume);
                tvPrice = (TextView) convertView.findViewById(R.id.tv_history_price);
                tvLateFees = (TextView) convertView.findViewById(R.id.tv_history_latefees);
                tvWaterFees = (TextView) convertView.findViewById(R.id.tv_history_waterfees);
                tvPaymentStatus = (TextView) convertView.findViewById(R.id.tv_history_payment_status);
//            holder.tv = (TextView) convertView.findViewById(R.id.tv_user_view_);

            if (lMeter.size() > 0) {
                tvHistoryDate.setText("水量日期：" + lMeter.get(position).getTimeAccount());
                tvLast.setText("上月止码：" + lMeter.get(position).getReadStart());
                tvThis.setText("本月止码：" + lMeter.get(position).getReadEnd());
//                tvPrice.setText("当前水价：" + lMeter.get(position).get());
//                tvLateFees.setText("滞纳金：" + lMeter.get(position).getHLateFees());
//                tvInfo.setText("加减数量：" + lMeter.get(position).getHInfo());
                tvWaterVolume.setText("实用水量：" + lMeter.get(position).getUsed());
//                tvWaterFees.setText("总水费：" + ((Integer.parseInt(lMeter.get(position).getUsed())-Integer.parseInt(lMeter.get(position).getHInfo()))
//                        *Integer.parseInt(lMeter.get(position).getHPrice())+Integer.parseInt(lMeter.get(position).getHLateFees())));
                String status = "";
                Integer s = lMeter.get(position).getStatus();
                switch ( s != null ? s : 0){
                    case 1:
                        status = "未缴费";
                        tvPaymentStatus.setTextColor(Color.parseColor("#8B4513"));
                    break;
                    case 20:
                        status = "已缴费";
                        tvPaymentStatus.setTextColor(Color.parseColor("#98FB98"));
                    break;
                    case 21:
                        status = "预缴费";
                    break;
                    case 22:
                        status = "IC卡缴费";
                    break;
                    case 23:
                        status = "IC卡更表";
                        break;
                }

                tvPaymentStatus.setText("缴费状态：" + status);

                tvWaterFees.setText("总水费：" +lMeter.get(position).getAccountFeeAll());
            }
            return convertView;
        }
}
