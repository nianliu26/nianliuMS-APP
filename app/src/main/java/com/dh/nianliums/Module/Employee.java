package com.dh.nianliums.Module;

import java.util.Date;


/**
 * 用于保存服务器返回的用户数据
 */
public class Employee {
    private String name;//员工编号：
    private String nickname;//员工姓名
    private String phone;//联系电话：
    private String sex;//员工性别：
    private String address;//家庭地址：
    private String password;//登录密码：
    private String birthday;//出生日期：
    private String photo;//用户照片的地址：
    private String zoneNumber;//负责区域：
    private String informationE;//员工备注：
    private String registered;//注册时间：




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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(String zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public String getInformationE() {
        return informationE;
    }

    public void setInformationE(String informationE) {
        this.informationE = informationE;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }
}
