package com.xxxx.cc.global;

/**
 * @author zhoufeng
 * @date 2018/12/24
 * @moduleName
 */
public class HttpRequest {

    public static class Login {
        public static String loginUser = "/sdk/api/v1/authentication";
        public static String postLoginUrl = "/sdk/api/v1/login";
    }

    public static class CallHistory {
        public static String callHistory = "/sdk/api/v1/callHistory";
        public static String currentCalls = "/sdk/api/v1/callHistory/currentCalls/";
        public static String calldetailWithCommRecords = "/sdk/api/v1/callHistory/calldetailWithCommRecords";
    }

    public static class Contant {
        public static String mineContacts = "/sdk/api/v1/contacts/mine";
        public static String communicationRecord = "/sdk/api/v1/callHistory/commRecords";
        public static String update = "/sdk/api/v1/contacts/update/";
        public static String updateAll = "/sdk/api/v1/contacts/updateAll/";
        public static String back = "/sdk/api/v1/contacts/back/";
        public static String saveSummary = "/sdk/api/v1/summary/save";
        public static String selfdefinedContacts = "/sdk/api/v1/selfdefined/contacts/all";
        public static String getOneCustom = "/sdk/api/v1/contacts/getOne/";
        public static String getSubItem = "/sdk/api/v1/selfdefined/contacts/item";
        public static String updateSummary = "/sdk/api/v1/summary/update/";
        public static String addCustom = "/sdk/api/v1/contacts/save";
    }

    public static class Version {
        public static String checkVersion = "/sdk/api/v1/version/android/";
    }

    public static String makecallExternal = "/sdk/api/v1/makecall/external";
    public static String makecallInternal = "/sdk/api/v1/makecall/internal";
    public static String uploadlog = "/sdk/api/v1/uploadlog/upload";
}

