package com.dysen.table;

import java.math.BigDecimal;

/**
 * 作者：沈迪 [dysen] on 2016-03-18 14:08.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：表数据
 */
public class tMeter {

    Integer id;
    /**
     * accountFeeAll : 199.39
     * code : 111
     * contactAddr : 公安巷
     * meterID : 11110301
     * readEnd : 2970
     * readStart : 2843
     * timeAccount : 05-12-31 10:17:21
     * statusAccount : 0(未抄)
     * used : 127
     * userName : 张宏伟
     * statusRead : 读表状态
     * statusUpdate : 上传状态
     */

    private String areaId;
    private String meterID;
    private String userName;
    private String used;// 用量
    private String timeAccount;
    private BigDecimal accountFeeAll;
    private Integer readStart;
    private Integer readEnd;
    private String ContactAddr;
    private String code;
    private String phone;
    private String ContactTel;

    private String statusRead;
    private String statusUpdate;
    private String readInfo;
    private Integer readStartTemp;
    private Integer readEndTemp;
    private String readNumber;
    private String readName;
    private String amrID;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
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



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMeterID() {
        return meterID;
    }

    public void setMeterID(String meterID) {
        this.meterID = meterID;
    }

    public Integer getReadEnd() {
        return readEnd;
    }

    public void setReadEnd(Integer readEnd) {
        this.readEnd = readEnd;
    }

    public Integer getReadStart() {
        return readStart;
    }

    public void setReadStart(Integer readStart) {
        this.readStart = readStart;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeAccount() {
        return timeAccount;
    }

    public void setTimeAccount(String timeAccount) {
        this.timeAccount = timeAccount;
    }

    public void setAccountFeeAll(BigDecimal accountFeeAll) {
        this.accountFeeAll = accountFeeAll;
    }

    public BigDecimal getAccountFeeAll() {
        return accountFeeAll;
    }

    public String getContactAddr() {
        return ContactAddr;
    }

    public void setContactAddr(String contactAddr) {
        ContactAddr = contactAddr;
    }

    public String getStatusUpdate() {
        return statusUpdate;
    }

    public void setStatusUpdate(String statusUpdate) {
        this.statusUpdate = statusUpdate;
    }

    public String getStatusRead() {
        return statusRead;
    }

    public void setStatusRead(String statusAccount) {
        this.statusRead = statusAccount;
    }

    public String getReadInfo() {
        return readInfo;
    }

    public void setReadInfo(String readInfo) {
        this.readInfo = readInfo;
    }

    public Integer getReadStartTemp() {
        return readStartTemp;
    }

    public void setReadStartTemp(Integer readStartTemp) {
        this.readStartTemp = readStartTemp;
    }

    public Integer getReadEndTemp() {
        return readEndTemp;
    }

    public void setReadEndTemp(Integer readEndTemp) {
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
