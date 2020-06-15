package com.xxxx.cc.model;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2018/12/24
 * @moduleName
 */
public class UserBean {


    /**
     * ccServerProxy : voip4.ketianyun.com:15060
     * ccUserInfo : {"domain":"ktsz.com","extensionNo":"100011","extensionPassword":"123456789"}
     * fromUser : 100002
     * orgId : b3c7d259aee544f59e7c1a3315051da2
     * sitePermisson : ["cc"]
     * token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmYmQ4OWM3ZTM0OTc0YTM2YWE4ZTliOGQzMDMzOGM0NiIsImF1dGgiOiJST0xFX1RISVJEX1BBUlRZIiwiZXhwIjoxNTY0NjM2Nzk0fQ.yAHPDogUh1RtFaSzNeQhU6AyU5bWZoXjhNtciUCET3XUR-5th4MURFiluo_JlsmFdLjkLaUitoC25VL8asbI6Q
     * userId : fbd89c7e34974a36aa8e9b8d30338c46
     * userPermission : ["cc"]
     * username : test002@ketianyun.com
     */
    private static final String SIP_CALL="siteEnableAppSipCall";
    private static final String DOUBLE_CALL="siteEnableAppDoubleCall";
    private static final String SIM_CALL="siteEnableAppSimCall";
    private boolean[] siteEnables;
    private String ccServerProxy;
    private CcUserInfoBean ccUserInfo;
    private String fromUser;
    private String orgId;
    private String token;
    private String userId;
    private String username;
    private String mobile;
    private String email;
    private Integer siteEnableAppSipCall;
    private List<String> sitePermisson;
    private List<String> userPermission;
    private String uname;
    public Integer getSiteEnableAppSipCall() {
        return siteEnableAppSipCall;
    }

    public boolean[] getSiteEnables() {
        siteEnables =new boolean[3];
        if(sitePermisson!=null&&!sitePermisson.isEmpty()){
            for(String s:sitePermisson){
                if(s!=null&&s.equals(SIP_CALL)){
                    siteEnables[0]=true;
                }else if(s!=null&&s.equals(DOUBLE_CALL)){
                    siteEnables[1]=true;
                }else if(s!=null&&s.equals(SIM_CALL)){
                    siteEnables[2]=true;
                }
            }
        }

        return siteEnables;
    }



    public void setSiteEnableAppSipCall(Integer siteEnableAppSipCall) {
        this.siteEnableAppSipCall = siteEnableAppSipCall;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getCcServerProxy() {
        return ccServerProxy;
    }

    public void setCcServerProxy(String ccServerProxy) {
        this.ccServerProxy = ccServerProxy;
    }

    public CcUserInfoBean getCcUserInfo() {
        return ccUserInfo;
    }

    public void setCcUserInfo(CcUserInfoBean ccUserInfo) {
        this.ccUserInfo = ccUserInfo;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getSitePermisson() {
        return sitePermisson;
    }

    public void setSitePermisson(List<String> sitePermisson) {
        this.sitePermisson = sitePermisson;
    }

    public List<String> getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(List<String> userPermission) {
        this.userPermission = userPermission;
    }

    public static class CcUserInfoBean {
        /**
         * domain : ktsz.com
         * extensionNo : 100011
         * extensionPassword : 123456789
         */

        private String domain;
        private String extensionNo;
        private String extensionPassword;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getExtensionNo() {
            return extensionNo;
        }

        public void setExtensionNo(String extensionNo) {
            this.extensionNo = extensionNo;
        }

        public String getExtensionPassword() {
            return extensionPassword;
        }

        public void setExtensionPassword(String extensionPassword) {
            this.extensionPassword = extensionPassword;
        }
    }
}
