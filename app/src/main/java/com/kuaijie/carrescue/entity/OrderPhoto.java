package com.kuaijie.carrescue.entity;

import java.util.Date;

import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;

@Message
public class OrderPhoto {
    @Index(0)
    private Long id;
    @Index(1)
    private Long orderId;
    @Index(2)
    private String photoName;
    @Index(3)
    private String photoAddr;
    @Index(4)
    private Byte photoType;
    @Index(5)
    private Long photoStaff;
    @Index(6)
    private String gmtCreate;
    @Index(7)
    private String gmtModified;

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

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName == null ? null : photoName.trim();
    }

    public String getPhotoAddr() {
        return photoAddr;
    }

    public void setPhotoAddr(String photoAddr) {
        this.photoAddr = photoAddr == null ? null : photoAddr.trim();
    }

    public Byte getPhotoType() {
        return photoType;
    }

    public void setPhotoType(Byte photoType) {
        this.photoType = photoType;
    }

    public Long getPhotoStaff() {
        return photoStaff;
    }

    public void setPhotoStaff(Long photoStaff) {
        this.photoStaff = photoStaff;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

	@Override
	public String toString() {
		return "OrderPhoto [id=" + id + ", orderId=" + orderId + ", photoName=" + photoName + ", photoAddr=" + photoAddr
				+ ", photoType=" + photoType + ", photoStaff=" + photoStaff + ", gmtCreate=" + gmtCreate
				+ ", gmtModified=" + gmtModified + "]";
	}
    
    
}