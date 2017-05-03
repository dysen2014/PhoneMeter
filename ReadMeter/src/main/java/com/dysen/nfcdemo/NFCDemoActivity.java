package com.dysen.nfcdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyUtils;
import com.dysen.myUtil.StatusBarUtil;
import com.dysen.myUtil.ToastDemo;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tMeter;
import com.dysen.type.meterSys.WaterEntryActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.action;

public class NFCDemoActivity extends MyActivityTools {

    NfcAdapter nfcAdapter;
    TextView Title, Promt;
    Button Read, Write;
    Spinner sSector, sBlock;
    EditText Key, Data, Sector, Block, wMeterId;
    TextView tvBlockCount;
    TextView txtReadContext;
    RadioGroup RadioGrp;
    RadioButton RadioBtn1,RadioBtn2;
    boolean flag = true;

    PendingIntent mPendingIntent;
    IntentFilter[]  mIntentFilter;
    String[][] mTechList;
    Intent it;
    long meterId;

    int position = 0;
    LinearLayout llData;

    private ArrayAdapter<CharSequence> adSector, adBlock;
    String strBlock="0", strSector="0", ssBlock = "0";
    private NfcB nfcB;
    private NfcA nfcA;
    //读取TAG
    MifareClassic mfc;
    private String code;
    private IsoDep isoDep;
    private ImageView nfcImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_demo);

        //透明状态栏
        StatusBarUtil.setTransparent(this);

        (this.bindView(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        idInit();// 资源 id 初始化

        nfcCheck();

        //适配器
        adSector = ArrayAdapter.createFromResource(this, R.array.sector, android.R.layout.simple_spinner_item);
        //设置样式 为适配器设置下拉列表下拉时的菜单样式
        adSector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器 将适配器添加到下拉列表上
        sSector.setAdapter(adSector);
        //适配器
        adBlock = ArrayAdapter.createFromResource(this, R.array.block, android.R.layout.simple_spinner_item);
        //设置样式 为适配器设置下拉列表下拉时的菜单样式
        adBlock.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器 将适配器添加到下拉列表上
        sBlock.setAdapter(adBlock);

        sSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strSector = adSector.getItem(position).toString();
                Sector.setText("第:");
//                Block.setText(adBlock.getItem(0));
                if (!"".equals(strSector) && !"".equals(strBlock)){
                    System.out.println("第"+strSector+"区第"+strBlock+"块");
                    ssBlock = (Integer.parseInt(strSector)*4 + Integer.parseInt(strBlock))+"";
                    tvBlockCount.setText("第"+ssBlock+"块");
//                    System.out.println("第"+ssBlock+"块");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sBlock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strBlock = adBlock.getItem(position).toString();
                //Block.getText().toString();

                Block.setText("区第:");
                if (!"".equals(strSector) && !"".equals(strBlock)){
                    System.out.println("第"+strSector+"区第"+strBlock+"块");
                    ssBlock = (Integer.parseInt(strSector)*4 + Integer.parseInt(strBlock))+"";
                    tvBlockCount.setText("第"+ssBlock+"块");
                    System.out.println("第"+ssBlock+"块");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter intentFilter2 = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter intentFilter3 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            intentFilter.addDataType("*/**");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mIntentFilter = new IntentFilter[]{intentFilter, intentFilter2, intentFilter3};

        Read.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
//                llData.setVisibility(View.VISIBLE);
                meterId = readFromTag(it);//读数据
                ToastDemo.myHint(NFCDemoActivity.this, "当前表号："+meterId, 5);
                if (meterId > 0){
//                    myDialog("");
                }
            }
        });

        Write.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Savils", "start");
                llData.setVisibility(View.VISIBLE);
                write2Tag(it);//写数据
                Log.i("Savils", "final");
            }
        });
        RadioGrp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId == RadioBtn1.getId())
                    flag = true;
                if(checkedId == RadioBtn2.getId())
                    flag = false;
            }
        });
    }


    //将数据转换为GB2312
    private String gb2312ToString(byte[] data) {
        String str = null;
        try {
            str = new String(data, "gb2312");//"utf-8"
        } catch (UnsupportedEncodingException e) {
        }
        return str;
    }

    /**
     * nfc 检查 设备是否支持 或 是否 开启
     */
    private void nfcCheck() {

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Title.setText("设备不支持NFC！");
            finish();
            return;
        }else {
            if (!nfcAdapter.isEnabled()) {
                Title.setText("请在系统设置中先启用NFC功能！");
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
//                        new Intent(Settings.ACTION_NFC_SETTINGS);
                startActivity(intent);
                return;
            }
        }
    }

    private void idInit() {
        Title = (TextView)bindView(R.id.Title);
        Promt = (TextView) bindView(R.id.Promt);
        Read = (Button)bindView(R.id.Read);
        Write = (Button)bindView(R.id.Write);
        Read.setEnabled(false);
        Write.setEnabled(false);
        sSector = (Spinner)bindView(R.id.sSector);
        sBlock = (Spinner)bindView(R.id.sBlock);
        Sector = (EditText)bindView(R.id.Sector);
        Block = (EditText)bindView(R.id.Block);
        Key =(EditText)bindView(R.id.Key);
        Data = (EditText)bindView(R.id.Data);
        wMeterId = (EditText)bindView(R.id.meterId);
        tvBlockCount = (TextView)bindView(R.id.BlockCount) ;
        llData = (LinearLayout)bindView(R.id.ll_data);

        Key.setText(R.string.nfc_key);
        Data.setText("");
        RadioGrp = (RadioGroup)bindView(R.id.RadioGrp);
        RadioBtn1 = (RadioButton)bindView(R.id.RadioBtn1);
        RadioBtn2 = (RadioButton)bindView(R.id.RadioBtn2);

        txtReadContext = bindView(R.id.nfc_read_context);
        nfcImg = bindView(R.id.nfc_img);
        txtReadContext.setText("请靠近您的卡片！");
        TextView tvHint = bindView(R.id.tv_hint);
        tvHint.setText("NFC 定位");

    }

    public void resolveIntent( Intent intent){
        String action = intent.getAction();
        //得到是否检测到ACTION_TECH_DISCOVERED触发
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            //处理该intent
            Title.setText("ACTION_NDEF 连接成功");
            NdefMessage[] msg = null;
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Parcelable[] rawMsg = intent.getParcelableArrayExtra(NfcAdapter.ACTION_NDEF_DISCOVERED);
            if (rawMsg != null){
                msg = new NdefMessage[rawMsg.length];
                for (int i=0; i<rawMsg.length; i++){
                    msg[i] = (NdefMessage) rawMsg[i];
                }
            }else {
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage message = new NdefMessage(new NdefRecord[]{record});
                msg = new NdefMessage[]{message};
            }
            Promt.setText("Scan a " + intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
            processNDEFMsg(msg);

//            boolean bl = readFromTag2(getIntent());
//            Log.i("dysen", bl+"---"+readResult);
//            if (bl)
//                ToastDemo.myHint(this, readResult);
//            else
//                ;

        }else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            //处理该intent
            Title.setText("ACTION_TECH 连接成功");
//            readFromTag(intent);

        }else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            //处理该intent
            Title.setText("ACTION_TAG 连接成功");
//            readFromTag(intent);
//
//            ToastDemo.myHint(NFCDemoActivity.this, "当前表号："+meterId);
//            if (meterId > 0){
//                myDialog("");
//            }
        }else{

        }

        Promt.setText("");

//            alert.dismiss();

        it = intent;
//        meterId = readFromTag(it);//读数据

        Read.setEnabled(true);
        Write.setEnabled(true);

    }

    boolean flagSelect, flagShow;
    Dialog dialog;
    /**
     * 提示操作员(是否定位到本表位置)
     */
    public void myDialog(String str) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        if (flagShow)
            dialog.dismiss();
        alert.create();

        List<tMeter> lMeter = dbMeter.findAllByWhere(tMeter.class, "meterID=" + "\'" + meterId + "\'");
        if (lMeter.size() > 0){
            String areaId = lMeter.get(0).getAreaId();
            //因为下标是从 0 开始的
            position = Integer.valueOf(areaId.substring(3))-1;
            code = lMeter.get(0).getCode();
            alert.setTitle("本表号："+meterId
                    +"  位于 "+lMeter.get(0).getContactAddr()+str+"\n是否进行抄表操作！！！");
            flagSelect = true;
        }else{
//
            alert.setTitle("本表号："+meterId+"    不在当前数据库中"+str);
            flagSelect = false;
        }

        alert.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // 退出
                dialog.dismiss();
                Intent i = new Intent(NFCDemoActivity.this, WaterEntryActivity.class);
                i.putExtra("item", position);
                i.putExtra("code", code);
                i.putExtra("statusRead", "");
                if(flagSelect) {
                    startActivity(i);
                }else {

                }
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = alert.show();
        if (dialog.isShowing()){
            flagShow = true;
        }else {
            flagShow = false;
        }
    }

    private void processNDEFMsg(NdefMessage[] msg) {

        if (msg == null || msg.length == 0){

            return;
        }else {

            for (int i=0; i< msg.length; i++){
                int length = msg[i].getRecords().length;
                NdefRecord[] records = msg[i].getRecords();
                for (int j=0; j< length; j++){
                    for(NdefRecord record : records){
                        parseRTDUriRecord(record);
                    }
                }
            }
        }
    }

    private void parseRTDUriRecord(NdefRecord record) {

        Uri uri = MyNFCRecordParse.parseWellKnownUriRecord(record);
        Promt.setText("Uri:\n"+uri);
    }


    @Override
    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){

            String type = intent.getType();
            if ("text/plain".equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                LogUtils.d("Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction()) || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {

            //取出封装在intent中的TAG
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            Log.i("dysen", intent.getStringExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)+"卡ID=" + MyUtils.byte2Hex(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
            boolean auth = false;
            ArrayList<String> list = new ArrayList<String>();
            for (String tech : tagFromIntent.getTechList()) {
                list.add(tech);
                System.out.println(" for tech:" + tech);
            }

//            else if (list.contains("android.nfc.tech.NfcA")) {
//                nfcA = NfcA.get(tagFromIntent);
//                try {
//                    nfcA.connect();
//                    if (nfcA.isConnected()) {
//                        System.out.println("已连接 NfcA");
////                        ToastDemo.myHint(NFCDemoActivity.this, "已连接", 4);
//                        meterId = readTypeA(nfcA);
//                    }
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }else if (list.contains("android.nfc.tech.NfcB")) {
//                nfcB = NfcB.get(tagFromIntent);
//                try {
//                    nfcB.connect();
//                    if (nfcB.isConnected()) {
//                        System.out.println("已连接 NfcB");
////                        ToastDemo.myHint(NFCDemoActivity.this, "身份证已连接", 1);
//                        new CommandAsyncTask().execute();
//
//                    }
//                    // nfcbTag.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }else if (list.contains("android.nfc.tech.IsoDep")){
//                isoDep = IsoDep.get(tagFromIntent);
//                try
//                {
//                    isoDep.connect();
//                    if (isoDep.isConnected()) {
//                        System.out.println("已连接 IsoDep");
//                        String s = readIsoDep(isoDep);
//                        Promt.setText("isoDep:"+s);
////                        ToastDemo.myHint(NFCDemoActivity.this, "isoDep:"+s, 4);
//                    }
//                } catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
            if (list.contains("android.nfc.tech.MifareClassic")) {

                mfc = MifareClassic.get(tagFromIntent);
                if (mfc != null) {
                    try {
                        mfc.connect();
                        if (mfc.isConnected()) {
                            System.out.println("已连接 MifareClassic");
                        } else {
                            ToastDemo.myHint(NFCDemoActivity.this, "请贴卡后再操作", 4);
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                meterId = readTag(mfc);
//                ToastDemo.myHint(this, "当前表号："+meterId, 4);
                txtReadContext.setText("当前表号："+meterId);
                nfcImg.setVisibility(View.GONE);
                if (meterId > 0){
                    myDialog("");
                }
            } else {
//                ToastDemo.myHint(this, "不支持卡类型", 4);
                txtReadContext.setText("暂不支持读取此类卡片");
                nfcImg.setVisibility(View.VISIBLE);
                nfcImg.setImageResource(R.drawable.nfc_no);
            }

            Write.setEnabled(false);
        }
        resolveIntent(intent);
        it = intent;
    }

    /**
     * 读取 NfcA 的数据
     * @param nfcA
     * @return
     */
    private long readTypeA(NfcA nfcA) {

        long l = -1;
        String atqa="", str = "", cardNum="";
        for(byte tmpByte:nfcA.getAtqa())
        {
            atqa+=tmpByte;
        }
        str+="tag Atqa:"+MyUtils.BytesToHexString(nfcA.getAtqa())+"\n";//获取卡的atqa
        str+="tag SAK:"+nfcA.getSak()+"\n";//获取卡的sak
        str+="max len:"+nfcA.getMaxTransceiveLength()+"\n";//获取卡片能接收的最大指令长度
        byte[] cmd=null;
        //cmd=new byte[]{0x41,0x54,0x4D,0x0A,0x52,0x01,0x00,0x01,(byte) 0xff,(byte)0xff,(byte)0xFF,(byte)0xFF,(byte)0xFF,(byte)0xFF,0x0D};
//        cmd=new byte[]{0x60,0x03,0x2f,0x5f,0x23, (byte) 0xfe, (byte) 0xc2,0x2f};//卡请求
        cmd = new byte[]{(byte) 0x30, (byte) 0x05};//我读取的NFC卡片使用的是NTAG216的芯片，这里的指令参数是根据其datasheet的说明写的。

        //TypeA 类型的第一个数据是0x60，我要想读的扇区是第0扇区的第3块(1-4)，也就是第3块的数据，KeyA密码是六个字节的0x2f,0x5f,0x23,0xfe,0xc2,0x2f
        try {
            LogUtils.i(str+"\nread format:"+MyUtils.BytesToHexString(MyUtils.StrtoByte("6002"+Key.getText().toString())).toUpperCase());
            cmd = new byte[]{(byte) 0x60, (byte) 0x02, (byte) 0x2F,(byte) 0x5F,(byte) 0x23,(byte) 0xFE,(byte) 0xC2,(byte) 0x2F};
            byte[] b = nfcA.transceive(cmd);
//            byte[] b = nfcA.transceive(cmd);
            LogUtils.i("nfcA read data="+MyUtils.BytesToHexString(b));
            l = getCardNum(b);
            cardNum="card Number:"+l;
            ToastDemo.myHint(NFCDemoActivity.this, "Card Number:"+l, 4);
            System.out.println("Card Number:"+l);
            return l;
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                nfcA.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//发送命令到卡片(找不到这个标签扇区读取指令,有知道的同学告诉一声我们的卡是ISO/IEC 14443 typeA标准的)，主要就是这个方法得不到返回的结果。因为指令不正确
        return -1;
    }

    private String readIsoDep(IsoDep isoDep){

        byte cmdSelect_PICCapp=(byte) 0x5A ; //select my app
        byte[] PICCAPPID = new byte[]{(byte)0x00 ,(byte)0x00 , (byte)0x00};
        byte[] tagResponse_cmdSelect_PICC_app = new byte[]{(byte)0x12 ,(byte)0x12};

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        stream.write((byte) 0x90);
        stream.write(cmdSelect_PICCapp);
        stream.write((byte) 0x00);
        stream.write((byte) 0x00);
        if (PICCAPPID != null) {
            stream.write((byte) PICCAPPID.length);
            try {
                stream.write(PICCAPPID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stream.write((byte) 0x00);

        byte[] b = stream.toByteArray();

        byte[] selectCommand = {
                (byte)0x00, // CLA
                (byte)0xA4, // INS
                (byte)0x04, // P1
                (byte)0x00, // P2
                (byte)0x0A, // LC
                (byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x05,(byte)0x06,(byte)0x07,(byte)0x08,(byte)0x09,(byte)0xFF, // AID
                (byte)0x7F  // LE
        };
        try {
            tagResponse_cmdSelect_PICC_app =  isoDep.transceive(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            isoDep.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return MyUtils.BytesToHexString(tagResponse_cmdSelect_PICC_app);
    }



    /**
     * 读取 NfcB 的数据
     */
    class CommandAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            byte[] search = new byte[] { 0x05, 0x00, 0x00 };
            search = new byte[] { 0x00, (byte) 0xA4, 0x00, 0x00, 0x02, 0x60,
                    0x02 };
            search = new byte[] { 0x1D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
                    0x01, 0x08 };
            byte[] result = new byte[] {};
            StringBuffer sb = new StringBuffer();
            try {
                byte[] cmd = new byte[] { 0x05, 0x00, 0x00 };
                ;
                result = nfcB.transceive(cmd);
                sb.append("寻卡指令:" + ByteArrayToHexString(cmd) + "\n");
                sb.append("收:" + ByteArrayToHexString(result) + "\n");
                cmd = new byte[] { 0x1D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08,
                        0x01, 0x08 };
                result = nfcB.transceive(cmd);
                sb.append("选卡指令:" + ByteArrayToHexString(cmd) + "\n");
                sb.append("收:" + ByteArrayToHexString(result) + "\n");
                sb.append("读固定信息指令\n");

                cmd = new byte[] { 0x00, (byte) 0xA4, 0x00, 0x00, 0x02, 0x60,
                        0x02 };
                result = nfcB.transceive(cmd);
                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
                sb.append("收:" + ByteArrayToHexString(result) + "\n");
                cmd = new byte[] { (byte) 0x80, (byte) 0xB0, 0x00, 0x00, 0x20 };
                result = nfcB.transceive(cmd);
                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
                sb.append("收:" + ByteArrayToHexString(result) + "\n");
                cmd = new byte[] { 0x00, (byte) 0x88, 0x00, 0x52, 0x0A,
                        (byte) 0xF0, 0x00, 0x0E, 0x0C, (byte) 0x89, 0x53,
                        (byte) 0xC3, 0x09, (byte) 0xD7, 0x3D };
                result = nfcB.transceive(cmd);
                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
                sb.append("收:" + ByteArrayToHexString(result) + "\n");
                cmd = new byte[] { 0x00, (byte) 0x88, 0x00, 0x52, 0x0A,
                        (byte) 0xF0, 0x00, };
                result = nfcB.transceive(cmd);
                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
                sb.append("收:" + ByteArrayToHexString(result) + "\n");
                cmd = new byte[] { 0x00, (byte) 0x84, 0x00, 0x00, 0x08 };
                result = nfcB.transceive(cmd);
                sb.append("发:" + ByteArrayToHexString(cmd) + "\n");
                sb.append("收:" + ByteArrayToHexString(result) + "\n");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Promt.setText(result);
            try {
                nfcB.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilter, mTechList);
    }

    String readResult;
    private boolean readFromTag2(Intent intent){
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage)rawArray[0];
        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
        try {
            if(mNdefRecord != null){
                readResult = new String(mNdefRecord.getPayload(),"UTF-8");
                Log.i("dysen", "NdefMsg:"+readResult);
                return true;
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        };
        return false;
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    /**
     * 读取 NDEF 的数据
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private long readFromTag(Intent intent) {
        it = intent;
        long meterId = -1;

        Log.i("dysen", "卡ID=" + MyUtils.byte2Hex(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
        //取出封装在intent中的TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        boolean auth = false;
        ArrayList<String> list = new ArrayList<String>();
        for (String tech : tagFromIntent.getTechList()) {
            list.add(tech);
            System.out.println(" for tech:" + tech);
        }

        if (list.contains("android.nfc.tech.MifareClassic")) {
            mfc = MifareClassic.get(tagFromIntent);

            if (mfc != null) {
                try {
                    mfc.connect();
                    if (mfc.isConnected()) {
                        System.out.println("已连接");
                    } else {
                        ToastDemo.myHint(NFCDemoActivity.this, "请贴卡后再操作", 4);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            meterId = readTag(mfc);
            Write.setEnabled(false);
//        Read.setEnabled(false);
        }
//        else if (list.contains("android.nfc.tech.NfcA")) {
//            nfcA = NfcA.get(tagFromIntent);
//            try {
//                nfcA.connect();
//                if (nfcA.isConnected()) {
//                    System.out.println("已连接");
//                    ToastDemo.myHint(NFCDemoActivity.this, "身份证已连接", 4);
////                     new CommandAsyncTask().execute();
//                    readTypeA(nfcA);
//                }
//                // nfcbTag.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
        else {
            ToastDemo.myHint(this, "不支持卡类型", 4);
        }
        return  meterId;
    }

    private String ByteArrayToHexString(byte[] inarray) { // converts byte
        // arrays to string
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    /**
     * 读取 MifareClassic 的数据
     * @param mfc
     * @return
     */
    public long readTag(MifareClassic mfc) {
        boolean auth = false;
        // 读取TAG
        long l = 0;
        try {
            String metaInfo = "";
            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            Log.i("dysen", "TAG的类型=" + type + "扇区数=" + sectorCount);
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            metaInfo += mfc + "\n卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n";
            for (int j = 0; j < sectorCount; j++) {
                //Authenticate a sector with key A.
                if (flag)
                    auth = mfc.authenticateSectorWithKeyA(j, getKey());
                else
//                    auth = mfc.authenticateSectorWithKeyB(0,getKey());
                    auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);
                int bCount;
                int bIndex;
                if (auth) {
                    metaInfo += "Sector " + j + ":验证成功\n";
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        metaInfo += "Block " + bIndex + " : "
                                + MyUtils.BytesToHexString(data) + "\n";
                        System.out.println("第" + j + "区第" + bIndex + "块" + "\n数据：" + MyUtils.BytesToHexString(mfc.readBlock(bIndex)));
                        if (j == 0 && bIndex == 2) {//第一个区第三块
//                            System.out.println("获得当前块的内容："+MyUtils.BytesToHexString(mfc.readBlock(bIndex)));
//                            Promt.setText("获得当前块的内容："+MyUtils.BytesToHexString(mfc.readBlock(bIndex)));
                            l = getCardNum(mfc.readBlock(bIndex));
                        }
                        bIndex++;
                    }
                } else {
                    metaInfo += "Sector " + j + ":验证失败\n";
                }
            }
            Promt.setText(metaInfo);
            return l;
        } catch (Exception e) {
            ToastDemo.myHint(this, "catch:"+e.getMessage(), 5);
            e.printStackTrace();
        } finally {
            if (mfc != null) {
                try {
                    mfc.close();
                } catch (IOException e) {
                    ToastDemo.myHint(this, "finally:"+e.getMessage(), 5);
                }
            }
        }
        return -1;
    }

    private long getCardNum(byte[] bytes) throws IOException {

        long l=-1;
        String readData = MyUtils.BytesToHexString(bytes);
        Log.i("dysen", readData);
        System.out.println("获得当前块的内容:" + readData);
        if (readData.substring(0, 6).equals("000000")) {//格式1 (反码)
            Promt.setText("当前编号：" + MeterIdUtil.getMeterId(readData));
            l = MeterIdUtil.getMeterId(readData);
        } else {//格式2
            Promt.setText("当前编号：" + MeterIdUtil.getMeterId2(readData));
            l = MeterIdUtil.getMeterId2(readData);
        }
        System.out.println("*************************meterId:" + l);
        return l;
    }

    /**
     * 写数据
     * @param intent
     */
    private void write2Tag(Intent intent){
        int sector;
        int block;
        String temp3;
        String temp2;
        String temp = strBlock;//0--3块
        temp2 = Data.getText().toString();
        temp3 = strSector;//0--15区
        //程序里 0--3， 0--15 ；
        block = Integer.parseInt(temp);
        sector = Integer.parseInt(temp3);
        System.out.println(sector+"**********************************"+block+"--"+Integer.parseInt(ssBlock));
        block = Integer.parseInt(ssBlock);
        String mId = wMeterId.getText().toString();
        byte[] bos_new = MyUtils.HexStringBytes(temp2);

        if(!mId.isEmpty()) {

            bos_new = MyUtils.HexStringBytes(MeterIdUtil.setMeterIdFormat(Long.valueOf(mId)));
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            boolean auth = false;
            //读取TAG
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            try {
                mfc.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                if(flag)
                    auth = mfc.authenticateSectorWithKeyA(0,getKey());
                else
//                    auth = mfc.authenticateSectorWithKeyB(0,getKey());
                    auth = mfc.authenticateSectorWithKeyA(0,MifareClassic.KEY_DEFAULT);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (auth) {
                // the last block of the sector is used for KeyA and KeyB cannot be overwritted
                try {
                    mfc.writeBlock(2,bos_new);
                    //mfc.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Title.setText("正在扫描Mifare卡");
//            Sector.setText("");
//            Block.setText("");
//            Data.setText("");
                ToastDemo.myHint(NFCDemoActivity.this, "Write Success!", 1);
                Write.setEnabled(false);
                Read.setEnabled(false);
            }
        }else {

        }
        if (!temp2.isEmpty()){

            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            boolean auth = false;
            //读取TAG
            MifareClassic mfc = MifareClassic.get(tagFromIntent);
            try {
                mfc.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                if(flag)
                    auth = mfc.authenticateSectorWithKeyA(sector,getKey());
                else
                    auth = mfc.authenticateSectorWithKeyB(sector,getKey());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (auth) {
                // the last block of the sector is used for KeyA and KeyB cannot be overwritted
                try {
                    mfc.writeBlock(block,bos_new);
                    //mfc.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Title.setText("正在扫描Mifare卡");
//            Sector.setText("");
//            Block.setText("");
//            Data.setText("");
                Toast.makeText(NFCDemoActivity.this, "Write Success!", Toast.LENGTH_SHORT).show();
                Write.setEnabled(false);
                Read.setEnabled(false);
            }
        }else{

        }
    }

    private byte[] getKey(){
        String str = Key.getText().toString();
        byte[] bos_new = MyUtils.StrtoByte(str);
        return bos_new;
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        LogUtils.e("Unsupported Encoding\t"+e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                Promt.setText("Read content: " + result);
            }
        }
    }
}

