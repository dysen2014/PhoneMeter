package com.dysen.ble;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.ble.parse.CmdString;
import com.dysen.ble.parse.MySingleParse;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.MyStringConversion;
import com.dysen.myUtil.MyUtils;
import com.dysen.myUtil.ToastDemo;
import com.dysen.mylibrary.utils.StatusBarUtil;
import com.dysen.mylibrary.utils.kjframe.KJDB;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tReadMeter;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author dy
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class DeviceControlActivity extends MyActivityTools implements OnClickListener {
	private final static String TAG = DeviceControlActivity.class
			.getSimpleName();

	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

	private static final String HTTP_IP = "http://192.168.1.217:80";

	private TextView mConnectionState;
	private TextView mDataReceive, mDataSend;
	private String mDeviceName;
	private String mDeviceAddress;
	private ExpandableListView mGattServicesList;
	private BluetoothLeService mBluetoothLeService;
	private boolean mConnected = false;
	static boolean flagHex = true;

	private final String LIST_NAME = "NAME";
	private final String LIST_UUID = "UUID";
	// byte[] WriteBytes = null;
	byte[] WriteBytes = new byte[20];

	private TextView tvTx, tvRx;
	boolean isHex;
	EditText etSend;
	String cmdName = "";
	LinearLayout llSum;
	TextView  tvSum;
	EditText etSum, etTime, etData, etMeterId, etNetId, edtFrequency, edtCapacity;
	Spinner spRate;

	long  intervalTime;
	Button  btnSend, btnControlBLE;
	String verifyCmd;
	
	String[] cmdStr, nameStr;
	private static final int UART_PROFILE_CONNECTED = 20;
	private static final int UART_PROFILE_DISCONNECTED = 21;
	private int mState = UART_PROFILE_DISCONNECTED, cmdIndex;

	List<tReadMeter> lM;
	KJDB dbMeter;
	int spIndex ;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gatt_services_characteristics);

		//透明状态栏
		StatusBarUtil.setTransparent(this);
		(this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});

		((TextView) this.findViewById(R.id.tv_hint)).setText("BLE抄表系统");
		dbMeter = KJDB.create(this, "tReadMeter");

		// 将该项目中包含的原始intent检索出来 赋值给一个Intent类型的变量intent
		final Intent intent = getIntent();
		mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
		mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
		verifyCmd = intent.getStringExtra("verify_cmd");

		String connStat = "成功连接 " + mDeviceName + " 设备";
		Log.i("dysen", "设备名："+mDeviceName+"\n设备地址："+mDeviceAddress);
		((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
		mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);

		// 隐藏 list 列表
		mGattServicesList.setVisibility(View.GONE);
		mConnectionState = (TextView) findViewById(R.id.connection_state);
		mDataReceive = (TextView) findViewById(R.id.receiveData);
		mDataSend = (TextView) findViewById(R.id.sendData);
		tvTx = (TextView) findViewById(R.id.tv_tx);
		tvRx = (TextView) findViewById(R.id.tv_rx);
		tvSum = (TextView) this.findViewById(R.id.tv_sum);
		etSum = (EditText) this.findViewById(R.id.et_sum);
		etTime = (EditText) this.findViewById(R.id.et_time);
		etMeterId = (EditText) this.findViewById(R.id.et_meter_id);
		etNetId = (EditText) this.findViewById(R.id.et_net_id);
		etData = (EditText) this.findViewById(R.id.et_data);
		etSend = (EditText) this.findViewById(R.id.et_send);
		llSum = (LinearLayout) this.findViewById(R.id.ll_sum);

		btnSend = (Button) this.findViewById(R.id.btn_send);

		btnSend.setOnClickListener(this);
		btnControlBLE = bindView(R.id.btn_);
		btnControlBLE.setText("扫描");
//		btnControlBLE.setBackgroundResource(R.drawable.tint_bottom);
		btnControlBLE.setOnClickListener(this);

		edtFrequency = bindView(R.id.edt_config_frequency);
		edtCapacity = bindView(R.id.edt_config_capacity);
		spRate = bindView(R.id.sp_config_rate);

//		//适配器
//		ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(this, R.array.rate_sum, android.R.layout.simple_spinner_item);
//		//设置样式 为适配器设置下拉列表下拉时的菜单样式
//		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		//加载适配器 将适配器添加到下拉列表上
//		spRate.setAdapter(ad);

		spRate.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				String[] rates = getResources().getStringArray(R.array.rate_sum);
				Log.i("dysen", position+"<<--->>"+rates[position]);
				spIndex = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		etData.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {

				funcSplit();

				return true;
			}
		});
//		getActionBar().setTitle(mDeviceName);
//		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

	}

	/**
	 * 各个功能的拼接
	 */
	private void funcSplit(){
		String meterId = etMeterId.getText().toString().trim();
		String netId = etNetId.getText().toString().trim();
		String frequencyId = edtFrequency.getText().toString().trim();
		String capacityId = edtCapacity.getText().toString().trim();
		String rateId = "";

		netId = netId.equals("") ? MyStringConversion.myInverseConver(MyUtils.dec2Hex("1"), 4): MyStringConversion.myInverseConver(MyUtils.dec2Hex(netId), 4);
		frequencyId = frequencyId.equals("") ? MyStringConversion.myInverseConver(MyUtils.dec2Hex("480"), 4): MyStringConversion.myInverseConver(MyUtils.dec2Hex(frequencyId), 4);
		capacityId = capacityId.equals("") ? MyStringConversion.myInverseStr(MyUtils.dec2Hex("20"), 2): MyStringConversion.myInverseStr(MyUtils.dec2Hex(capacityId), 2);
		rateId = spIndex == 0 ? MyStringConversion.myInverseStr(MyUtils.dec2Hex("0"), 2): MyStringConversion.myInverseStr(MyUtils.dec2Hex(String.valueOf(spIndex)), 2);

		if ("".equals(meterId)) {
			meterId = MyStringConversion.myInverseConver(MyUtils.dec2Hex("0"), 8);
		}else {
			meterId = MyStringConversion.myInverseConver(MyUtils.dec2Hex(meterId), 8);
		}

		CmdString.sWakeUp = CmdString.cmdWakeUp(netId);
		CmdString.sReadMeter = CmdString.cmdReadMeter(netId, meterId);
		CmdString.sBaseRead = CmdString.cmdBaseRead(netId, meterId);
		CmdString.sSetConfig = CmdString.cmdSetConfig(frequencyId, capacityId, rateId);
		CmdString.sReadConfig = CmdString.cmdReadConfig();
		CmdString.sOpen = CmdString.cmdOpen(netId, meterId);
		CmdString.sClose = CmdString.cmdClose(netId, meterId);
//				CmdString. = ;

		cmdStr = new String[]{CmdString.sWakeUp, CmdString.sReadMeter, CmdString.sBaseRead, CmdString.sReadConfig, CmdString.sSetConfig, CmdString.sOpen, CmdString.sClose};
		nameStr = new String[]{"唤醒", "抄表(读单元)", "基站抄表", "读无线配置", "设无线配置", "单元开阀", "单元关阀"};

		myChangeCmd(nameStr);
	}


	/**
	 * 选择命令发送
	 * @param nameStr
	 */
	private void myChangeCmd(final String[] nameStr) {

		new AlertDialog.Builder(this)
				.setTitle(" 参数选择")
				.setSingleChoiceItems(nameStr, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {

								cmdIndex = which;
							}
						}).setInverseBackgroundForced(true)
				.setNegativeButton("取消", null)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int which) {

//                        strParaneter = strCmd[cmdIndex];
						etData.setText(cmdStr[cmdIndex]);
					}
				}).show();

	}

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
			case R.id.btn_send:
				mySend();
				break;
			case R.id.btn_:
				controlBLE();
				break;
		}
	}

	/**
	 * info 连接/断开 BLE
	 */
	private void controlBLE() {
		if (mConnected){
			btnControlBLE.setText("断开");
			bleDisConn();
		}else {
			btnControlBLE.setText("连接");
			if (mDeviceAddress == null || mDeviceAddress.equals("")){

				startActivityForResult(new Intent(this, DeviceScanActivity.class), 1);
			}else {//蓝牙地址不为空！(有地址)
				bleConn(mDeviceAddress);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		mDeviceName = data.getStringExtra(EXTRAS_DEVICE_NAME);
		mDeviceAddress = data.getStringExtra(EXTRAS_DEVICE_ADDRESS);

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
				clearUI();
				mBluetoothLeService.enableTXNotification();
				mState = UART_PROFILE_DISCONNECTED;
				mBluetoothLeService.close();
//				mBluetoothLeService.connect(mDeviceAddress);// 重新连接蓝牙

			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				mBluetoothLeService.enableTXNotification();
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

				String data = displayData(intent
						.getStringExtra(BluetoothLeService.EXTRA_DATA));
//				Log.i("dysen", "data:"+data);

			}else if (action.equals(mBluetoothLeService.DEVICE_DOES_NOT_SUPPORT_UART)){
				ToastDemo.myHint(DeviceControlActivity.this, "Device doesn't support UART. Disconnecting", 5);
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

	/**
	 * 自定义弹框
	 * @param str
	 * @return
     */
	public AlertDialog.Builder alertDemo(String str) {

		Builder alert = new AlertDialog.Builder(this)
				.setTitle(str)
				.setInverseBackgroundForced(true)
				.setPositiveButton("知道咯",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								dialog.dismiss();

							}
						});

		return alert;
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

		if ("".equals(etSum.getText().toString())) {
			etSum.setText(1 + ""); // 默认金额 1元 = 100分
		}
		intervalTime = Long.parseLong(etSum.getText().toString());

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
		boolean isConn = mBluetoothLeService.connect(bleMac);

		return isConn;
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
			final boolean result = mBluetoothLeService.connect(mDeviceAddress);
			Log.d(TAG, "Connect request result=" + result);
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

	/**
	 *	dysen
	 *	2015-4-18 下午12:26:10
	 *	info:	发送数据
	 */
	public void mySend(){
		String secFunc = "";
		if (!mConnected) {
			ToastDemo.myHint(this, "请先连接蓝牙设备,再做操作", 2);
		} else {

			String sendCmd = etData.getText().toString();

			if ( "".equals(sendCmd)) {
				ToastDemo.myHint(this, "协议不能为空", 2);
				return;
			}else{
				mDataSend.setText(MyStringConversion.myStr(sendCmd));
				mySendMsg(sendCmd);
			}
		}
	}

	/**
	 *	发送数据写到 BLE 块
	 * sendy 2015-1-15 下午3:07:39 函数说明
	 */
	private void mySendMsg(String sendData) {

			if (!"".equals(sendData)) {
				if (!flagHex) {
					// changeCmd(); // 选择命令发送
					// write string
					WriteBytes = sendData.getBytes();
					mDataSend.setText(sendData);
					isHex = false;
					// flagHex = false;
				} else if (flagHex) {
					WriteBytes = hex2byte(sendData.getBytes());
					mDataReceive.setText(cmdName);
					// mDataSend.setText(cmdName);
					System.out.println("此时功能号为：" + cmdName);
					isHex = true;
				}
				mDataReceive.setTextSize(18);
				mDataSend.setTextSize(18);
			}

			tvTx.setText("Tx " + WriteBytes.length);
			tvTx.setTextColor(Color.WHITE);
			tvTx.setTextSize(18);
			tvRx.setText("Rx 00");
			tvRx.setTextColor(Color.WHITE);
			tvTx.setTextSize(18);

		mBluetoothLeService.writeRXCharacteristic(WriteBytes);

//			System.out.println("发送的数据："+MyStringConversion.myStr(sendData));
			mDataSend.setText(nameStr[cmdIndex]+"：\n"+MyStringConversion.myStr(sendData));
		if (nameStr[cmdIndex].equals("唤醒")){
			mDataReceive.setText("正在唤醒...");
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

	/**
	 * 接收的数据
	 * @param data
	 * @return
     */
	private String displayData(String data) {

		String RxCmd;

		if (data != null) {

//			Log.i("dysen", "data>>"+data);
			str += data;
			RxCmd = MyUtils.removeBlank(data);// 去掉字符中间的空格

//			Log.i("dysen", RxCmd.length()+"RxCmd:"+RxCmd);
			if (RxCmd.length() < 18){
				reData = str;
				str += "\n\n";
			}else {
//				Log.i("dysen", MyUtils.HexSUM(RxCmd.substring(2, RxCmd.length() - 2))+"求校验和："+RxCmd.substring(2, RxCmd.length() - 2)+"校验和："+RxCmd.substring(RxCmd.length() - 2, RxCmd.length()));
				if (MyUtils.HexSUM(RxCmd.substring(2, RxCmd.length() - 2)).equals(RxCmd.substring(RxCmd.length() - 2, RxCmd.length()))){
					reData = str;
					str += "\n\n";
				}else {
					reData = str;
				}
			}
			mDataReceive.setText(str);
		}

		String[] pkg = reData.split("\n\n");
		saveMeterData(pkg);
		String sParse = MySingleParse.mySingleParseStr("", MyUtils.removeBlank(pkg[pkg.length-1]), cmdIndex);
		ss = MyUtils.removeBlank(pkg[pkg.length-1]);
//				pkg[pkg.length-1];
		// System.out.println(ss.trim().length()+"ss的长度："+ss);
		if (isHex) {

			if (!TextUtils.isEmpty(ss)) {
				tvRx.setText("Rx " + ss.trim().length() / 2);
				System.out.println("ss:"+ss);

				mDataReceive.setText(ss+"\n"+sParse);
				mDataReceive.setTextSize(18);

			} else {
				tvRx.setText("Rx " + ss.trim().length() / 2);
				mDataReceive.setText(MyUtils.myStr(ss));
				mDataReceive.setTextSize(18);
			}
//			 str = strParse = "";
		} else {
			tvRx.setText("Rx " + ss.trim().length());
			mDataReceive.setText(str);
			// str = strParse = "";
		}
		if ("".equals(sMore)) {
			sMore = data;
		}
//		System.out.println(ss.substring(ss.length()-2, ss.length()));

		tvRx.setTextColor(Color.WHITE);
		tvRx.setTextSize(18);
		// tvRx.setVisibility(View.GONE); //隐藏
		mDataReceive.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {

				new AlertDialog.Builder(DeviceControlActivity.this)
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
		return reData ;
	}


	/**
	 * 保存抄表数据
	 * @param pkg
     */
	private void saveMeterData(String[] pkg) {

		String s = MyUtils.removeBlank(pkg[pkg.length-1]);
			lM = MySingleParse.singleParse(s);

			if (lM != null && lM.size() > 0) {//有效数据  (抄表数据）
//				Log.i("dysen", lM.size()+"pkg---"+s);
				dbMeter.deleteByWhere(tReadMeter.class, "meterId=" + "\'" + lM.get(0).getMeterId() + "\'");
				Log.i("dysen", "数据记录条数："+dbMeter.findAll(tReadMeter.class).size()
				+"\n抄表数据条数："+lM.size());
				for (tReadMeter m : lM) {

					Log.i("dysen", m.getMeterId()+"___"+m.getMeterSty());
					dbMeter.save(m);
				}
			}
	}

	/**
	 * 清除函数 sen dy 2015-2-7 上午8:51:56
	 */
	private void myClaer() {
		str = strParse = "";
		mDataReceive.setText(strParse);
	}
}
