package com.dysen.table;

/**
 * 作者：沈迪 [dysen] on 2016-01-04 10:57.
 * 邮箱：dysen@outlook.com | dy.sen@qq.com
 * 描述：
 */
public class TempLogin {

    int id;
    /**
     * msg : 登陆成功！
     * obj : {"id":1,"logName":"111","name":"11","pw":"111","readNumber":0}
     * activity_select_mid_item : true
     */

    private String msg;
    /**
     * id : 1
     * logName : 111
     * name : 11
     * pw : 111
     * readNumber : 0
     */

    private ObjBean obj;
    private boolean success;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public static class ObjBean {
        private int id;
        private String logName;
        private String name;
        private String pw;
        private String readNumber;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLogName() {
            return logName;
        }

        public void setLogName(String logName) {
            this.logName = logName;
        }

        public String getName() {
            return name;
        }

        public void setName(String readName) {
            this.name = readName;
        }

        public String getPw() {
            return pw;
        }

        public void setPw(String pw) {
            this.pw = pw;
        }

        public String getReadNumber() {
            return readNumber;
        }

        public void setReadNumber(String readNumber) {
            this.readNumber = readNumber;
        }
    }
}
