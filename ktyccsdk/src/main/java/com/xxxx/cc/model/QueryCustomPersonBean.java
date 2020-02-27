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
     * custom_10:
     * department :
     * duty :
     * gender : -1
     * memo :
     * shares : no
     * cdate_1:2020-02-04
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
    private String number_1;
    private String number_2;
    private String number_3;
    private String number_4;
    private String number_5;
    private String time_1;
    private String time_2;
    private String time_3;
    private String time_4;
    private String time_5;
    private String custom_1;
    private String custom_2;
    private String custom_3;
    private String custom_4;
    private String custom_5;
    private String custom_6;
    private String custom_7;
    private String custom_8;
    private String custom_9;
    private String custom_10;
    private String custom_11;
    private String custom_12;
    private String custom_13;
    private String custom_14;
    private String custom_15;
    private String custom_16;
    private String custom_17;
    private String custom_18;
    private String custom_19;
    private String custom_20;
    private String custom_21;


    private String datetime_1;
    private String datetime_2;
    private String datetime_3;
    private String datetime_4;

    private String cdate_1;
    private String cdate_2;
    private String cdate_3;
    private String cdate_4;
    private String cdate_5;

    @Generated(hash = 1826468955)
    public QueryCustomPersonBean(Long queryCustomId, String ckind, String creater, String createrName, String createtime, boolean datastatus,
            String email, String id, String mobile, String name, String organ, String orgi, String owner, String ownerName, String phone,
            String pinyin, String realMobileNumber, int statusInSeas, String updatetime, String updateusername, String username,
            String address, String company, String department, String duty, String gender, String memo, String shares, String letters,
            String displayNameSpelling, String number_1, String number_2, String number_3, String number_4, String number_5, String time_1,
            String time_2, String time_3, String time_4, String time_5, String custom_1, String custom_2, String custom_3, String custom_4,
            String custom_5, String custom_6, String custom_7, String custom_8, String custom_9, String custom_10, String custom_11,
            String custom_12, String custom_13, String custom_14, String custom_15, String custom_16, String custom_17, String custom_18,
            String custom_19, String custom_20, String custom_21, String datetime_1, String datetime_2, String datetime_3, String datetime_4,
            String cdate_1, String cdate_2, String cdate_3, String cdate_4, String cdate_5) {
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
        this.number_1 = number_1;
        this.number_2 = number_2;
        this.number_3 = number_3;
        this.number_4 = number_4;
        this.number_5 = number_5;
        this.time_1 = time_1;
        this.time_2 = time_2;
        this.time_3 = time_3;
        this.time_4 = time_4;
        this.time_5 = time_5;
        this.custom_1 = custom_1;
        this.custom_2 = custom_2;
        this.custom_3 = custom_3;
        this.custom_4 = custom_4;
        this.custom_5 = custom_5;
        this.custom_6 = custom_6;
        this.custom_7 = custom_7;
        this.custom_8 = custom_8;
        this.custom_9 = custom_9;
        this.custom_10 = custom_10;
        this.custom_11 = custom_11;
        this.custom_12 = custom_12;
        this.custom_13 = custom_13;
        this.custom_14 = custom_14;
        this.custom_15 = custom_15;
        this.custom_16 = custom_16;
        this.custom_17 = custom_17;
        this.custom_18 = custom_18;
        this.custom_19 = custom_19;
        this.custom_20 = custom_20;
        this.custom_21 = custom_21;
        this.datetime_1 = datetime_1;
        this.datetime_2 = datetime_2;
        this.datetime_3 = datetime_3;
        this.datetime_4 = datetime_4;
        this.cdate_1 = cdate_1;
        this.cdate_2 = cdate_2;
        this.cdate_3 = cdate_3;
        this.cdate_4 = cdate_4;
        this.cdate_5 = cdate_5;
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

    public String getCustom_9() {
        return this.custom_9;
    }

    public void setCustom_9(String custom_9) {
        this.custom_9 = custom_9;
    }

    public String getCustom_8() {
        return this.custom_8;
    }

    public void setCustom_8(String custom_8) {
        this.custom_8 = custom_8;
    }

    public String getCustom_7() {
        return this.custom_7;
    }

    public void setCustom_7(String custom_7) {
        this.custom_7 = custom_7;
    }

    public String getCustom_6() {
        return this.custom_6;
    }

    public void setCustom_6(String custom_6) {
        this.custom_6 = custom_6;
    }

    public String getCustom_5() {
        return this.custom_5;
    }

    public void setCustom_5(String custom_5) {
        this.custom_5 = custom_5;
    }

    public String getCustom_4() {
        return this.custom_4;
    }

    public void setCustom_4(String custom_4) {
        this.custom_4 = custom_4;
    }

    public String getCustom_3() {
        return this.custom_3;
    }

    public void setCustom_3(String custom_3) {
        this.custom_3 = custom_3;
    }

    public String getCustom_2() {
        return this.custom_2;
    }

    public void setCustom_2(String custom_2) {
        this.custom_2 = custom_2;
    }

    public String getCustom_1() {
        return this.custom_1;
    }

    public void setCustom_1(String custom_1) {
        this.custom_1 = custom_1;
    }

    public String getTime_5() {
        return this.time_5;
    }

    public void setTime_5(String time_5) {
        this.time_5 = time_5;
    }

    public String getTime_4() {
        return this.time_4;
    }

    public void setTime_4(String time_4) {
        this.time_4 = time_4;
    }

    public String getTime_3() {
        return this.time_3;
    }

    public void setTime_3(String time_3) {
        this.time_3 = time_3;
    }

    public String getTime_2() {
        return this.time_2;
    }

    public void setTime_2(String time_2) {
        this.time_2 = time_2;
    }

    public String getTime_1() {
        return this.time_1;
    }

    public void setTime_1(String time_1) {
        this.time_1 = time_1;
    }

    public String getNumber_5() {
        return this.number_5;
    }

    public void setNumber_5(String number_5) {
        this.number_5 = number_5;
    }

    public String getNumber_4() {
        return this.number_4;
    }

    public void setNumber_4(String number_4) {
        this.number_4 = number_4;
    }

    public String getNumber_3() {
        return this.number_3;
    }

    public void setNumber_3(String number_3) {
        this.number_3 = number_3;
    }

    public String getNumber_2() {
        return this.number_2;
    }

    public void setNumber_2(String number_2) {
        this.number_2 = number_2;
    }

    public String getNumber_1() {
        return this.number_1;
    }

    public void setNumber_1(String number_1) {
        this.number_1 = number_1;
    }

    public String getDatetime_1() {
        return this.datetime_1;
    }

    public void setDatetime_1(String datetime_1) {
        this.datetime_1 = datetime_1;
    }

    @Override
    public String toString() {
        return "QueryCustomPersonBean{" +
                "queryCustomId=" + queryCustomId +
                ", ckind='" + ckind + '\'' +
                ", creater='" + creater + '\'' +
                ", createrName='" + createrName + '\'' +
                ", createtime='" + createtime + '\'' +
                ", datastatus=" + datastatus +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", organ='" + organ + '\'' +
                ", orgi='" + orgi + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", phone='" + phone + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", realMobileNumber='" + realMobileNumber + '\'' +
                ", statusInSeas=" + statusInSeas +
                ", updatetime='" + updatetime + '\'' +
                ", updateusername='" + updateusername + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", company='" + company + '\'' +
                ", department='" + department + '\'' +
                ", duty='" + duty + '\'' +
                ", gender='" + gender + '\'' +
                ", memo='" + memo + '\'' +
                ", shares='" + shares + '\'' +
                ", letters='" + letters + '\'' +
                ", displayNameSpelling='" + displayNameSpelling + '\'' +
                ", number_1='" + number_1 + '\'' +
                ", number_2='" + number_2 + '\'' +
                ", number_3='" + number_3 + '\'' +
                ", number_4='" + number_4 + '\'' +
                ", number_5='" + number_5 + '\'' +
                ", time_1='" + time_1 + '\'' +
                ", time_2='" + time_2 + '\'' +
                ", time_3='" + time_3 + '\'' +
                ", time_4='" + time_4 + '\'' +
                ", time_5='" + time_5 + '\'' +
                ", custom_1='" + custom_1 + '\'' +
                ", custom_2='" + custom_2 + '\'' +
                ", custom_3='" + custom_3 + '\'' +
                ", custom_4='" + custom_4 + '\'' +
                ", custom_5='" + custom_5 + '\'' +
                ", custom_6='" + custom_6 + '\'' +
                ", custom_7='" + custom_7 + '\'' +
                ", custom_8='" + custom_8 + '\'' +
                ", custom_9='" + custom_9 + '\'' +
                ", datetime_1='" + datetime_1 + '\'' +
                '}';
    }

    public String getCustom_21() {
        return this.custom_21;
    }

    public void setCustom_21(String custom_21) {
        this.custom_21 = custom_21;
    }

    public String getCustom_20() {
        return this.custom_20;
    }

    public void setCustom_20(String custom_20) {
        this.custom_20 = custom_20;
    }

    public String getCustom_19() {
        return this.custom_19;
    }

    public void setCustom_19(String custom_19) {
        this.custom_19 = custom_19;
    }

    public String getCustom_18() {
        return this.custom_18;
    }

    public void setCustom_18(String custom_18) {
        this.custom_18 = custom_18;
    }

    public String getCustom_17() {
        return this.custom_17;
    }

    public void setCustom_17(String custom_17) {
        this.custom_17 = custom_17;
    }

    public String getCustom_16() {
        return this.custom_16;
    }

    public void setCustom_16(String custom_16) {
        this.custom_16 = custom_16;
    }

    public String getCustom_15() {
        return this.custom_15;
    }

    public void setCustom_15(String custom_15) {
        this.custom_15 = custom_15;
    }

    public String getCustom_14() {
        return this.custom_14;
    }

    public void setCustom_14(String custom_14) {
        this.custom_14 = custom_14;
    }

    public String getCustom_13() {
        return this.custom_13;
    }

    public void setCustom_13(String custom_13) {
        this.custom_13 = custom_13;
    }

    public String getCustom_12() {
        return this.custom_12;
    }

    public void setCustom_12(String custom_12) {
        this.custom_12 = custom_12;
    }

    public String getCustom_11() {
        return this.custom_11;
    }

    public void setCustom_11(String custom_11) {
        this.custom_11 = custom_11;
    }

    public String getCustom_10() {
        return this.custom_10;
    }

    public void setCustom_10(String custom_10) {
        this.custom_10 = custom_10;
    }

    public String getCdate_5() {
        return this.cdate_5;
    }

    public void setCdate_5(String cdate_5) {
        this.cdate_5 = cdate_5;
    }

    public String getCdate_4() {
        return this.cdate_4;
    }

    public void setCdate_4(String cdate_4) {
        this.cdate_4 = cdate_4;
    }

    public String getCdate_3() {
        return this.cdate_3;
    }

    public void setCdate_3(String cdate_3) {
        this.cdate_3 = cdate_3;
    }

    public String getCdate_2() {
        return this.cdate_2;
    }

    public void setCdate_2(String cdate_2) {
        this.cdate_2 = cdate_2;
    }

    public String getCdate_1() {
        return this.cdate_1;
    }

    public void setCdate_1(String cdate_1) {
        this.cdate_1 = cdate_1;
    }

    public String getDatetime_4() {
        return this.datetime_4;
    }

    public void setDatetime_4(String datetime_4) {
        this.datetime_4 = datetime_4;
    }

    public String getDatetime_3() {
        return this.datetime_3;
    }

    public void setDatetime_3(String datetime_3) {
        this.datetime_3 = datetime_3;
    }

    public String getDatetime_2() {
        return this.datetime_2;
    }

    public void setDatetime_2(String datetime_2) {
        this.datetime_2 = datetime_2;
    }
}

