package com.dysen.load;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dysen.dl_notification.DownLoadService;
import com.dysen.dl_notification.SelfDialog;
import com.dysen.dl_notification.UpdateInfo;
import com.dysen.dl_notification.UpdateInfoService;
import com.dysen.info.DataInfo;
import com.dysen.login_register.LoginDemo;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.QuitAllActivity;
import com.dysen.myUtil.ToastDemo;
import com.dysen.mylibrary.utils.StatusBarUtil;
import com.dysen.mylibrary.utils.util.AppUtils;
import com.dysen.mylibrary.utils.util.SharedPreUtils;
import com.dysen.mylibrary.utils.util.ToastUtils;
import com.dysen.qj.wMeter.R;
import com.dysen.type.about.AboutActivity;
import com.dysen.type.dataSys.DataSysActivity;
import com.dysen.type.meterSys.MeterSysActivity;
import com.dysen.type.meterSys.systemSet.SystemSetActivity;

import java.util.Timer;
import java.util.TimerTask;

import static com.dysen.mylibrary.utils.util.AppUtils.getVersionCode;

/**
 * sen dy 2015-2-27 下午2:41:12
 * 描述: 主页
 */
public class UserDemo extends MyActivityTools {

	String HTTP_IP ;
	public static Button btnLoginJoin;
	public static TextView tvLoginJoin;

	public static DataInfo data;

	public static boolean isLoginSuccess, isUpd=true;
	public static String readName;
	public static String readId;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_demo);

//		ToastUtils.show(this, "标题", "内容展示");
		//透明状态栏
		StatusBarUtil.setTransparent(this);

		idInit();
		HTTP_IP = this.getText(R.string.HTTP_IP).toString();
		System.out.println("IP地址：" + HTTP_IP);

		btnLoginJoin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (isLoginSuccess) {
				myDialog();
				// }
			}
		});

		// myUserImg();
	}



	@Override
	protected void onResume() {
		super.onResume();

		if (!isLoginSuccess && isUpd) {//程序启动时 检查 app 版本

//			startActivity(new Intent(this, UpdAppActivity.class));
			checkUpdate();
			isUpd = false;
		}
	}

	// 更新版本要用到的一些信息
	private UpdateInfo info;
	private ProgressDialog progressDialog;
	UpdateInfoService updateInfoService;
	private SelfDialog selfDialog;
	public static String dlUrl, appName;

	/**
	 * app 版本更新 检查
	 */
	public void checkUpdate(){
//		Toast.makeText(this, "正在检查版本更新..", Toast.LENGTH_SHORT).show();
		// 自动检查有没有新版本 如果有新版本就提示更新
		new Thread() {
			public void run() {
				try {
					updateInfoService = new UpdateInfoService(UserDemo.this);
					info = updateInfoService.getUpDateInfo();
					if (info != null) {

						if (!info.getVersion().equals(AppUtils.getVersionName(UserDemo.this))) {

							if (info.getVersion().compareTo(AppUtils.getVersionName(UserDemo.this)) == 1) {//大于时 返回 1 反则 -1
								handler1.sendEmptyMessage(0);
							}
						} else {
							Toast.makeText(UserDemo.this, "同一版本，无需更新" + "\nserver versionCode:" + info.getVersion() + "local versionCode:" + getVersionCode(UserDemo.this), Toast.LENGTH_LONG).show();
							System.out.println("同一版本，无需更新");
						}
					}else {
						System.out.println("更新的 update.txt 文件配置格式有误。");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			// 如果有更新就提示
			if (updateInfoService.isNeedUpdate()) {
				showUpdateDialog();//提示用户是否更新App
			}
		};
	};

	//显示是否要更新的对话框
	private void showUpdateDialog() {//显示 自定义提示框

		selfDialog = new SelfDialog(UserDemo.this);

		selfDialog.setTitle("发现新版本啦!" );
		selfDialog.setInfo("版本："+ info.getVersion()+"\t\t\t大小："+info.getPkgLen());
		selfDialog.setMessage(info.getDescription());
		selfDialog.setYesOnclickListener("立即更新", new SelfDialog.onYesOnclickListener() {
			@Override
			public void onYesClick() {

				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
//					downFile(info.getUrl());
					startDownLoadService(info.getUrl());
				} else {
					ToastUtils.showLong(UserDemo.this, "SD卡不可用，请插入SD卡", 3);
					System.out.println("SD卡不可用，请插入SD卡");
				}
//				Toast.makeText(UpdAppActivity.this,"点击了--确定--按钮",Toast.LENGTH_LONG).show();
				selfDialog.dismiss();
			}
		});
		selfDialog.setNoOnclickListener("稍后再说", new SelfDialog.onNoOnclickListener() {
			@Override
			public void onNoClick() {
//				Toast.makeText(UpdAppActivity.this,"点击了--取消--按钮",Toast.LENGTH_LONG).show();
				selfDialog.dismiss();
			}
		});
		selfDialog.show();
	}

	public void startDownLoadService(String download_url){//App 更新服务
		Intent intent=new Intent(this, DownLoadService.class);
//		intent.putExtra("app_name",AppUtils.getPackageInfo(this).packageName.split("\\.")[(AppUtils.getPackageInfo(this).packageName.split("\\.").length-1)]);
//		intent.putExtra("download_url",download_url);
		appName = this.getText(R.string.app_name).toString();
		System.out.println(appName+"___________________________________");
//				AppUtils.getPackageInfo(this).packageName.split("\\.")[(AppUtils.getPackageInfo(this).packageName.split("\\.").length-1)];
		dlUrl = download_url;
        intent.putExtra("apkUrl", dlUrl);
//		finish();
		startService(intent);//启动App更新服务
	}

	private void idInit() {

		btnLoginJoin = bindView(R.id.btn_login_join);
		tvLoginJoin = bindView(R.id.tv_login_join);
	}

	public void systemSet(View v){

		if (!isLoginSuccess) {
			myDialog();
		}else {
//        ToastDemo.myHint(this, "", 5);
			startActivity(new Intent(this, SystemSetActivity.class));
		}
	}


	/**
	 *	dysen
	 *	2015-8-27 下午2:56:40
	 *	info: 无线自动抄表系统(WAMR) wireless auto meter read
	 */
	public void btnWAMR(View v) {
		if (!isLoginSuccess) {
			myDialog();
		}else {
			intent = new Intent(this, MeterSysActivity.class);

			startActivity(intent);
//			startActivityForResult(intent, 2);
		}
	}

	public void btnDataSM(View v) {
		if (!isLoginSuccess) {
			myDialog();
		}else {
			intent = new Intent(this, DataSysActivity.class);
			startActivity(intent);
		}
	}

	public void btnAbout(View v) {
		if (!isLoginSuccess) {
			myDialog();
		}else {
			intent = new Intent(this, AboutActivity.class);
//			intent = new Intent(this, Test.class);
			startActivity(intent);
		}
	}

	// 设置回退
	private static boolean isExit = false;
	private static boolean hasTask = false;
	Timer tExit = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			isExit = false;
			hasTask = true;
		}
	};

	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (isExit == false) {
				isExit = true;
				ToastDemo.myHint(this, "再按一次退出程序\ue021", 2);
				if (!hasTask) {
					tExit.schedule(task, 2000);// 2s 内点击两次返回键则退出
				}
			} else {
				// finish();
				 QuitApp();
				// startActivity(new Intent(this, ButtonBar.class));
			}

		}
		return false;
	}

	public void QuitApp() {
		new AlertDialog.Builder(this)
				.setTitle("提示 ")
				// \ue114
				.setMessage("您 ! 确定要退出  ")
				// \ue021 \ue020
				.setPositiveButton("确定 ",// \ue021
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

								finish(); // 加这个
											// 可以退出整个程序(所有
											// activity)
								// 退出整个程序
								QuitAllActivity.getInstance().exit();

							}
						})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						new ToastDemo().myHint(UserDemo.this, "欢迎回来 ", 5);// \ue021\ue021\ue021
					}
				}).show();
	}

	/**
	 * sen dy
	 * 
	 * 2015-6-23 下午4:06:27
	 * 
	 * info: 提示用户登录,注册
	 */
	public void myDialog() {
//String title, String msg
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// 退出
						dialog.dismiss();
						startActivityForResult(new Intent(UserDemo.this, LoginDemo.class), 1);
					}
				});
		alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
//				tvLoginJoin.setText(Html
//						.fromHtml("<font  color=\"green\"><u>"
//								+ readName + "</u></font>"));// "登录成功"
		if (isLoginSuccess){
			alert.setTitle("重新登录");
//			alert.setMessage("是否确认"+ "重新登录" +"?");
			alert.create().show();
		}else {
			startActivityForResult(new Intent(UserDemo.this, LoginDemo.class), 1);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        readId = data.getExtras().getString("read_id");
		readName = data.getExtras().getString("read_name");
		isLoginSuccess = data.getExtras().getBoolean("flag_login");
		
		if (isLoginSuccess) {
			
			SharedPreUtils.put(this, "read_id", readId);
			SharedPreUtils.put(this, "read_name", readName);
			btnLoginJoin
			.setBackgroundResource(R.drawable.pressed);
			tvLoginJoin.setText(Html
					.fromHtml("<font  color=\"green\"><u>"
							+ readName + "</u></font>"));// "登录成功"
			// 得到新Activity关闭后返回的数据
		}else {
			btnLoginJoin
					.setBackgroundResource(R.drawable.normal);
			tvLoginJoin.setText(getText(R.string.login_txt));// "登录失败"
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
