package com.dysen.mylibrary.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dysen on 2015-10-22.
 */
public class MyIpConvert {

    /**
     *	dysen
     *	2015-10-22 下午3:01:49
     *	info: ip 字符串转 int
     * 如  192.168.0.186 ———>  -1174361920
     */
    public static int ip2Int(String ip) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ip);// 在给定主机名的情况下确定主机的 // IP
        // 址。
        byte[] bytes = address.getAddress();// 返回此 InetAddress 对象的原始 IP 地址
        int a, b, c, d;
        a = (bytes[0]);
        b = (bytes[1]);
        c = (bytes[2]);
        d = (bytes[3]);

        int result = ((d << 24) & 0xff000000) | ((c << 16) & 0xff0000)
                | ((b << 8) & 0xff00) | (a & 0xff);
        return result;
    }

    /**
     *	dysen
     *	2015-10-22 下午3:01:49
     *	info: int 转 ip 字符串
     * 如  -1174361920 ———>  192.168.0.186
     */
    public static String int2Ip(int ip) {

        try {
            byte[] bytes = new byte[4];
            bytes[0] = (byte) (0xff & ip);
            bytes[1] = (byte) ((0xff00 & ip) >> 8);
            bytes[2] = (byte) ((0xff0000 & ip) >> 16);
            bytes[3] = (byte) ((0xff000000 & ip) >> 24);

            return InetAddress.getByAddress(bytes).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }
}
