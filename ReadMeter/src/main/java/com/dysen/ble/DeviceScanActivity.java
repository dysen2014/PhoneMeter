package com.dysen.ble;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.myUtil.dialog.SpotsDialog;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.mylibrary.utils.util.PhoneUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.table.tBleInfo;

import java.util.ArrayList;
import java.util.List;

import static com.dysen.myUtil.MyActivityTools.dbBle;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
@SuppressLint("NewApi")
public class DeviceScanActivity extends ListActivity {
	private LeDeviceListAdapter mLeDeviceListAdapter;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private Handler mHandler;
	long bleAddr;
	private static final int REQUEST_ENABLE_BT = 2;
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 5000;
	private AlertDialog alert;
	private String mDeviceName, mDeviceAddress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mHandler = new Handler();

		// 检查设备是否支持BLE
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
					.show(); // 该设备不支持BLE
			finish();
		}

		// 检查设备是否支持BLE
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// 检查设备是否支持蓝牙
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, R.string.error_bluetooth_not_supported,
					Toast.LENGTH_SHORT).show(); // 该设备不支持BLE
			finish();
			return;
		}

		alert = new SpotsDialog(this, "扫描设备");
//		alert.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		if (!mScanning) {
			menu.findItem(R.id.menu_stop).setVisible(false);
			menu.findItem(R.id.menu_scan).setVisible(true);
			menu.findItem(R.id.menu_refresh).setActionView(null);
		} else {
			menu.findItem(R.id.menu_stop).setVisible(true);
			menu.findItem(R.id.menu_scan).setVisible(false);
			menu.findItem(R.id.menu_refresh).setActionView(
					R.layout.actionbar_indeterminate_progress);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_scan:
				mLeDeviceListAdapter.clear();
				scanLeDevice(true);
				break;
			case R.id.menu_stop:
				scanLeDevice(false);
				break;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		/**
		 * 确保蓝牙设备可用，否则弹窗提示 设备匹配 与普通蓝牙设备的匹配类似，区别在于要判断是否是BLE设备
		 */
		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}

		/**
		 * 在确保 ble 可用及蓝牙已打开后，开始扫描外围设备。扫描过程由回调接口监控着。 一旦发现外围 ble 设备，便将其加入 ble
		 * 设备列表中。
		 */
		mLeDeviceListAdapter = new LeDeviceListAdapter();
		setListAdapter(mLeDeviceListAdapter);
		LogUtils.i("一旦发现外围 ble 设备，便将其加入 ble\t\t * 设备列表中"+alert);
		if (alert.isShowing())
			alert.dismiss();
		scanLeDevice(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// User chose not to enable Bluetooth.
		if (requestCode == REQUEST_ENABLE_BT
				&& resultCode == Activity.RESULT_CANCELED) {
			// mBluetoothAdapter.disable(); // 关闭蓝牙
			exitBleScan();
			return;
		}else if (requestCode == 3){
			scanLeDevice(true);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		scanLeDevice(false);
		mLeDeviceListAdapter.clear();
	}

	/**
	 * 搜索出的设备，点击某一设备后执行 点击搜索出的某一设备后，将尝试与其进行连接。同时将设备的地址放入广播中，最终 作为入参传递给
	 * BluetoothAdapter.getRemoteDevice，以获取外围设备 device。
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
		if (device == null)
			return;
		final Intent intent = new Intent();
		intent.putExtra(BleReadMeterActivity.EXTRAS_DEVICE_NAME,
				device.getName());
		intent.putExtra(BleReadMeterActivity.EXTRAS_DEVICE_ADDRESS,
				device.getAddress());
		if (mScanning) {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			mScanning = false;
		}

//		startActivity(intent);
		setResult(1, intent);//返回 name 和 mac
		finish();

	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// 在预定义的扫描时间段之后停止扫描.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					alert.cancel();
					invalidateOptionsMenu();
				}
			}, SCAN_PERIOD);

			mScanning = true;
			alert.show();
			mBluetoothAdapter.startLeScan(mLeScanCallback);
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				if(PhoneUtils.isGpsEnable(this)){
					mBluetoothAdapter.startLeScan(mLeScanCallback);
				}else {
					Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivityForResult(intent,3);
				}
			}

		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
			alert.cancel();
		}
		invalidateOptionsMenu();
	}

	/* *
	 * 搜索Ble设备的过程是异步进行的，当搜索到设备时执行如下回调接口
	 */
	private class LeDeviceListAdapter extends BaseAdapter {
		private ArrayList<BluetoothDevice> mLeDevices;
		private LayoutInflater mInflator;

		public LeDeviceListAdapter() {
			super();
			mLeDevices = new ArrayList<BluetoothDevice>();
			mInflator = DeviceScanActivity.this.getLayoutInflater();
		}

		public void addDevice(BluetoothDevice device) {
			if (!mLeDevices.contains(device)) {
				mLeDevices.add(device);
			}
		}

		public BluetoothDevice getDevice(int position) {
			return mLeDevices.get(position);
		}

		public void clear() {
			mLeDevices.clear();
		}

		@Override
		public int getCount() {
			return mLeDevices.size();
		}

		@Override
		public Object getItem(int i) {
			return mLeDevices.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			final ViewHolder viewHolder;
			// General ListView optimization code.
			if (view == null) {
				view = mInflator.inflate(R.layout.listitem_device, null);
				viewHolder = new ViewHolder();
				viewHolder.deviceAddress = (TextView) view
						.findViewById(R.id.device_address);
				viewHolder.deviceName = (TextView) view
						.findViewById(R.id.device_name);
				viewHolder.deviceRssi = (TextView) view.findViewById(R.id.device_rssi);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			BluetoothDevice device = mLeDevices.get(i);
			final String deviceName = device.getName();

			if (deviceName != null && deviceName.length() > 0)
				viewHolder.deviceName.setText(deviceName);
			else
				viewHolder.deviceName.setText(R.string.unknown_device);
			viewHolder.deviceAddress.setText("MAC:\t"+device.getAddress());
			viewHolder.deviceRssi.setText("RSSI:\t"+mRssi);

			return view;
		}
	}

	int mRssi;
	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
							 final byte[] scanRecord) {
			mRssi = rssi;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mLeDeviceListAdapter.addDevice(device);
					mLeDeviceListAdapter.notifyDataSetChanged();
				}
			});
		}
	};

	static class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
		TextView deviceRssi;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBleScan();
		}
		return false;
	}


	private void exitBleScan() {
		Intent intent = new Intent();
		List<tBleInfo> listBle = dbBle.findAll(tBleInfo.class, "id DESC");

		if (listBle == null ){
			listBle = new ArrayList<tBleInfo>();
		}
		if (listBle.size() > 0) {
			mDeviceName = listBle.get(0).getBleName();
			mDeviceAddress = listBle.get(0).getBleMac().trim().toUpperCase();
		}else {
			mDeviceName = "";
			mDeviceAddress = "";
		}
		intent.putExtra("DEVICE_NAME", mDeviceName);
		intent.putExtra("DEVICE_ADDRESS", mDeviceAddress);
		setResult(1, intent);// 返回数据
		finish();
	}
}