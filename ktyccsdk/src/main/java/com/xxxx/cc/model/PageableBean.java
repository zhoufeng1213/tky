package com.xxxx.cc.model;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class PageableBean {
    private int offset;
    private int pageNumber;
    private int pageSize;
    private boolean paged;
    private SortBean sort;
    private boolean unpaged;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isPaged() {
        return paged;
    }

    public void setPaged(boolean paged) {
        this.paged = paged;
    }

    public SortBean getSort() {
        return sort;
    }

    public void setSort(SortBean sort) {
        this.sort = sort;
    }

    public boolean isUnpaged() {
        return unpaged;
    }

    public void setUnpaged(boolean unpaged) {
        this.unpaged = unpaged;
    }
}
