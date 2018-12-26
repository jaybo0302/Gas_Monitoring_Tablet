package com.peric.gas_monitoring_tablet.POJO;

import java.util.Date;

public class Device {
    private String device_id;
    private String elec_percent;
    private String O2;
    private String CH4;
    private String person_num; //人员个数
    private String position; //位置，用户自己编辑
    private int comm_state;  //0 通信断开； 1 通信正常
    private int O2_state;  //0 正常； 1 低报； 2高报
    private int CH4_state;  //0 正常； 1 低报； 2高报
    private long updateTime; //最近更新时间
    private String temperature; //温度

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getElec_percent() {
        return elec_percent;
    }

    public void setElec_percent(String elec_percent) {
        this.elec_percent = elec_percent;
    }

    public String getO2() {
        return O2;
    }

    public void setO2(String o2) {
        O2 = o2;
    }

    public String getCH4() {
        return CH4;
    }

    public void setCH4(String CH4) {
        this.CH4 = CH4;
    }

    public String getPerson_num() {
        return person_num;
    }

    public void setPerson_num(String person_num) {
        this.person_num = person_num;
    }

    public int getComm_state() {
        return comm_state;
    }

    public void setComm_state(int comm_state) {
        this.comm_state = comm_state;
    }

    public int getO2_state() {
        return O2_state;
    }

    public void setO2_state(int o2_state) {
        O2_state = o2_state;
    }

    public int getCH4_state() {
        return CH4_state;
    }

    public void setCH4_state(int CH4_state) {
        this.CH4_state = CH4_state;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
