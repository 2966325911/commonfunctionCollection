package com.kangengine.customview.util.crash;

/**
 * @author : Vic
 * time    : 2018-11-09 15:49
 * desc    :
 */

public class CrashInfo {
    // 平台
    private String platform;
    // 版本号
    private String osversion;
    // 制造商
    private String manufacturer;
    // 手机型号
    private String model;
    // 厂商定制系统型号
    private String display;
    private String time;
    private String versionName;
    private String versionCode;
    private String content;

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOsversion() {
        return this.osversion;
    }

    public void setOsversion(String osversion) {
        this.osversion = osversion;
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVersionCode() {
        return this.versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}

