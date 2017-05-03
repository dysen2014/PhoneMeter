package com.dysen.update;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by dy on 2016-11-22.
 */

public class UpdateDownloadRequest implements Runnable {

    String downloadUrl;
    String localFilePath;
    UpdateDownloadListener updateDownloadListener;
    boolean isDownloading = false;
    long currentLength;
    DownloadResponseHandler downloadResponseHandler;

    public UpdateDownloadRequest(String downloadUrl, String localFilePath, UpdateDownloadListener updateDownloadListener){

        this.downloadUrl = downloadUrl;
        this.localFilePath = localFilePath;
        this.updateDownloadListener = updateDownloadListener;
        this.isDownloading = true;
        this.downloadResponseHandler = new DownloadResponseHandler();
    }

    //真正的去建立连接的方法
    private void makeRequest() throws IOException, InterruptedException{

        if (!Thread.currentThread().isInterrupted()){

            URL url = new URL(downloadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.connect();//阻塞我们当前的线程
            currentLength = httpURLConnection.getContentLength();
            if (!Thread.currentThread().isInterrupted()){

                //真正的完成文件的下载
                downloadResponseHandler.sendResponseMessage(httpURLConnection.getInputStream());
            }
        }
    }

    @Override
    public void run() {

        try {
            makeRequest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化 数字 精确到小数后两位
     * @param value
     * @return
     */
    private String get2PointFloatStr(float value){

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        return decimalFormat.format(value);
    }

    public class DownloadResponseHandler {

        protected static final int SUCCESS_MESSAGE = 0;
        protected static final int FAILURE_MESSAGE = 1;
        protected static final int START_MESSAGE = 2;
        protected static final int FINISH_MESSAGE = 3;
        protected static final int NETWORK_OFF = 4;
        protected static final int PROGRESS_CHANGED = 5;

        private int mCompleteSize = 0;
        private int progress = 0;

        private Handler handler;//真正的完成线程间的通信

        public DownloadResponseHandler(){

            handler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                }
            };
        }

        /**
         * 用来发送不同的消息对象
         */
        protected void sendFinishMessage(){

            sendMessage(obtainMessage(FINISH_MESSAGE, null));
        }

        private void sendProgressChangdMessage(int progress){
            sendMessage(obtainMessage(PROGRESS_CHANGED, new Object[]{progress}));
        }

        protected void sendFailureMessage(FailureCode failureCode){
            sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{failureCode}));
        }

        protected void sendMessage(Message message){
            if (handler != null){
                handler.sendMessage(message);
            }else {
                handleSelfMessage(message);
            }
        }

        /**
         * 获取一个消息对象
         * @param responseMessage
         * @param response
         * @return
         */
        protected Message obtainMessage(int responseMessage, Object response){

            Message message = null;
            if (handler != null){
                message = handler.obtainMessage(responseMessage, response);
            }else {
                message = Message.obtain();
                message.what = responseMessage;
                message.obj = response;
            }

            return message;
        }

        protected void handleSelfMessage(Message message){

            Object[] response;
            switch (message.what){
                case FAILURE_MESSAGE:
                    response = (Object[]) message.obj;
                    handleFailureMessage((FailureCode)response[0]);
                    break;
                case PROGRESS_CHANGED:
                    response = (Object[]) message.obj;
                    handleProgressMessage(((Integer) response[0]).intValue());
                    break;
                case FINISH_MESSAGE:
                    onFinish();
                    break;
            }
        }
        protected void handleProgressMessage(int progress){
            updateDownloadListener.onProgressChanged(progress, "");
        }

        protected void handleFailureMessage(FailureCode failureCode){

            onFailure(failureCode);
        }

        //接口的回调
        public void onFinish(){
            updateDownloadListener.onFinished(mCompleteSize, "");
        }

        public void onFailure(FailureCode failureCode){
            updateDownloadListener.onFailure();
        }

        void sendResponseMessage(InputStream inputStream){

            RandomAccessFile randomAccessFile = null;
            mCompleteSize = 0;

            byte[] buffer = new byte[1024];
            int length = -1;
            int limit = 0;
            try {
                randomAccessFile = new RandomAccessFile(localFilePath, "rwd");

                while ((length = inputStream.read(buffer)) != -1){

                    if (isDownloading){
                        randomAccessFile.write(buffer, 0, length);
                        mCompleteSize += length;
                        if (mCompleteSize < currentLength){
                            progress = (int) Float.parseFloat(get2PointFloatStr(mCompleteSize / currentLength));
                            if (limit / 30 ==0 && progress <= 100){//limit/30 为了限制 notification 的更新频率
                                sendProgressChangdMessage(progress);
                            }
                            limit++;
                        }
                    }
                }
                sendFinishMessage();
            } catch (FileNotFoundException e) {
                sendFailureMessage(FailureCode.IO);
            } catch (IOException e) {
                sendFailureMessage(FailureCode.IO);
            }finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (randomAccessFile != null){
                            randomAccessFile.close();
                        }
                    } catch (IOException e) {
                       sendFailureMessage(FailureCode.IO);
                    }
            }
        }
    }

    /**
     * 包含了下载过程中可能出现的异常情况
     */
    public enum FailureCode{
        UnknownHost, Socket, SocketTimeout, ConnectTimeout,
        IO, HttpResponse, JSON, Interrupted
    }
}
