package com.xxxx.cc.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
@Entity
public class ContentBean {
    /**
     * agentUnames : test002
     * agentUserName : test002@ketianyun.com
     * alegQuality : 0.0
     * ani : 100011
     * area : 江苏 苏州市
     * billingInSec : 0
     * blegQuality : 0.0
     * blegUuid : 8dd4383a-b2d1-4c01-bc28-33993ab14bc4
     * bridged : false
     * chargeMin : 0
     * contactName : 张三
     * createTime : 2019-08-02 14:13:00
     * daSampleCategory : 5
     * daSampleDetail : 您拨打的电话正在通话中
     * daSampleName : 正在通话中
     * daTone : sample
     * direction : OUTBOUND
     * dnbr : 057128093580
     * dnis : 13913136453
     * durationInSec : 12
     * hangupCause : NORMAL_CLEARING
     * hangupTime : 2019-08-02 14:13:12
     * id : 7cdf59eed0e7c3100a53717dd698eb6a
     * ivrTime : 2019-08-02 14:12:58
     * lat : 0.0
     * lng : 0.0
     * orgId : b3c7d259aee544f59e7c1a3315051da2
     * reserved1 : b3c7d259aee544f59e7c1a3315051da2
     * reserved2 : 主叫挂机
     * satisfy : 0
     * transfered : false
     * transferee : false
     * updateTime : 2019-08-02 14:13:13
     * userId : fbd89c7e34974a36aa8e9b8d30338c46
     * uuid : cb613408-1b2c-4a0f-bbb4-930219192073
     * waitInBillingSec : 0
     * waitInIvrSec : 0
     * waitInQueueSec : 0
     * waitInSec : 12
     */

    @Id(autoincrement = true)
    private Long contentId;

    private String agentUnames;
    private String agentUserName;
    private double alegQuality;
    private String ani;
    private String area;
    private int billingInSec;
    private double blegQuality;
    private String blegUuid;
    private boolean bridged;
    private int chargeMin;
    private String contactName;
    private String context;
    private String createTime;
    private String daSampleCategory;
    private String daSampleDetail;
    private String daSampleName;
    private String daTone;
    private String direction;
    private String dnbr;
    private String dnis;
    private int durationInSec;
    private String hangupCause;
    private String hangupTime;
    @Index(unique = true)
    private String id;
    private String ivrTime;
    private double lat;
    private double lng;
    private String orgId;
    private String reserved1;
    private String reserved2;
    private int satisfy;
    private boolean transfered;
    private boolean transferee;
    private String updateTime;
    private String userId;
    private String uuid;
    private String recordFile;
    private String reserved5;
    private int waitInBillingSec;
    private int waitInIvrSec;
    private int waitInQueueSec;
    private int waitInSec;
    private boolean isPlay = false;

    @Generated(hash = 1224615260)
    public ContentBean(Long contentId, String agentUnames, String agentUserName, double alegQuality,
                       String ani, String area, int billingInSec, double blegQuality, String blegUuid,
                       boolean bridged, int chargeMin, String contactName, String context, String createTime,
                       String daSampleCategory, String daSampleDetail, String daSampleName, String daTone,
                       String direction, String dnbr, String dnis, int durationInSec, String hangupCause,
                       String hangupTime, String id, String ivrTime, double lat, double lng, String orgId,
                       String reserved1, String reserved2, int satisfy, boolean transfered, boolean transferee,
                       String updateTime, String userId, String uuid, String recordFile, String reserved5,
                       int waitInBillingSec, int waitInIvrSec, int waitInQueueSec, int waitInSec, boolean isPlay) {
        this.contentId = contentId;
        this.agentUnames = agentUnames;
        this.agentUserName = agentUserName;
        this.alegQuality = alegQuality;
        this.ani = ani;
        this.area = area;
        this.billingInSec = billingInSec;
        this.blegQuality = blegQuality;
        this.blegUuid = blegUuid;
        this.bridged = bridged;
        this.chargeMin = chargeMin;
        this.contactName = contactName;
        this.context = context;
        this.createTime = createTime;
        this.daSampleCategory = daSampleCategory;
        this.daSampleDetail = daSampleDetail;
        this.daSampleName = daSampleName;
        this.daTone = daTone;
        this.direction = direction;
        this.dnbr = dnbr;
        this.dnis = dnis;
        this.durationInSec = durationInSec;
        this.hangupCause = hangupCause;
        this.hangupTime = hangupTime;
        this.id = id;
        this.ivrTime = ivrTime;
        this.lat = lat;
        this.lng = lng;
        this.orgId = orgId;
        this.reserved1 = reserved1;
        this.reserved2 = reserved2;
        this.satisfy = satisfy;
        this.transfered = transfered;
        this.transferee = transferee;
        this.updateTime = updateTime;
        this.userId = userId;
        this.uuid = uuid;
        this.recordFile = recordFile;
        this.reserved5 = reserved5;
        this.waitInBillingSec = waitInBillingSec;
        this.waitInIvrSec = waitInIvrSec;
        this.waitInQueueSec = waitInQueueSec;
        this.waitInSec = waitInSec;
        this.isPlay = isPlay;
    }

    @Generated(hash = 1643641106)
    public ContentBean() {
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDaSampleCategory() {
        return daSampleCategory;
    }

    public void setDaSampleCategory(String daSampleCategory) {
        this.daSampleCategory = daSampleCategory;
    }

    public String getDaSampleDetail() {
        return daSampleDetail;
    }

    public void setDaSampleDetail(String daSampleDetail) {
        this.daSampleDetail = daSampleDetail;
    }

    public String getDaSampleName() {
        return daSampleName;
    }

    public void setDaSampleName(String daSampleName) {
        this.daSampleName = daSampleName;
    }

    public String getDaTone() {
        return daTone;
    }

    public void setDaTone(String daTone) {
        this.daTone = daTone;
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

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile;
    }

    public String getReserved5() {
        return reserved5;
    }

    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }

    public boolean getTransferee() {
        return this.transferee;
    }

    public boolean getTransfered() {
        return this.transfered;
    }

    public boolean getBridged() {
        return this.bridged;
    }

    public Long getContentId() {
        return this.contentId;
    }


    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public boolean getIsPlay() {
        return this.isPlay;
    }

    public void setIsPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }
}
