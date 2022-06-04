package com.example.demo1.Code.clock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.demo1.Code.systemtime.SystemTime.*;

/**
 * 用于后端实现算法的闹钟
 */
public class Clock {

    GregorianCalendar calendar;

    //计算机系统实时时间的信息
    int year;
    int month;
    int date;
    int week;
    int hour;
    int minute;
    int second;

    public Clock(EventClock eventClock) {

        Timer t = new Timer();
        Task task = new Task(eventClock);

        t.schedule(task, 0, 1); //闹钟刷新时间间隔(单位：ms)

    }

    //模拟时间流逝
    class Task extends TimerTask {

        public EventClock eventClock;
        public int setDate;
        public int setWeek;
        public int setMonth;
        public int setHour;
        public int setMinute;
        public int setSecond;
        public String setName;
        public int setType;

        public Task(EventClock eventClock) {
            this.eventClock = eventClock;
        }

        public void run() {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String i_t = getStartTime();
            Calendar initial_time = shiftDate(i_t);

            //模拟系统的当前时间及基本时间信息
            calendar = (GregorianCalendar) shiftDate(df.format(showSimulateTime(initial_time)));

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            date = calendar.get(Calendar.DATE);
            week = calendar.get(Calendar.DAY_OF_WEEK);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);

            //获取闹钟的所有信息信息
            setMonth = eventClock.getClockTime().getStartMonth();
            setDate = eventClock.getClockTime().getStartDate();
            setWeek = eventClock.getClockTime().getWeek();
            setHour = eventClock.getClockTime().getStartHour();
            setMinute = eventClock.getClockTime().getStartMinute();
            setSecond = 0;
            setName = eventClock.getClockName();
            setType = eventClock.getClockType();

            //Calendar.WEEK从周日开始
            if (week == 1) {
                week = 7;
            } else {
                week = week - 1;
            }

            month = month + 1;//Calendar.MONTH从0开始

            switch (setType) {

                //一次性闹钟
                case 0 -> {
                    if (setMonth == month && setDate == date && setHour == hour && setMinute == minute && setSecond == second) {
                        System.out.println(this.setName + " time!");
                    }
                }
                //每天一次闹钟
                case 1 -> {
                    if (setHour == hour && setMinute == minute && setSecond == second) {
                        System.out.println(this.setName + " time!");
                    }
                }
                //每周一次闹钟
                case 7 -> {
                    if (setWeek == week && setHour == hour && setMinute == minute && setSecond == second) {
                        System.out.println(this.setName + " time!");
                    }
                }
            }

        }
    }

}
