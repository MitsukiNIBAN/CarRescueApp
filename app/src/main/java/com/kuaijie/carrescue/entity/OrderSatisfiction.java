package com.kuaijie.carrescue.entity;

import java.util.Date;

import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;

@Message
public class OrderSatisfiction {
    @Index(0)
    private Long id;
    @Index(1)
    private Long orderId;
    @Index(2)
    private Long photoId;
    @Index(3)
    private Integer satisfiction;
    @Index(4)
    private String memo;
    @Index(5)
    private Date gmtCreate;
    @Index(6)
    private Date gmtModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public Integer getSatisfiction() {
        return satisfiction;
    }

    public void setSatisfiction(Integer satisfiction) {
        this.satisfiction = satisfiction;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}