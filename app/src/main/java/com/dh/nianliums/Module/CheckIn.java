package com.dh.nianliums.Module;

import java.util.Date;

/**
 * 保存服务器发送的签到数据
 */
public class CheckIn {
    private String number;//数据编号：
    private String name;//员工编号：
    private String nickname;//员工名称：
    private Date record;//登记时间：
    private String condition;//签到状态：
    private String instrumentNumber;//领取工具：
    private String zoneNumber;//负责区域：
    private String information1;//备注信息：

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getRecord() {
        return record;
    }

    public void setRecord(Date record) {
        this.record = record;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getInstrumentNumber() {
        return instrumentNumber;
    }

    public void setInstrumentNumber(String instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
    }

    public String getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(String zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public String getInformation1() {
        return information1;
    }

    public void setInformation1(String information1) {
        this.information1 = information1;
    }
}
