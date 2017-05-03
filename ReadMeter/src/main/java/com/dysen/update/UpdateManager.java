package com.dysen.update;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by dy on 2016-11-22.
 * 下载调度管理器， 调用我们的 UpdateDownloadRequest
 */

public class UpdateManager {

    private static UpdateManager updateManager;
    private ThreadPoolExecutor threadPoolExecutor;
    private UpdateDownloadRequest updateDownloadRequest;

    public UpdateManager(){

        threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    static{
        updateManager = new UpdateManager();
    }

    public static UpdateManager getInstance(){

        return updateManager;
    }

    public void startDownloads(String downloadUrl, String localPath, UpdateDownloadListener updateDownloadListener){

        if (updateDownloadRequest != null){
            return;
        }

        checkLocalFilePath(localPath);

        //开始真正的下载任务
        updateDownloadRequest = new UpdateDownloadRequest(downloadUrl, localPath, updateDownloadListener);
        Future<?> future = threadPoolExecutor.submit(updateDownloadRequest);
    }

    /**
     * 检查文件路径是否存在
     * @param localPath
     */
    private void checkLocalFilePath(String localPath) {

        File dir = new File(localPath.substring(0, localPath.lastIndexOf("/")+1));

        if (!dir.exists()){
            dir.mkdir();//创建文件夹
        }

        File file = new File(localPath);
        if(!file.exists()){
            try {
                file.createNewFile();//创建文件
            }catch (Exception e){

            }
        }
    }
}
