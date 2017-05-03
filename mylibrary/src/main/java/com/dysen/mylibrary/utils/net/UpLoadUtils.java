package com.dysen.mylibrary.utils.net;

import com.dysen.mylibrary.utils.util.LogUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by dy on 2016-11-14.
 */

public class UpLoadUtils {

    static final int TIME_OUT = 10*1000;
    static final String CHARSET = "UTF-8";

    public static int upLoadFile(File file, String requestURL){
        int res = 0;

        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--";
        String LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart.from-data";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(TIME_OUT);
            httpURLConnection.setConnectTimeout(TIME_OUT);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Charset", CHARSET);
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-type", CONTENT_TYPE+";boundary="+BOUNDARY);

            if (file != null){
                LogUtils.i("upload start");
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(PREFIX);
                stringBuffer.append(BOUNDARY);
                stringBuffer.append(LINE_END);

                stringBuffer.append("Content-Disposition: from-data=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
                stringBuffer.append("Content-Type: application/octet-stream; Charset=" + CHARSET + LINE_END);
                stringBuffer.append(LINE_END);//空白行

                dataOutputStream.write(stringBuffer.toString().getBytes());
                InputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;

                while ((len = inputStream.read(bytes)) != -1){
                    dataOutputStream.write(bytes, 0, len);
                    LogUtils.i("upload bytes: "+len);
                }
                inputStream.close();
                dataOutputStream.write(LINE_END.getBytes());
                String end = PREFIX + BOUNDARY + PREFIX + LINE_END;
                dataOutputStream.write(end.getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();
                res = httpURLConnection.getResponseCode();
                if (res == 200){
                    LogUtils.i("upload success");

                }else{
                    LogUtils.i("upload fail");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }
}
