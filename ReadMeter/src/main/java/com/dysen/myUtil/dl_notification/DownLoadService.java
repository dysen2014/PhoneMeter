package com.dysen.myUtil.dl_notification;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.dysen.load.UserDemo;
import com.dysen.mylibrary.utils.util.ToastUtils;
import com.dysen.qj.wMeter.R;

import java.io.File;

/**
 * Created by dy on 2016-10-21.
 */

public class DownLoadService extends Service {
    String download_url;
    String savePath= Environment.getExternalStorageDirectory()+"/Test.apk";
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
        mFile=new File(savePath);
        Log.e("test","执行onStartCommand");
        //设置想要展示的数据内容
        Intent intent_noti = new Intent();
        intent_noti.setAction(Intent.ACTION_VIEW);
        //文件的类型，从tomcat里面找
        intent_noti.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
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
                        mFile=file;
                        DownLoadService.this.stopSelf();
                        ToastUtils.showLong(DownLoadService.this, "下载完成", 2);
                    }

                    @Override
                    public void onFailure(Throwable t, String strMsg) {

                    }
                });
        currentNotify = notify7;
        return super.onStartCommand(intent, flags, startId);

    }
}
