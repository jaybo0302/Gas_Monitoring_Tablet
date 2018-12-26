package com.peric.gas_monitoring_tablet.utils;

import com.peric.gas_monitoring_tablet.POJO.GasData;

/**
 * Created by cd on 2018/8/17.
 */

public final class GasDataUtil {
    private  GasDataUtil(){}

    public static GasData dataHandle(String data) {
        System.out.println(data);
        if(data != null ){
            data = data.toUpperCase();
            if(data.substring(0, 2).equals("FF")){
                GasData gd = new GasData();
                gd.setDestAddr(String.valueOf(Integer.parseInt(data.substring(2, 4), 16)));
                gd.setLocalAddr(String.valueOf(Integer.parseInt(data.substring(4, 6), 16)));
                gd.setState(String.valueOf(Integer.parseInt(data.substring(6, 8), 16)));

                switch(data.substring(8,10)) {
                    case "01" : gd.setGas("O2");break;
                    case "02" : gd.setGas("CL2");break;
                    case "03" : gd.setGas("NH3");break;
                    case "04" : gd.setGas("O3");break;
                    case "05" : gd.setGas("EX");break;
                    case "06" : gd.setGas("CO");break;
                    case "07" : gd.setGas("NO");break;
                    case "08" : gd.setGas("N20");break;
                    case "09" : gd.setGas("CO2");break;
                    case "0A" : gd.setGas("NO2");break;
                    case "0B" : gd.setGas("SO2");break;
                    case "0C" : gd.setGas("HCL");break;
                    case "0D" : gd.setGas("H2S");break;
                    case "0E" : gd.setGas("VOC");break;
                }

                switch(data.substring(10, 12)) {
                    case "E1": gd.setDataUnit("%LEL");break;
                    case "E2": gd.setDataUnit("%VOL");break;
                    case "E3": gd.setDataUnit("PPM");break;
                    case "E4": gd.setDataUnit("PPB");break;
                    case "E5": gd.setDataUnit("mg/m3");break;
                    case "E6": gd.setDataUnit("ug/m3");break;
                }
                if(gd.getDataUnit().equals("%LEL") || gd.getDataUnit().equals("%VOL") || gd.getDataUnit().equals("mg/m3")) {
                    int i1 = Integer.parseInt(data.substring(12, 14), 16);
                    if(i1 > 9999) {i1 = 9999;}
                    int i2 = Integer.parseInt(data.substring(14, 16), 16);
                    gd.setGasLevel(new java.text.DecimalFormat("0.0").format(Double.valueOf(String.valueOf(i1) + "." + String.valueOf(i2))));
                }else {
                    gd.setGasLevel(String.valueOf(Integer.parseInt((data.substring(12, 14)) + data.substring(14, 16), 16)));
                }
                if(gd.getDataUnit().equals("%LEL") || gd.getDataUnit().equals("%VOL") || gd.getDataUnit().equals("mg/m3")) {
                    int i1 = Integer.parseInt(data.substring(16, 18), 16);
                    if(i1 > 9999) {i1 = 9999;}
                    int i2 = Integer.parseInt(data.substring(18, 20), 16);
                    gd.setLow(new java.text.DecimalFormat("0.0").format(Double.valueOf(String.valueOf(i1) + "." + String.valueOf(i2))));
                }else {
                    gd.setLow(String.valueOf(Integer.parseInt((data.substring(16, 18) + data.substring(18, 20)), 16)));
                }
                if(gd.getDataUnit().equals("%LEL") || gd.getDataUnit().equals("%VOL") || gd.getDataUnit().equals("mg/m3")) {
                    int i1 = Integer.parseInt(data.substring(20, 22), 16);
                    if(i1 > 9999) {i1 = 9999;}
                    int i2 = Integer.parseInt(data.substring(22, 24), 16);
                    gd.setHigh(new java.text.DecimalFormat("0.0").format(Double.valueOf(String.valueOf(i1) + "." + String.valueOf(i2))));
                }else {
                    //i = Integer.parseInt(datas[10], 16) * 100 + Integer.parseInt(datas[11], 16);
                    gd.setHigh(String.valueOf(Integer.parseInt((data.substring(20, 22) + data.substring(22, 24)), 16)));
                }
                switch(data.substring(24, 26)) {
                    case "01" : gd.setGas1("O2");break;
                    case "02" : gd.setGas1("CL2");break;
                    case "03" : gd.setGas1("NH3");break;
                    case "04" : gd.setGas1("O3");break;
                    case "05" : gd.setGas1("EX");break;
                    case "06" : gd.setGas1("CO");break;
                    case "07" : gd.setGas1("NO");break;
                    case "08" : gd.setGas1("N20");break;
                    case "09" : gd.setGas1("CO2");break;
                    case "0A" : gd.setGas1("NO2");break;
                    case "0B" : gd.setGas1("SO2");break;
                    case "0C" : gd.setGas1("HCL");break;
                    case "0D" : gd.setGas1("H2S");break;
                    case "0E" : gd.setGas1("VOC");break;
                }

                switch(data.substring(26, 28)) {
                    case "E1": gd.setDataUnit1("%LEL");break;
                    case "E2": gd.setDataUnit1("%VOL");break;
                    case "E3": gd.setDataUnit1("PPM");break;
                    case "E4": gd.setDataUnit1("PPB");break;
                    case "E5": gd.setDataUnit1("mg/m3");break;
                    case "E6": gd.setDataUnit1("ug/m3");break;
                }

                if(gd.getDataUnit1().equals("%LEL") || gd.getDataUnit1().equals("%VOL") || gd.getDataUnit1().equals("mg/m3")) {
                    int i1 = Integer.parseInt(data.substring(28, 30), 16);
                    if(i1 > 9999) {i1 = 9999;}
                    int i2 = Integer.parseInt(data.substring(30, 32), 16);
                    gd.setGasLevel1(new java.text.DecimalFormat("0.0").format(Double.valueOf(String.valueOf(i1) + "." + String.valueOf(i2))));
                }else {
                    gd.setGasLevel1(String.valueOf(Integer.parseInt((data.substring(28, 30)) + data.substring(30, 32), 16)));
                }
                if(gd.getDataUnit1().equals("%LEL") || gd.getDataUnit1().equals("%VOL") || gd.getDataUnit1().equals("mg/m3")) {
                    int i1 = Integer.parseInt(data.substring(32, 34), 16);
                    if(i1 > 9999) {i1 = 9999;}
                    int i2 = Integer.parseInt(data.substring(34 ,36), 16);
                    gd.setLow1(new java.text.DecimalFormat("0.0").format(Double.valueOf(String.valueOf(i1) + "." + String.valueOf(i2))));
                }else {
                    //i = Integer.parseInt(datas[8], 16) * 100 + Integer.parseInt(datas[9], 16);
                    gd.setLow1(String.valueOf(Integer.parseInt((data.substring(32, 34) + data.substring(34, 36)), 16)));
                }
                if(gd.getDataUnit1().equals("%LEL") || gd.getDataUnit1().equals("%VOL") || gd.getDataUnit1().equals("mg/m3")) {
                    int i1 = Integer.parseInt(data.substring(36, 38), 16);
                    if(i1 > 9999) {i1 = 9999;}
                    int i2 = Integer.parseInt(data.substring(38 ,40), 16);
                    gd.setHigh1(new java.text.DecimalFormat("0.0").format(Double.valueOf(String.valueOf(i1) + "." + String.valueOf(i2))));
                }else {
                    //i = Integer.parseInt(datas[10], 16) * 100 + Integer.parseInt(datas[11], 16);
                    gd.setHigh1(String.valueOf(Integer.parseInt((data.substring(36, 38) + data.substring(38, 40)), 16)));
                }
                int i = Integer.parseInt(data.substring(40, 42), 16);
                if(i > 127) {
                    i = i - 255;
                }
                gd.setTemperature(String.valueOf(i));
                gd.setDeO2Voltage(String.valueOf(Integer.parseInt((data.substring(42, 44) + data.substring(44, 46)), 16)));
                gd.setDeComVoltage(String.valueOf(Integer.parseInt((data.substring(46, 48) + data.substring(48, 50)), 16)));
                gd.setDePowerVoltage(String.valueOf(Integer.parseInt((data.substring(50, 52) + data.substring(52, 54)), 16)));
                gd.setDeTemp(String.valueOf(Integer.parseInt(data.substring(54, 56), 16)));
                gd.setCheck(String.valueOf(Integer.parseInt(data.substring(56, 58), 16)));

                if(gd.getGas().equals("O2")) {
                    if(Double.parseDouble(gd.getGasLevel()) < Double.parseDouble(gd.getLow())){
                        gd.setWarningState(1);
                        gd.setO2Warning(1);
                    }else if(Double.parseDouble(gd.getGasLevel()) >= Double.parseDouble(gd.getLow()) && Double.parseDouble(gd.getGasLevel()) < Double.parseDouble(gd.getHigh())){
                        gd.setWarningState(0);
                    }else{
                        gd.setWarningState(1);
                        gd.setO2Warning(1);
                    }
                }
                if(gd.getGas1().equals("EX")) {
                    if(Double.parseDouble(gd.getGasLevel1()) < Double.parseDouble(gd.getLow1())){
                        if(gd.getWarningState() != 0) {
                            gd.setWarningState(0);
                        }
                    }else if(Double.parseDouble(gd.getGasLevel1()) >= Double.parseDouble(gd.getLow1()) && Double.parseDouble(gd.getGasLevel1()) < Double.parseDouble(gd.getHigh1())){
                        gd.setWarningState(1);
                        gd.setCH4Warning(1);
                    }else{
                        gd.setWarningState(1);
                        gd.setCH4Warning(1);
                    }
                }
                return gd;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
}
