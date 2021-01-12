package com.xxxx.cc.model;
/*
* {
  "callId": "string",
  "ani": "string",
  "dnis": "string",
  "area": "string",
  "userId": "string",
  "agentUserName": "string",
  "orgId": "string",
  "direction": "string",
  "billingInSec": 0,
  "waitInBillingSec": 0,
  "duration": 0,
  "chargeMin": 0,
  "createTime": "2020-06-11T06:31:07.495Z",
  "bridgeTime": "2020-06-11T06:31:07.495Z",
  "hangupTime": "2020-06-11T06:31:07.495Z",
  "contactName": "string",
  "appName": "string"
}
* */
public class CallHistoryBean {
              private String callId;
              private String ani;
              private String dnis;
              private String area;
              private String userId;
              private String agentUserName;
              private String orgId;
              private String direction;
              private int billingInSec;
              private int waitInBillingSec;
              private int duration;
              private int chargeMin;
              private String createTime;
              private String bridgeTime;
              private String hangupTime;
              private String contactName;
              private String appName;

    public CallHistoryBean() {
        this.appName="android";
        this.direction="OUTBOUND";
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getAni() {
        return ani;
    }

    public void setAni(String ani) {
        this.ani = ani;
    }

    public String getDnis() {
        return dnis;
    }

    public void setDnis(String dnis) {
        this.dnis = dnis;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgentUserName() {
        return agentUserName;
    }

    public void setAgentUserName(String agentUserName) {
        this.agentUserName = agentUserName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getBillingInSec() {
        return billingInSec;
    }

    public void setBillingInSec(int billingInSec) {
        this.billingInSec = billingInSec;
    }

    public int getWaitInBillingSec() {
        return waitInBillingSec;
    }

    public void setWaitInBillingSec(int waitInBillingSec) {
        this.waitInBillingSec = waitInBillingSec;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getChargeMin() {
        return chargeMin;
    }

    public void setChargeMin(int chargeMin) {
        this.chargeMin = chargeMin;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBridgeTime() {
        return bridgeTime;
    }

    public void setBridgeTime(String bridgeTime) {
        this.bridgeTime = bridgeTime;
    }

    public String getHangupTime() {
        return hangupTime;
    }

    public void setHangupTime(String hangupTime) {
        this.hangupTime = hangupTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
