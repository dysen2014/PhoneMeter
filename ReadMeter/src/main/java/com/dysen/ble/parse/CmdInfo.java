package com.dysen.ble.parse;

/**
 * sen dy 2014-12-2 上午11:06:21
 */
public class CmdInfo {

	int pkgId;// 包号
	String netId;// 网号
	int funcId1;// 主功能号
	int bDevice;// 从机发
	int dataLen;// 字节长
	int funcId2;// 次功能号
	int reponseNum;// 应答命令记录数

	private String srcString;

	public String getSrcString() {
		return srcString;
	}

	public void setSrcString(String srcString) {
		this.srcString = srcString;
	}

	public int getPkgId() {
		return pkgId;
	}

	public void setPkgId(int pkgId) {
		this.pkgId = pkgId;
	}

	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}

	public int getFuncId1() {
		return funcId1;
	}

	public void setFuncId1(int funcId1) {
		this.funcId1 = funcId1;
	}

	public int getbDevice() {
		return bDevice;
	}

	public void setbDevice(int bDevice) {
		this.bDevice = bDevice;
	}

	public int getDataLen() {
		return dataLen;
	}

	public void setDataLen(int dataLen) {
		this.dataLen = dataLen;
	}

	public int getFuncId2() {
		return funcId2;
	}

	public void setFuncId2(int funcId2) {
		this.funcId2 = funcId2;
	}

	public int getReponseNum() {
		return reponseNum;
	}

	public void setReponseNum(int reponseNum) {
		this.reponseNum = reponseNum;
	}

}
