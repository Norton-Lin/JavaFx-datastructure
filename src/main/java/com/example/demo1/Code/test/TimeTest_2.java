
package com.example.demo1.Code.test;

import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Event;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TimeTest_2 {

    private int speed;//时间快进速度

    /**
     * 设置模拟系统时间快进速度
     *
     * @param speed 目标快进速度
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * 获取模拟系统时间快进速度
     *
     * @return speed 当前快进速度
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * 获取系统当前时间
     *
     * @return 系统当前时间
     */
    public Calendar getSystemTime() {
        return Calendar.getInstance();
    }

    /**
     * 获取模拟系统当前时间
     *
     * @param initial_time 模拟系统初始时间
     * @return 不带格式的模拟系统当前时间
     */
    public long showSimulateTime(Calendar initial_time) {

        Calendar current_time = getSystemTime();//当前系统时间

        long interval = (current_time.getTimeInMillis() - initial_time.getTimeInMillis()) * this.getSpeed();

        return initial_time.getTimeInMillis() + interval;// show_time为模拟系统的当前时间

    }

    /**
     * 将规定格式的时间字符串转换为Calendar类型
     *
     * @param t 字符串类型的时间
     * @return Calendar类型的时间
     */
    public Calendar shiftDate(String t) {

        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(t);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }


    /**
     * 设置闹钟并提醒
     *
     * @param cycle_type 闹钟类型
     * @param event      需要添加闹钟的事项
     */
    public void setAlarmClock(int cycle_type, Event event) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //这个时间为模拟系统开始使用时计算机的时间，这里的仅做测试用，不是最终形式
        String i_t = "2022-04-27 15:55:11";
        Calendar initial_time = shiftDate(i_t);

        while (true) {

            Calendar temp = shiftDate(df.format(showSimulateTime(initial_time)));

            int d1 = event.getM_tTime().getStartDate();
            int d2 = temp.get(Calendar.DATE);

            int h1 = event.getM_tTime().getStartHour();
            int h2 = temp.get(Calendar.HOUR);

            int m1 = event.getM_tTime().getStartMinute();
            int m2 = temp.get(Calendar.MINUTE);

            switch (cycle_type) {

                //单次闹钟
                case 0 -> {

                    if (d1 == d2 && h1 == h2 && m1 == m2) {
                        System.out.println("Time for activity :-)");
                    }

                }

                //每天一次闹钟
                case 1 -> {

                    if (h1 == h2 && m1 == m2) {
                        System.out.println("Time for activity :-)");
                    }

                }

                //每周一次闹钟
                case 7 -> {

                    if ((d2 - d1) % 7 == 0 && h1 == h2 && m1 == m2) {
                        System.out.println("Time for activity :-)");
                    }

                }
            }
        }
    }


    //仅作测试使用
    public static void main(String[] args) {

        TimeTest_2 st = new TimeTest_2();
        Event event = new Event();
        Time time=new Time();
        time.setStartMonth(5);
        time.setStartDate(6);
        time.setStartHour(13);
        time.setStartMinute(30);
        event.setM_tTime(time);

        st.setSpeed(600);//设置快进速度

        st.setAlarmClock(1, event);//设置一个每天提醒的闹钟

    }

}
