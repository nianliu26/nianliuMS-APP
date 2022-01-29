package com.dh.nianliums.Module;

import android.app.Application;

public class StaticData extends Application  {
    private String currentName;
    public static final int LOGIN_FLAG = 1;//登录标识符，用于服务器确定执行哪个逻辑
    public static final int LOGON_FLAG = 2;//注册标识符
    public static final int IMAGE_FLAG = 3;//发送图片标识符
    public static final String WEB_PATH = "ms/login";//TODO 网页地址，部署到云服务器上需要更改
    //TODO 标识符还未完善


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }


    public static int getLoginFlag() {
        return LOGIN_FLAG;
    }

    public static int getLogonFlag() {
        return LOGON_FLAG;
    }

    public static int getImageFlag() {
        return IMAGE_FLAG;
    }
}
