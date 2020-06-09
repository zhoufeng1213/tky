package com.xxxx.cc.model;

import java.util.List;

public class SocketLoginResponseBean {
    /*
     {"code":200,"message":"login success.","data":{"orgId":"b3c7d259aee544f59e7c1a3315051da2","domain":"ktsz.com","agentNo":"100004","userId":"90afed8fa9b64e598513500058e1140f"}}

     */
    private int code;
    private String message;
    private SocketLoginUserBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SocketLoginUserBean getData() {
        return data;
    }

    public void setData(SocketLoginUserBean data) {
        this.data = data;
    }
}
