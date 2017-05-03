package com.dysen.type.meterSys;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dysen.table.tMeter;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.myUtil.ToastDemo;
import com.dysen.qj.wMeter.R;
import com.dysen.type.dataSys.PaymentHistoryActivity;
import com.dysen.type.dataSys.UsewAnalysisActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 通过表号查询缴费记录
 */
public class SelectMIdActivity extends MyActivityTools implements AdapterView.OnItemClickListener, View.OnClickListener{

    Button btnSelect;
    EditText etMeterId, etUserName;
    List<tMeter> lMeter;
    ListView lv;
    String type;
    ArrayList<HashMap<String, Object>> listItem;
    int mId;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mid);

        //透明状态栏
        StatusBarUtil.setTransparent(this);
        ((TextView) this.bindView(R.id.tv_hint)).setText("输入表号");

        (this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        type = getIntent().getStringExtra("type");
        Log.i("dysen", type);
        etMeterId = (EditText) this.findViewById(R.id.et_meterId_mid);
        etUserName = (EditText) this.findViewById(R.id.et_username_mid);
        btnSelect = (Button) this.findViewById(R.id.btn_select_mid);

        btnSelect.setEnabled(false);
        //初始状态查询数据库所有数据，并显示在listView中
        lv = (ListView) findViewById(R.id.select_lv);
        listItem = new ArrayList<HashMap<String, Object>>();

            etMeterId.addTextChangedListener(textWatcher);

            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String sMeterId = etMeterId.getText().toString();
                    String sUserName = etUserName.getText().toString();

                    if (!TextUtils.isEmpty(sMeterId)) {//&& !TextUtils.isEmpty(sUserName)

                        if (type.equals("payment") || type.equals("check")) {
                            Intent intent = new Intent(SelectMIdActivity.this, PaymentHistoryActivity.class);

                            intent.putExtra("meter_num", sMeterId);
                            startActivity(intent);

                        } else if (type.equals("analyse")) {
                            Intent intent = new Intent(SelectMIdActivity.this, UsewAnalysisActivity.class);

                            intent.putExtra("meter_num", sMeterId);
                            startActivity(intent);
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://wx.qjzls.com:8090/admin/query/assay?meterID=" +sMeterId)));
                        }else if (type.equals("location")){

                            Intent intent = new Intent(SelectMIdActivity.this, WaterEntryActivity.class);

                            intent.putExtra("item", mId);
                            intent.putExtra("code", code);
                            intent.putExtra("statusRead", "");
                            System.out.println("位置定位："+mId);
//                        intent.putExtra("meter_num", sMeterId);
                            startActivity(intent);
                        }else if(type.equals("photo")){
                            Intent intent = new Intent(SelectMIdActivity.this, PhotoShowActivity.class);

                            intent.putExtra("meter_num", sMeterId);
                            startActivity(intent);

                        }
                    } else {
                        ToastDemo.myHint(SelectMIdActivity.this, "表号可能有误！", 2);
                    }
                }
            });

        }

    /**
     * 更新listView显示内容
     * @param listItem 更新后的arraylist
     * @return具有新内容的适配器
     * @author mingwell
     */
    public SimpleAdapter refresh(ArrayList<HashMap<String, Object>> listItem){

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, listItem,//需要绑定的数据
                R.layout.activity_select_mid_item,//每一行的布局
                //动态数组中的数据源的键对应到定义布局的View中
                new String[] {"name","meterID", "ID", "addr"},
                new int[] {R.id.txt__username_mid_item, R.id.txt_meterId_mid_item, R.id.txt_id_mid_item, R.id.txt_addr_mid_item} );
        return mSimpleAdapter;
    }

    String str;
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            Log.d("TAG","afterTextChanged--------------->");
            str = etMeterId.getText().toString();
            try {
                if (str.equals("")){
                    str = "-1";
                }
                getMeter(str);
//                etUserName.setText(lMeter.get(0).getUserName());

            } catch (Exception e) {
                // TODO: handle exception
//                showDialog();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            Log.d("TAG","beforeTextChanged--------------->");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            Log.d("TAG","onTextChanged--------------->");

        }
    };

    /**
     * 获得表数据(数据库中)
     */
    public void getMeter(String meterId){
        //清空listitem内容
        listItem.clear();
        btnSelect.setEnabled(false);
        lMeter = dbMeter.findAllByWhere(tMeter.class, "meterID LIKE " + "\'" + meterId + "%\'" +" OR amrID LIKE "+ "\'%" + meterId + "%\'" );
        if (lMeter.size() >0) {

            for (int i = 0; i < lMeter.size(); i++) {

                System.out.println(lMeter.get(i).getUserName()+"第"+i+"用户：\t"+lMeter.get(i).getAreaId()+"***"+lMeter.get(i).getMeterID());
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("name", ""+lMeter.get(i).getUserName());
                map.put("meterID", ""+lMeter.get(i).getMeterID());
                map.put("ID", ""+lMeter.get(i).getAmrID());
                map.put("addr", ""+lMeter.get(i).getContactAddr());
                listItem.add(map);
            }

            if (lMeter.size() == 1 && meterId.length() >= 7){
//                btnSelect.setEnabled(true);
//                //因为下标是从 0 开始的
//                mId = Integer.valueOf(lMeter.get(0).getAreaId().substring(3)) -1;
//                entryData(0);//当只有一天记录时
            }else {

            }
//            etUserName.setText(lMeter.get(0).getUserName());
        }else {//"http://duzhaohui.f3322.net:8080/admin/search/findFlowByCode?code="+code

            etUserName.setText("");
            //清空listitem内容
            listItem.clear();
            if (!str.equals("-1")) {
                showDialog();
            }
        }

        //定义适配器
        SimpleAdapter mSimpleAdapter = refresh(listItem);
        //为ListView绑定适配器
        lv.setAdapter(mSimpleAdapter);

        lv.setOnItemClickListener(this);
    }

    private void showDialog(){

        ToastDemo.myHint(this, "你输入的整型数字有误，请改正", 2);
    }

    public void entryData(int position){

        String meterId = lMeter.get(position).getMeterID();
        Log.i("dysen", mId+"________position:"+position+"\tmeterId:"+meterId);
        etMeterId.setText(meterId);
        lMeter = dbMeter.findAllByWhere(tMeter.class, "meterID = " + "\'" + meterId + "\'");
        //因为下标是从 0 开始的
        mId = Integer.valueOf(lMeter.get(0).getAreaId().substring(3)) -1;
        code = lMeter.get(0).getCode();
        Log.i("dysen", position+"***"+mId);
//        if (mId > 0)
//            btnSelect.setEnabled(true);
        listItem.clear();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long i) {

        entryData(position);
        btnSelect.setEnabled(true);
    }
}
