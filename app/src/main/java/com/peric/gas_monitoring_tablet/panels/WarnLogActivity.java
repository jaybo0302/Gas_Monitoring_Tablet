package com.peric.gas_monitoring_tablet.panels;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.peric.gas_monitoring_tablet.R;
import com.peric.gas_monitoring_tablet.utils.WarnLogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WarnLogActivity extends Activity {
    private Button dateSelect;
    private TextView dataLog;
    private TextView currentDate;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_log);
        dataLog = findViewById(R.id.log_text);
        dateSelect = findViewById(R.id.date_select);
        currentDate = findViewById(R.id.currentDate);
        context = getApplicationContext();
        // 保持常亮的屏幕的状态
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        currentDate.setText(date);
        //处理日志界面数据
        if ("".equals(WarnLogUtil.readWarnLog(date))) {
            dataLog.setText("该日期暂无报警日志");
        } else {
            dataLog.setText(WarnLogUtil.readWarnLog(date));
        }

        dateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(WarnLogActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = year+"-"+((monthOfYear+1)>9?monthOfYear+1:"0"+(monthOfYear+1))+"-"+(dayOfMonth>9?dayOfMonth:"0"+dayOfMonth);
                        currentDate.setText(date);
                        //处理日志界面数据
                        if ("".equals(WarnLogUtil.readWarnLog(date))) {
                            dataLog.setText("该日期暂无报警日志");
                        } else {
                            dataLog.setText(WarnLogUtil.readWarnLog(date));
                        }
                    }
                },Integer.parseInt(date.split("-")[0]),(Integer.parseInt(date.split("-")[1])-1),Integer.parseInt(date.split("-")[2])).show();
            }
        });
    }
}
