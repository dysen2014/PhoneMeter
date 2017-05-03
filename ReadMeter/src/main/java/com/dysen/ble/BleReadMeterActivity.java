package com.dysen.ble;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.ble.parse.CmdString;
import com.dysen.ble.parse.MySingleParse;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyStringConversion;
import com.dysen.myUtil.MyUtils;
import com.dysen.myUtil.ToastDemo;
import com.dysen.myUtil.dialog.SpotsDialog;
import com.dysen.mylibrary.utils.SharedPreferencesDemo;
import com.dysen.mylibrary.utils.StatusBarUtil;
import com.dysen.mylibrary.utils.kjframe.KJDB;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBleInfo;
import com.dysen.table.tMeterId;
import com.dysen.table.tReadMeter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author dy
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class BleReadMeterActivity extends MyActivityTools implements OnClickListener {
	private final static String TAG = BleReadMeterActivity.class
			.getSimpleName();

	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

	private TextView mConnectionState, mDataReceive, mDataSend, tvTx, tvRx;;
	public static String mDeviceName="", mDeviceAddress="";
	private ExpandableListView mGattServicesList;
	private BluetoothLeService mBluetoothLeService;
	private boolean mConnected = false, flagHex = true, isHex, isConn;

	private final String LIST_NAME = "NAME", LIST_UUID = "UUID";
	// byte[] WriteBytes = null;
	byte[] WriteBytes = new byte[20];

	EditText  etMeterIdStart, etMeterIdEnd, etNetId;
	Button  btnControlBLE;

	String[] cmdStr, nameStr;
	private static final int UART_PROFILE_CONNECTED = 20;
	private static final int UART_PROFILE_DISCONNECTED = 21;
	private int mState = UART_PROFILE_DISCONNECTED;
	int cmdIndex, readMeterNum;

	List<tReadMeter> lM, lMeterId;
	KJDB dbMeter;
	List<tBleInfo> listBle;
	private SpotsDialog alert;
	private SharedPreferences preferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_ble_read_meter);

		//透明状态栏
		StatusBarUtil.setTransparent(this);
		bindView(R.id.ll_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});
		((TextView) bindView(R.id.tv_hint)).setText("BLE抄表系统");

		objInit();
		viewInit();

		// 将该项目中包含的原始intent检索出来 赋值给一个Intent类型的变量intent
		final Intent intent = getIntent();
		mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
		mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

		listBle = dbBle.findAll(tBleInfo.class, "id DESC");

		if (listBle.size() > 0) {
			mDeviceName = listBle.get(0).getBleName();
			mDeviceAddress = listBle.get(0).getBleMac().trim().toUpperCase();
		}else {
			btnControlBLE.setText("扫描");
			startActivityForResult(new Intent(this, DeviceScanActivity.class), 1);
		}

		String connStat = "成功连接 " + mDeviceName + " 设备";
		Log.i("dysen", "设备名："+mDeviceName+"\n设备地址："+mDeviceAddress);

		etMeterIdStart.addTextChangedListener(textWatcher);
		etMeterIdEnd.addTextChangedListener(textWatcher);

		btnControlBLE.setOnClickListener(this);
		bindView(R.id.btn_search_ble).setOnClickListener(this);

//		getActionBar().setTitle(mDeviceName);
//		getActionBar().setDisplayHomeAsUpEnabled(true);
			Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
			bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

		mId = init();
	}

	private void viewInit() {
		btnControlBLE = bindView(R.id.btn_);
		((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
		mGattServicesList = bindView(R.id.gatt_services_list);
		mConnectionState = bindView(R.id.connection_state);
		mDataReceive = bindView(R.id.receiveData);
		mDataSend = bindView(R.id.sendData);
		tvTx = bindView(R.id.tv_tx);
		tvRx = bindView(R.id.tv_rx);
		etMeterIdStart = bindView(R.id.et_meter_id_start);
		etMeterIdEnd = bindView(R.id.et_meter_id_end);
		etNetId = bindView(R.id.et_net_id);

	}

	private void objInit() {

		preferences = getSharedPreferences("mId", this.MODE_PRIVATE);
		dbMeter = KJDB.create(this, "tReadMeter");
	}

	/**
	 * 各个功能的拼接
	 */
	private void funcSplit(String netId, String mId){

		netId = netId.equals("") ? MyStringConversion.myInverseConver(MyUtils.dec2Hex("1"), 4):
				MyStringConversion.myInverseConver(MyUtils.dec2Hex(netId), 4);
		if ("".equals(mId)) {
			mId = MyStringConversion.myInverseConver(MyUtils.dec2Hex("0"), 8);
		}else {
			mId = MyStringConversion.myInverseConver(MyUtils.dec2Hex(mId), 8);
		}
		CmdString.sWakeUp = CmdString.cmdWakeUp(netId);
		CmdString.sReadMeter = CmdString.cmdReadMeter(netId, mId);
		CmdString.sBaseRead = CmdString.cmdBaseRead(netId, mId);

//				CmdString. = ;

		cmdStr = new String[]{CmdString.sWakeUp, CmdString.sReadMeter, CmdString.sBaseRead};
		nameStr = new String[]{"唤醒", "抄表(读单元)", "基站抄表"};

	}

	public void readWakeUp(View v){
		initBubble(v, "唤醒");
		mySend(0);//唤醒

	}
	public void readMeter(View v){

		initBubble(v, "抄表(读单元)");
		mySend(1);//抄表(读单元)

	}
	public void baseRead(View v){
		initBubble(v, "基站抄表");
//		dbMeter.deleteByWhere(tReadMeter.class, "id>0");
		mySend(2);//基站抄表

	}
//	public void a(View v){
//funcSplit();
// 	cmdIndex = 2;
//mySend(CmdString.sBaseRead);

//	}




	/**
	 *	dysen
	 *	2015-4-18 上午10:37:19
	 *	info:	提示内容
	 */
	public void myHint(String str){
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.btn_:
				controlBLE(mDeviceAddress);
				break;
			case R.id.btn_search_ble:
				bleDisConn();
				startActivityForResult(new Intent(this, DeviceScanActivity.class), 1);
				break;
		}
	}

	/**
	 * info 连接/断开 BLE
	 */
	private void controlBLE(String mDeviceAddress) {
		if (mConnected){
			btnControlBLE.setText("断开");
			bleDisConn();
		}else {
			btnControlBLE.setText("连接");
			if (mDeviceAddress == null || mDeviceAddress.equals("")){

				startActivityForResult(new Intent(this, DeviceScanActivity.class), 1);
			}else {//蓝牙地址不为空！(有地址)

				Log.i("dysen", "bleName:"+mDeviceName+"\tbleMac:"+mDeviceAddress);
				isConn = bleConn(mDeviceAddress);
				if (!isConn){
					mDeviceAddress = "";
					mDeviceName = "";
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mDeviceName = data.getStringExtra(EXTRAS_DEVICE_NAME);
		mDeviceAddress = data.getStringExtra(EXTRAS_DEVICE_ADDRESS);

		bleInfo.setBleName(mDeviceName);
		bleInfo.setBleMac(mDeviceAddress);
		List<tBleInfo> l = dbBle.findAll(tBleInfo.class);
		if (l.size() >0){
			for (tBleInfo t :l){
				if (bleInfo.getBleMac().equals(t.getBleMac())) {
					dbBle.update(bleInfo, "bleMac=" + "\'" + t.getBleMac() + "\'");
				}
			}
		}else {
			dbBle.save(bleInfo);
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	// Code to manage Service lifecycle.
	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
									   IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			bleConn(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	private IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.DEVICE_DOES_NOT_SUPPORT_UART);
		return intentFilter;
	}

	int charaProp;
	BluetoothGattCharacteristic characteristic;
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

		String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				mConnected = true;

				updateConnectionState(R.string.connected, mConnected);
				invalidateOptionsMenu();

				mState = UART_PROFILE_CONNECTED;

			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				mConnected = false;
				updateConnectionState(R.string.disconnected, mConnected);
				invalidateOptionsMenu();
//				clearUI();
				mBluetoothLeService.enableTXNotification();
				mState = UART_PROFILE_DISCONNECTED;
				mBluetoothLeService.close();
				mBluetoothLeService.connect(mDeviceAddress);// 重新连接蓝牙

			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				mBluetoothLeService.enableTXNotification();
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

				displayData(intent
						.getStringExtra(BluetoothLeService.EXTRA_DATA));

			}else if (action.equals(mBluetoothLeService.DEVICE_DOES_NOT_SUPPORT_UART)){
				ToastDemo.myHint(BleReadMeterActivity.this, "Device doesn't support UART. Disconnecting", 3);
				mBluetoothLeService.disconnect();
			}
		}
	};

	public static String bin2hex(String bin) {
		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		byte[] bs = bin.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(digital[bit]);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
		}
		return sb.toString();
	}

	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		b = null;
		return b2;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gatt_services, menu);
		if (mConnected) {
			menu.findItem(R.id.menu_connect).setVisible(false);
			menu.findItem(R.id.menu_disconnect).setVisible(true);
		} else {
			menu.findItem(R.id.menu_connect).setVisible(true);
			menu.findItem(R.id.menu_disconnect).setVisible(false);
		}
//		menu.add(1, 1, 1, "关\t于");

		return super.onCreateOptionsMenu(menu);
		// return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.menu_connect:
				bleConn(mDeviceAddress);
				return true;
			case R.id.menu_disconnect:
				bleDisConn();
				return true;
			case android.R.id.home:
				onBackPressed();
				return true;
//			case 1: // 关于
//				new AlertDialog.Builder(this)
//						.setTitle("手机抄表系统")
//						.setInverseBackgroundForced(true)
//						.setMessage(
//								"作者： sendy\n公司： 信控科技有限公司  \n电话：(+86)027-87373101\n"
//										+ "\t\t\t\t\t\t本公司坐落于中国湖北省武汉市,供应远程自动化控制 GPRS无线传输模块等 !!! ")
//						.show();
//				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 连接蓝牙BLE
	 * @param bleMac
     */
	public boolean bleConn(String bleMac){
		Log.i("dysen", "blerMAc:"+bleMac);
		if (bleMac == null || bleMac.equals("")) {
			startActivityForResult(new Intent(this, DeviceScanActivity.class), 1);

		}else {
			boolean isConn = mBluetoothLeService.connect(bleMac);
			return isConn;
		}

		return false;
	}

	/**
	 * 断开蓝牙BLE
	 */
	public void bleDisConn(){
		mBluetoothLeService.disconnect();
	}

	TextView tvWkUp;
	private boolean connFlag;
	View alertView;

	/**
	 * 清除数据区域
	 */
	private void clearUI() {
		mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
		mDataReceive.setText(R.string.receive_data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

		if (mBluetoothLeService != null) {
			if (mDeviceAddress == null | mDeviceAddress.equals("")) {
				return;
			} else {
				final boolean result = mBluetoothLeService.connect(mDeviceAddress);
				LogUtils.i("Connect request result=" + result);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mGattUpdateReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d(TAG, "onDestroy()");

		try {
			LocalBroadcastManager.getInstance(this).unregisterReceiver(mGattUpdateReceiver);
		} catch (Exception ignore) {
			Log.e(TAG, ignore.toString());
		}
		unbindService(mServiceConnection);
		mBluetoothLeService.stopSelf();
		mBluetoothLeService = null;

	}

	String mId = "", meterIdStart="";
	long txLen;
	/**
	 *	dysen
	 *	2015-4-18 下午12:26:10
	 *	info:	发送数据
	 */
	public void mySend(int id){

		saveStartEndId();
		String sendCmd = "";
		cmdIndex = id;
		str = "";
		if (!mConnected) {
			ToastDemo.myHint(this, "请先连接蓝牙设备,再做操作", 2);
		} else {
			if (cmdIndex == 2) {
				init();//更新表号的结束号
				meterIdStart = etMeterIdStart.getText().toString().trim() ;
				mDataReceive.setText("正在基站抄表！！！");
//				alert = new SpotsDialog(this, "reading meter");
//				alert.show();
					//实例化异步任务
					myAsyncTask = new MyAsyncTask();
					//开始执行异步任务
					myAsyncTask.execute();

			}else {
				mId = etMeterIdStart.getText().toString().trim();
				funcSplit("1", mId);
				sendCmd = cmdStr[cmdIndex];

				if ( "".equals(sendCmd)) {
					ToastDemo.myHint(BleReadMeterActivity.this, "协议不能为空", 2);
					return;
				}else{

					mDataSend.setText(MyStringConversion.myStr(sendCmd));
					mySendMsg(sendCmd);
				}
			}
		}
	}

	MyAsyncTask myAsyncTask;
	class MyAsyncTask extends AsyncTask<Integer, Integer, String>{

		//onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			alert = new SpotsDialog(BleReadMeterActivity.this, "read meter");
			alert.show();
			System.out.println("开始执行异步线程");
		}

		@Override
		protected String doInBackground(Integer... params) {
			SystemClock.sleep(2000);
			alert.cancel();
			for (int i = 0; i < readMeterNum; i++){

				mId = String.valueOf(Integer.parseInt(meterIdStart) + i);
				funcSplit("1", mId);
				Log.i("dysen", "i="+i+"\tmId="+mId);
				mySendBaseMsg(CmdString.sBaseRead);

				SystemClock.sleep(500);
			}
			return ss;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			for (int i = 0; i < readMeterNum; i++) {
				mId = String.valueOf(Integer.parseInt(meterIdStart) + i);
				funcSplit("1", mId);
				mDataSend.setText(nameStr[cmdIndex]);
				txLen += nameStr[cmdIndex].length()/2;
//				tvTx.setText("Tx "+txLen+"("+nameStr[cmdIndex].length()/2+")");
				tvTx.setText("Tx "+16);
				tvTx.setTextColor(Color.WHITE);
				SystemClock.sleep(100);
			}
//			Log.i("dysen", "******************************抄表返回："+ss);
//			alert.cancel();
//			mDataReceive.setText("抄表返回："+ss);
		}
	}

	private String init() {

//		listMeter = dbMeterId.findAll(tMeterId.class);

//		if (listMeter.size() > 0){
//
//			etMeterIdStart.setText(listMeter.get(0).getmIdStart());
//			etMeterIdEnd.setText(listMeter.get(0).getmIdEnd());
//		}else {
//			etMeterIdStart.setText(1+"");
//			etMeterIdEnd.setText(5+"");
//		}
		getStartEndId();
		String meterIdStart = etMeterIdStart.getText().toString().trim();
		String meterIdEnd = etMeterIdEnd.getText().toString().trim();
		String netId = etNetId.getText().toString().trim();

		if (meterIdStart.equals("") || meterIdEnd.equals("") || netId.equals("")){
			etNetId.setText("1");
			etMeterIdStart.setText(1+"");
			etMeterIdEnd.setText(5+"");
		}else {
			readMeterNum = Integer.parseInt(meterIdEnd) - Integer.parseInt(meterIdStart) + 1;
			readMeterNum = readMeterNum <= 0 ? 1 : readMeterNum;
		}

		mId = meterIdStart;

		return mId;
	}

	int ii;
	private void mySendBaseMsg(String sendData) {

		if (!"".equals(sendData)) {
			if (!flagHex) {
				// write string
				WriteBytes = sendData.getBytes();
//					mDataSend.setText(sendData);
				isHex = false;
				// flagHex = false;
			} else if (flagHex) {
				WriteBytes = hex2byte(sendData.getBytes());
				isHex = true;
				Log.i("data", ++ii+"send Data="+sendData);
			}
			str = "";
			mBluetoothLeService.writeRXCharacteristic(WriteBytes);
		}else {

		}

		System.out.println(cmdIndex+"发送的数据："+MyStringConversion.myStr(sendData));
	}

	private void saveStartEndId() {
		String mNetId = etNetId.getText().toString();
		String mStart = etMeterIdStart.getText().toString();
		String mEnd = etMeterIdEnd.getText().toString();

		SharedPreferencesDemo.mySetValue(preferences, "mNetId", mNetId);
		SharedPreferencesDemo.mySetValue(preferences, "mStart", mStart);
		SharedPreferencesDemo.mySetValue(preferences, "mEnd", mEnd);
	}

	private void getStartEndId(){

		String mNetId = SharedPreferencesDemo.myGetValue(preferences, "mNetId");
		String mStart = SharedPreferencesDemo.myGetValue(preferences, "mStart");
		String mEnd = SharedPreferencesDemo.myGetValue(preferences, "mEnd");

		etNetId.setText(mNetId);
		etMeterIdStart.setText(mStart);
		etMeterIdEnd.setText(mEnd);
	}

	/**
	 *	发送数据写到 BLE 块
	 * sendy 2015-1-15 下午3:07:39 函数说明
	 */
	private void mySendMsg(String sendData) {

		txLen += sendData.length()/2;
//		tvTx.setText("Tx "+txLen+"("+sendData.length()/2+")");
		tvTx.setText("Tx "+sendData.length()/2);
		tvTx.setTextColor(Color.WHITE);
			if (!"".equals(sendData)) {
				if (!flagHex) {
					// changeCmd(); // 选择命令发送
					// write string
					WriteBytes = sendData.getBytes();
//					mDataSend.setText(sendData);
					isHex = false;
					// flagHex = false;
				} else if (flagHex) {
					WriteBytes = hex2byte(sendData.getBytes());
//					mDataReceive.setText(nameStr[cmdIndex]);
					// mDataSend.setText(cmdName);
					System.out.println("此时功能号为：" + nameStr[cmdIndex]);
					isHex = true;
				}
//				mDataReceive.setTextSize(18);
//				mDataSend.setTextSize(18);
			}

//			tvTx.setText("Tx " + WriteBytes.length);
//			tvTx.setTextColor(Color.WHITE);
//			tvTx.setTextSize(18);
//			tvRx.setText("Rx 00");
//			tvRx.setTextColor(Color.WHITE);
//			tvTx.setTextSize(18);

		mBluetoothLeService.writeRXCharacteristic(WriteBytes);

			System.out.println(cmdIndex+"发送的数据："+MyStringConversion.myStr(sendData));
//			mDataSend.setText(nameStr[cmdIndex]+"：\n"+MyStringConversion.myStr(sendData));
		if (cmdIndex == 0){
			mDataReceive.setText("正在唤醒...");
		}
		if (cmdIndex == 2){
//			mDataReceive.setText("正在抄"+ mId +"号表...");
		}
	}

	/**
	 * 更新连接状态
	 * @param resourceId
	 * @param flag
     */
	private void updateConnectionState(final int resourceId, final boolean flag) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (flag) {
					mConnectionState.setText(mDeviceName);
					mConnectionState.setTextColor(Color.GREEN);
					mConnectionState.setTextSize(20);
					btnControlBLE.setText("断开");
					// mConnectionState.setText(getText(resourceId) +
					// mDeviceName
					// + "设备");
				} else {
					mConnectionState.setText(mDeviceName);
					mConnectionState.setTextColor(Color.RED);
					btnControlBLE.setText("连接");
					// mConnectionState.setText(getText(resourceId) +
					// mDeviceName
					// + "设备");
				}
			}
		});
	}

	String str = "", ss = "";
	private String strParse = "";
	String sMore = "";
	String reData;
	public static int dataLen;
	long rxLen;
	int count;
	/**
	 * 接收的数据
	 * @param data
	 * @return
     */
	private void displayData(String data) {

		if (data != null) {

			str += data;
//			Log.i("data", ++count+"==="+dataLen);
			rxLen += dataLen;
			String sParse = "";
			List<String> pkgList = MySingleParse.searchReadData(MyUtils.removeBlank(str));

			if (pkgList != null && pkgList.size() >0){
			Log.i("dysen", "pkgList.size="+pkgList.size());
				for (int i=0; i<pkgList.size(); i++){

//					Log.i("dysen", i+"str:"+MyUtils.removeBlank(str)+"\ns:"+pkgList.get(i)+"\t数据包的长度："+pkgList.get(i).length());
					if (pkgList.get(i).length() == 82) {//抄表

						reData = pkgList.get(i) + ",";
//						Log.i("dysen", "reData:"+reData + "****************************抄表数据包：" + pkgList.get(i));
						String[] pkg = reData.split(",");

						sParse = MySingleParse.mySingleParseStr("BleReadMeterActivity", MyUtils.removeBlank(pkg[pkg.length-1]), cmdIndex);
						ss = MyUtils.removeBlank(pkg[pkg.length-1]);
//						Log.i("dysen", "#################################ss:"+ss);
						saveMeterData(pkg);
					}else if (pkgList.get(i).length() == 36){

						String funcNum = pkgList.get(i).substring(16, 18);
						if (funcNum.equals("F9")) {//唤醒

						}else if (funcNum.equals("F4")) {//设无线配置

						}
						reData = pkgList.get(i);
						ss = pkgList.get(i);
						sParse = MySingleParse.mySingleParseStr("BleReadMeterActivity", pkgList.get(i), cmdIndex);
					}
				}
			}

		 System.out.println(ss.trim().length()+"ss的长度："+ss+"\tcmdIndex:"+cmdIndex);
		if (isHex) {

			if (!TextUtils.isEmpty(ss)) {
				mDataReceive.setText(ss+"\n"+sParse);
			} else {
				mDataReceive.setText(MyUtils.myStr(ss));
			}
//			tvRx.setText("Rx " + rxLen+"("+ss.length()/2+")");
			tvRx.setText("Rx " + ss.length()/2);
			mDataReceive.setTextSize(18);
//
		} else {
			tvRx.setText("Rx " + ss.trim().length());
			mDataReceive.setText(str);
		}
		tvRx.setTextColor(Color.WHITE);
		tvRx.setTextSize(18);
		mDataReceive.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				new Builder(BleReadMeterActivity.this)
						.setTitle(" 是否清除  !!!")// Whether to remove
						.setPositiveButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
														int arg1) {
										myClaer(); // 清空接收区
									}

								}).setNegativeButton("否", null).show();
				return false;
			}
		});
		}
	}


	/**
	 * 保存抄表数据
	 * @param pkg
     */
	private void saveMeterData(String[] pkg) {

//		dbMeter.deleteByWhere(tReadMeter.class, "id>0");
		for (int i=0; i < pkg.length; i++){
			Log.i("dysen", pkg.length+"抄表数据s："+pkg[i]);
			lM = MySingleParse.singleParse(MyUtils.removeBlank(pkg[i]));

			if (lM != null && lM.size() > 0 && (cmdIndex == 1 || cmdIndex ==2) ) {//有效数据  (抄表数据）
//				Log.i("dysen", lM.size()+"pkg---"+s);
//				dbMeter.deleteByWhere(tReadMeter.class, "meterId=" + "\'" + lM.get(0).getMeterId() + "\'");

				Log.i("dysen", "数据记录条数："+lM.size()
						+"\n抄表数据条数："+lM.size());

				for (tReadMeter m : lM) {

					Log.i("dysen", m.getMeterId()+"___"+m.getSrcString());

					if (m.getSrcString().equals("唤醒成功") || m.getSrcString().equals("配置成功")){
//						ToastDemo.myHint(this, "非抄表协议");
					}else {

							lMeterId = dbMeter.findAllByWhere(tReadMeter.class, "meterId=" + "\'" + m.getMeterId() + "\'");
						if (lMeterId.size()>0) {

							dbMeter.update(m, "meterId=" + "\'" + m.getMeterId() + "\'");
						}else {

							dbMeter.save(m);
						}
					}
				}
			}else {
//				ToastDemo.myHint(this, "无效数据  (抄表数据）");
			}
		}
		showReadMeterData(dbMeter.findAll(tReadMeter.class, "id DESC"));
	}

	public void showReadMeterData(List<tReadMeter> lM){

		ListView lv = bindView(R.id.lv_ble_read);
		lM = dbMeter.findAll(tReadMeter.class, "meterDate2 DESC");

		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		LogUtils.i("__————————————————————————————————database size="+lM.size());
		for (tReadMeter t : lM) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("meterId", t.getMeterId());
			map.put("readNum", t.getLong_meterEnd());
			map.put("readDate", t.getMeterDate());
			Log.i("dysen", "表号："+t.getMeterId()+"\n读数："+t.getLong_meterEnd()+"\n日期："+t.getMeterDate());

			listItem.add(map);
		}

		//定义适配器
		SimpleAdapter mSimpleAdapter = refresh(listItem);
		mSimpleAdapter.notifyDataSetChanged();
		//为ListView绑定适配器
		lv.setAdapter(mSimpleAdapter);
	}

	/**
	 * 更新listView显示内容
	 * @param listItem 更新后的arraylist
	 * @return具有新内容的适配器
	 * @author mingwell
	 */
	public SimpleAdapter refresh(ArrayList<HashMap<String, Object>> listItem){

		SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, listItem,//需要绑定的数据
				R.layout.activity_ble_read_meter_item,//每一行的布局
				//动态数组中的数据源的键对应到定义布局的View中
				new String[] {"meterId", "readNum", "readDate"},
				new int[] {R.id.txt_ble_read_item1, R.id.txt_ble_read_item2, R.id.txt_ble_read_item3} );
		return mSimpleAdapter;
	}


	/**
	 * 清除函数 sen dy 2015-2-7 上午8:51:56
	 */
	private void myClaer() {
		str = strParse = "";
		mDataReceive.setText(strParse);
	}

	String mNetId, mStart, mEnd;
	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			Log.d("TAG","afterTextChanged--------------->");
			mNetId = etNetId.getText().toString();
			mStart = etMeterIdStart.getText().toString();
			mEnd = etMeterIdEnd.getText().toString();

//			meterId.setmNetId(mNetId);
//			meterId.setmIdStart(mStart);
//			meterId.setmIdEnd(mEnd);
//
//			saveData(meterId);
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

	private void saveData(tMeterId mId) {

		Log.i("dysen", "mmm="+dbMeterId.findAll(tMeterId.class).size());
		if (dbMeterId.findAll(tMeterId.class).size() >0){
			dbMeterId.update(mId, "id=1");
		}else {
			dbMeterId.saveBindId(mId);
		}
		Log.i("dysen", "netId="+mId.getmNetId()+"\t\tstart="+ mId.getmIdStart()+"\t\tend="+mId.getmIdEnd());
	}
}
