package com.dysen.dl_notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.dysen.load.UserDemo;
import com.dysen.mylibrary.utils.util.ToastUtils;
import com.dysen.qj.wMeter.BuildConfig;
import com.dysen.qj.wMeter.R;

import java.io.File;

/**
 * Created by dy on 2016-10-21.
 */

public class DownLoadService extends Service {
    String download_url;
    String savePath= Environment.getExternalStorageDirectory()+"/test.apk";
    private int requestCode = (int) SystemClock.uptimeMillis();
    private NotifyUtil currentNotify;
    File mFile;
    private String appName;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        download_url = UserDemo.dlUrl;
//                intent.getStringExtra("download_url");
        appName = UserDemo.appName;
//                intent.getStringExtra("app_name");
        mFile = new File(savePath);
        Log.e("test",savePath+"\n执行onStartCommand");
        //设置想要展示的数据内容
        Intent intent_noti = new Intent();
        intent_noti.setAction(Intent.ACTION_VIEW);
        //判断是否是Android N 以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent_noti.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", mFile);
            intent_noti.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            //文件的类型，从tomcat里面找
//            intent_noti.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
            intent_noti.setDataAndType(Uri.parse("file://" + mFile.getAbsolutePath()), "application/vnd.android.package-archive");
//            intent_noti.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        PendingIntent rightPendIntent = PendingIntent.getActivity(this,
                requestCode, intent_noti, PendingIntent.FLAG_UPDATE_CURRENT);

        int smallIcon = R.drawable.ic_launcher;
        String ticker = appName;
        //实例化工具类，并且调用接口
        NotifyUtil notify7 = new NotifyUtil(this, 7);

        notify7.notify_progress(rightPendIntent, smallIcon, ticker, appName, "正在下载中",
                false, false, false, download_url, savePath, new NotifyUtil.DownLoadListener() {
                    @Override
                    public void OnSuccess(File file) {
                        mFile = file;
                        DownLoadService.this.stopSelf();
                        ToastUtils.showLong(DownLoadService.this, "下载完成", 1);
                    }

                    @Override
                    public void onFailure(Throwable t, String strMsg) {
                        DownLoadService.this.stopSelf();
                        ToastUtils.showLong(DownLoadService.this, "下载失败", 3);
                    }
                });
        currentNotify = notify7;
        return super.onStartCommand(intent, flags, startId);

    }
}
