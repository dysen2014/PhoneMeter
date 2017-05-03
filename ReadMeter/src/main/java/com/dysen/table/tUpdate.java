package com.dysen.table;

import java.math.BigDecimal;

/**
 * Created by dy on 2016-09-27.
 */

public class tUpdate {

    private String areaId;
    private String meterID;
    private String userName;
    private String used;// 用量
    private String timeAccount;
    private BigDecimal accountFeeAll;
    private int readStart;
    private int readEnd;
//    private String ContactAddr;
    private String code;
    private String phone;
    private String ContactTel;

    private String statusRead;
    private String statusUpdate;
    private String readInfo;
    private int readEndTemp;
    private String readNumber;
    private String readName;
    private String amrID;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getMeterID() {
        return meterID;
    }

    public void setMeterID(String meterID) {
        this.meterID = meterID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getTimeAccount() {
        return timeAccount;
    }

    public void setTimeAccount(String timeAccount) {
        this.timeAccount = timeAccount;
    }

    public BigDecimal getAccountFeeAll() {
        return accountFeeAll;
    }

    public void setAccountFeeAll(BigDecimal accountFeeAll) {
        this.accountFeeAll = accountFeeAll;
    }

    public int getReadStart() {
        return readStart;
    }

    public void setReadStart(int readStart) {
        this.readStart = readStart;
    }

    public int getReadEnd() {
        return readEnd;
    }

    public void setReadEnd(int readEnd) {
        this.readEnd = readEnd;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactTel() {
        return ContactTel;
    }

    public void setContactTel(String contactTel) {
        ContactTel = contactTel;
    }

    public String getStatusRead() {
        return statusRead;
    }

    public void setStatusRead(String statusRead) {
        this.statusRead = statusRead;
    }

    public String getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(String statusUpdate) {
        this.statusUpdate = statusUpdate;
    }

    public String getReadInfo() {
        return readInfo;
    }

    public void setReadInfo(String readInfo) {
        this.readInfo = readInfo;
    }

    public int getReadEndTemp() {
        return readEndTemp;
    }

    public void setReadEndTemp(int readEndTemp) {
        this.readEndTemp = readEndTemp;
    }

    public String getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(String readNumber) {
        this.readNumber = readNumber;
    }

    public String getReadName() {
        return readName;
    }

    public void setReadName(String readName) {
        this.readName = readName;
    }

    public String getAmrID() {
        return amrID;
    }

    public void setAmrID(String amrID) {
        this.amrID = amrID;
    }
}
