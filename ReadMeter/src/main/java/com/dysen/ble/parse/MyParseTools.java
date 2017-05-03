package com.dysen.ble.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * sen dy 2015-1-23 下午3:02:56
 */
public class MyParseTools {

    public static String meterStat1Info(long meterStat1) {
        List<String> stat1 = new ArrayList<String>();
        int mStat1 = 0;

        if (meterStat1 == 0) {
            meterStat1 = 1;
        }
        mStat1 = (int) (Math.log(meterStat1) / Math.log(2));
        stat1.add("强干扰");
        stat1.add("拆卸");
        stat1.add("阀故障");
        stat1.add("关阀0");
        stat1.add("开阀1");
        stat1.add("流量异常");
        stat1.add("正常");
        stat1.add("泄漏报警");
        return stat1.get(mStat1);
    }

    public static String meterStat2Info(long meterStat2) {

        List<String> stat2 = new ArrayList<String>();
        if (meterStat2 >= 32) {
			/*
			 * B6...b5: 锂电池状态 0： 电池彻底坏；1：电压低，激活无效；2：有电压，激活无效3：激活中，结果不定，（每天激活1次）
			 * 　　 4：有电压，激活有效，（2天激活1次）5：电压饱满，状态， （3天激活1次）　6：电源过高，异常 b4,b3:碱性电池状态
			 * 　　 00：无电池 01：电压低 02：电压合格 03：电池高 B3:强制中继* b2:WAN智能中继* b1:人工智能中继
			 * b0:中继(预留)
			 */
            meterStat2 /= 32;
            stat2.add("电池彻底坏");
            stat2.add("电压低，激活无效");
            stat2.add("有电压，激活无效");
            stat2.add("激活中，结果不定");
            stat2.add("有电压，激活有效");
            stat2.add("电压饱满，状态");
            stat2.add("电源过高，异常");
        } else if (meterStat2 >= 8) {

            meterStat2 /= 8;
            stat2.add("无电池	");
            stat2.add("电压低");
            stat2.add("电压正常");
            stat2.add("电压高");
        } else {

            meterStat2 /= 2;
            stat2.add("中继(预留)");
            stat2.add("固定中继");
            stat2.add("WAN智能中继");
            stat2.add("强制中继");
        }
        return stat2.get((int)meterStat2);
    }

    public static double sfactorInfo(long scaling_factor) {
        ArrayList<Double> sfactor = new ArrayList<Double>();
        sfactor.add(0.0001);
        sfactor.add(0.001);
        sfactor.add(0.01);
        sfactor.add(0.1);
        sfactor.add(1.0);
        sfactor.add(10.0);
        sfactor.add(100.0);
        sfactor.add(1000.0);

        double dscaling_factor = 0;
        switch ((int)scaling_factor) {

            case 0:
                dscaling_factor = 0.0001;
                break;
            case 1:
                dscaling_factor = 0.001;
                break;
            case 2:
                dscaling_factor = 0.01;
                break;
            case 3:
                dscaling_factor = 0.1;
                break;
            case 4:
                dscaling_factor = 1.0;
                break;
            case 5:
                dscaling_factor = 10.0;
                break;
            case 6:
                dscaling_factor = 100.0;
                break;
            case 7:
                dscaling_factor = 1000.0;
                break;
        }
        return dscaling_factor;
    }

    public static String meterStyInfo(long meterSty) {

        String strSty = "";
        switch ((int)meterSty) {

            // 0:水；1：燃气；2：电；3：热水表；4：热能表；5:燃气流量计;...15:其他
            // String[] strSty2 = {"水表", "燃气表", "电表", "热水表", "热能表",
            // "燃气流量表", "", "煤气表", "", ""};
            case 0:
                strSty = "水表";
                break;
            case 1:
                strSty = "燃气表";
                break;
            case 2:
                strSty = "电表";
                break;
            case 3:
                strSty = "热水表";
                break;
            case 4:
                strSty = "热能表";
                break;
            case 5:
                strSty = "燃气流量表";
                break;
            case 6:
                strSty = "";
                break;
            case 7:
                strSty = "煤气表";
                break;
            case 15:
                strSty = "其它";
                break;
        }
        return strSty;
    }
}
