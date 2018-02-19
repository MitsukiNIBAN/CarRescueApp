package com.kuaijie.carrescue.entity;

import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;

import java.util.Date;

@Message
public class OrderProcedure {
    @Index(0)
    private Long id;
    @Index(1)
    private Date gmtCreate;
    @Index(2)
    private Date gmtModified;
    @Index(3)
    private Long contractDetailId;
    @Index(4)
    private Long procedureId;
    @Index(5)
    private Integer procedureOrder;
    @Index(6)
    private Byte state;
    @Index(7)
    private String procedureName;
    @Index(8)
    private Long pictureType;
    @Index(9)
    private String remark;
    @Index(10)
    private String pictureTypeName;
    @Index(11)
    private Integer pictureNum;
    @Index(12)
    private String remarkPicture;
    @Index(13)
    private Long child1;
    @Index(14)
    private Long child2;
    @Index(15)
    private Long stage;

    public Long getPictureType() {
        return pictureType;
    }

    public void setPictureType(Long pictureType) {
        this.pictureType = pictureType;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPictureTypeName() {
        return pictureTypeName;
    }

    public void setPictureTypeName(String pictureTypeName) {
        this.pictureTypeName = pictureTypeName;
    }

    public Integer getPictureNum() {
        return pictureNum;
    }

    public void setPictureNum(Integer integer) {
        this.pictureNum = integer;
    }

    public String getRemarkPicture() {
        return remarkPicture;
    }

    public void setRemarkPicture(String remarkPicture) {
        this.remarkPicture = remarkPicture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getContractDetailId() {
        return contractDetailId;
    }

    public void setContractDetailId(Long contractDetailId) {
        this.contractDetailId = contractDetailId;
    }

    public Long getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(Long procedureId) {
        this.procedureId = procedureId;
    }

    public Integer getProcedureOrder() {
        return procedureOrder;
    }

    public void setProcedureOrder(Integer procedureOrder) {
        this.procedureOrder = procedureOrder;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Long getChild1() {
        return child1;
    }

    public void setChild1(Long child1) {
        this.child1 = child1;
    }

    public Long getChild2() {
        return child2;
    }

    public void setChild2(Long child2) {
        this.child2 = child2;
    }

    public Long getStage() {
        return stage;
    }

    public void setStage(Long stage) {
        this.stage = stage;
    }
}