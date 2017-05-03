package com.dysen.login_register;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dysen.info.DataInfo;
import com.dysen.myUtil.IsPhoneNumber;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.ToastDemo;
import com.dysen.mylibrary.utils.StatusBarUtil;
import com.dysen.mylibrary.utils.util.LogUtils;
import com.dysen.qj.wMeter.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * sen dy 2015-2-27 下午2:41:12
 */
public class UpdatePwd extends MyActivityTools {

	private String HTTP_IP = "";

	EditText etUpdatePwd, etUpdatePhone;
	Button btnUpdateOk, btnShow, btnGet;
	TextView tvUpdateName;
	private Handler handler;
	String userName;
	Integer loginId;
	long bleAddr = 1;
	ListView list;

	private String updateUrl;
	private byte[] sendData;
	public static DataInfo data;
	String pwd, phone;
	boolean isCipher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.update_pwd);

		//透明状态栏
		StatusBarUtil.setTransparent(this);
		(this.findViewById(R.id.ll_back)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		boolean bl = FlymeSetStatusBarLightMode(getWindow(), true);
////		ToastDemo.myHint(this,   "bl:"+bl);
//		boolean b =MIUISetStatusBarLightMode(getWindow(), true);
////		ToastDemo.myHint(this,   "b:"+b);

		((TextView) this.findViewById(R.id.tv_hint)).setText("更新密码");
		HTTP_IP = this.getText(R.string.HTTP_IP).toString();

		myFindId();
		myHandler();

	}

	private void myHandler() {
		handler = new Handler() {
			private String strMsg;
			private DataInfo data;

			@Override
			public void handleMessage(Message msg) {

				data = (DataInfo) msg.obj;
				if (data == null) {

					ToastDemo.myHint(UpdatePwd.this, "表号不匹配 ！！！", 2);
				} else {
					strMsg = data.getMsg().toString();
					if (data.getSuccess()) {

						if ("修改成功".equals(strMsg)) {

							ToastDemo.myHint(UpdatePwd.this, strMsg, 1);
							finish();
						} else {
							ToastDemo.myHint(UpdatePwd.this, strMsg, 4);
						}
					} else {
						ToastDemo.myHint(UpdatePwd.this, strMsg, 4);
					}
				}
			}
		};

	}

	private void myFindId() {

		loginId = getIntent().getExtras().getInt("login_id");
		userName = getIntent().getStringExtra("user_name");
		etUpdatePwd = (EditText) this.findViewById(R.id.et_update_pwd);
		etUpdatePhone = (EditText) this.findViewById(R.id.et_update_phone);
		tvUpdateName = (TextView) this.findViewById(R.id.tv_update_name);
		btnUpdateOk = (Button) this.findViewById(R.id.btn_update_ok);
		btnShow = (Button) this.findViewById(R.id.btn_show);
		btnShow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				myCipher();
			}
		});

		btnGet = (Button) this.findViewById(R.id.btn_get);
		btnGet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				myGetOwnNumber();
			}
		});
		
		if (!"".equals(userName)) {
			tvUpdateName.setText(userName + " 用户");
		}
		mySubmit();
	}

	/**
	 *	dysen
	 *	2015-7-13 下午3:24:09
	 *	info:	提交修改信息
	 */
	public void mySubmit() {

		btnUpdateOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pwd = etUpdatePwd.getText().toString();
				phone = etUpdatePhone.getText().toString();
				System.out.println("pwd:" + pwd + "phone:" + phone);

				if (!"".equals(pwd) && !"".equals(phone)) {
					
					boolean mobileNo = IsPhoneNumber.isMobileNo(phone);
					if (mobileNo) {
						if (!"".equals(loginId)) {
							
							myUpdatePwd(loginId, pwd, phone);
						}
					} else {
						ToastDemo.myHint(UpdatePwd.this, "注册手机号不合规则", 2);
					}
				} else {
					ToastDemo.myHint(UpdatePwd.this, "修改参数不可为空！！！", 2);

				}
			}
		});
	}

	/**
	 *	dysen
	 *	2015-7-2 上午8:53:28
	 *	info: 获取本机号码
	 */
	private void myGetOwnNumber() {
		
		TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
//		mTelephonyMgr.getLine1Number();
		System.out.println(mTelephonyMgr.getLine1Number()+"	用户：\n");
		etUpdatePhone.setText(mTelephonyMgr.getLine1Number().replace("+86", ""));
	}

	
	/**
	 * sen dy
	 * 
	 * 2015-6-23 下午4:56:10
	 * 
	 * info: 密文明文显示密码
	 */
	private void myCipher() {

		if (isCipher) {
			((EditText) findViewById(R.id.et_update_pwd))
					.setTransformationMethod(PasswordTransformationMethod
							.getInstance()); // 密文显示
			btnShow.setBackgroundResource(R.drawable.eyes_press);
			isCipher = false;
			ToastDemo.myHint(this, "密文显示", 2);
		} else {
			((EditText) findViewById(R.id.et_update_pwd))
					.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());// 明文显示
			btnShow.setBackgroundResource(R.drawable.eyes_normal);
			isCipher = true;
			ToastDemo.myHint(UpdatePwd.this, "明文显示", 2);
		}
	}

	/**
	 * dysen 2015-4-17 下午4:12:50 info: 更新密码
	 */
	public void myUpdatePwd(Integer id, String pwd, String phone) {

		if (!"".equals(id)) {

			sendData = // meterId.getBytes();
			("id=" + id + "&&pw=" + pwd + "&&phone=" + phone).getBytes();
			System.out.println("sendData:" + sendData);
			updateUrl = HTTP_IP + "/app/login/modify?";
			new Thread(Connect).start(); // 启动下载内容线程
			System.out.println("id=" + id + "&&pw=" + pwd + "&&phone=" + phone);
			LogUtils.i("更改密码\nid=" + id + "&&pw=" + pwd + "&&phone=" + phone);
		} else {
			// myHint("请先输入表号在进行查询操作！！！");
		}
	}

	Runnable Connect = new Runnable() {

		private String str;
		private static final int TIMEOUT = 10000;// 10秒
		private static final String encoding = "utf-8";

		// "http://192.168.1.103:8080/water/seach/seachId?";
		// "http://dysen.oicp.net/ssh/uploadOrDownload/upload";

		URL url;
		HttpURLConnection conn;

		@Override
		public void run() {
			// System.out.println("---------run -------");

			try {
				url = new URL(updateUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(TIMEOUT);
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setRequestProperty("Charset", encoding);
				conn.setRequestProperty("Content-Length",
						String.valueOf(sendData.length));
				conn.connect();

				System.out.println(new String(sendData) + "开始下载");
				OutputStream outStream = conn.getOutputStream();

				outStream.write(sendData);
				outStream.flush();
				outStream.close();

				// 从 服务器 获取数据
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), encoding));
				String retData = null;
				String responseData = "";
				while ((retData = in.readLine()) != null) {
					responseData += retData;
					System.out.println(responseData + "下载的内容");

				}
				String str = responseData;

				data = JSON.parseObject(responseData, DataInfo.class);
				resultJson(data);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private void resultJson(DataInfo data) {
		try {
			Message message = Message.obtain();
			message.obj = data;

			handler.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
