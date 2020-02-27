package com.xxxx.cc.model;

import java.io.Serializable;

public class CustomDefinedBean implements Serializable {
    private boolean callTaskField;
    private String comment;
    private String createdtime;
    private String createrid;
    private String creatername;
    private boolean customInput;
    private String defaultValue;
    private String field;
    private String id;
    private String moudle;
    private String name;
    private int number;
    private String orgid;
    private boolean requireWrite;
    private int sequence;
    private boolean showInPage;
    private boolean showInServiceSummary;
    private boolean showInWorkorder;
    private boolean supportEdit;
    private boolean supportExport;
    private boolean supportImport;
    private int supportSearch;
    private String type;
    private String typename;
    private String updatetime;
    private String updateuser;
    private String value;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCallTaskField() {
        return callTaskField;
    }

    public void setCallTaskField(boolean callTaskField) {
        this.callTaskField = callTaskField;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getCreaterid() {
        return createrid;
    }

    public void setCreaterid(String createrid) {
        this.createrid = createrid;
    }

    public String getCreatername() {
        return creatername;
    }

    public void setCreatername(String creatername) {
        this.creatername = creatername;
    }

    public boolean isCustomInput() {
        return customInput;
    }

    public void setCustomInput(boolean customInput) {
        this.customInput = customInput;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoudle() {
        return moudle;
    }

    public void setMoudle(String moudle) {
        this.moudle = moudle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public boolean isRequireWrite() {
        return requireWrite;
    }

    public void setRequireWrite(boolean requireWrite) {
        this.requireWrite = requireWrite;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isShowInPage() {
        return showInPage;
    }

    public void setShowInPage(boolean showInPage) {
        this.showInPage = showInPage;
    }

    public boolean isShowInServiceSummary() {
        return showInServiceSummary;
    }

    public void setShowInServiceSummary(boolean showInServiceSummary) {
        this.showInServiceSummary = showInServiceSummary;
    }

    public boolean isShowInWorkorder() {
        return showInWorkorder;
    }

    public void setShowInWorkorder(boolean showInWorkorder) {
        this.showInWorkorder = showInWorkorder;
    }

    public boolean isSupportEdit() {
        return supportEdit;
    }

    public void setSupportEdit(boolean supportEdit) {
        this.supportEdit = supportEdit;
    }

    public boolean isSupportExport() {
        return supportExport;
    }

    public void setSupportExport(boolean supportExport) {
        this.supportExport = supportExport;
    }

    public boolean isSupportImport() {
        return supportImport;
    }

    public void setSupportImport(boolean supportImport) {
        this.supportImport = supportImport;
    }

    public int getSupportSearch() {
        return supportSearch;
    }

    public void setSupportSearch(int supportSearch) {
        this.supportSearch = supportSearch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }

    @Override
    public String toString() {
        return "CustomDefinedBean{" +
                "callTaskField=" + callTaskField +
                ", comment='" + comment + '\'' +
                ", createdtime='" + createdtime + '\'' +
                ", createrid='" + createrid + '\'' +
                ", creatername='" + creatername + '\'' +
                ", customInput=" + customInput +
                ", defaultValue='" + defaultValue + '\'' +
                ", field='" + field + '\'' +
                ", id='" + id + '\'' +
                ", moudle='" + moudle + '\'' +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", orgid='" + orgid + '\'' +
                ", requireWrite=" + requireWrite +
                ", sequence=" + sequence +
                ", showInPage=" + showInPage +
                ", showInServiceSummary=" + showInServiceSummary +
                ", showInWorkorder=" + showInWorkorder +
                ", supportEdit=" + supportEdit +
                ", supportExport=" + supportExport +
                ", supportImport=" + supportImport +
                ", supportSearch=" + supportSearch +
                ", type='" + type + '\'' +
                ", typename='" + typename + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", updateuser='" + updateuser + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
