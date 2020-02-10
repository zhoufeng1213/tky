package com.xxxx.cc.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


@Entity
public class FileDownloadVO {
    @Id(autoincrement = true)
    private Long id;

    private String url;

    private String absPath;

    private long downStamp;

    @Generated(hash = 121549471)
    public FileDownloadVO(Long id, String url, String absPath, long downStamp) {
        this.id = id;
        this.url = url;
        this.absPath = absPath;
        this.downStamp = downStamp;
    }

    @Generated(hash = 191995851)
    public FileDownloadVO() {
    }


    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getAbsPath() {
        return this.absPath;
    }

    public void setAbsPath(String absPath) {
        this.absPath = absPath;
    }

    public long getDownStamp() {
        return this.downStamp;
    }

    public void setDownStamp(long downStamp) {
        this.downStamp = downStamp;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
