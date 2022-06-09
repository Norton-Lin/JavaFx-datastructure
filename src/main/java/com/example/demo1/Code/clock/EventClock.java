package com.example.demo1.Code.clock;

import com.example.demo1.Code.Util.Time;

/**
 * 用于和数据库交互的闹钟
 */
public class EventClock {

    private Time clockTime;//闹钟的时间
    private String clockName;//闹钟的名字
    private int clockType;//闹钟的类型

    public EventClock() {
        clockTime = new Time();
    }

    public EventClock(Time time, String name, int type) {
        this.clockTime = time;
        this.clockName = name;
        this.clockType = type;
    }

    public Time getClockTime() {
        return clockTime;
    }

    public void setClockTime(Time clockTime) {
        this.clockTime = clockTime;
    }

    public String getClockName() {
        return clockName;
    }

    public void setClockName(String clockName) {
        this.clockName = clockName;
    }

    public int getClockType() {
        return clockType;
    }

    public void setClockType(int clockType) {
        this.clockType = clockType;
    }

    @Override
    public String toString() {
        String result;
        result = "闹钟名为：" + this.clockName;
        return result;
    }
}
