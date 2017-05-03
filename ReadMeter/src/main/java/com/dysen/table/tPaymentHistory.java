package com.dysen.table;

/**
 * Created by dysen_000 on 2016-06-04.
 * Info：
 */
public class tPaymentHistory {


    int id;
    /**
     * accountFeeAll : 94.85
     * meterID : 11114379
     * readEnd : 915
     * readStart : 882
     * timeAccount : 2015-04-01 12:43:36
     * used : 33
     * userName : 胡传英
     */

    private double accountFeeAll;
    private String meterID;
    private int readEnd;
    private int readStart;
    private String timeAccount;
    private String used;
    private String userName;
    private Integer status;


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public double getAccountFeeAll() {
        return accountFeeAll;
    }

    public void setAccountFeeAll(double accountFeeAll) {
        this.accountFeeAll = accountFeeAll;
    }

    public String getMeterID() {
        return meterID;
    }

    public void setMeterID(String meterID) {
        this.meterID = meterID;
    }

    public int getReadEnd() {
        return readEnd;
    }

    public void setReadEnd(int readEnd) {
        this.readEnd = readEnd;
    }

    public int getReadStart() {
        return readStart;
    }

    public void setReadStart(int readStart) {
        this.readStart = readStart;
    }

    public String getTimeAccount() {
        return timeAccount;
    }

    public void setTimeAccount(String timeAccount) {
        this.timeAccount = timeAccount;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
