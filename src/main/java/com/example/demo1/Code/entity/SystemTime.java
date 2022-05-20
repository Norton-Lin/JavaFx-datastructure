
package com.example.demo1.Code.entity;

import com.example.demo1.Code.Util.Time;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import static com.example.demo1.Code.entity.SystemTime.shiftDate;
import static com.example.demo1.Code.entity.SystemTime.showSimulateTime;

/**
 * ģ��ϵͳʱ��+��������
 * �������µ����⣺
 * 1.ʱ��ͼ�λ�����Ӧ�ý���ǰ����ɣ��������������������
 * 2.���������˵�ʱ��Ӧ��ʹ��print��ӡ�ַ���������Ӧ����ǰ����ʾ������ҲҪ��ǰ���ٹ�ͨ
 * 3.ģ��ʵ��ϵͳ����ʼʱ��Ӧ�����û���ʼʹ��ϵͳ��ʱ��ļ����ϵͳʱ�䣬���Ҳ��ǰ���й�
 * �ӿڣ�
 * ���ݿ�ӿ� :��setClock()�����е������ӵ����ݿ�
 * ǰ�˽ӿ� :1.��ͣϵͳʱ���ƽ� 2.�������ӽӿ�Ϊ SystemTime.setClock()
 */
public class SystemTime {

    private static int speed;//ʱ�����ٶ�

    /**
     * ����ģ��ϵͳʱ�����ٶ�
     *
     * @param speed Ŀ�����ٶ�
     */
    public void setSpeed(int speed) {
        SystemTime.speed = speed;
    }

    /**
     * ��ȡģ��ϵͳʱ�����ٶ�
     *
     * @return ��ǰ����ٶ�
     */
    public int getSpeed() {
        return SystemTime.speed;
    }

    /**
     * ��ͣϵͳʱ���ƽ�
     */
    public void stopTime() {
        SystemTime.speed = 0;
    }

    /**
     * ��ȡģ��ϵͳ��ǰʱ��
     *
     * @param initial_time ģ��ϵͳ��ʼʱ��
     * @return ������ʽ��ģ��ϵͳ��ǰʱ��
     */
    public static long showSimulateTime(Calendar initial_time) {

        Calendar current_time = Calendar.getInstance();//��ǰϵͳʱ��

        long interval = (current_time.getTimeInMillis() - initial_time.getTimeInMillis()) * speed;//ʱ���϶

        return initial_time.getTimeInMillis() + interval;// show_timeΪģ��ϵͳ�ĵ�ǰʱ��

    }

    /**
     * ���涨��ʽ��ʱ���ַ���ת��ΪCalendar����
     *
     * @param t �ַ������͵�ʱ��
     * @return Calendar���͵�ʱ��
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
     * ���ݴ���Ĳ�������ָ������
     *
     * @param clockTime ����ʱ��
     * @param clockName ��������
     * @param clockType ��������
     * @param allClock  ���û������������б�Ӧ�������ݿ�������˴���Ϊ��ʱд��
     */
    public void setClock(Time clockTime, String clockName, int clockType, ArrayList<EventClock> allClock) {

        //�˴�Ӧ�ô����ݿ��еõ����û�����������

        allClock.add(new EventClock(clockTime, clockName, clockType));//�����û��������б�

        //���������߳��б�Ϊ����������������߳�
        ArrayList<Runnable> clockThread = new ArrayList<>();
        for (EventClock ec : allClock) {
            clockThread.add(new ClockThread(ec));
        }

        //�����߳��б������е������߳�����߳�
        ArrayList<Thread> Threads = new ArrayList<>();
        for (Runnable ct : clockThread) {
            Threads.add(new Thread(ct));
        }

        //���������߳�
        for (Thread t : Threads) {
            t.start();
        }

    }

    //main��������������
    public static void main(String[] args) {

        SystemTime test_1 = new SystemTime();
        test_1.setSpeed(600);//���ÿ���ٶ�

        ArrayList<EventClock> allClock = new ArrayList<>();

        Time time_1 = new Time();
        time_1.setStartMonth(5);//�ʱ���Ӧ���·�
        time_1.setStartDate(11);//�ʱ���Ӧ������
        time_1.setWeek(3);//�ʱ���Ӧ������
        time_1.setStartHour(18);//�ʱ���Ӧ��Сʱ
        time_1.setStartMinute(25);//�ʱ���Ӧ�ķ���
        String name_1 = "��һ���";
        int type_1 = 1;

        Time time_2 = new Time();
        time_2.setStartMonth(5);//�ʱ���Ӧ���·�
        time_2.setStartDate(11);//�ʱ���Ӧ������
        time_2.setWeek(3);//�ʱ���Ӧ������
        time_2.setStartHour(19);//�ʱ���Ӧ��Сʱ
        time_2.setStartMinute(0);//�ʱ���Ӧ�ķ���
        String name_2 = "�ڶ����";
        int type_2 = 1;

        allClock.add(new EventClock(time_1, name_1, type_1));

        test_1.setClock(time_2, name_2, type_2, allClock);

        //ģ��ϵͳʱ��ͼ�λ�
        SimulatedTime simulatedTime = new SimulatedTime();
        simulatedTime.setLocationRelativeTo(null);//ʱ�Ӵ�����ʾ����Ļ����
        simulatedTime.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        simulatedTime.setVisible(true);

    }
}

/**
 * �����߳�
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
 * ���ں��ʵ���㷨������
 */
class Clock{

    GregorianCalendar calendar;

    //�����ϵͳʵʱʱ�����Ϣ
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

        t.schedule(task, 0, 1); //����ˢ��ʱ����(��λ��ms)

    }

    //ģ��ʱ������
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
            String i_t = "2022-05-11 10:54:00";
            Calendar initial_time = shiftDate(i_t);

            //ģ��ϵͳ�ĵ�ǰʱ�估����ʱ����Ϣ
            calendar = (GregorianCalendar) shiftDate(df.format(showSimulateTime(initial_time)));

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            date = calendar.get(Calendar.DATE);
            week = calendar.get(Calendar.DAY_OF_WEEK);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);

            //��ȡ���ӵ�������Ϣ��Ϣ
            setMonth = eventClock.clockTime.getStartMonth();
            setDate = eventClock.clockTime.getStartDate();
            setWeek = eventClock.clockTime.getWeek();
            setHour = eventClock.clockTime.getStartHour();
            setMinute = eventClock.clockTime.getStartMinute();
            setSecond = 0;
            setName = eventClock.clockName;
            setType = eventClock.clockType;

            //Calendar.WEEK�����տ�ʼ
            if (week == 1) {
                week = 7;
            } else {
                week = week - 1;
            }

            month = month + 1;//Calendar.MONTH��0��ʼ

            switch (setType) {

                //һ��������
                case 0 -> {
                    if (setMonth == month && setDate == date && setHour == hour && setMinute == minute && setSecond == second) {
                        System.out.println(this.setName + " time!");
                    }
                }
                //ÿ��һ������
                case 1 -> {
                    if (setHour == hour && setMinute == minute && setSecond == second) {
                        System.out.println(this.setName + " time!");
                    }
                }
                //ÿ��һ������
                case 7 -> {
                    if (setWeek == week && setHour == hour && setMinute == minute && setSecond == second) {
                        System.out.println(this.setName + " time!");
                    }
                }
            }

        }
    }

}

/**
 * ģ��ϵͳʱ���Լ�ͼ�λ�
 */
class SimulatedTime extends JFrame {

    MyPanel clockPanel;

    Ellipse2D.Double e;
    Line2D.Double hourLine;
    Line2D.Double minLine;
    Line2D.Double secondLine;
    GregorianCalendar calendar;

    //�����ϵͳʵʱʱ�����Ϣ
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

        t.schedule(task, 0, 1); //�ӱ�ˢ��ʱ����(��λ��ms)

    }

    //ģ��ʱ������
    class Task extends TimerTask {

        public void run() {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String i_t = "2022-05-11 10:54:00";
            Calendar initial_time = shiftDate(i_t);

            //ģ��ϵͳ�ĵ�ǰʱ�估����ʱ����Ϣ
            calendar = (GregorianCalendar) shiftDate(df.format(showSimulateTime(initial_time)));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            date = calendar.get(Calendar.DATE);
            week = calendar.get(Calendar.DAY_OF_WEEK);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);

            timeStr = year + "��" + month + "��" + date + "�� " + "����" + week + " " + hour + " : " + minute + " : " + second;

            hourLine.x2 = X + 40 * Math.cos(hour * (Math.PI / 6) - Math.PI / 2);
            hourLine.y2 = Y + 40 * Math.sin(hour * (Math.PI / 6) - Math.PI / 2);
            minLine.x2 = X + 45 * Math.cos(minute * (Math.PI / 30) - Math.PI / 2);
            minLine.y2 = Y + 45 * Math.sin(minute * (Math.PI / 30) - Math.PI / 2);
            secondLine.x2 = X + 50 * Math.cos(second * (Math.PI / 30) - Math.PI / 2);
            secondLine.y2 = Y + 50 * Math.sin(second * (Math.PI / 30) - Math.PI / 2);

            repaint();
        }

    }

    //ʱ��ͼ�λ�
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

            g2.drawString("12", 55, 25);//����ʱ��

            g2.drawString("6", 55, 105);

            g2.drawString("9", 15, 65);

            g2.drawString("3", 100, 65);

            g2.drawString(timeStr, 0, 130);

            g2.draw(e);

            g2.draw(hourLine);//ʱ��

            g2.draw(minLine);//����

            g2.draw(secondLine);//����

        }

    }
}