package com.xxxx.cc.global;

/**
 * @author zhoufeng
 * @date 2018/12/24
 * @moduleName
 */
public class HttpRequest {

    public static class Login {
        public static String loginUser = "/sdk/api/v1/authentication";
    }

    public static class CallHistory {
        public static String callHistory = "/sdk/api/v1/callHistory";
        public static String currentCalls = "/sdk/api/v1/callHistory/currentCalls/";
    }

    public static class Contant {
        public static String mineContacts = "/sdk/api/v1/contacts/mine";
        public static String communicationRecord = "/sdk/api/v1/callHistory/commRecords";
        public static String update = "/sdk/api/v1/contacts/update/";
        public static String back = "/sdk/api/v1/contacts/back/";
        public static String saveSummary = "/sdk/api/v1/summary/save";
        public static String selfdefinedContacts = "/sdk/api/v1/selfdefined/contacts/all";

    }

    public static class Version {
        public static String checkVersion = "/sdk/api/v1/version/android/";
    }

    public static String makecall = "/sdk/api/v1/makecall/external";
}
