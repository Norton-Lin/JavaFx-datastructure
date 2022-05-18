
package com.example.demo1.Code.test;

import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Event;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 模拟系统时间+设置闹钟
 * 存在以下的问题：
 * 1.时钟图形化界面应该交给前端完成，而不是体现在这个类中
 * 2.当闹钟响了的时候不应该使用print打印字符串，而是应该再前端显示出来，也要和前端再沟通
 * 3.模拟实践系统的起始时间应该是用户开始使用系统的时候的计算机系统时间，这个也和前端有关
 * 4.没有考虑闹钟的类型(已解决)
 */
public class TimeTest_1 {

    private static int speed;//时间快进速度

    /**
     * 设置模拟系统时间快进速度
     *
     * @param speed 目标快进速度
     */
    public void setSpeed(int speed) {
        TimeTest_1.speed = speed;
    }

    /**
     * 为指定的事项设置闹钟
     *
     * @param event 指定事项
     */
    public void setClock(Event event, int type) {

        Clock t = new Clock();

        t.setLocationRelativeTo(null);//时钟窗体显示在屏幕中央
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        t.setVisible(true);

        //输入要设置的闹钟时间
        Clock.setDate = event.getM_tTime().getStartDate();
        Clock.setHour = event.getM_tTime().getStartHour();
        Clock.setMinute = event.getM_tTime().getStartMinute();
        Clock.setSecond = 0;

        t.setType(type);
    }

    /**
     * 获取模拟系统当前时间
     *
     * @param initial_time 模拟系统初始时间
     * @return 不带格式的模拟系统当前时间
     */
    public static long showSimulateTime(Calendar initial_time) {

        Calendar current_time = Calendar.getInstance();//当前系统时间

        long interval = (current_time.getTimeInMillis() - initial_time.getTimeInMillis()) * speed;

        return initial_time.getTimeInMillis() + interval;// show_time为模拟系统的当前时间

    }

    /**
     * 将规定格式的时间字符串转换为Calendar类型
     *
     * @param t 字符串类型的时间
     * @return Calendar类型的时间
     */
    public static Calendar shiftDate(String t) {

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

    public static class Clock extends JFrame {

        MyPanel clockPanel;

        Ellipse2D.Double e;
        Line2D.Double hourLine;
        Line2D.Double minLine;
        Line2D.Double secondLine;
        GregorianCalendar calendar;

        int type;

        int month;
        int date;
        int week;
        int hour;
        int minute;
        int second;
        String timeStr = "";

        static int setMonth;
        static int setDate;
        static int setWeek;
        static int setHour;
        static int setMinute;
        static int setSecond;

        public static final int X = 60;
        public static final int Y = 60;
        public static final int X_BEGIN = 10;
        public static final int Y_BEGIN = 10;

        //public static final int RADIAN = 50;

        public Clock() {

            setSize(300, 200);
            setTitle("Clock");

            clockPanel = new MyPanel();
            add(clockPanel);

            Timer t = new Timer();
            Task task = new Task();

            t.schedule(task, 0, 1); //钟表刷新时间间隔(单位：ms)

        }

        public void setType(int type) {
            this.type = type;
        }

        //循环执行模拟时间流逝
        class Task extends TimerTask {

            public void run() {

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String i_t = "2022-05-04 16:20:11";
                Calendar initial_time = shiftDate(i_t);

                //模拟系统的当前时间
                calendar = (GregorianCalendar) shiftDate(df.format(showSimulateTime(initial_time)));

                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DATE);
                week = calendar.get(Calendar.DAY_OF_WEEK);
                hour = calendar.get(Calendar.HOUR);
                minute = calendar.get(Calendar.MINUTE);
                second = calendar.get(Calendar.SECOND);

                //Calendar.WEEK从周日开始
                if (week == 1) {
                    week = 7;
                } else {
                    week = week - 1;
                }

                month = month + 1;//Calendar.MONTH从0开始

                //时钟显示内容
                timeStr = "Current Time: " + month + " - " + date + " " + "星期" + week + " " + hour + " : " + minute + " : " + second;

                switch (type) {

                    //一次性闹钟
                    case 0 -> {
                        if (setMonth == month && setDate == date && setHour == hour && setMinute == minute && setSecond == second) {
                            System.out.println("time!");
                        }
                    }
                    //每天一次闹钟
                    case 1 -> {
                        if (setHour == hour && setMinute == minute && setSecond == second) {
                            System.out.println("time!");
                        }
                    }
                    //每周一次闹钟
                    case 7 -> {
                        if (setWeek == week && setHour == hour && setMinute == minute && setSecond == second) {
                            System.out.println("time!");
                        }
                    }

                }

                hourLine.x2 = X + 40 * Math.cos(hour * (Math.PI / 6) - Math.PI / 2);
                hourLine.y2 = Y + 40 * Math.sin(hour * (Math.PI / 6) - Math.PI / 2);
                minLine.x2 = X + 45 * Math.cos(minute * (Math.PI / 30) - Math.PI / 2);
                minLine.y2 = Y + 45 * Math.sin(minute * (Math.PI / 30) - Math.PI / 2);
                secondLine.x2 = X + 50 * Math.cos(second * (Math.PI / 30) - Math.PI / 2);
                secondLine.y2 = Y + 50 * Math.sin(second * (Math.PI / 30) - Math.PI / 2);

                repaint();
            }
        }

        //实现图形化界面，后续可能得交给前端实现
        class MyPanel extends JPanel {

            public MyPanel() {

                e = new Ellipse2D.Double(X_BEGIN, Y_BEGIN, 100, 100);

                hourLine = new Line2D.Double(X, Y, X, Y);

                minLine = new Line2D.Double(X, Y, X, Y);

                secondLine = new Line2D.Double(X, Y, X, Y);

            }

            public void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g2.drawString("12", 55, 25);//整点时间

                g2.drawString("6", 55, 105);

                g2.drawString("9", 15, 65);

                g2.drawString("3", 100, 65);

                g2.drawString(timeStr, 0, 130);

                g2.draw(e);

                g2.draw(hourLine);//时针

                g2.draw(minLine);//分针

                g2.draw(secondLine);//秒针

            }

        }

    }

    //main方法仅作测试用
    public static void main(String[] args) {

        TimeTest_1 test_1 = new TimeTest_1();

        Time time = new Time();

        time.setStartMonth(12);//活动时间对应的月份
        time.setStartDate(4);//活动时间对应的日期
        time.setWeek(3);//活动时间对应的星期
        time.setStartHour(4);//活动时间对应的小时
        time.setStartMinute(38);//活动时间对应的分钟

        Event event = new Event();
        event.setM_tTime(time);

        test_1.setSpeed(600);//设置快进速度
        test_1.setClock(event, 1);//设置一个闹钟
    }

}
