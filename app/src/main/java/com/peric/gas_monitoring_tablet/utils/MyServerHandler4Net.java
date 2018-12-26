package com.peric.gas_monitoring_tablet.utils;

import com.peric.gas_monitoring_tablet.POJO.Device;
import com.peric.gas_monitoring_tablet.POJO.GasData;
import com.peric.gas_monitoring_tablet.common.Constants;
import com.peric.gas_monitoring_tablet.panels.Gas_Panel_Activity;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import static com.peric.gas_monitoring_tablet.common.Constants.device_list;

public class MyServerHandler4Net extends IoHandlerAdapter {
    public static final int VHIGH = 4100;
    public static final int VLOW = 700;
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("服务端与客户端创建连接...");
    }
    @Override
    public void sessionOpened(IoSession session) throws Exception {
    	System.out.println("服务端与客户端连接打开...");
    }
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        System.out.println(message.toString());
        String msg = message.toString().replaceAll("\r|\n", "");
        //如果是中转，将此代码放开。
        GasData gd = GasDataUtil.dataHandle(msg.replace(" ", ""));
        if (gd != null) {
            int location = Integer.parseInt(gd.getLocalAddr()) - 1;
            Device device = device_list.get(location);
            device.setDevice_id("设备" + String.valueOf(location + 1));
            device.setPosition(Constants.positionMap.get(location+1));
            int power = VHIGH;
            if (Integer.parseInt(gd.getDePowerVoltage()) < VHIGH) {
                power = Integer.parseInt(gd.getDePowerVoltage());
            }

            device.setElec_percent(String.valueOf(100-(4100-power)*100/VLOW));

            device.setO2(String.valueOf(gd.getGasLevel()));
            device.setCH4(String.valueOf(gd.getGasLevel1()));
            device.setPerson_num(gd.getDeTemp());
            device.setComm_state(gd.getWarningState());
            device.setO2_state(gd.getO2Warning());
            device.setCH4_state(gd.getCH4Warning());
            device.setUpdateTime(System.currentTimeMillis());
            device.setTemperature(gd.getTemperature());
            if (gd.getO2Warning() == 1 || gd.getCH4Warning() ==1) {
                Gas_Panel_Activity.warnSet.add(location);
                if (!Gas_Panel_Activity.mediaPlayer.isPlaying()) {
                    Gas_Panel_Activity.mediaPlayer.start();
                }
                Gas_Panel_Activity.adapter.update(device_list);
                //记录报警日志
                WarnLogUtil.writeWarnLog(gd);
            } else {
                Gas_Panel_Activity.warnSet.remove(location);
                if (Gas_Panel_Activity.warnSet.size() < 1) {
                    if (Gas_Panel_Activity.mediaPlayer.isPlaying()) {
                        Gas_Panel_Activity.mediaPlayer.pause();
                    }
                }
            }
        }
        //session.close();
    }
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    	System.out.println("服务端发送信息成功...");
//        session.close();
    }
    @Override
    public void sessionClosed(IoSession session) throws Exception {}
    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
    	System.out.println("服务端进入空闲状态...");
    }
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
    	System.out.println("服务端异常..." + cause);
    }
}