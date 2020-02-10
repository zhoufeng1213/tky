package com.xxxx.cc.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * @author zhoufeng
 * @date 2020/2/5
 * @moduleName
 */
@Entity
public class QueryCustomPersonBean {
    /**
     * ckind : 客户
     * creater : 7a12c9203f6640ec897f7685dcd1e3e4
     * createrName : 李婷
     * createtime : 2019-04-19 19:48:08
     * custom_6 : 1
     * custom_8 : bbbbbb
     * datastatus : true
     * email :
     * id : ceae4815f4f94d5ca51159118eff9da2
     * mobile : 18551200225
     * name : summer2
     * number_1 : 0
     * number_2 : 0
     * number_3 : 0
     * number_4 : 0
     * number_5 : 0
     * organ : 2c916ee06a67cd93016a871c20aa00ed
     * orgi : b3c7d259aee544f59e7c1a3315051da2
     * owner : gctest
     * ownerName : gctest
     * phone :
     * pinyin : s
     * realMobileNumber : 18551200225
     * statusInSeas : 0
     * time_1 : 0
     * time_2 : 0
     * time_3 : 0
     * time_4 : 0
     * time_5 : 0
     * updatetime : 2020-02-04 21:48:50
     * updateusername : gctest
     * username : test001@ketianyun.com
     * address :
     * company :
     * custom_1 :
     * custom_3 :
     * custom_4 :
     * custom_5 :
     * custom_7 :
     * custom_9 :
     * department :
     * duty :
     * gender : -1
     * memo :
     * shares : no
     */

    @Id(autoincrement = true)
    private Long queryCustomId;

    private String ckind;
    private String creater;
    private String createrName;
    private String createtime;
    private boolean datastatus;
    private String email;
    @Index(unique = true)
    private String id;
    private String mobile;
    private String name;
    private String organ;
    private String orgi;
    private String owner;
    private String ownerName;
    private String phone;
    private String pinyin;
    private String realMobileNumber;
    private int statusInSeas;
    private String updatetime;
    private String updateusername;
    private String username;
    private String address;
    private String company;
    private String department;
    private String duty;
    private String gender;
    private String memo;
    private String shares;
    private String letters;
    private String displayNameSpelling;

    @Generated(hash = 1490741814)
    public QueryCustomPersonBean(Long queryCustomId, String ckind, String creater,
            String createrName, String createtime, boolean datastatus,
            String email, String id, String mobile, String name, String organ,
            String orgi, String owner, String ownerName, String phone,
            String pinyin, String realMobileNumber, int statusInSeas,
            String updatetime, String updateusername, String username,
            String address, String company, String department, String duty,
            String gender, String memo, String shares, String letters,
            String displayNameSpelling) {
        this.queryCustomId = queryCustomId;
        this.ckind = ckind;
        this.creater = creater;
        this.createrName = createrName;
        this.createtime = createtime;
        this.datastatus = datastatus;
        this.email = email;
        this.id = id;
        this.mobile = mobile;
        this.name = name;
        this.organ = organ;
        this.orgi = orgi;
        this.owner = owner;
        this.ownerName = ownerName;
        this.phone = phone;
        this.pinyin = pinyin;
        this.realMobileNumber = realMobileNumber;
        this.statusInSeas = statusInSeas;
        this.updatetime = updatetime;
        this.updateusername = updateusername;
        this.username = username;
        this.address = address;
        this.company = company;
        this.department = department;
        this.duty = duty;
        this.gender = gender;
        this.memo = memo;
        this.shares = shares;
        this.letters = letters;
        this.displayNameSpelling = displayNameSpelling;
    }

    @Generated(hash = 1961341732)
    public QueryCustomPersonBean() {
    }

    public String getCkind() {
        return ckind;
    }

    public void setCkind(String ckind) {
        this.ckind = ckind;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }


    public boolean isDatastatus() {
        return datastatus;
    }

    public void setDatastatus(boolean datastatus) {
        this.datastatus = datastatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ;
    }

    public String getOrgi() {
        return orgi;
    }

    public void setOrgi(String orgi) {
        this.orgi = orgi;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getRealMobileNumber() {
        return realMobileNumber;
    }

    public void setRealMobileNumber(String realMobileNumber) {
        this.realMobileNumber = realMobileNumber;
    }

    public int getStatusInSeas() {
        return statusInSeas;
    }

    public void setStatusInSeas(int statusInSeas) {
        this.statusInSeas = statusInSeas;
    }


    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateusername() {
        return updateusername;
    }

    public void setUpdateusername(String updateusername) {
        this.updateusername = updateusername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public boolean getDatastatus() {
        return this.datastatus;
    }

    public Long getQueryCustomId() {
        return this.queryCustomId;
    }

    public void setQueryCustomId(Long queryCustomId) {
        this.queryCustomId = queryCustomId;
    }

    public String getDisplayNameSpelling() {
        return this.displayNameSpelling;
    }

    public void setDisplayNameSpelling(String displayNameSpelling) {
        this.displayNameSpelling = displayNameSpelling;
    }

    public String getLetters() {
        return this.letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }
}

