package com.kuaijie.carrescue.entity;

import org.msgpack.annotation.Message;
import org.msgpack.annotation.Index;

import java.math.BigDecimal;

@Message
public class Order {
    @Index(0)
    private Long id;
    @Index(1)
    private String orderId;
    @Index(2)
    private String caseId;
    @Index(3)
    private Long orderTypeId;
    @Index(4)
    private Long carTypeId;
    @Index(5)
    private Long operationTypeId;
    @Index(6)
    private String aidAddress;
    @Index(7)
    private String goalAddress;
    @Index(8)
    private Long environmentId;
    @Index(9)
    private Long businessClientId;
    @Index(10)
    private String client;
    @Index(11)
    private String clientPhone;
    @Index(12)
    private String carOwner;
    @Index(13)
    private String carOwnerPhone;
    @Index(14)
    private String receiveCarPhone;
    @Index(15)
    private String takeCarPhone;
    @Index(16)
    private String faultPlate;
    @Index(17)
    private String carNumber;
    @Index(18)
    private String scheduledTime;
    @Index(19)
    private Byte makeReceipt;
    @Index(20)
    private String predictTime;
    @Index(21)
    private BigDecimal predictMiles;
    @Index(22)
    private Long equationId;
    @Index(23)
    private BigDecimal charge;
    @Index(24)
    private Long dispatchId;
    @Index(25)
    private Byte chargeWay;
    @Index(26)
    private Byte orderState;
    @Index(27)
    private Long orderStaff;
    @Index(28)
    private String memo;
    @Index(29)
    private String gmtCreate;
    @Index(30)
    private String gmtModified;
    @Index(31)
    private BigDecimal aidLongitude;
    @Index(32)
    private BigDecimal aidLatitude;
    @Index(33)
    private BigDecimal goalLongitude;
    @Index(34)
    private BigDecimal goalLatitude;
    @Index(35)
    private Long verifierId;
    @Index(36)
    private String verifierName;
    @Index(37)
    private String verifyTime;
    @Index(38)
    private Boolean isVerify;
    @Index(39)
    private Byte pushWay;
    @Index(40)
    private Byte quoteWay;
    @Index(41)
    private String name;                    //下单人
    @Index(42)
    private String description;             //车辆类型描述
    @Index(43)
    private String operationType;           //业务类型
    @Index(44)
    private String environmentName;         //周围环境
    @Index(45)
    private String clientName;              //客户单位
    @Index(46)
    private String CalFormula;              //计算公式
    @Index(47)
    private String orderType;               //订单类型
    @Index(48)
    private String frameNumber;             //车架号
    @Index(49)
    private String disGmtCreate;            //调度时间


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public Long getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(Long orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public Long getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(Long carTypeId) {
        this.carTypeId = carTypeId;
    }

    public Long getOperationTypeId() {
        return operationTypeId;
    }

    public void setOperationTypeId(Long operationTypeId) {
        this.operationTypeId = operationTypeId;
    }

    public String getAidAddress() {
        return aidAddress;
    }

    public void setAidAddress(String aidAddress) {
        this.aidAddress = aidAddress;
    }

    public String getGoalAddress() {
        return goalAddress;
    }

    public void setGoalAddress(String goalAddress) {
        this.goalAddress = goalAddress;
    }

    public Long getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(Long environmentId) {
        this.environmentId = environmentId;
    }

    public Long getBusinessClientId() {
        return businessClientId;
    }

    public void setBusinessClientId(Long businessClientId) {
        this.businessClientId = businessClientId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public String getCarOwnerPhone() {
        return carOwnerPhone;
    }

    public void setCarOwnerPhone(String carOwnerPhone) {
        this.carOwnerPhone = carOwnerPhone;
    }

    public String getReceiveCarPhone() {
        return receiveCarPhone;
    }

    public void setReceiveCarPhone(String receiveCarPhone) {
        this.receiveCarPhone = receiveCarPhone;
    }

    public String getTakeCarPhone() {
        return takeCarPhone;
    }

    public void setTakeCarPhone(String takeCarPhone) {
        this.takeCarPhone = takeCarPhone;
    }

    public String getFaultPlate() {
        return faultPlate;
    }

    public void setFaultPlate(String faultPlate) {
        this.faultPlate = faultPlate;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Byte getMakeReceipt() {
        return makeReceipt;
    }

    public void setMakeReceipt(Byte makeReceipt) {
        this.makeReceipt = makeReceipt;
    }

    public String getPredictTime() {
        return predictTime;
    }

    public void setPredictTime(String predictTime) {
        this.predictTime = predictTime;
    }

    public Long getEquationId() {
        return equationId;
    }

    public void setEquationId(Long equationId) {
        this.equationId = equationId;
    }

    public Long getDispatchId() {
        return dispatchId;
    }

    public void setDispatchId(Long dispatchId) {
        this.dispatchId = dispatchId;
    }

    public Byte getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(Byte chargeWay) {
        this.chargeWay = chargeWay;
    }

    public Byte getOrderState() {
        return orderState;
    }

    public void setOrderState(Byte orderState) {
        this.orderState = orderState;
    }

    public Long getOrderStaff() {
        return orderStaff;
    }

    public void setOrderStaff(Long orderStaff) {
        this.orderStaff = orderStaff;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public Long getVerifierId() {
        return verifierId;
    }

    public void setVerifierId(Long verifierId) {
        this.verifierId = verifierId;
    }

    public String getVerifierName() {
        return verifierName;
    }

    public void setVerifierName(String verifierName) {
        this.verifierName = verifierName;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Boolean getVerify() {
        return isVerify;
    }

    public void setVerify(Boolean verify) {
        isVerify = verify;
    }

    public Byte getPushWay() {
        return pushWay;
    }

    public void setPushWay(Byte pushWay) {
        this.pushWay = pushWay;
    }

    public Byte getQuoteWay() {
        return quoteWay;
    }

    public void setQuoteWay(Byte quoteWay) {
        this.quoteWay = quoteWay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCalFormula() {
        return CalFormula;
    }

    public void setCalFormula(String calFormula) {
        CalFormula = calFormula;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public BigDecimal getPredictMiles() {
        return predictMiles;
    }

    public void setPredictMiles(BigDecimal predictMiles) {
        this.predictMiles = predictMiles;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public BigDecimal getAidLongitude() {
        return aidLongitude;
    }

    public void setAidLongitude(BigDecimal aidLongitude) {
        this.aidLongitude = aidLongitude;
    }

    public BigDecimal getAidLatitude() {
        return aidLatitude;
    }

    public void setAidLatitude(BigDecimal aidLatitude) {
        this.aidLatitude = aidLatitude;
    }

    public BigDecimal getGoalLongitude() {
        return goalLongitude;
    }

    public void setGoalLongitude(BigDecimal goalLongitude) {
        this.goalLongitude = goalLongitude;
    }

    public BigDecimal getGoalLatitude() {
        return goalLatitude;
    }

    public void setGoalLatitude(BigDecimal goalLatitude) {
        this.goalLatitude = goalLatitude;
    }

    public String getDisGmtCreate() {
        return disGmtCreate;
    }

    public void setDisGmtCreate(String disGmtCreate) {
        this.disGmtCreate = disGmtCreate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", caseId='" + caseId + '\'' +
                ", orderTypeId=" + orderTypeId +
                ", carTypeId=" + carTypeId +
                ", operationTypeId=" + operationTypeId +
                ", aidAddress='" + aidAddress + '\'' +
                ", goalAddress='" + goalAddress + '\'' +
                ", environmentId=" + environmentId +
                ", businessClientId=" + businessClientId +
                ", client='" + client + '\'' +
                ", clientPhone='" + clientPhone + '\'' +
                ", carOwner='" + carOwner + '\'' +
                ", carOwnerPhone='" + carOwnerPhone + '\'' +
                ", receiveCarPhone='" + receiveCarPhone + '\'' +
                ", takeCarPhone='" + takeCarPhone + '\'' +
                ", faultPlate='" + faultPlate + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", scheduledTime='" + scheduledTime + '\'' +
                ", makeReceipt=" + makeReceipt +
                ", predictTime='" + predictTime + '\'' +
                ", predictMiles=" + predictMiles +
                ", equationId=" + equationId +
                ", charge=" + charge +
                ", dispatchId=" + dispatchId +
                ", chargeWay=" + chargeWay +
                ", orderState=" + orderState +
                ", orderStaff=" + orderStaff +
                ", memo='" + memo + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModified='" + gmtModified + '\'' +
                ", aidLongitude=" + aidLongitude +
                ", aidLatitude=" + aidLatitude +
                ", goalLongitude=" + goalLongitude +
                ", goalLatitude=" + goalLatitude +
                ", verifierId=" + verifierId +
                ", verifierName='" + verifierName + '\'' +
                ", verifyTime='" + verifyTime + '\'' +
                ", isVerify=" + isVerify +
                ", pushWay=" + pushWay +
                ", quoteWay=" + quoteWay +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", operationType='" + operationType + '\'' +
                ", environmentName='" + environmentName + '\'' +
                ", clientName='" + clientName + '\'' +
                ", CalFormula='" + CalFormula + '\'' +
                ", orderType='" + orderType + '\'' +
                ", frameNumber='" + frameNumber + '\'' +
                ", disGmtCreate=" + disGmtCreate + '\'' +
                '}';
    }
}