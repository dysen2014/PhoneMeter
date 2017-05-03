package com.dysen.myUtil.httpUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 作者：沈迪 [dysen] on 2016-04-15 16:31.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class UpDateData {

    private String str;
    private String Updata = "upData";
    private static final int TIMEOUT = 10000;// 10000毫秒
    private  final String encoding = "utf-8";
    String updateUrl = "http://192.168.1.214:80/ssh/uploadOrDownload/upload";
    // "http://dysen.oicp.net/ssh/uploadOrDownload/upload";
    URL url;
    HttpURLConnection conn;
    byte[] sendData;
    public static String upDateStr;

    public UpDateData(String url, String data){
        this.updateUrl = url;
        this.sendData = data.getBytes();
        new Thread(Connect).start(); // 启动上传内容线程
    }

    /*
     * Function  :   发送Post请求到服务器
     */
    Runnable Connect = new Runnable() {

        @Override
        public void run() {

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
                System.out.println(new String(sendData) + "开始上传");
                OutputStream outStream = conn.getOutputStream();

                outStream.write(sendData);
                outStream.flush();
                outStream.close();

//                BufferedReader in = new BufferedReader(new InputStreamReader(
//                        conn.getInputStream(), encoding));
//                String retData = null;
//                String responseData = "";
//                while ((retData = in.readLine()) != null) {
//                    responseData += retData;
//                    str = responseData;
//                }
//                upDateStr =  str;
//                System.out.println(responseData);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
}
