package com.dh.nianliums.Module;

import java.util.Date;

/**
 * 用于保存服务器发送的人员流动数据
 */
public class EmployeeChange {
    private String number;//数据编号：
    private String name;//员工编号：
    private String nickname;//员工名称：
    private String changeStatus;//流动状态：
    private Date changeData;//记录日期：
    private String information3;//备注信息：

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

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public Date getChangeData() {
        return changeData;
    }

    public void setChangeData(Date changeData) {
        this.changeData = changeData;
    }

    public String getInformation3() {
        return information3;
    }

    public void setInformation3(String information3) {
        this.information3 = information3;
    }
}
