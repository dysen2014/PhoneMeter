package com.dysen.myUtil.adapter_util;

import com.dysen.table.tMeter3;
import com.dysen.myUtil.MyActivityTools;
import com.dysen.myUtil.PercentDemo;

import java.util.List;

/**
 *
 */
public class ContentItem {

    List<tMeter3> list ;
    tMeter3 meter;
    MyActivityTools myTools;

    String readMAddr2, readMStatus, readMDate2, percent;
    long sum, completed;
    /**
     * 表册
     * @param date 抄表月
     * @param status    状态
     * @param addr  地址
     * @param mSum  总户数
     * @param mCompleted 已抄数
     */
    public ContentItem(String date, String status, String addr, long mSum, long mCompleted) {
        readMAddr2 = addr;
        readMStatus = status;
        readMDate2 = date;
        sum = mSum;
        completed = mCompleted;
        percent = PercentDemo.getPercent(mCompleted, mSum);
        System.out.println("百分比："+percent);
    }

    String uViewName, uViewPhone, uViewAddr, uViewInfo;
    long uViewItem, uViewNum, uViewLast, uViewThis, uViewWaterVolume;

    /**
     * 用户浏览界面
     * @param mViewItem 第几户
     * @param mViewName 用户名
//     * @param mViewNum  用户编号
//     * @param mViewPhone    电话
     * @param mViewAddr 地址
     * @param mViewLast 上月止码
     * @param mViewThis 本月止码
//     * @param mViewInfo 详情
     *  mViewWaterVolume  水量
     */
    public ContentItem(long mViewItem, String mViewName, String mViewAddr, long mViewLast, long mViewThis, String mStatusRead){
        uViewItem = mViewItem;
        uViewName = mViewName;
        uViewAddr = mViewAddr;
        uViewLast = mViewLast;
        uViewThis = mViewThis;
        uViewInfo = mStatusRead;
        uViewWaterVolume = mViewThis - mViewLast;
    }

    String hDate, hPaymentStatus;
    long hInfo, hLateFees, hPrice, hLast, hThis, hWaterVolume, hWaterFees;

    /**
     * 用户历史缴费界面
     * @param mDate
     * @param mLast
     * @param mThis
     * @param mInfo
     * @param mWaterVolume
     * @param mPrice
     * @param mLateFees
     * @param mWaterFees
     * @param mPaymentStatus
     */
    public ContentItem(String mDate, long mLast, long mThis, long mInfo, long mWaterVolume, long mPrice, long mLateFees,
                       long mWaterFees, String mPaymentStatus){
        hDate = mDate;
        hLast = mLast;
        hThis = mThis;
        hInfo = mInfo;
        hWaterVolume = mThis - mLast;
//                mWaterVolume;
        hPrice = mPrice;
        hLateFees = mLateFees;
        hWaterFees = hWaterVolume * hPrice + hLateFees;
//                mWaterFees;
        hPaymentStatus = mPaymentStatus;
    }

    String sArea, sReadName;
    long meterNum;
//    myTools.delData(tMeter3.class, "id>0");
    /***/
    public ContentItem(String mArea, String mReadName, String date, String status, String addr, long mSum, long mCompleted, String mViewName, long mViewNum, String mViewPhone, String mViewAddr, long mViewLast,
                       long mViewThis, String mViewInfo, String mInfo2, long mMeterNum){
        myTools = new MyActivityTools();

        sArea = mArea;
        sReadName = mReadName;
        readMAddr2 = addr;
        readMStatus = status;
        readMDate2 = date;
        sum = mSum;
        completed = mCompleted;
        percent = PercentDemo.getPercent(mCompleted, mSum);
        uViewName = mViewName;
        uViewNum = mViewNum;
        uViewPhone = mViewPhone;
        uViewAddr = mViewAddr;
        uViewLast = mViewLast;
        uViewThis = mViewThis;
        uViewInfo = mViewInfo;
        uViewWaterVolume = mViewThis - mViewLast;
        meterNum = mMeterNum;
        meter = new tMeter3();

        meter.setReadMtArea(sArea);
        meter.setReadMtName(sReadName);
        meter.setReadMtDate(readMDate2);
        meter.setReadMtStatus(readMStatus);
        meter.setReadMtAddr(readMAddr2);
        meter.setReadMtSum(sum);
        meter.setReadMtCompleted(completed);
        meter.setUserName(uViewName);
        meter.setUserAddr(uViewAddr);
        meter.setUserNum(uViewNum);
        meter.setUserPhone(uViewPhone);
        meter.setMtLast(uViewLast);
        meter.setMtThis(uViewThis);
        meter.setInfo(uViewInfo);
//        meter.setmMeterVolume(uViewWaterVolume);
        meter.setMeterNum(meterNum);

        MyActivityTools.db.save(meter);
    }

    String mCode, mCodeName;
    public ContentItem(String code, String codeName){
        mCode = code;
        mCodeName = codeName;
    }
}
