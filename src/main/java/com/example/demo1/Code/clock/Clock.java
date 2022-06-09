package com.example.demo1.Code.clock;

import com.example.demo1.Code.systemtime.SystemTime;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 用于后端实现算法的闹钟
 */
public class Clock {

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

        t.schedule(task, 0, 3600000 / SystemTime.getSpeed()); //闹钟刷新时间间隔(单位：ms)

    }

    //模拟时间流逝
    class Task extends TimerTask {

        public EventClock eventClock;
        public int setDate;
        public int setWeek;
        public int setMonth;
        public int setHour;
        public String setName;
        public int setType;

        public Task(EventClock eventClock) {
            this.eventClock = eventClock;
        }

        public void run() {

            //获取模拟系统当前时间
            year = SystemTime.getCurrentTime().get(Calendar.YEAR);
            month = SystemTime.getCurrentTime().get(Calendar.MONTH);
            date = SystemTime.getCurrentTime().get(Calendar.DATE);
            week = SystemTime.getCurrentTime().get(Calendar.DAY_OF_WEEK);
            hour = SystemTime.getCurrentTime().get(Calendar.HOUR_OF_DAY);
            minute = SystemTime.getCurrentTime().get(Calendar.MINUTE);
            second = SystemTime.getCurrentTime().get(Calendar.SECOND);

            //获取闹钟的信息
            setMonth = eventClock.getClockTime().getStartMonth();
            setDate = eventClock.getClockTime().getStartDate();
            setWeek = eventClock.getClockTime().getWeek();
            setHour = eventClock.getClockTime().getStartHour();
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
                    if (setMonth == month && setDate == date && setHour == hour) {
                        JFrame jf = new JFrame("闹钟");
                        jf.setSize(300, 200);
                        jf.setLocation(200, 100);
                        jf.setVisible(true);
                        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JPanel panel = new JPanel();
                        JLabel size = new JLabel(this.setName + " 时间到");
                        size.setFont(new Font("宋体", Font.PLAIN, 30));
                        size.setForeground(Color.PINK);

                        panel.add(size);

                        jf.setContentPane(panel);
                        jf.setVisible(true);
                    }
                }

                //每天一次闹钟
                case 1 -> {
                    if (setHour == hour) {
                        JFrame jf = new JFrame("闹钟");
                        jf.setSize(300, 200);
                        jf.setLocation(200, 100);
                        jf.setVisible(true);
                        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JPanel panel = new JPanel();
                        JLabel size = new JLabel(this.setName + " 时间到");
                        size.setFont(new Font("宋体", Font.PLAIN, 30));
                        size.setForeground(Color.PINK);

                        panel.add(size);

                        jf.setContentPane(panel);
                        jf.setVisible(true);
                    }
                }

                //每周一次闹钟
                case 7 -> {
                    if (setWeek == week && setHour == hour) {
                        JFrame jf = new JFrame("闹钟");
                        jf.setSize(300, 200);
                        jf.setLocation(200, 100);
                        jf.setVisible(true);
                        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JPanel panel = new JPanel();
                        JLabel size = new JLabel(this.setName + " 时间到");
                        size.setFont(new Font("宋体", Font.PLAIN, 30));
                        size.setForeground(Color.PINK);

                        panel.add(size);

                        jf.setContentPane(panel);
                        jf.setVisible(true);
                    }
                }
            }

        }
    }

}
