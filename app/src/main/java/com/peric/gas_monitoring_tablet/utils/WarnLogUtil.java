package com.peric.gas_monitoring_tablet.utils;

import android.os.Environment;

import com.peric.gas_monitoring_tablet.POJO.GasData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cd on 2018/9/19.
 */

public final class WarnLogUtil {
    private  WarnLogUtil(){}

    private final static String WARNLOGDIR = "gasmonitor";
    private final static String LOGFILESUFFIX = ".log";
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void writeWarnLog(GasData gd) {
        String gasPath = Environment.getExternalStorageDirectory().getPath() + "/" + WARNLOGDIR;
        File gasDir = new File(gasPath);
        if (!gasDir.exists()) {
            gasDir.mkdir();
        }
        File currentLogFile = new File(gasPath+"/"+ sdf.format(new Date()).split(" ")[0] + LOGFILESUFFIX);
        if (!currentLogFile.exists()) {
            try {
                currentLogFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter bwHour = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentLogFile,true)));
            //报警格式      设备1|氧气（可燃气）报警|32.3|3.9|2018-12-12 12:12:12
            bwHour.write("设备" + gd.getLocalAddr() + "|" + (gd.getCH4Warning() == 1 ? "可燃气" : "氧气") + "报警|" + gd.getGasLevel() + "|" + gd.getGasLevel1() + "|" + sdf.format(new Date()) + "\r\n");
            bwHour.flush();
            bwHour.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readWarnLog(String date) {
        String gasPath = Environment.getExternalStorageDirectory().getPath() + "/" + WARNLOGDIR;
        File gasDir = new File(gasPath);
        if (!gasDir.exists()) {
            gasDir.mkdir();
        }
        File currentLogFile = new File(gasPath+"/"+ date + LOGFILESUFFIX);
        if (!currentLogFile.exists()) {
            return "";
        } else {
            String content = "";
            try {
                BufferedReader br = new BufferedReader(new FileReader(currentLogFile));
                String line = null;
                while ((line = br.readLine()) != null) {
                    content += (line + "\r\n");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }
    }
}
