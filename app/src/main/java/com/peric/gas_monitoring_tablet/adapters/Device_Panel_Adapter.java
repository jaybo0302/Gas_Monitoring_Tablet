package com.peric.gas_monitoring_tablet.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peric.gas_monitoring_tablet.POJO.Device;
import com.peric.gas_monitoring_tablet.R;
import com.peric.gas_monitoring_tablet.panels.Device_Panel;
import com.peric.gas_monitoring_tablet.panels.Gas_Panel_Activity;
import com.peric.gas_monitoring_tablet.widgets.ElectricityView;

import java.util.List;

import me.zhouzhuo.zzhorizontalprogressbar.ZzHorizontalProgressBar;

public class Device_Panel_Adapter extends RecyclerView.Adapter{
    private OnRecyclerItemLongListener mOnItemLong = null;
    private List<Device> device_list;

    static class ViewHolder extends RecyclerView.ViewHolder  implements View.OnLongClickListener {
        ConstraintLayout toolbar;  //标题栏
        TextView title;
        TextView postion;
        TextView elec_percent;  //电量百分比
        ElectricityView elec;  //电量插件
        ZzHorizontalProgressBar pb_1;  //氧气进度条
        ZzHorizontalProgressBar pb_2;  //可燃气进度条
        TextView O2;  //氧气text
        TextView CH4;  //可燃气text
        TextView person_num; //人员数量
        TextView comm;  //通讯状态text，由iconfont.tff
        TextView icon_person; //人员数量图标
        TextView temperature;
        private OnRecyclerItemLongListener mOnItemLong = null;

        public ViewHolder(View itemView,OnRecyclerItemLongListener longListener) {
            super(itemView);
            toolbar = itemView.findViewById(R.id.toolbar);
            title = itemView.findViewById(R.id.deivce_title);
            elec_percent = itemView.findViewById(R.id.elec_percent);
            elec = itemView.findViewById(R.id.elec);
            pb_1 = itemView.findViewById(R.id.pb_1);
            pb_2 = itemView.findViewById(R.id.pb_2);
            O2 = itemView.findViewById(R.id.O2);
            CH4 = itemView.findViewById(R.id.CH4);
            person_num = itemView.findViewById(R.id.person_num);
            comm = itemView.findViewById(R.id.comm);
            icon_person = itemView.findViewById(R.id.icon_person);
            postion = itemView.findViewById(R.id.device_position);
            temperature = itemView.findViewById(R.id.temperature);
            elec.setPower(0.0f);
            elec.setMainColor(Color.WHITE);

            Typeface iconfont = Typeface.createFromAsset(Gas_Panel_Activity.context.getAssets(), "iconfont/iconfont.ttf");
            comm.setTypeface(iconfont);
            icon_person.setTypeface(iconfont);
            this.mOnItemLong = longListener;
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            if(mOnItemLong != null){
                mOnItemLong.onItemLongClick(view,getPosition());
            }
            return true;
        }
    }

    public Device_Panel_Adapter(List<Device> device_list){
        this.device_list = device_list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_panel, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnItemLong);
        return holder;
    }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Device device = device_list.get(position);
        ((ViewHolder)holder).title.setText(device.getDevice_id());
        ((ViewHolder)holder).elec_percent.setText(device.getElec_percent() + " %");
        ((ViewHolder)holder).elec.setPower(Float.parseFloat(device.getElec_percent())/100);
        ((ViewHolder)holder).O2.setText(device.getO2());
        ((ViewHolder)holder).pb_1.setProgress(Float.valueOf(device.getO2()).intValue());
        ((ViewHolder)holder).CH4.setText(device.getCH4());
        ((ViewHolder)holder).pb_2.setProgress(Float.valueOf(device.getCH4()).intValue());
        ((ViewHolder)holder).person_num.setText(device.getPerson_num());
        ((ViewHolder)holder).postion.setText(device.getPosition());
        ((ViewHolder)holder).temperature.setText(device.getTemperature());
        //颜色设置
        //通讯图标颜色
        if(System.currentTimeMillis() - device.getUpdateTime() > 3*60*1000){
            ((ViewHolder)holder).comm.setTextColor(Gas_Panel_Activity.context.getResources().getColor(R.color.theme_red));
        }else{
            ((ViewHolder)holder).comm.setTextColor(Gas_Panel_Activity.context.getResources().getColor(R.color.progress_bar));
        }
        //氧气进度条颜色
        if(device.getO2_state() == 2){
            ((ViewHolder)holder).pb_1.setProgressColor(Gas_Panel_Activity.context.getResources().getColor(R.color.theme_red));
        }else if(device.getO2_state() == 1){
            ((ViewHolder)holder).pb_1.setProgressColor(Gas_Panel_Activity.context.getResources().getColor(R.color.theme_yellow));
        }else{
            ((ViewHolder)holder).pb_1.setProgressColor(Gas_Panel_Activity.context.getResources().getColor(R.color.progress_bar));
        }
        //可燃气进度条颜色
        if(device.getCH4_state() == 2){
            ((ViewHolder)holder).pb_2.setProgressColor(Gas_Panel_Activity.context.getResources().getColor(R.color.theme_red));
        }else if(device.getCH4_state() == 1){
            ((ViewHolder)holder).pb_2.setProgressColor(Gas_Panel_Activity.context.getResources().getColor(R.color.theme_yellow));
        }else{
            ((ViewHolder)holder).pb_2.setProgressColor(Gas_Panel_Activity.context.getResources().getColor(R.color.progress_bar));
        }
        //面板头部颜色
        int device_state =  device.getO2_state() > device.getCH4_state()? device.getO2_state() : device.getCH4_state();
        if(device_state == 2){
            ((ViewHolder)holder).toolbar.setBackgroundColor(Gas_Panel_Activity.context.getResources().getColor(R.color.theme_red));
        }else if(device_state == 1){
            ((ViewHolder)holder).toolbar.setBackgroundColor(Gas_Panel_Activity.context.getResources().getColor(R.color.theme_yellow));
        }else{
            ((ViewHolder)holder).toolbar.setBackgroundColor(Gas_Panel_Activity.context.getResources().getColor(R.color.theme_black));
        }

    }

    @Override
    public int getItemCount() {
        return device_list.size();
    }

    public void update(List<Device> device_list){
        this.device_list = device_list;
        notifyDataSetChanged();
    }

    public interface OnRecyclerItemLongListener{
        void onItemLongClick(View view,int position);
    }

    public void setOnItemLongClickListener(OnRecyclerItemLongListener listener){
        this.mOnItemLong =  listener;
    }

}
