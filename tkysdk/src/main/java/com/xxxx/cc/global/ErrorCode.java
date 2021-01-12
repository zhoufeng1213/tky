package com.xxxx.cc.global;

public class ErrorCode {
    public static final int SUCCESS = 0;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int NOT_NET_ERROR = 45006;//网络连接失败，请检查网络
    public static final int REGISTER_ERROR = 45007;
    public static final int USER_NAME_IS_EMPTY = 45000;
    public static final int PASSWORD_IS_EMPTY = 45001;
    public static final int USERNAME_DOESNT_EXIST = 45002;
    public static final int PASSWORD_ISNT_CORRECT = 45003;
    public static final int USER_HASBEEN_DISABLED = 45004;
    public static final int USER_HASBEEN_DELETED = 45005;


}
