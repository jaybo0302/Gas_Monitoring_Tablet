package com.peric.gas_monitoring_tablet.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.peric.gas_monitoring_tablet.R;

/**
 * 电量百分比的view
 *
 * @author Sherwin.Ye
 * @data 2016年5月5日 下午2:10:30
 * @desc ElectricityView.java
 */
public class ElectricityView extends android.support.v7.widget.AppCompatTextView {

    private static final int COLOR_DEFAULT = Color.rgb(39, 39, 39);

    /**
     * 电量百分比 0.0~1.0
     */
    private float power = 0f;
    /**
     * 角半径
     */
    private int radio = 0;

    /**
     * 边框宽度
     */
    private int border = 0;

    private Paint mPaint;
    /**
     * 标签背景颜色，包括填充色
     */
    private int mainColor = COLOR_DEFAULT;

    /**
     * 得到label背景颜色
     *
     * @return
     */
    public int getMainColor() {
        return mainColor;
    }

    /**
     * 设置label背景颜色
     *
     * @param mainColor
     */
    public void setMainColor(int mainColor) {
        this.mainColor = mainColor;
        invalidate();
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
        invalidate();
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
        invalidate();
    }

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        if (power < 0) {
            power = 0;
        } else if (power > 1) {
            power = 1;
        }
        this.power = power;
        invalidate();
    }

    public ElectricityView(Context context) {
        this(context, null);
    }

    public ElectricityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElectricityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ElectricityView, defStyle, 0);
        border = typedArray.getDimensionPixelSize(R.styleable.ElectricityView_electric_border_width, dp2px(getContext(), 1));
        radio = typedArray.getDimensionPixelSize(R.styleable.ElectricityView_electric_radius, dp2px(getContext(), 2));
        mainColor = typedArray.getColor(R.styleable.ElectricityView_electric_main_color, COLOR_DEFAULT);
        power = typedArray.getFloat(R.styleable.ElectricityView_electric_power, 0.0f);
        if (power < 0) {
            power = 0;
        } else if (power > 1) {
            power = 1;
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(getTextSize());
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public static int dp2px(Context context, float dpValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //		super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        mPaint.setColor(getResources().getColor(R.color.theme_white));

        // 画边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(border);
        mPaint.setAntiAlias(true);// 设置画笔的锯齿效果

        int bodyWidth = width - border; // 电量主体部分的宽度，不包括正极标志矩形部分，正极矩形宽度为border，高度为height/3
        int bodyHeight = height; // 电量主体部分的高度
        RectF ovalf = new RectF(border/2, border/2, bodyWidth - border/2, bodyHeight - border/2);// 设置个新的长方形，边框以线中间为基准计算
        canvas.drawRoundRect(ovalf, radio, radio, mPaint);//第二个参数是x半径，第三个参数是y半径

        // 画正极图标
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        mPaint.setAntiAlias(true);// 设置画笔的锯齿效果

        RectF rightRect = new RectF(bodyWidth, height * 1 / 3, width, height * 2 / 3);// 设置个新的长方形
        canvas.drawRect(rightRect, mPaint);

        // 画电量百分比
        mPaint.setColor(mainColor);
        int powerWidth = bodyWidth - border * 4; // 显示电量百分比部分宽度
        int powerHeight = bodyHeight - border * 4; // 显示电量百分比部分高度

        RectF powerRect = new RectF(border * 2, border * 2, border*2 + powerWidth * power, border * 2 + powerHeight);// 设置个新的长方形
        canvas.drawRect(powerRect, mPaint);

        super.onDraw(canvas);
    }

}
