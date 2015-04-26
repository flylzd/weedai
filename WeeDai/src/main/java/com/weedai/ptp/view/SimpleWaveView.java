package com.weedai.ptp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

//http://www.cnblogs.com/uncle2000/p/3796034.html
public class SimpleWaveView extends View {
    private Paint Paint = new Paint();
    private int Color = -141309;
    private int Alpha = 255;
    private Path path = new Path();
    private Handler mHandler;
    private int Frequency = 128;
    private long c = 0L;
    private double PeakSpacing = 3.0D;
    private float SpeedH = 0.04F;
    private float SpeedV = 0.033F;
    private boolean FlagO = true;
    private boolean FlagR = false;
    private float Percentage = 0.6F;
    private float Refresh = 0.0F;
    private float Amplitude = 3.0F;
    private int ColorLevel1 = 0;
    private int ColorLevel2 = 0;
    private int ColorLevel3 = 0;
    private float Level1 = 0.0F;
    private float Level2 = 0.0F;

//    public SimpleWaveView(Context context) {
//        super(context);
//        this.Paint.setStrokeWidth(1.0F);
//        this.Paint.setAlpha(this.Alpha);
//        this.mHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                if ((msg.what != 0) && (msg.what == 1)) {
//                    if ((SimpleWaveView.this.Percentage > 0.0D)
//                            && (SimpleWaveView.this.FlagO)) {
//                        SimpleWaveView.this.Percentage -= SimpleWaveView.this.SpeedV;
//                    } else if (SimpleWaveView.this.Percentage < SimpleWaveView.this.Refresh) {
//                        SimpleWaveView.this.FlagO = false;
//                        SimpleWaveView.this.Percentage += SimpleWaveView.this.SpeedV;
//                    } else {
//                        SimpleWaveView.this.FlagO = true;
//                        SimpleWaveView.this.FlagR = false;
//                    }
//                }
//                SimpleWaveView.this.invalidate();
//            }
//        };
//        new Timer().schedule(new TimerTask() {
//            public void run() {
//                if (SimpleWaveView.this.FlagR)
//                    SimpleWaveView.this.mHandler.sendEmptyMessage(1);
//                else
//                    SimpleWaveView.this.mHandler.sendEmptyMessage(0);
//            }
//        }, 0L, this.Frequency);
//    }

    public SimpleWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.Paint.setStrokeWidth(1.0F);
        this.Paint.setAlpha(this.Alpha);
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if ((msg.what != 0) && (msg.what == 1)) {
                    if ((SimpleWaveView.this.Percentage > 0.0D)
                            && (SimpleWaveView.this.FlagO)) {
                        SimpleWaveView.this.Percentage -= SimpleWaveView.this.SpeedV;
                    } else if (SimpleWaveView.this.Percentage < SimpleWaveView.this.Refresh) {
                        SimpleWaveView.this.FlagO = false;
                        SimpleWaveView.this.Percentage += SimpleWaveView.this.SpeedV;
                    } else {
                        SimpleWaveView.this.FlagO = true;
                        SimpleWaveView.this.FlagR = false;
                    }
                }
                SimpleWaveView.this.invalidate();
            }
        };
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (SimpleWaveView.this.FlagR)
                    SimpleWaveView.this.mHandler.sendEmptyMessage(1);
                else
                    SimpleWaveView.this.mHandler.sendEmptyMessage(0);
            }
        }, 0L, this.Frequency);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int width = getWidth();
        int height = getHeight();
        if (this.Level1 == 0.0F)
            this.Paint.setColor(this.Color);
        else if (this.Level2 == 0.0F) {
            if ((this.Percentage >= 0.0F) && (this.Percentage < this.Level1))
                this.Paint.setColor(this.ColorLevel1);
            else
                this.Paint.setColor(this.ColorLevel2);
        } else if ((this.Percentage >= 0.0F) && (this.Percentage < this.Level1))
            this.Paint.setColor(this.ColorLevel1);
        else if ((this.Percentage >= this.Level1)
                && (this.Percentage < this.Level2))
            this.Paint.setColor(this.ColorLevel2);
        else if ((this.Percentage >= this.Level2) && (this.Percentage <= 1.0F)) {
            this.Paint.setColor(this.ColorLevel3);
        }

        if ((width == 0) || (height == 0)) {
            canvas.drawRect(0.0F, height / 2, width, height, this.Paint);
            return;
        }
        if (this.c >= 8388607L) {
            this.c = 0L;
        }
        this.c += 1L;
        float f1 = height * (1.0F - this.Percentage);
        int top = (int) (f1 + this.Amplitude);
        this.path.reset();
        this.path
                .addCircle(width / 2, width / 2, width / 2, Path.Direction.CCW);
        canvas.clipPath(this.path, Region.Op.REPLACE);
        canvas.drawRect(0.0F, top, width, height, this.Paint);

        int stopX = 0;
        while (stopX < width) {
            int startY = (int) (f1 - this.Amplitude
                    * Math.sin(3.141592653589793D
                    * (2.0D * (stopX * this.PeakSpacing + (float) (this.c * width)
                    * this.SpeedH * 2.0F)) / width));
            canvas.drawLine(stopX, startY, stopX, top, this.Paint);
            int i4 = stopX + 1;
            stopX = i4;
        }
        canvas.restore();
    }


    /**
     * Level1在(0%,100%)之间则ColorLevel1，ColorLevel2生效，有两种颜色。Level2在(Level1,100%)之间则ColorLevel1，ColorLevel2，ColorLevel3生效，有三种颜色。
     * 该方法一旦生效，则setColor()方法自动失效。
     * @param ColorLevel1 [0%,Level1)之间的波浪的颜色
     * @param ColorLevel2 Level2生效的情况下，显示为[Level1,Level2)之间的波浪的颜色，Level2不生效的情况下，显示为[Level1,100%]之间的波浪的颜色。
     * @param ColorLevel3 [Level2,100%]之间的波浪的颜色
     * @param Level1 第一分界
     * @param Level2 第二分界，大于Level1生效，否则默认失效。
     */
    public void setWaveDiffrentColor(int ColorLevel1, int ColorLevel2,
                                     int ColorLevel3, float Level1, float Level2) {
        this.ColorLevel1 = ColorLevel1;
        this.ColorLevel2 = ColorLevel2;
        this.ColorLevel3 = ColorLevel3;
        if ((Level1 > 0.0F) && (Level1 < 1.0F))
            this.Level1 = Level1;
        if ((Level2 > Level1) && (Level2 < 1.0F))
            this.Level2 = Level2;
    }

    /**
     * 设置波浪的颜色，如果设置了setWaveDiffrentColor()方法则setColor()自动失效。
     * @param color波浪颜色
     */
    public void setColor(int color) {
        this.Color = color;
    }

    /**
     * 从刷新前的高度降到0，然再升到刷新后的高度
     * @param refresh 刷新之后的高度
     */
    public void setRefresh(float refresh) {
        if ((refresh >= 0.0F) && (refresh <= 1.0F)) {
            this.Refresh = refresh;
            this.FlagR = true;
        }
    }

    public float getAlpha() {
        return this.Alpha;
    }

    /**
     * 取值0~255之间，255代表不透明。
     * @param alpha 透明度
     */
    public void setAlpha(int alpha) {
        if ((alpha >= 0) && (alpha <= 255))
            this.Alpha = alpha;
    }

    public double getPeakSpacing() {
        return this.PeakSpacing;
    }

    /**
     * 决定了两个波峰之间的距离，该参数越大，波峰之间的距离越小
     * @param peakSpacing 波间距（反比）
     */

    public void setPeakSpacing(double peakSpacing) {
        this.PeakSpacing = peakSpacing;
    }

    public float getPercentage() {
        return this.Percentage;
    }

    /**
     *  当前波浪停留的位置。取值0~1之间。
     * @param percentage 百分比
     */
    public void setPercentage(float percentage) {
        if ((percentage >= 0.0F) && (percentage <= 1.0F))
            this.Percentage = percentage;
    }

    public float getAmplitude() {
        return this.Amplitude;
    }

    /**
     * 波的高度
     * @param amplitude 幅度
     */
    public void setAmplitude(float amplitude) {
        this.Amplitude = amplitude;
    }

    public float getSpeedH() {
        return this.SpeedH;
    }

    public float getSpeedV() {
        return this.SpeedV;
    }

    /**
     *
     * @param speedH 水平波浪的速度
     */
    public void setSpeedH(float speedH) {
        this.SpeedH = speedH;
    }

    /**
     *
     * @param speedV 升降的速度
     */
    public void setSpeedV(float speedV) {
        this.SpeedV = speedV;
    }

}
