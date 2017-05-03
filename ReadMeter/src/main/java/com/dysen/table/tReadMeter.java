package com.dysen.table;


import java.util.Date;

public class tReadMeter {

	int id;
	String meterId ;
	long long_meterEnd; // 读数
	double dscaling_factor = 0.0; // 比例因子
	// int scaling_factor = 0; // 比例因子
	String meterSty = ""; // 表类型
	double meterVoltage1, meterVoltage2; // 表电压1,2
	String meterStat1 = "", meterStat2 = "";// 表状态1，2
	long meterPrepaid; // 表预付费
	long meterStar; // 表底数
	long meterTemperature; // 表温度
	Date meterDate2;
	String meterDate, // 时间
			reserve; // 保留
	long concentratorId, netId;
	long userId;
	private String srcString;
	String remainingVolume;
	String lSn, lCornetId, lNetId, lMeterId, lAreaId, lRepeater;
	String setState;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSrcString() {
		return srcString;
	}

	public void setSrcString(String srcString) {
		this.srcString = srcString;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public tReadMeter() {

	}

	public long getNetId() {
		return netId;
	}

	public void setNetId(long netId) {
		this.netId = netId;
	}

	public String getMeterId() {
		return meterId;
	}

	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}

	public long getLong_meterEnd() {
		return long_meterEnd;
	}

	public void setLong_meterEnd(long long_meterEnd) {
		this.long_meterEnd = long_meterEnd;
	}

	public double getDscaling_factor() {
		return dscaling_factor;
	}

	public void setDscaling_factor(double dscaling_factor) {
		this.dscaling_factor = dscaling_factor;
	}

	public String getMeterSty() {
		return meterSty;
	}

	public void setMeterSty(String meterSty) {
		this.meterSty = meterSty;
	}

	public double getMeterVoltage1() {
		return meterVoltage1;
	}

	public void setMeterVoltage1(double meterVoltage1) {
		this.meterVoltage1 = meterVoltage1;
	}

	public double getMeterVoltage2() {
		return meterVoltage2;
	}

	public void setMeterVoltage2(double meterVoltage2) {
		this.meterVoltage2 = meterVoltage2;
	}

	public String getMeterStat1() {
		return meterStat1;
	}

	public void setMeterStat1(String meterStat1) {
		this.meterStat1 = meterStat1;
	}

	public String getMeterStat2() {
		return meterStat2;
	}

	public void setMeterStat2(String meterStat2) {
		this.meterStat2 = meterStat2;
	}

	public long getMeterPrepaid() {
		return meterPrepaid;
	}

	public void setMeterPrepaid(long meterPrepaid) {
		this.meterPrepaid = meterPrepaid;
	}

	public long getMeterStar() {
		return meterStar;
	}

	public void setMeterStar(long meterStar) {
		this.meterStar = meterStar;
	}

	public long getMeterTemperature() {
		return meterTemperature;
	}

	public void setMeterTemperature(long meterTemperature) {
		this.meterTemperature = meterTemperature;
	}

	public Date getMeterDate2() {
		return meterDate2;
	}

	public void setMeterDate2(Date meterDate2) {
		this.meterDate2 = meterDate2;
	}

	public long getConcentratorId() {
		return concentratorId;
	}

	public void setConcentratorId(long concentratorId) {
		this.concentratorId = concentratorId;
	}

	public String getMeterDate() {
		return meterDate;
	}

	public void setMeterDate(String meterDate) {
		this.meterDate = meterDate;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}


	/************************读配置 解析*************************************/
	long capacity, frequency;
	String rate;

	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	public long getFrequency() {
		return frequency;
	}

	public void setFrequency(long frequency) {
		this.frequency = frequency;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}
	/********************************************************************/

	// private Integer id;
	// private Short version; // 版本
	// private Timestamp readtime; // 读表时间
	// private String devSn; // 设备号
	// private Integer meterSn; // 表号
	// private String iniValue; // 初始值
	// private String remaingas; // 剩余气量
	// private String totalGas; // 全部气量
	// private String pressure; // 压力
	// private String temperature; // 温度
	// private String statusByte; // 状态位
	// private String status; // 状态
	// private Short pathCount; // 路径数
	// private String userId; // 用户ID
	// private Double rate; // 等级/价格
	// private String netNo; // 网号
	// private Timestamp meterClock; // 表时间
	// private Integer path0;
	// private Integer path1;
	// private Integer path2;
	// private Integer path3;
	// private Integer path4;
	// private Integer path5;
	// private Integer path6;
	// private Integer path7;
	// private Integer path8;
	// private Integer path9;
	// private Integer path10;
	// private Integer path11;
	// private Integer path12;
	// private Integer path13;
	// private Integer path14;
	// private Integer path15;
	// private Double vbat1;
	// private Double vbat2;

	public String getRemainingVolume() {
		return remainingVolume;
	}

	public void setRemainingVolume(String remainingVolume) {
		this.remainingVolume = remainingVolume;
	}


	public String getlSn() {
		return lSn;
	}

	public void setlSn(String lSn) {
		this.lSn = lSn;
	}

	public String getlCornetId() {
		return lCornetId;
	}

	public void setlCornetId(String lCornetId) {
		this.lCornetId = lCornetId;
	}

	public String getlNetId() {
		return lNetId;
	}

	public void setlNetId(String lNetId) {
		this.lNetId = lNetId;
	}

	public String getlMeterId() {
		return lMeterId;
	}

	public void setlMeterId(String lMeterId) {
		this.lMeterId = lMeterId;
	}

	public String getlAreaId() {
		return lAreaId;
	}

	public void setlAreaId(String lAreaId) {
		this.lAreaId = lAreaId;
	}

	public String getlRepeater() {
		return lRepeater;
	}

	public void setlRepeater(String lRepeater) {
		this.lRepeater = lRepeater;
	}

	public String getSetState() {
		return setState;
	}

	public void setSetState(String setState) {
		this.setState = setState;
	}
}
