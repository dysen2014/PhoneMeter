package com.dysen.info;

/**
 * Login entity. @author MyEclipse Persistence Tools
 */

public class Plogin implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String pw;

	private String repw;

	public String getRepw() {
		return repw;
	}

	public void setRepw(String repw) {
		this.repw = repw;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

}