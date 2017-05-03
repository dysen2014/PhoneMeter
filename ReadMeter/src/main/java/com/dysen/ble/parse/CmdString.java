package com.dysen.ble.parse;

import android.util.Log;

import com.dysen.myUtil.MyStringConversion;
import com.dysen.myUtil.MyUtils;

/**
 * Created by dysen_000 on 2016-07-16 16:35.
 * Email：dysen@outlook.com
 * Info：
 */
public class CmdString {

    public static String sWakeUp = "3C00010000000000F90008080100000000000000854F";
    public static String sWakeUpLora = "3C00010000000000F90008080100000000000000854F";

    public static String sReadMeter = "3C0001000100000022000803EAAE01C8";
    public static String sReadMeterLora = "3C0001000100000024000803EAAE01CA";
    public static  String sReadCmdrLora;
    public static  String sSetCmdrLora;
    public static String sBaseRead = "3C00010001000000EA000803EAAE0190";
    public static String sSetConfig = "";
    public static String sReadConfig = "3C00000000000000F5000800FD";
    public static String sOpen ="";
    public static String sClose ="";
    public static String sSetSN;
    public static String sGetSN = "3C00000000000000FE00080006";
    public static String sSetNetId;



    /**
     * info 唤醒
     */
    public static String cmdWakeUp(String netId){

//        String sWakeUp = "3C00010000000000F90008080100000000000000854F";

        String sWakeUp = "00" + netId + "00000000F90008080100000000000000";
        sWakeUp = "3C" + sWakeUp + MyStringConversion.myInverseStr(MyUtils.crcDemo(sWakeUp), 4).toUpperCase();
        Log.i("dysen", sWakeUp);

        return sWakeUp;
    }

    /**
     * info 单抄
     */
    public static String cmdReadMeter(String netId, String meterId){

//        String sReadMeter = "3C0001000100000022000803EAAE01C8";

        String sReadMeter = "00" + netId + meterId + "22000803EAAE01";
        sReadMeter = "3C" + sReadMeter + MyUtils.HexSUM(sReadMeter);

        return sReadMeter;
    }

    /**
     * info 单抄 lora
     */
    public static String cmdReadMeterLora(String netId, String meterId){

//        String sReadMeter = "3C0001000100000022000803EAAE01C8";

        String sReadMeter = "00" + netId + meterId + "24000803EAAE01";
        sReadMeter = "3C" + sReadMeter + MyUtils.HexSUM(sReadMeter);

        return sReadMeter;
    }

    public static String cmdReadCmdrLora(String netId, String meterId){

//        String sReadMeter = "3C0001000100000022000803EAAE01C8";

        String sReadMeter = "00" + netId + meterId + "42000800";
        sReadMeter = "3C" + sReadMeter + MyUtils.HexSUM(sReadMeter);

        return sReadMeter;
    }

    public static String cmdSetCmdrLora(String netId, String meterId, String data){

//        String sReadMeter = "3C0001000100000022000803EAAE01C8";

        String sReadMeter = "00" + netId + meterId + "43000811"+data;
        sReadMeter = "3C" + sReadMeter + MyUtils.HexSUM(sReadMeter);

        return sReadMeter;
    }

    /**
     * info 基站抄表
     */
    public static String cmdBaseRead(String netId, String meterId){

//        String sBaseRead = "3C00010001000000EA000803EAAE0190";

        String sBaseRead = "00" + netId + meterId + "EA000803EAAE01";
        sBaseRead = "3C" + sBaseRead + MyUtils.HexSUM(sBaseRead);

        return sBaseRead;
    }

    /**
     * info 设无线配置
     */
    public static String cmdSetConfig(String frequency, String capacity, String rate){

        //设无线配置
        String sSetConfig = "00" + "0000" + "00000000" + "F400080B00"+ capacity + rate + "00" +frequency + "0001000601";

        //无线设置无线参数
//        String sSetConfig = "00" + "0100" + "01000000" + "F300080B00"+ capacity + rate + "00" +frequency + "0000000000";
        sSetConfig = "3C" + sSetConfig + MyStringConversion.myInverseStr(MyUtils.crcDemo(sSetConfig), 4).toUpperCase();

        Log.i("dysen", "set:"+sSetConfig);
        return sSetConfig;
    }

    /**
     * info 读无线配置
     */
    public static String cmdReadConfig(){

        String sReadConfig = "3C00000000000000F5000800FD";

        return sReadConfig;
    }

    /**
     * info 开阀
     */
    public static String cmdOpen(String netId, String meterId){

        String sOpen = "00" + netId + meterId + "2500080431323334";
        sOpen = "3C" + sOpen  + MyUtils.HexSUM(sOpen);

        return sOpen;
    }

    /**
     * info 关阀
     */
    public static String cmdClose(String netId, String meterId){

        String sClose = "00" + netId + meterId + "2600080431323334";
        sClose = "3C" + sClose + MyUtils.HexSUM(sClose);

        return sClose;
    }

    /**
     * info
     */
    public static String cmdSetSN(String netId, String meterId){

        String sSetSN = "00" + netId + meterId + "F8000800";
        sSetSN = "3C" + sSetSN + MyUtils.HexSUM(sSetSN);

        return sSetSN;
    }

    /**
     * info
     */
    public static String cmdGetSN(){

        String sGetSN = "3C00000000000000FE00080006";

        return sGetSN;
    }

    /**
     * info
     */
    public static String cmdSetNetId(){

        String sNetId = "";

        return sNetId;
    }

    /**
     * info
     */
//    public static String cmd(){
//
//    }

}
