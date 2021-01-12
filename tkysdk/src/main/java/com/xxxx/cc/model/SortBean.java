package com.xxxx.cc.model;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class SortBean {
    /**
     * sorted : true
     * unsorted : false
     */

    private boolean sorted;
    private boolean unsorted;

    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public boolean isUnsorted() {
        return unsorted;
    }

    public void setUnsorted(boolean unsorted) {
        this.unsorted = unsorted;
    }
}
