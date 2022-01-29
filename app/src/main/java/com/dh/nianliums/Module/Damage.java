package com.dh.nianliums.Module;


/**
 * 用于保存服务器发送的工具损坏信息
 */
public class Damage {
    private String number;//数据编号：
    private String instrumentNumber;//工具编号：
    private String declare;//申报人：
    private String investigate;//审批人：
    private String reviewStatus;//审批状态：
    private String information2;//备注信息：

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    public void setInstrumentNumber(String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    public String getDeclare() {
        return declare;
    }

    public void setDeclare(String declare) {
        this.declare = declare;
    }

    public String getInvestigate() {
        return investigate;
    }

    public void setInvestigate(String investigate) {
        this.investigate = investigate;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getInformation2() {
        return information2;
    }

    public void setInformation2(String information2) {
        this.information2 = information2;
    }
}
