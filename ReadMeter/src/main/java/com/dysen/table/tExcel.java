package com.dysen.table;

/**
 * Created by dy on 2016-09-28.
 */

public class tExcel {

    Integer id;
    Integer readStart, readEnd;
    String meterId, amrId, userName, month, readTime, addr, readType, readName, readNumber, readInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getAmrId() {
        return amrId;
    }

    public void setAmrId(String amrId) {
        this.amrId = amrId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public Integer getReadStart() {
        return readStart;
    }

    public void setReadStart(Integer readStart) {
        this.readStart = readStart;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getReadEnd() {
        return readEnd;
    }

    public void setReadEnd(Integer readEnd) {
        this.readEnd = readEnd;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }

    public String getReadName() {
        return readName;
    }

    public void setReadName(String readName) {
        this.readName = readName;
    }

    public String getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(String readNumber) {
        this.readNumber = readNumber;
    }

    public String getReadInfo() {
        return readInfo;
    }

    public void setReadInfo(String readInfo) {
        this.readInfo = readInfo;
    }
}
