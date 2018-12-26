package com.peric.gas_monitoring_tablet.panels;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.SerialPortServiceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.peric.gas_monitoring_tablet.POJO.Device;
import com.peric.gas_monitoring_tablet.POJO.GasData;
import com.peric.gas_monitoring_tablet.R;
import com.peric.gas_monitoring_tablet.adapters.Device_Panel_Adapter;
import com.peric.gas_monitoring_tablet.common.Constants;
import com.peric.gas_monitoring_tablet.utils.AppConfig;
import com.peric.gas_monitoring_tablet.utils.DataReceiveServer;
import com.peric.gas_monitoring_tablet.utils.GasDataUtil;
import com.peric.gas_monitoring_tablet.utils.WarnLogUtil;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

import static com.peric.gas_monitoring_tablet.common.Constants.device_list;

public class Gas_Panel_Activity extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION";
    private static final String TAG = "gas-monitoring";
    private CH34xUARTDriver driver;
    public static Context context;
    private Toolbar toolbar;
    private int retval;
    public static  Device_Panel_Adapter adapter;
    private  RecyclerView recyclerView;
    private SerialPortServiceManager mSeriport;
    private byte[] read_buffer = new byte[1024];
    public static byte[]  part_serialPortNode = null;
    public static int     part_baud = 9600;
    public static int     part_data_size = 8;
    public static int     part_stop_bit = 1;
    public static Set<Integer> warnSet = new HashSet<>();
    public static MediaPlayer mediaPlayer;
    public static final int VHIGH = 4100;
    public static final int VLOW = 700;
    private static read_thread rt;
    private long mExitTime = 0;
    private static class MyHandler extends Handler {
        private final WeakReference<Gas_Panel_Activity> mActivity;
        public MyHandler(Gas_Panel_Activity activity) {
            mActivity = new WeakReference<Gas_Panel_Activity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final Gas_Panel_Activity currentActivity = mActivity.get();
            try{
                if (currentActivity != null) {
                    Log.i(TAG, msg.obj.toString());
                    GasData gd = GasDataUtil.dataHandle(msg.obj.toString().replace(" ", ""));
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
                            warnSet.add(location);
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                            }
                            adapter.update(device_list);
                            //记录报警日志
                            WarnLogUtil.writeWarnLog(gd);
                        } else {
                            warnSet.remove(location);
                            if (warnSet.size() < 1) {
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.pause();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private final MyHandler mHandler = new MyHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_panel);
        context = getApplicationContext();
        //初始化位置配置文件
        File file = new File(getCacheDir(), AppConfig.APP_CONFIG);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 保持常亮的屏幕的状态
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        toolbar = findViewById(R.id.toolbar_panel);
        setSupportActionBar(toolbar);
        //初始化位置集合
        final AppConfig appConfig = AppConfig.getInstance(this);
        appConfig.getKeyValue();
        Constants.initDeviceList();
        //创建列表，recyclerView
        recyclerView = findViewById(R.id.recycler_view);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new Device_Panel_Adapter(Constants.device_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        adapter.setOnItemLongClickListener(new Device_Panel_Adapter.OnRecyclerItemLongListener() {
               @Override
               public void onItemLongClick(View view, final int position) {
                   final EditText et = new EditText(context);
                   new AlertDialog.Builder(Gas_Panel_Activity.this).setTitle("输入位置（过长字符可能影响界面显示）")
                           .setIcon(R.mipmap.ic_launcher)
                           .setView(et)
                           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   appConfig.set(String.valueOf(position + 1), et.getText().toString());
                                   appConfig.getKeyValue();
                                   device_list.get(position).setPosition(et.getText().toString());
                                   adapter.update(device_list);
                               }
                           }).setNegativeButton("取消",null).show();
               }
        });
        /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });*/
        /* 340驱动代码
        driver = new CH34xUARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), context,
                ACTION_USB_PERMISSION);
        //判断系统是否支持USB HOST
        if (!driver.UsbFeatureSupported()) {
            Dialog dialog = new AlertDialog.Builder(Gas_Panel_Activity.this)
                    .setTitle("提示")
                    .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    System.exit(0);
                                }
                            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        //打开串口链接。
        retval = driver.ResumeUsbList();
        // ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
        if (retval == -1) {
            Toast.makeText(Gas_Panel_Activity.this, "打开设备失败!请重新拔插设备！",
                    Toast.LENGTH_SHORT).show();
            driver.CloseDevice();
        } else if (retval == 0){
            //对串口设备进行初始化操作
            if (!driver.UartInit()) {
                Toast.makeText(Gas_Panel_Activity.this, "设备初始化失败!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(Gas_Panel_Activity.this, "打开设备成功!", Toast.LENGTH_SHORT).show();
            //new readThread().start();//开启读线程读取串口接收的数据
            driver.SetConfig(9600, (byte)8, (byte)1, (byte)0, (byte)0);
            new readThread().start();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("未授权限");
            builder.setMessage("确认退出吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }*/

        //更新界面先更新device_list，再调用adapter的update方法
        //adapter.update(device_list);
        // 取得串口服务；
        mSeriport = new SerialPortServiceManager(0);//0--->colse log  1---->open log
        if(mSeriport == null) {
            //串口工具创建失败。
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("警告");
            builder.setPositiveButton("确定", null);
            // builder.setIcon(android.R.drawable.xx);
            builder.setMessage("串口工具创建失败");
            builder.show();
        }
        part_serialPortNode = new String("/dev/ttyS0\0").getBytes();
        int ret = mSeriport.open(part_serialPortNode, part_baud, part_data_size, part_stop_bit); // 打开一个串口；
        Log.d(TAG, new String(part_serialPortNode) +"   baud:" + part_baud + "  "+"Button open return:"+ret);
        if(ret <= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("警告");
            builder.setPositiveButton("确定", null);
            // builder.setIcon(android.R.drawable.xx);
            builder.setMessage("设备打开异常" + "(" + ret + ")");
            builder.show();
        } else {
            rt = new read_thread();
            rt.start();
        }
        new Timer().schedule(new UpdateTask(), 2000, 5000);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_panel);
        //menu item点击事件监听
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.receive:
                        //开启串口设备，接受数据。关闭网路
                        if (mSeriport.isReady() < 1) {
                            mSeriport.open(part_serialPortNode, part_baud, part_data_size, part_stop_bit); // 打开一个串口；
                        }
                        DataReceiveServer.stop();
                        break;
                    case R.id.net:
                        //开启网络端口，接受网络数据。关闭串口
                        if(mSeriport.isReady() > 0) {
                            mSeriport.close();
                        }
                        DataReceiveServer.start();
                        break;
                    case R.id.warn_log:
                        Intent i = new Intent(context, WarnLogActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        break;
                }
                return false;
            }
        });
        initMediaPlayer();
    }

    /**
     * 初始化报警音乐
     */
    private void initMediaPlayer() {
        try {
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor file = getAssets().openFd("warn.mp3");
            mediaPlayer.setDataSource(file.getFileDescriptor()); // 指定音频文件的路径
            mediaPlayer.prepare(); // 让MediaPlayer进入到准备状态
            mediaPlayer.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新界面数据任务
     */
    private class UpdateTask extends TimerTask {
        @Override
        public void run() {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    adapter.update(device_list);
                }
            });
        }
    }

    /**
     * ch340驱动接受数据线程
     */
    private class readThread extends Thread {
        public void run() {
            byte[] buffer = new byte[29];
            while (true) {
                Message msg = Message.obtain();
                int length = driver.ReadData(buffer, 29);
                if (length == 29) {
                    if ((buffer[0] & 0xff) != 0xff) {
                        continue;
                    }
                    int checkPlus = 0;
                    for(int i = 0; i < 28; i++) {
                        checkPlus += ((buffer[i]) & 0xff);
                    }
                    checkPlus = ~checkPlus + 1;
                    String cps = Integer.toHexString(checkPlus);
                    cps = cps.substring(cps.length() - 2, cps.length());
                    String check = 	Integer.toHexString(0xFF & buffer[28]);
                    if(check.length() == 1) {check = "0" + check;}
                    System.out.println(check);
                    System.out.println(cps);
                    if(!check.equals(cps)) {
                        continue;
                    }
                    String recv = hex2String(buffer, length);
                    msg.obj = recv;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    // 新芯片驱动数据读取线程 ------------------------------------------------------->
    // read serialport data thread
    public class read_thread extends Thread {
        private int ret_receive = 0;
        @Override
        public void run() {
            while (!this.isInterrupted()) {
                if(mSeriport.isReady() > 0) {
                    ret_receive = mSeriport.wait_data();
                    if(ret_receive > 0){
                        ret_receive = mSeriport.read(read_buffer, 29, 100);// 10ms read
                        System.out.println(ret_receive);
                        if (ret_receive > 0) {
                            String insert_data = null;
                            try {
                                if (ret_receive == 29) {
                                    if ((read_buffer[0] & 0xff) != 0xff) {
                                        continue;
                                    }
                                    int checkPlus = 0;
                                    for (int i = 0; i < 28; i++) {
                                        checkPlus += ((read_buffer[i]) & 0xff);
                                    }
                                    checkPlus = ~checkPlus + 1;
                                    String cps = Integer.toHexString(checkPlus);
                                    cps = cps.substring(cps.length() - 2, cps.length());
                                    String check = Integer.toHexString(0xFF & read_buffer[28]);
                                    if (check.length() == 1) {
                                        check = "0" + check;
                                    }
                                    if (!check.equals(cps)) {
                                        continue;
                                    }
                                    insert_data = hex2String(read_buffer, ret_receive);
                                    //insert_data = new String(read_buffer, 0, ret_receive, "ISO-8859-1");
                                } else {
                                    continue;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message msg = Message.obtain();
                            msg.obj = insert_data;
                            mHandler.sendMessage(msg);
                        }
                    }
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 16进制字节转字符串
     * @param b 字节数组
     * @param length 字节长度
     * @return
     */
    public static String hex2String(byte[] b, int length) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < length; i++) {
            String hex = Integer.toHexString(0xFF & b[i]);
            if(hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
            sb.append(" ");
        }
        String s = sb.toString();
        return s;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);//加载menu布局
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                rt.interrupt();
                System.exit(0);
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
