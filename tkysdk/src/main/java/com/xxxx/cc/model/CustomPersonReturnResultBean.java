package com.xxxx.cc.model;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2020/2/5
 * @moduleName
 */
public class CustomPersonReturnResultBean {


    /**
     * code : 0
     * message : success
     * page : {"content":[{"ckind":"客户","creater":"7a12c9203f6640ec897f7685dcd1e3e4","createrName":"李婷","createtime":"2019-04-19 19:48:08","custom_6":"1","custom_8":"bbbbbb","datastatus":true,"email":"","id":"ceae4815f4f94d5ca51159118eff9da2","mobile":"18551200225","name":"summer2","number_1":0,"number_2":0,"number_3":0,"number_4":0,"number_5":0,"organ":"2c916ee06a67cd93016a871c20aa00ed","orgi":"b3c7d259aee544f59e7c1a3315051da2","owner":"gctest","ownerName":"gctest","phone":"","pinyin":"s","realMobileNumber":"18551200225","statusInSeas":0,"time_1":0,"time_2":0,"time_3":0,"time_4":0,"time_5":0,"updatetime":"2020-02-04 21:48:50","updateusername":"gctest","username":"test001@ketianyun.com"},{"address":"","ckind":"","company":"","creater":"83d4c439977142d5ac9622e30095f458","createrName":"Sky 叶","createtime":"2020-01-09 23:01:34","custom_1":"","custom_3":"","custom_4":"","custom_5":"","custom_6":"ca","custom_7":"","custom_8":"安安","custom_9":"","datastatus":true,"department":"","duty":"","email":"","gender":"-1","id":"744d46e0467e46d09efb5bfe6bed686b","memo":"","mobile":"18659573817","name":"11","number_1":0,"number_2":0,"number_3":0,"number_4":0,"number_5":0,"organ":"2c916ee069c388fe0169c4a701f8002c","orgi":"b3c7d259aee544f59e7c1a3315051da2","owner":"gctest","ownerName":"gctest","phone":"","pinyin":"1","realMobileNumber":"18659573817","shares":"no","statusInSeas":0,"time_1":0,"time_2":0,"time_3":0,"time_4":0,"time_5":0,"updatetime":"2020-02-04 21:39:59","updateusername":"skyy","username":"skyy"}],"first":true,"last":true,"number":0,"numberOfElements":2,"pageable":{"offset":0,"pageNumber":0,"pageSize":100,"paged":true,"sort":{"sorted":true,"unsorted":false},"unpaged":false},"size":100,"sort":{"$ref":"$.page.pageable.sort"},"totalElements":2,"totalPages":1}
     */

    private int code;
    private String message;
    private PageBean page;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public static class PageBean {
        /**
         * content : [{"ckind":"客户","creater":"7a12c9203f6640ec897f7685dcd1e3e4","createrName":"李婷","createtime":"2019-04-19 19:48:08","custom_6":"1","custom_8":"bbbbbb","datastatus":true,"email":"","id":"ceae4815f4f94d5ca51159118eff9da2","mobile":"18551200225","name":"summer2","number_1":0,"number_2":0,"number_3":0,"number_4":0,"number_5":0,"organ":"2c916ee06a67cd93016a871c20aa00ed","orgi":"b3c7d259aee544f59e7c1a3315051da2","owner":"gctest","ownerName":"gctest","phone":"","pinyin":"s","realMobileNumber":"18551200225","statusInSeas":0,"time_1":0,"time_2":0,"time_3":0,"time_4":0,"time_5":0,"updatetime":"2020-02-04 21:48:50","updateusername":"gctest","username":"test001@ketianyun.com"},{"address":"","ckind":"","company":"","creater":"83d4c439977142d5ac9622e30095f458","createrName":"Sky 叶","createtime":"2020-01-09 23:01:34","custom_1":"","custom_3":"","custom_4":"","custom_5":"","custom_6":"ca","custom_7":"","custom_8":"安安","custom_9":"","datastatus":true,"department":"","duty":"","email":"","gender":"-1","id":"744d46e0467e46d09efb5bfe6bed686b","memo":"","mobile":"18659573817","name":"11","number_1":0,"number_2":0,"number_3":0,"number_4":0,"number_5":0,"organ":"2c916ee069c388fe0169c4a701f8002c","orgi":"b3c7d259aee544f59e7c1a3315051da2","owner":"gctest","ownerName":"gctest","phone":"","pinyin":"1","realMobileNumber":"18659573817","shares":"no","statusInSeas":0,"time_1":0,"time_2":0,"time_3":0,"time_4":0,"time_5":0,"updatetime":"2020-02-04 21:39:59","updateusername":"skyy","username":"skyy"}]
         * first : true
         * last : true
         * number : 0
         * numberOfElements : 2
         * pageable : {"offset":0,"pageNumber":0,"pageSize":100,"paged":true,"sort":{"sorted":true,"unsorted":false},"unpaged":false}
         * size : 100
         * sort : {"$ref":"$.page.pageable.sort"}
         * totalElements : 2
         * totalPages : 1
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
        private List<QueryCustomPersonBean> content;

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

        public List<QueryCustomPersonBean> getContent() {
            return content;
        }

        public void setContent(List<QueryCustomPersonBean> content) {
            this.content = content;
        }

        public static class PageableBean {
            /**
             * offset : 0
             * pageNumber : 0
             * pageSize : 100
             * paged : true
             * sort : {"sorted":true,"unsorted":false}
             * unpaged : false
             */

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

            public static class SortBean {
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
        }

        public static class SortBeanX {
            /**
             * $ref : $.page.pageable.sort
             */

            private String $ref;

            public String get$ref() {
                return $ref;
            }

            public void set$ref(String $ref) {
                this.$ref = $ref;
            }
        }

    }
}
