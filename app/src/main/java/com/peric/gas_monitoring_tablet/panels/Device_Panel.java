package com.peric.gas_monitoring_tablet.panels;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.peric.gas_monitoring_tablet.widgets.ElectricityView;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

import com.peric.gas_monitoring_tablet.R;

/**
 * Created by BigDeibo on 2018/7/19.
 *
 * 平板中已废弃
 */

public class Device_Panel extends ConstraintLayout {

    private ConstraintLayout toolbar;  //标题栏
    private TextView title;
    private TextView elec_percent;  //电量百分比
    private ElectricityView elec;  //电量插件
    private ZzHorizontalProgressBar pb_1;  //氧气进度条
    private ZzHorizontalProgressBar pb_2;  //可燃气进度条
    private TextView O2;  //氧气text
    private TextView CH4;  //可燃气text
    private TextView person_num; //人员数量
    private TextView comm;  //通讯状态text，由iconfont.tff
    private TextView icon_person; //人员数量图标

    public Device_Panel(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.device_panel, this);

        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.deivce_title);
        elec_percent = findViewById(R.id.elec_percent);
        elec = findViewById(R.id.elec);
        pb_1 = findViewById(R.id.pb_1);
        pb_2 = findViewById(R.id.pb_2);
        O2 = findViewById(R.id.O2);
        CH4 = findViewById(R.id.CH4);
        person_num = findViewById(R.id.person_num);
        comm = findViewById(R.id.comm);
        icon_person = findViewById(R.id.icon_person);

        elec.setPower(0.0f);
        elec.setMainColor(Color.WHITE);
    }

    public ConstraintLayout getToolbar() {
        return toolbar;
    }

    public void setToolbar(ConstraintLayout toolbar) {
        this.toolbar = toolbar;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public TextView getElec_percent() {
        return elec_percent;
    }

    public void setElec_percent(TextView elec_percent) {
        this.elec_percent = elec_percent;
    }

    public ElectricityView getElec() {
        return elec;
    }

    public void setElec(ElectricityView elec) {
        this.elec = elec;
    }

    public ZzHorizontalProgressBar getPb_1() {
        return pb_1;
    }

    public void setPb_1(ZzHorizontalProgressBar pb_1) {
        this.pb_1 = pb_1;
    }

    public ZzHorizontalProgressBar getPb_2() {
        return pb_2;
    }

    public void setPb_2(ZzHorizontalProgressBar pb_2) {
        this.pb_2 = pb_2;
    }

    public TextView getO2() {
        return O2;
    }

    public void setO2(TextView o2) {
        O2 = o2;
    }

    public TextView getCH4() {
        return CH4;
    }

    public void setCH4(TextView CH4) {
        this.CH4 = CH4;
    }

    public TextView getPerson_num() {
        return person_num;
    }

    public void setPerson_num(TextView person_num) {
        this.person_num = person_num;
    }

    public TextView getComm() {
        return comm;
    }

    public void setComm(TextView comm) {
        this.comm = comm;
    }

    public TextView getIcon_person() {
        return icon_person;
    }

    public void setIcon_person(TextView icon_person) {
        this.icon_person = icon_person;
    }
}
