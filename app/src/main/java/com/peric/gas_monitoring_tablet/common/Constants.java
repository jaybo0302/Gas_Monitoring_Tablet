package com.peric.gas_monitoring_tablet.common;

import com.peric.gas_monitoring_tablet.POJO.Device;
import com.peric.gas_monitoring_tablet.utils.AppConfig;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cd on 2018/9/7.
 */

public class Constants {
    public static List<Device> device_list = new LinkedList<>();
    public static Map<Integer, String> positionMap = new HashMap<>();
    public static void initDeviceList() {
        //初始化deviceList
        device_list.clear();
        for(int i = 0; i < 30; i++){
            Device device = new Device();
            device.setDevice_id("设备" + String.valueOf(i + 1));
            device.setPosition(positionMap.get(i+1));
            device.setElec_percent("0");
            device.setO2("0.0");
            device.setCH4("0.0");
            device.setPerson_num("0");
            device.setComm_state(0);
            device.setO2_state(0);
            device.setCH4_state(0);
            device.setUpdateTime(0);
            device.setTemperature("xx");
            device_list.add(device);
        }
    }
}
