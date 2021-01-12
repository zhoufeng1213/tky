package com.xxxx.cc.model;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class PageBean {
    /**
     * content : [{"agentUnames":"test002","agentUserName":"test002@ketianyun.com","alegQuality":0,"ani":"100011","area":"江苏 苏州市","billingInSec":0,"blegQuality":0,"blegUuid":"8dd4383a-b2d1-4c01-bc28-33993ab14bc4","bridged":false,"chargeMin":0,"contactName":"张三","createTime":"2019-08-02 14:13:00","daSampleCategory":"5","daSampleDetail":"您拨打的电话正在通话中","daSampleName":"正在通话中","daTone":"sample","direction":"OUTBOUND","dnbr":"057128093580","dnis":"13913136453","durationInSec":12,"hangupCause":"NORMAL_CLEARING","hangupTime":"2019-08-02 14:13:12","id":"7cdf59eed0e7c3100a53717dd698eb6a","ivrTime":"2019-08-02 14:12:58","lat":0,"lng":0,"orgId":"b3c7d259aee544f59e7c1a3315051da2","reserved1":"b3c7d259aee544f59e7c1a3315051da2","reserved2":"主叫挂机","satisfy":0,"transfered":false,"transferee":false,"updateTime":"2019-08-02 14:13:13","userId":"fbd89c7e34974a36aa8e9b8d30338c46","uuid":"cb613408-1b2c-4a0f-bbb4-930219192073","waitInBillingSec":0,"waitInIvrSec":0,"waitInQueueSec":0,"waitInSec":12}]
     * first : true
     * last : false
     * number : 0
     * numberOfElements : 1
     * pageable : {"offset":0,"pageNumber":0,"pageSize":1,"paged":true,"sort":{"sorted":true,"unsorted":false},"unpaged":false}
     * size : 1
     * sort : {"$ref":"$.page.pageable.sort"}
     * totalElements : 20
     * totalPages : 20
     */

    private boolean first;
    private boolean last;
    private int number;
    private int numberOfElements;
    private PageableBean pageable;
    private int size;
    private SortBeanX sort;
    private int totalElements;
    private int totalPages;
    private List<ContentBean> content;

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public PageableBean getPageable() {
        return pageable;
    }

    public void setPageable(PageableBean pageable) {
        this.pageable = pageable;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public SortBeanX getSort() {
        return sort;
    }

    public void setSort(SortBeanX sort) {
        this.sort = sort;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }
}
