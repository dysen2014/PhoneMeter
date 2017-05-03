package com.dysen.table;

import java.util.List;

/**
 * 作者：沈迪 [dysen] on 2016-04-01 14:29.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：片区
 */
public class tBook {

    /**
     * code : 111
     * codeName : 东片1区IC卡私户
     * pid : 1
     */

    int id;
    private String code;
    private String codeName;
    private String pid;
    private List<tBook> childs;
    private String readNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<tBook> getChilds() {
        return childs;
    }

    public void setChilds(List<tBook> childs) {
        this.childs = childs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getReadNumber() {
        return readNumber;
    }

    public void setReadNumber(String readNumber) {
        this.readNumber = readNumber;
    }
}
