
package com.example.demo1.Code.systemtime;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.*;

import static com.example.demo1.Code.systemtime.SystemTime.*;

/**
 * 模拟系统时间
 * 前端接口 :
 * 1.启动系统时间接口为 SystemTimeStart()
 * 2.设置快进速度接口为 setSpeed()
 * 3.暂停系统时间接口为 stopTime()
 * 4.获得当前系统时间接口为 getCurrentTime()
 */
public class SystemTime {

    private static int speed;//时间快进速度
    private static String StartTime;//开始计时时间
    private static Calendar CurrentTime;//当前时间
    private static boolean flag = true;//时间推进状况
    private static String currentTime;//当前时间，String类型
    private static Calendar temp;

    /**
     * 设置模拟系统时间快进速度
     *
     * @param speed 目标快进速度
     */
    public static void setSpeed(int speed) {
        temp = Calendar.getInstance();
        SystemTime.speed = speed;
        SystemTime.StartTime = getStringCurrentTime();
    }

    /**
     * 获取模拟系统时间快进速度
     *
     * @return 当前快进速度
     */
    public static int getSpeed() {
        return SystemTime.speed;
    }

    /**
     * 暂停系统时间推进
     */
    public static void stopTime() {
        SystemTime.flag = false;
    }

    /**
     * 继续系统时间推进
     */
    public static void restartTime() {
        SystemTime.flag = true;
        temp = Calendar.getInstance();
        SystemTime.StartTime = getStringCurrentTime();
    }

    /**
     * 获取当前系统时间推进情况
     *
     * @return 标志
     */
    public static boolean getFlag() {
        return SystemTime.flag;
    }

    /**
     * 为StartTime赋初值
     * 系统运行时调用该方法，将此时的计算机系统时间设置为模拟系统的开始时间
     */
    public static void findStartTime() {
        Date current_time = new Date();//当前系统时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SystemTime.StartTime = sdf.format(current_time);
        SystemTime.temp = Calendar.getInstance();
        SystemTime.CurrentTime = temp;
    }

    /**
     * 获得模拟系统时间的开始时间
     *
     * @return 开始时间
     */
    public static String getStartTime() {
        return StartTime;
    }

    /**
     * 获取模拟系统当前时间
     *
     * @param initial_time 模拟系统初始时间
     * @return 不带格式的模拟系统当前时间
     */
    public static long showSimulateTime(Calendar initial_time) {

        Calendar current_time = Calendar.getInstance();//当前系统时间

        long interval = (current_time.getTimeInMillis() - temp.getTimeInMillis()) * getSpeed();//时间间隙

        return initial_time.getTimeInMillis() + interval;// show_time为模拟系统的当前时间

    }

    /**
     * 设置当前系统时间，Calendar类型
     *
     * @param calendar 计算得到的时间
     */
    public static void setCurrentTime(Calendar calendar) {
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CurrentTime = calendar;
    }

    /**
     * 获取当前系统时间，Calendar类型
     *
     * @return Calendar类型的当前系统时间
     */
    public static Calendar getCurrentTime() {
        return CurrentTime;
    }

    /**
     * 设置当前系统时间，String类型
     *
     * @param CurrentTime Calendar类型的当前系统时间
     */
    public static void setStringCurrentTime(Calendar CurrentTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTime = sdf.format(CurrentTime.getTime());
    }

    /**
     * 获取当前系统时间，String类型
     *
     * @return String类型的当前系统时间
     */
    public static String getStringCurrentTime() {
        return currentTime;
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

    /**
     * 和前端的接口
     */
    public void SystemTimeStart() {

        setSpeed(1);//设置模拟系统初始快进速度
        findStartTime();//设置模拟系统开始计时时间

        //开始模拟系统时间
        SimulatedTime simulatedTime = new SimulatedTime();
        simulatedTime.setLocationRelativeTo(null);//时钟窗体显示在屏幕中央
        simulatedTime.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        simulatedTime.setVisible(true);

    }
}

/**
 * 模拟系统时间以及图形化
 */
class SimulatedTime extends JFrame {

    MyPanel clockPanel;
    Ellipse2D.Double e;
    Line2D.Double hourLine;
    Line2D.Double minLine;
    Line2D.Double secondLine;
    GregorianCalendar calendar;

    //计算机系统实时时间的信息
    int year;
    int month;
    int date;
    int week;
    int hour;
    int minute;
    int second;
    String display = "";

    public static final int X = 60;
    public static final int Y = 60;
    public static final int X_BEGIN = 10;
    public static final int Y_BEGIN = 10;

    public SimulatedTime() {

        //时钟图形化界面
        setSize(300, 200);
        setTitle("Clock");
        clockPanel = new MyPanel();
        add(clockPanel);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        //模拟时间
        Timer t = new Timer();
        Task task = new Task();

        t.schedule(task, 0, 1); //钟表刷新时间间隔(单位：ms)

    }

    //模拟时间流逝
    class Task extends TimerTask {

        public void run() {

            if (getFlag()) {

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startTime = getStartTime();
                Calendar initialTime = shiftDate(startTime);

                //模拟系统的当前时间及基本时间信息
                calendar = (GregorianCalendar) shiftDate(df.format(showSimulateTime(initialTime)));

                //如果系统时间未暂停，则更新当前系统模拟时间

                setCurrentTime(calendar);
                setStringCurrentTime(getCurrentTime());


                year = getCurrentTime().get(Calendar.YEAR);
                month = getCurrentTime().get(Calendar.MONTH);
                date = getCurrentTime().get(Calendar.DATE);
                week = getCurrentTime().get(Calendar.DAY_OF_WEEK);
                hour = getCurrentTime().get(Calendar.HOUR_OF_DAY);
                minute = getCurrentTime().get(Calendar.MINUTE);
                second = getCurrentTime().get(Calendar.SECOND);

                //Calendar.WEEK从周日开始
                if (week == 1) {
                    week = 7;
                } else {
                    week = week - 1;
                }

                month = month + 1;//Calendar.MONTH从0开始

                display = year + "年" + month + "月" + date + "日 " + "星期" + week + " " + hour + " : " + minute + " : " + second;

                hourLine.x2 = X + 40 * Math.cos(hour * (Math.PI / 6) - Math.PI / 2);
                hourLine.y2 = Y + 40 * Math.sin(hour * (Math.PI / 6) - Math.PI / 2);
                minLine.x2 = X + 45 * Math.cos(minute * (Math.PI / 30) - Math.PI / 2);
                minLine.y2 = Y + 45 * Math.sin(minute * (Math.PI / 30) - Math.PI / 2);
                secondLine.x2 = X + 50 * Math.cos(second * (Math.PI / 30) - Math.PI / 2);
                secondLine.y2 = Y + 50 * Math.sin(second * (Math.PI / 30) - Math.PI / 2);

                repaint();
            }
        }

    }

    //时钟图形化
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

            g2.drawString(display, 0, 130);

            g2.draw(e);

            g2.draw(hourLine);//时针

            g2.draw(minLine);//分针

            g2.draw(secondLine);//秒针

        }

    }
}