package com.xxxx.tky.model;

/**
 * @author zhoufeng
 * @date 2020/2/9
 * @moduleName
 */
public class SaveSummaryBean {
   private String calldetailId;
    private String contactid;
    private String phonenumber;
    private String  summary;


    public String getCalldetailId() {
        return calldetailId;
    }

    public void setCalldetailId(String calldetailId) {
        this.calldetailId = calldetailId;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
