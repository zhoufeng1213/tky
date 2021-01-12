package com.xxxx.cc.model;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class RegisterRequestBean {


    /**
     {
     "name": "string",
     "phone": "string",
     "password": "string",
     "code": "string"
     }
     */


    private String name;
    private String phone;
    private String password;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
