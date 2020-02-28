package com.xxxx.cc.model;

/**
 * @author zhoufeng
 * @date 2020/2/8
 * @moduleName
 */
public class CommunicationRecordResponseBean {
    public CallDetailDTO callDetailDTO;
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

    public CallDetailDTO getCallDetailDTO() {
        return callDetailDTO;
    }

    public void setCallDetailDTO(CallDetailDTO callDetailDTO) {
        this.callDetailDTO = callDetailDTO;
    }

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


    /**
     * agentUnames : gctest
     * agentUserName : gctest
     * alegQuality : 0.0
     * ani : 100010
     * area : 江苏 苏州市
     * billingInSec : 82
     * blegQuality : 0.0
     * blegUuid : 56156268-e2af-40b5-8e66-76e206bff8eb
     * bridgeTime : 2020-02-08 11:24:07
     * bridged : false
     * chargeMin : 2
     * commRecoeds : Jeremy测试新增沟通记录
     * contactName : 123456
     * context : 200
     * createTime : 2020-02-08 11:24:00
     * direction : OUTBOUND
     * dnbr : 02759761050
     * dnis : 13861336511
     * durationInSec : 89
     * hangupCause : NORMAL_CLEARING
     * hangupTime : 2020-02-08 11:25:29
     * id : AXAi1LDUqpj8dL742W-J
     * ivrTime : 2020-02-08 11:24:00
     * lat : 0.0
     * lng : 0.0
     * orgId : b3c7d259aee544f59e7c1a3315051da2
     * profile : 172.17.238.94
     * recordFile : /s/pqMbcv7dCrvfOcN/download
     * reserved1 : 4a4405f8abf841d699c06139b69fdb78
     * reserved2 : 被叫挂机
     * reserved5 : /s/Km0zpdLUD9ek26n/download
     * satisfy : 0
     * transfered : false
     * transferee : false
     * updateTime : 2020-02-08 11:25:40
     * userId : 11a81f688c31477bbea7bf954bd3a7cf
     * uuid : 7dd3933b-0c80-495e-a525-37ec2f4d56b3
     * waitInBillingSec : 0
     * waitInIvrSec : 0
     * waitInQueueSec : 0
     * waitInSec : 7
     */

    public static class CallDetailDTO {
        private String agentUnames;
        private String agentUserName;
        private double alegQuality;
        private String ani;
        private String area;
        private int billingInSec;
        private double blegQuality;
        private String blegUuid;
        private String bridgeTime;
        private boolean bridged;
        private int chargeMin;
        private String contactName;
        private String context;
        private String createTime;
        private String direction;
        private String dnbr;
        private String dnis;
        private int durationInSec;
        private String hangupCause;
        private String hangupTime;
        private String id;
        private String ivrTime;
        private double lat;
        private double lng;
        private String orgId;
        private String profile;
        private String recordFile;
        private String reserved1;
        private String reserved2;
        private String reserved5;
        private int satisfy;
        private boolean transfered;
        private boolean transferee;
        private String updateTime;
        private String userId;
        private String uuid;
        private String callId;
        private int waitInBillingSec;
        private int waitInIvrSec;
        private int waitInQueueSec;
        private int waitInSec;

        public String getCallId() {
            return callId;
        }

        public void setCallId(String callId) {
            this.callId = callId;
        }

        public String getAgentUnames() {
            return agentUnames;
        }

        public void setAgentUnames(String agentUnames) {
            this.agentUnames = agentUnames;
        }

        public String getAgentUserName() {
            return agentUserName;
        }

        public void setAgentUserName(String agentUserName) {
            this.agentUserName = agentUserName;
        }

        public double getAlegQuality() {
            return alegQuality;
        }

        public void setAlegQuality(double alegQuality) {
            this.alegQuality = alegQuality;
        }

        public String getAni() {
            return ani;
        }

        public void setAni(String ani) {
            this.ani = ani;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int getBillingInSec() {
            return billingInSec;
        }

        public void setBillingInSec(int billingInSec) {
            this.billingInSec = billingInSec;
        }

        public double getBlegQuality() {
            return blegQuality;
        }

        public void setBlegQuality(double blegQuality) {
            this.blegQuality = blegQuality;
        }

        public String getBlegUuid() {
            return blegUuid;
        }

        public void setBlegUuid(String blegUuid) {
            this.blegUuid = blegUuid;
        }

        public String getBridgeTime() {
            return bridgeTime;
        }

        public void setBridgeTime(String bridgeTime) {
            this.bridgeTime = bridgeTime;
        }

        public boolean isBridged() {
            return bridged;
        }

        public void setBridged(boolean bridged) {
            this.bridged = bridged;
        }

        public int getChargeMin() {
            return chargeMin;
        }

        public void setChargeMin(int chargeMin) {
            this.chargeMin = chargeMin;
        }


        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public String getDnbr() {
            return dnbr;
        }

        public void setDnbr(String dnbr) {
            this.dnbr = dnbr;
        }

        public String getDnis() {
            return dnis;
        }

        public void setDnis(String dnis) {
            this.dnis = dnis;
        }

        public int getDurationInSec() {
            return durationInSec;
        }

        public void setDurationInSec(int durationInSec) {
            this.durationInSec = durationInSec;
        }

        public String getHangupCause() {
            return hangupCause;
        }

        public void setHangupCause(String hangupCause) {
            this.hangupCause = hangupCause;
        }

        public String getHangupTime() {
            return hangupTime;
        }

        public void setHangupTime(String hangupTime) {
            this.hangupTime = hangupTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIvrTime() {
            return ivrTime;
        }

        public void setIvrTime(String ivrTime) {
            this.ivrTime = ivrTime;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getRecordFile() {
            return recordFile;
        }

        public void setRecordFile(String recordFile) {
            this.recordFile = recordFile;
        }

        public String getReserved1() {
            return reserved1;
        }

        public void setReserved1(String reserved1) {
            this.reserved1 = reserved1;
        }

        public String getReserved2() {
            return reserved2;
        }

        public void setReserved2(String reserved2) {
            this.reserved2 = reserved2;
        }

        public String getReserved5() {
            return reserved5;
        }

        public void setReserved5(String reserved5) {
            this.reserved5 = reserved5;
        }

        public int getSatisfy() {
            return satisfy;
        }

        public void setSatisfy(int satisfy) {
            this.satisfy = satisfy;
        }

        public boolean isTransfered() {
            return transfered;
        }

        public void setTransfered(boolean transfered) {
            this.transfered = transfered;
        }

        public boolean isTransferee() {
            return transferee;
        }

        public void setTransferee(boolean transferee) {
            this.transferee = transferee;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getWaitInBillingSec() {
            return waitInBillingSec;
        }

        public void setWaitInBillingSec(int waitInBillingSec) {
            this.waitInBillingSec = waitInBillingSec;
        }

        public int getWaitInIvrSec() {
            return waitInIvrSec;
        }

        public void setWaitInIvrSec(int waitInIvrSec) {
            this.waitInIvrSec = waitInIvrSec;
        }

        public int getWaitInQueueSec() {
            return waitInQueueSec;
        }

        public void setWaitInQueueSec(int waitInQueueSec) {
            this.waitInQueueSec = waitInQueueSec;
        }

        public int getWaitInSec() {
            return waitInSec;
        }

        public void setWaitInSec(int waitInSec) {
            this.waitInSec = waitInSec;
        }
    }
}
