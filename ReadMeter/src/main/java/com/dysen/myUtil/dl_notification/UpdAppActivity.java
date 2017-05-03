package com.dysen.myUtil.dl_notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dysen.mylibrary.utils.util.AppUtils;
import com.dysen.qj.wMeter.R;

import static com.dysen.mylibrary.utils.util.AppUtils.getVersionCode;

//import com.dysen.mylibrary.utils.util.AppUtils;

/**
 * 设置界面，包括检查版本更新、同步记录等功能。
 * @author Shen
 */
public class UpdAppActivity extends Activity {

	// 更新版本要用到的一些信息
	private UpdateInfo info;
	private ProgressDialog progressDialog;
	UpdateInfoService updateInfoService;
	private SelfDialog selfDialog;
	public static String dlUrl, appName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upd_app);
		Button button=(Button)findViewById(R.id.button1);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				checkUpdate();
			}
		});
	}



	@Override
	protected void onResume() {
		super.onResume();

		checkUpdate();
	}

	public void checkUpdate(){
//		Toast.makeText(this, "正在检查版本更新..", Toast.LENGTH_SHORT).show();
		// 自动检查有没有新版本 如果有新版本就提示更新
		new Thread() {
			public void run() {
				try {
					updateInfoService = new UpdateInfoService(UpdAppActivity.this);
					info = updateInfoService.getUpDateInfo();
					if (info != null) {

						if (!info.getVersion().equals(AppUtils.getVersionName(UpdAppActivity.this))) {

							if (info.getVersion().compareTo(AppUtils.getVersionName(UpdAppActivity.this)) == 1) {//大于时 返回 1 反则 -1
								handler1.sendEmptyMessage(0);
							}
						} else {
							Toast.makeText(UpdAppActivity.this, "同一版本，无需更新" + "\nserver versionCode:" + info.getVersion() + "local versionCode:" + getVersionCode(UpdAppActivity.this), Toast.LENGTH_LONG).show();
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
				showUpdateDialog();
			}
		};
	};

	//显示是否要更新的对话框
	private void showUpdateDialog() {

		selfDialog = new SelfDialog(UpdAppActivity.this);

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
					Toast.makeText(UpdAppActivity.this, "SD卡不可用，请插入SD卡",
							Toast.LENGTH_SHORT).show();
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

//	//显示是否要更新的对话框
//	private void showUpdateDialog() {
//
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setIcon(android.R.drawable.ic_dialog_info);
//		builder.setTitle("请升级APP至版本" + info.getVersion());
////		builder.setMessage(info.getDescription());
//		builder.setCancelable(false);
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				if (Environment.getExternalStorageState().equals(
//						Environment.MEDIA_MOUNTED)) {
//					downFile(info.getUrl());
//				} else {
//					Toast.makeText(UpdAppActivity.this, "SD卡不可用，请插入SD卡",
//							Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//			}
//		});
//		builder.create().show();
//	}

	void downFile(final String url) {
		progressDialog = new ProgressDialog(UpdAppActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setTitle("正在下载");
		progressDialog.setMessage("请稍候...");
		progressDialog.setProgress(0);
		progressDialog.show();
//		updateInfoService.downLoadFile(url, progressDialog,handler1);
		startDownLoadService(url);
	}

	public void startDownLoadService(String download_url){
		Intent intent=new Intent(this, DownLoadService.class);
//		intent.putExtra("app_name",AppUtils.getPackageInfo(this).packageName.split("\\.")[(AppUtils.getPackageInfo(this).packageName.split("\\.").length-1)]);
//		intent.putExtra("download_url",download_url);
		appName = this.getText(R.string.app_name).toString();
		System.out.println(appName+"___________________________________");
// AppUtils.getPackageInfo(this).packageName.split("\\.")[(AppUtils.getPackageInfo(this).packageName.split("\\.").length-1)];
		dlUrl = download_url;
		finish();
		startService(intent);
	}

}