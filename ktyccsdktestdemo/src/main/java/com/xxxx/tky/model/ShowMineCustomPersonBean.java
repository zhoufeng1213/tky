package com.xxxx.tky.model;

/**
 * @author zhoufeng
 * @date 2020/2/8
 * @moduleName
 */
public class ShowMineCustomPersonBean {
    private String id;
    private String name;
    private String phone;
    private String desc;
    private String updateTime;
    private String letters;
    private String displayNameSpelling;


    public ShowMineCustomPersonBean(String id, String name, String phone, String desc, String updateTime, String letters, String displayNameSpelling) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.desc = desc;
        this.updateTime = updateTime;
        this.letters = letters;
        this.displayNameSpelling = displayNameSpelling;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }

    public String getDisplayNameSpelling() {
        return displayNameSpelling;
    }

    public void setDisplayNameSpelling(String displayNameSpelling) {
        this.displayNameSpelling = displayNameSpelling;
    }
}
