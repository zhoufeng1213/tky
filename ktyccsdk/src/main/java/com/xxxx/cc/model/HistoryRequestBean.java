package com.xxxx.cc.model;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class HistoryRequestBean {


    /**
     * begin : 0
     * end : 1559697230000
     * page : 0
     * size : 20
     * userIds : ["11a81f688c31477bbea7bf954bd3a7cf"]
     * "direction": "OUTBOUND",
     * "dnis":"18556722815",
     */

    private long begin;
    private long end;
    private int page;
    private int size;
    private String direction;
    private String dnis;
    private List<String> userIds;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDnis() {
        return dnis;
    }

    public void setDnis(String dnis) {
        this.dnis = dnis;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
