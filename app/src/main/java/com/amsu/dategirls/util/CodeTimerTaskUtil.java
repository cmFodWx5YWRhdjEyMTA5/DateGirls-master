package com.amsu.dategirls.util;

import android.app.Activity;
import android.util.Log;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by HP on 2017/2/28.
 */

//计时器，timeSpan为时间间隔

public class CodeTimerTaskUtil extends TimerTask {
    private static final String TAG = "RunTimerTaskUtil";
    private long timeSpan;
    private boolean mIsFirstStart = true;  //第一次开始
    private boolean mIsTimeerRunning;  //是否正在运行
    private Timer mTimer;
    private long outTime;
    private Activity activity;
    private int allScend;

    public CodeTimerTaskUtil(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        if (mIsTimeerRunning){
            if (allScend==0){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onTimeChangeListerner.onTimeOut();
                    }
                });
                mIsTimeerRunning = false;
                destoryTime();
                return;
            }
            try {
                Thread.sleep(timeSpan);
                allScend--;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onTimeChangeListerner.onFormatStringTimeChange(allScend);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public void startTime(){
        if (mTimer==null){
            mTimer = new Timer();
            mTimer.schedule(this,timeSpan,timeSpan);
        }
        mIsTimeerRunning = true;
    }


    public void destoryTime(){
        if (!mIsTimeerRunning && mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void setOnTimeChangeListerner(int allScend,long timeSpan, OnTimeChangeListerner onTimeChangeListerner){
        this.allScend = allScend;
        this.timeSpan = timeSpan;
        this.onTimeChangeListerner = onTimeChangeListerner;
    }

    public interface OnTimeChangeListerner{
        void onFormatStringTimeChange(int currentScend);
        void onTimeOut();
    }


    private OnTimeChangeListerner onTimeChangeListerner;





}