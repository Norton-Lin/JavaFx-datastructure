
package com.example.demo1.Code.entity;

import com.example.demo1.Code.Util.Time;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;
import javax.swing.*;

import static com.example.demo1.Code.entity.SystemTime.*;

/**
 * 模拟系统时间+设置闹钟
 * 存在以下的问题：
 * 1.时钟图形化界面应该交给前端完成，而不是体现在这个类中
 * 2.当闹钟响了的时候不应该使用print打印字符串，而是应该再前端显示出来，也要和前端再沟通
 * 3.模拟实践系统的起始时间应该是用户开始使用系统的时候的计算机系统时间，这个也和前端有关
 * 接口：
 * 数据库接口 :在setClock()方法中调用闹钟的数据库
 * 前端接口 :1.暂停系统时间推进 2.设置闹钟接口为 SystemTime.setClock()
 */
public class SystemTime {

    private static int speed = 360;//模拟系统时间快进速度
    private static String StartTime;//模拟系统开始计时时间
    private static Calendar CurrentTime;//模拟系统当前时间
    private static boolean flag = true;//模拟系统时间前进状态

    /**
     * 设置模拟系统时间快进速度
     *
     * @param speed 目标快进速度
     */
    public void setSpeed(int speed) {
        SystemTime.speed = speed;
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
     * 获取模拟系统时间推进状态
     *
     * @return 推进状态
     */
    public static boolean getFlag() {
        return SystemTime.flag;
    }

    /**
     * 系统运行时调用该方法，将此时的计算机系统时间设置为模拟系统的开始时间
     */
    public void setStartTime() {
        Date current_time = new Date();//当前系统时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StartTime = sdf.format(current_time);
        //System.out.println(SystemTime.getStartTime());

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
     * 获取long型模拟系统当前时间
     *
     * @param initial_time 模拟系统初始时间
     * @return 不带格式的模拟系统当前时间
     */
    public static long showSimulateTime(Calendar initial_time) {

        Calendar current_time = Calendar.getInstance();//当前系统时间

        long interval = (current_time.getTimeInMillis() - initial_time.getTimeInMillis()) * getSpeed();//时间间隙

        return initial_time.getTimeInMillis() + interval;// show_time为模拟系统的当前时间

    }

    /**
     * 设置模拟系统当前时间
     *
     * @param calendar 当前时间
     */
    public static void setCurrentTime(Calendar calendar) {
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SystemTime.CurrentTime = calendar;
    }

    /**
     * 获得Calendar型的系统当前时间
     *
     * @return 当前时间
     */
    public static Calendar getCurrentTime() {
        return SystemTime.CurrentTime;
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
     * 根据传入的参数设置指定闹钟
     *
     * @param clockTime 闹钟时间
     * @param clockName 闹钟名字
     * @param clockType 闹钟类型
     * @param allClock  该用户的所有闹钟列表，应该由数据库读出，此处仅为临时写法
     */
    public void setClock(Time clockTime, String clockName, int clockType, ArrayList<EventClock> allClock) {

        //此处应该从数据库中得到该用户的所有闹钟

        allClock.add(new EventClock(clockTime, clockName, clockType));//更新用户的闹钟列表

        //创建闹钟线程列表并为所有闹钟添加闹钟线程
        ArrayList<Runnable> clockThread = new ArrayList<>();
        for (EventClock ec : allClock) {
            clockThread.add(new ClockThread(ec));
        }

        //创建线程列表并将所有的闹钟线程添加线程
        ArrayList<Thread> Threads = new ArrayList<>();
        for (Runnable ct : clockThread) {
            Threads.add(new Thread(ct));
        }

        //模拟系统时间未暂停推进时启动闹钟
        if (getFlag()) {
            //启动所有线程
            for (Thread t : Threads) {
                t.start();
            }
        }


    }

    //main方法仅作测试用
    public static void main(String[] args) {

        SystemTime test_1 = new SystemTime();
        test_1.setSpeed(36000);//设置快进速度
        test_1.setStartTime();//设置模拟系统开始计时时间
        //stopTime();

        ArrayList<EventClock> allClock = new ArrayList<>();

        Time time_1 = new Time();
        time_1.setStartMonth(3);//活动时间对应的月份
        time_1.setStartDate(18);//活动时间对应的日期
        time_1.setWeek(3);//活动时间对应的星期
        time_1.setStartHour(21);//活动时间对应的小时
        time_1.setStartMinute(21);//活动时间对应的分钟
        String name_1 = "第一个活动";
        int type_1 = 7;

        Time time_2 = new Time();
        time_2.setStartMonth(5);//活动时间对应的月份
        time_2.setStartDate(22);//活动时间对应的日期
        time_2.setWeek(7);//活动时间对应的星期
        time_2.setStartHour(9);//活动时间对应的小时
        time_2.setStartMinute(21);//活动时间对应的分钟
        String name_2 = "第二个活动";
        int type_2 = 7;

        allClock.add(new EventClock(time_1, name_1, type_1));

        test_1.setClock(time_2, name_2, type_2, allClock);

        //模拟系统时间图形化
        SimulatedTime simulatedTime = new SimulatedTime();
        simulatedTime.setLocationRelativeTo(null);//时钟窗体显示在屏幕中央
        simulatedTime.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        simulatedTime.setVisible(true);

    }
}

/**
 * 闹钟线程
 */
class ClockThread implements Runnable {

    public EventClock eventclock;

    public ClockThread(EventClock eventclock) {
        this.eventclock = eventclock;
    }

    @Override
    public void run() {

        Clock t = new Clock(this.eventclock);

    }
}

/**
 * 用于后端实现算法的闹钟
 */
class Clock {

    //模拟系统当前时间的信息
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

        /*if(SystemTime.getSpeed()<=1000){
            t.schedule(task, 0, 1000/SystemTime.getSpeed()); //闹钟刷新时间间隔(单位：ms)

        }else{
            t.schedule(task, 0, 1); //闹钟刷新时间间隔(单位：ms)
        }*/

        t.schedule(task, 0, 3600000 / SystemTime.getSpeed()); //闹钟刷新时间间隔(单位：ms)

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
            Calendar calendar = shiftDate(df.format(showSimulateTime(initial_time)));

            if (SystemTime.getCurrentTime() != null && SystemTime.getFlag()) {
                year = SystemTime.getCurrentTime().get(Calendar.YEAR);
                month = SystemTime.getCurrentTime().get(Calendar.MONTH);
                date = SystemTime.getCurrentTime().get(Calendar.DATE);
                week = SystemTime.getCurrentTime().get(Calendar.DAY_OF_WEEK);
                hour = SystemTime.getCurrentTime().get(Calendar.HOUR_OF_DAY);
                minute = SystemTime.getCurrentTime().get(Calendar.MINUTE);
                second = SystemTime.getCurrentTime().get(Calendar.SECOND);
            } else if (SystemTime.getCurrentTime() == null && SystemTime.getFlag()) {
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DATE);
                week = calendar.get(Calendar.DAY_OF_WEEK);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                second = calendar.get(Calendar.SECOND);
            }


            //获取闹钟的所有信息信息
            setMonth = eventClock.clockTime.getStartMonth();
            setDate = eventClock.clockTime.getStartDate();
            setWeek = eventClock.clockTime.getWeek();
            setHour = eventClock.clockTime.getStartHour();
            setMinute = eventClock.clockTime.getStartMinute();
            setSecond = 0;
            setName = eventClock.clockName;
            setType = eventClock.clockType;

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
                        //System.out.println(this.setName + " time!");
                        JFrame jf = new JFrame("闹钟提醒");//创建对象
                        jf.setSize(300, 200);//设置窗口大小
                        jf.setLocation(200, 100);//设置窗口位置
                        jf.setVisible(true);//设置窗口可见
                        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//传递参数使关闭按钮有效

                        JPanel panel = new JPanel();
                        JLabel size = new JLabel(this.setName + " 时间到!");
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
                        //System.out.println(this.setName + " time!");
                        JFrame jf = new JFrame("闹钟提醒");//创建对象
                        jf.setSize(300, 200);//设置窗口大小
                        jf.setLocation(100, 100);//设置窗口位置
                        jf.setVisible(true);//设置窗口可见
                        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//传递参数使关闭按钮有效

                        JPanel panel = new JPanel();
                        JLabel size = new JLabel(this.setName + " 时间到!");
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
                        //System.out.println(this.setName + " time!");
                        JFrame jf = new JFrame("闹钟提醒");//创建对象
                        jf.setSize(300, 200);//设置窗口大小
                        jf.setLocation(100, 100);//设置窗口位置
                        jf.setVisible(true);//设置窗口可见
                        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//传递参数使关闭按钮有效

                        JPanel panel = new JPanel();
                        JLabel size = new JLabel(this.setName + " 时间到!");
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

/**
 * 模拟系统时间以及图形化
 */
class SimulatedTime extends JFrame {

    MyPanel clockPanel;

    Ellipse2D.Double e;
    Line2D.Double hourLine;
    Line2D.Double minLine;
    Line2D.Double secondLine;
    Calendar calendar;

    //计算机系统实时时间的信息
    int year;
    int month;
    int date;
    int week;
    int hour;
    int minute;
    int second;
    String timeStr = "";

    public static final int X = 60;
    public static final int Y = 60;
    public static final int X_BEGIN = 10;
    public static final int Y_BEGIN = 10;

    public SimulatedTime() {

        setSize(300, 200);
        setTitle("Clock");

        clockPanel = new MyPanel();
        add(clockPanel);

        Timer t = new Timer();
        Task task = new Task();

        t.schedule(task, 0, 1); //钟表刷新时间间隔(单位：ms)

    }

    //模拟时间流逝
    class Task extends TimerTask {

        public void run() {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String i_t = getStartTime();
            Calendar initial_time = shiftDate(i_t);

            //模拟系统的当前时间及基本时间信息
            calendar = shiftDate(df.format(showSimulateTime(initial_time)));

            //系统时间未被暂停时更新保持更新系统时间
            if (SystemTime.getFlag()) {
                SystemTime.setCurrentTime(calendar);
            }

            //根据最近的模拟系统时间画钟表
            year = SystemTime.getCurrentTime().get(Calendar.YEAR);
            month = SystemTime.getCurrentTime().get(Calendar.MONTH);
            date = SystemTime.getCurrentTime().get(Calendar.DATE);
            week = SystemTime.getCurrentTime().get(Calendar.DAY_OF_WEEK);
            hour = SystemTime.getCurrentTime().get(Calendar.HOUR_OF_DAY);
            minute = SystemTime.getCurrentTime().get(Calendar.MINUTE);
            second = SystemTime.getCurrentTime().get(Calendar.SECOND);

            //Calendar.WEEK从周日开始
            if (week == 1) {
                week = 7;
            } else {
                week = week - 1;
            }

            month = month + 1;//Calendar.MONTH从0开始

            timeStr = year + "年" + month + "月" + date + "日 " + "星期" + week + " " + hour + " : " + minute + " : " + second;

            hourLine.x2 = X + 40 * Math.cos(hour * (Math.PI / 6) - Math.PI / 2);
            hourLine.y2 = Y + 40 * Math.sin(hour * (Math.PI / 6) - Math.PI / 2);
            minLine.x2 = X + 45 * Math.cos(minute * (Math.PI / 30) - Math.PI / 2);
            minLine.y2 = Y + 45 * Math.sin(minute * (Math.PI / 30) - Math.PI / 2);
            secondLine.x2 = X + 50 * Math.cos(second * (Math.PI / 30) - Math.PI / 2);
            secondLine.y2 = Y + 50 * Math.sin(second * (Math.PI / 30) - Math.PI / 2);

            repaint();


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

            g2.drawString(timeStr, 0, 130);

            g2.draw(e);

            g2.draw(hourLine);//时针

            g2.draw(minLine);//分针

            g2.draw(secondLine);//秒针

        }

    }
}