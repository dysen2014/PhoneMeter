package com.dysen.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.dysen.qj.wMeter.R;

import java.io.File;

/**
 * Created by dy on 2016-11-22.
 * App 更新下载后台服务
 */

public class UpdateService extends Service {

    private String apkUrl;
    private String filePath;
    private NotificationManager notificationManager;
    private Notification notification;

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        filePath = Environment.getExternalStorageDirectory() + "test/t  est.apk";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            notifyUser(getString(R.string.update_download_failed), getString(R.string.update_download_fail_msg), 0);
            stopSelf();
        }
        apkUrl = intent.getStringExtra("apkUrl");
        notifyUser(getString(R.string.update_download_start), getString(R.string.update_download_start), 0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        UpdateManager.getInstance().startDownloads(apkUrl, filePath, new UpdateDownloadListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {

                notifyUser(getString(R.string.update_download_progressing), getString(R.string.update_download_progressing), progress);
            }

            @Override
            public void onFinished(int completeSize, String downloadUrl) {

                notifyUser(getString(R.string.update_download_finish), getString(R.string.update_download_finish), 100);
                stopSelf();
            }

            @Override
            public void onFailure() {

                notifyUser(getString(R.string.update_download_failed), getString(R.string.update_download_fail_msg), 0);
                stopSelf();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 更新我们的 notification 告诉用户 当前下载的进度
     * @param retult
     * @param reason
     * @param progress
     */
    private void notifyUser(String retult, String reason, int progress){

        //使用它  兼容android 不同系统(厂家)的 notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_dialog))
        .setContentTitle(getString(R.string.app_name));
        if (progress > 0 && progress < 100){
            builder.setProgress(100, progress, false);
        }else {
            builder.setProgress(0, 0, false);//隐藏
        }
        builder.setAutoCancel(true);//可以被 自动清除
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(retult);
        builder.setContentIntent(progress >= 100 ? getContentIntent() : PendingIntent.getActivity(this, 0, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT));
        notification = builder.build();
        notificationManager.notify(0, notification);
    }

    /**
     * 下载完成后   调用系统的安装程序，进行安装
     * @return
     */
    private PendingIntent getContentIntent() {

        File apkFile = new File(filePath);

        //系统的 安装程序
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()), "application/vnd.android.package-archive");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}
