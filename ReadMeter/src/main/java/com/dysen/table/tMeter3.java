package com.dysen.table;

/**
 * 作者：沈迪 [dysen] on 2016-03-18 14:08.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：表数据
 */
public class tMeter3 {

    int id;
    String readMtArea, readMtName, readMtDate, readMtStatus, readMtAddr;// readMtPercent;
    long readMtSum, readMtCompleted;
    String userName, userPhone, userAddr, info;
    long userNum, mtLast, mtThis;// mMeterVolume;
    long meterNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReadMtArea() {
        return readMtArea;
    }

    public void setReadMtArea(String readMtArea) {
        this.readMtArea = readMtArea;
    }

    public String getReadMtName() {
        return readMtName;
    }

    public void setReadMtName(String readMtName) {
        this.readMtName = readMtName;
    }

    public String getReadMtDate() {
        return readMtDate;
    }

    public void setReadMtDate(String readMtDate) {
        this.readMtDate = readMtDate;
    }

    public String getReadMtStatus() {
        return readMtStatus;
    }

    public void setReadMtStatus(String readMtStatus) {
        this.readMtStatus = readMtStatus;
    }

    public String getReadMtAddr() {
        return readMtAddr;
    }

    public void setReadMtAddr(String readMtAddr) {
        this.readMtAddr = readMtAddr;
    }

    public long getReadMtSum() {
        return readMtSum;
    }

    public void setReadMtSum(long readMtSum) {
        this.readMtSum = readMtSum;
    }

    public long getReadMtCompleted() {
        return readMtCompleted;
    }

    public void setReadMtCompleted(long readMtCompleted) {
        this.readMtCompleted = readMtCompleted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getUserNum() {
        return userNum;
    }

    public void setUserNum(long userNum) {
        this.userNum = userNum;
    }

    public long getMtLast() {
        return mtLast;
    }

    public void setMtLast(long mLast) {
        this.mtLast = mLast;
    }

    public long getMtThis() {
        return mtThis;
    }

    public void setMtThis(long mThis) {
        this.mtThis = mThis;
    }

    public long getMeterNum() {
        return meterNum;
    }

    public void setMeterNum(long meterNum) {
        this.meterNum = meterNum;
    }
}
