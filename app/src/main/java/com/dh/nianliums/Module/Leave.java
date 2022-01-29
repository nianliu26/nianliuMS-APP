package com.dh.nianliums.Module;


import java.util.Date;

/**
 * 用于保存服务器发送的请假数据
 */
public class Leave {
    private String number;//数据编号：
    private String name;//员工编号：
    private String nickname;//员工名称：
    private Date leaveData;//请假时间：
    private String cause;//请假原因：
    private String leaveStatus;//审批状态：
    private String leaveInvestigate;//审批人：
    private String information4;//备注信息：



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getLeaveData() {
        return leaveData;
    }

    public void setLeaveData(Date leaveData) {
        this.leaveData = leaveData;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

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

    public String getLeaveInvestigate() {
        return leaveInvestigate;
    }

    public void setLeaveInvestigate(String leaveInvestigate) {
        this.leaveInvestigate = leaveInvestigate;
    }

    public String getInformation4() {
        return information4;
    }

    public void setInformation4(String information4) {
        this.information4 = information4;
    }
}
