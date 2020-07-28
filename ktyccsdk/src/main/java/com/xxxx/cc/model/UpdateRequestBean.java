package com.xxxx.cc.model;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class UpdateRequestBean {


    /**
     {
     "name": "string",
     "phone": "string",
     "password": "string",
     "code": "string"
     }
     */


    private String uname;
    private String mobile;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
