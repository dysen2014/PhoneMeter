package com.dysen.myUtil.httpUtil;

/**
 * 作者：沈迪 [dysen] on 2016-04-11 12:26.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class DownloadData {
    static String data = "";

    public static void main(String[] args) {
        System.out.println("***1***\n"+dlBook("http://duzhaohui.f3322.net:80/local.json"));
        System.out.println("***2***\n"+dlBook2("http://duzhaohui.f3322.net:80/tMeter.json"));

    }

    /**
     * 下载数据
     */
    public static String dlBook(final String url) {//"http://duzhaohui.f3322.net:80/local.json"

        new Thread(new Runnable() {
            @Override
            public void run() {
                String t = HttpRequest.sendPost(url, "");
                data = t;
            }
        }).start();
        return data;
    }

    public static String dlBook2(String url) {//"http://duzhaohui.f3322.net:80/local.json"

        String t = HttpRequest.sendPost(url, "");

        return t.length() > 0 ? t : "";
    }
}
