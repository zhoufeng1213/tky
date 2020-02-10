package com.xxxx.tky.model;

/**
 * @author zhoufeng
 * @date 2020/2/8
 * @moduleName
 */
public class CommunicationRecordRequestBean {


    /**
     * page : 0
     * phone : 13861336511
     * size : 10
     */

    private int page;
    private String phone;
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
