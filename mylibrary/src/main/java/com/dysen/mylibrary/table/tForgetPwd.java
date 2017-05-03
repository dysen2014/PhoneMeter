package com.dysen.mylibrary.table;

/**
 * Created by dysen_000 on 2016-06-21.
 * Info：
 */
public class tForgetPwd {

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * msg : 密码修改成功！
     * success : true
     */

    private String msg;
    private boolean success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
