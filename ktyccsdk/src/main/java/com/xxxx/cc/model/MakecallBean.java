package com.xxxx.cc.model;

/**
 * @author xiuli.liu
 * @date 2020/2/9
 * @moduleName
 */
public class MakecallBean {
    private String caller;
    private String callee;
    private String name;
    private String appname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCallee() {
        return callee;
    }

    public void setCallee(String callee) {
        this.callee = callee;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }


}
