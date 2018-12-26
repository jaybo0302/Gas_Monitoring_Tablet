package com.peric.gas_monitoring_tablet.utils;

import android.content.Context;

import com.peric.gas_monitoring_tablet.common.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by cd on 2018/9/19.
 */

public class AppConfig {
    public static final String APP_CONFIG = "config";

    private static AppConfig instance;
    private static Context mContext;

    private AppConfig(Context context) {
        mContext = context.getApplicationContext();
    }

    public static AppConfig getInstance(Context context) {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig(context);
                }
            }
        }
        return instance;
    }

    private Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            fis = new FileInputStream(new File(mContext.getCacheDir(), APP_CONFIG));
            props.load(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }
    public void getKeyValue(){
        Properties props = get();
        for (Object obj : props.keySet()) {
            Constants.positionMap.put(Integer.parseInt(String.valueOf(obj)), String.valueOf(props.get(obj)));
        }
    }
    /**
     * 对外提供的get方法
     * @param key
     * @return
     */
    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(mContext.getCacheDir(), APP_CONFIG));
            p.store(fos, null);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对外提供的保存key value方法
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    /**
     * 对外提供的删除方法
     * @param key
     */
    public void remove(String... key) {
        Properties props = get();
        for (String k : key) {
            props.remove(k);
        }
        setProps(props);
    }
}