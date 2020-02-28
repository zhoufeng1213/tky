package com.xxxx.cc.model;


public class CommunicationRecordBean {

    private Data data;
    private String message;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class Data {
        public String calldetailId;
        public String communication;
        public String contactid;
        public String createTime;
        public String id;
        public String mobile;
        public String orgi;
        public String uname;
        public String userid;
        public String username;


        public String getCalldetailId() {
            return calldetailId;
        }

        public void setCalldetailId(String calldetailId) {
            this.calldetailId = calldetailId;
        }

        public String getCommunication() {
            return communication;
        }

        public void setCommunication(String communication) {
            this.communication = communication;
        }

        public String getContactid() {
            return contactid;
        }

        public void setContactid(String contactid) {
            this.contactid = contactid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getOrgi() {
            return orgi;
        }

        public void setOrgi(String orgi) {
            this.orgi = orgi;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }


    }
}
