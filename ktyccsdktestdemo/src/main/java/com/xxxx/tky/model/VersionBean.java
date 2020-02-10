package com.xxxx.tky.model;

/**
 * @author zhoufeng
 * @date 2020/2/8
 * @moduleName
 */
public class VersionBean {

    /**
     * comment :
     * downloadUrl : https://www.ketianyun.com/callcenter/tky/app/download/android/tky.apk
     * message : 已经是最新版本
     * needForceUpdate : false
     * needUpdate : false
     * newVersion :
     */

    private String comment;
    private String downloadUrl;
    private String message;
    private boolean needForceUpdate;
    private boolean needUpdate;
    private String newVersion;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNeedForceUpdate() {
        return needForceUpdate;
    }

    public void setNeedForceUpdate(boolean needForceUpdate) {
        this.needForceUpdate = needForceUpdate;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
}
