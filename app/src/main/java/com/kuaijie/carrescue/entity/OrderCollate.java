package com.kuaijie.carrescue.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;

@Message
public class OrderCollate {
    @Index(0)
    private Long id;
    @Index(1)
    private Long orderId;
    @Index(2)
    private BigDecimal reachMiles;
    @Index(3)
    private BigDecimal trailerMiles;
    @Index(4)
    private BigDecimal roadToll;
    @Index(5)
    private BigDecimal mealFee;
    @Index(6)
    private Byte wheelNum;
    @Index(7)
    private BigDecimal commission;
    @Index(8)
    private BigDecimal orderCost;
    @Index(9)
    private Byte collateState;
    @Index(10)
    private Long collateStaff;
    @Index(11)
    private String memo;
    @Index(12)
    private String gmtCreate;
    @Index(13)
    private String gmtModified;
    @Index(14)
    private String achieveTime;
    @Index(15)
    private BigDecimal agencyCost;
    @Index(16)
    private String orderNumber;
    
    

    public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

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

    public BigDecimal getReachMiles() {
        return reachMiles;
    }

    public void setReachMiles(BigDecimal reachMiles) {
        this.reachMiles = reachMiles;
    }

    public BigDecimal getTrailerMiles() {
        return trailerMiles;
    }

    public void setTrailerMiles(BigDecimal trailerMiles) {
        this.trailerMiles = trailerMiles;
    }

    public BigDecimal getRoadToll() {
        return roadToll;
    }

    public void setRoadToll(BigDecimal roadToll) {
        this.roadToll = roadToll;
    }

    public BigDecimal getMealFee() {
        return mealFee;
    }

    public void setMealFee(BigDecimal mealFee) {
        this.mealFee = mealFee;
    }

    public Byte getWheelNum() {
        return wheelNum;
    }

    public void setWheelNum(Byte wheelNum) {
        this.wheelNum = wheelNum;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(BigDecimal orderCost) {
        this.orderCost = orderCost;
    }

    public Byte getCollateState() {
        return collateState;
    }

    public void setCollateState(Byte collateState) {
        this.collateState = collateState;
    }

    public Long getCollateStaff() {
        return collateStaff;
    }

    public void setCollateStaff(Long collateStaff) {
        this.collateStaff = collateStaff;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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

	public String getAchieveTime() {
		return achieveTime;
	}

	public void setAchieveTime(String achieveTime) {
		this.achieveTime = achieveTime;
	}

	public BigDecimal getAgencyCost() {
        return agencyCost;
    }

    public void setAgencyCost(BigDecimal agencyCost) {
        this.agencyCost = agencyCost;
    }

	@Override
	public String toString() {
		return "OrderCollate [id=" + id + ", orderId=" + orderId + ", reachMiles=" + reachMiles + ", trailerMiles="
				+ trailerMiles + ", roadToll=" + roadToll + ", mealFee=" + mealFee + ", wheelNum=" + wheelNum
				+ ", commission=" + commission + ", orderCost=" + orderCost + ", collateState=" + collateState
				+ ", collateStaff=" + collateStaff + ", memo=" + memo + ", gmtCreate=" + gmtCreate + ", gmtModified="
				+ gmtModified + ", achieveTime=" + achieveTime + ", agencyCost=" + agencyCost + ", orderNumber="
				+ orderNumber + "]";
	}
    
    
}