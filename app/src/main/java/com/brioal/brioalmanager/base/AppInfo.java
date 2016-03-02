package com.brioal.brioalmanager.base;

import android.graphics.drawable.Drawable;

/**
 * 存储app信息的base类
 * Created by brioal on 16-2-16.
 */
public class AppInfo {
    private Drawable appIcon ; //应用图标
    private String appName ; //应用名称
    private String appPackage; //应用包名
    private String appDesc; //应用描述
    private long  appTime; //应用安装时间

    public AppInfo(String appName, String appPackage, String appDesc, long appTime,Drawable appIcon) {
        this.appName = appName;
        this.appPackage = appPackage;
        this.appDesc = appDesc;
        this.appTime = appTime;
        this.appIcon = appIcon;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }


    public String getAppName() {
        return appName;
    }


    public String getAppPackage() {
        return appPackage;
    }


    public String getAppDesc() {
        return appDesc;
    }


    public long getAppTime() {
        return appTime;
    }

}
