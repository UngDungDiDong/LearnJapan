package com.japan.jav.learnjapan.setting_khang.model;

public class SettingNotification {
    private String token;
    private String time;
    private String msg;

    public SettingNotification() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SettingNotification(String token, String time, String msg) {

        this.token = token;
        this.time = time;
        this.msg = msg;
    }
}
