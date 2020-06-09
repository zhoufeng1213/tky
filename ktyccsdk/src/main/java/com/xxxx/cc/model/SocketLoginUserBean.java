package com.xxxx.cc.model;

public class SocketLoginUserBean {
    /*
   {"orgId":"b3c7d259aee544f59e7c1a3315051da2","domain":"ktsz.com","agentNo":"100004","userId":"90afed8fa9b64e598513500058e1140f"}

     */
    private String orgId;
    private String domain;
    private String agentNo;
    private String userId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
