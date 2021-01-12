package com.xxxx.cc.model;

import com.xxxx.cc.global.ErrorCode;

/**
 * @author zhoufeng
 */
public class BaseBean {


    /**
     * code : 0
     * data : {}
     * message : success
     */

    private int code;
    private Object data;
    private String message;

    public boolean isOk() {
        return code == ErrorCode.SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
