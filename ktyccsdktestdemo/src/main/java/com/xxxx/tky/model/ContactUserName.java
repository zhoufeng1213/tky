package com.xxxx.tky.model;

import java.io.Serializable;

/**
 * @author zhoufeng
 * @date 2020/2/2
 * @moduleName
 */
public class ContactUserName implements Serializable {
    private String userId;
    private String name;
    private String phoneNumber;
    private String letters;
    private String displayNameSpelling;

    public ContactUserName(String userId, String name, String phoneNumber, String letters, String displayNameSpelling) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.letters = letters;
        this.displayNameSpelling = displayNameSpelling;
    }

    public String getDisplayNameSpelling() {
        return displayNameSpelling;
    }

    public void setDisplayNameSpelling(String displayNameSpelling) {
        this.displayNameSpelling = displayNameSpelling;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }
}
