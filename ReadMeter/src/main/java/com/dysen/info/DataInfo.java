package com.dysen.info;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataInfo implements Serializable {
	private boolean success;
	private String msg = "";
	private String o = "";
	private Object obj = null;

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
