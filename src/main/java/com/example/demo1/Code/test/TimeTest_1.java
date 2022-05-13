
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
 * ģ��ϵͳʱ��+��������
 * �������µ����⣺
 * 1.ʱ��ͼ�λ�����Ӧ�ý���ǰ����ɣ��������������������
 * 2.���������˵�ʱ��Ӧ��ʹ��print��ӡ�ַ���������Ӧ����ǰ����ʾ������ҲҪ��ǰ���ٹ�ͨ
 * 3.ģ��ʵ��ϵͳ����ʼʱ��Ӧ�����û���ʼʹ��ϵͳ��ʱ��ļ����ϵͳʱ�䣬���Ҳ��ǰ���й�
 * 4.û�п������ӵ�����(�ѽ��)
 */
public class TimeTest_1 {

    private static int speed;//ʱ�����ٶ�

    /**
     * ����ģ��ϵͳʱ�����ٶ�
     *
     * @param speed Ŀ�����ٶ�
     */
    public void setSpeed(int speed) {
        TimeTest_1.speed = speed;
    }

    /**
     * Ϊָ����������������
     *
     * @param event ָ������
     */
    public void setClock(Event event, int type) {

        Clock t = new Clock();

        t.setLocationRelativeTo(null);//ʱ�Ӵ�����ʾ����Ļ����
        t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        t.setVisible(true);

        //����Ҫ���õ�����ʱ��
        Clock.setDate = event.getM_tTime().getStartDate();
        Clock.setHour = event.getM_tTime().getStartHour();
        Clock.setMinute = event.getM_tTime().getStartMinute();
        Clock.setSecond = 0;

        t.setType(type);
    }

    /**
     * ��ȡģ��ϵͳ��ǰʱ��
     *
     * @param initial_time ģ��ϵͳ��ʼʱ��
     * @return ������ʽ��ģ��ϵͳ��ǰʱ��
     */
    public static long showSimulateTime(Calendar initial_time) {

        Calendar current_time = Calendar.getInstance();//��ǰϵͳʱ��

        long interval = (current_time.getTimeInMillis() - initial_time.getTimeInMillis()) * speed;

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

            t.schedule(task, 0, 1); //�ӱ�ˢ��ʱ����(��λ��ms)

        }

        public void setType(int type) {
            this.type = type;
        }

        //ѭ��ִ��ģ��ʱ������
        class Task extends TimerTask {

            public void run() {

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String i_t = "2022-05-04 16:20:11";
                Calendar initial_time = shiftDate(i_t);

                //ģ��ϵͳ�ĵ�ǰʱ��
                calendar = (GregorianCalendar) shiftDate(df.format(showSimulateTime(initial_time)));

                month = calendar.get(Calendar.MONTH);
                date = calendar.get(Calendar.DATE);
                week = calendar.get(Calendar.DAY_OF_WEEK);
                hour = calendar.get(Calendar.HOUR);
                minute = calendar.get(Calendar.MINUTE);
                second = calendar.get(Calendar.SECOND);

                //Calendar.WEEK�����տ�ʼ
                if (week == 1) {
                    week = 7;
                } else {
                    week = week - 1;
                }

                month = month + 1;//Calendar.MONTH��0��ʼ

                //ʱ����ʾ����
                timeStr = "Current Time: " + month + " - " + date + " " + "����" + week + " " + hour + " : " + minute + " : " + second;

                switch (type) {

                    //һ��������
                    case 0 -> {
                        if (setMonth == month && setDate == date && setHour == hour && setMinute == minute && setSecond == second) {
                            System.out.println("time!");
                        }
                    }
                    //ÿ��һ������
                    case 1 -> {
                        if (setHour == hour && setMinute == minute && setSecond == second) {
                            System.out.println("time!");
                        }
                    }
                    //ÿ��һ������
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

        //ʵ��ͼ�λ����棬�������ܵý���ǰ��ʵ��
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

    //main��������������
    public static void main(String[] args) {

        TimeTest_1 test_1 = new TimeTest_1();

        Time time = new Time();

        time.setStartMonth(12);//�ʱ���Ӧ���·�
        time.setStartDate(4);//�ʱ���Ӧ������
        time.setWeek(3);//�ʱ���Ӧ������
        time.setStartHour(4);//�ʱ���Ӧ��Сʱ
        time.setStartMinute(38);//�ʱ���Ӧ�ķ���

        Event event = new Event();
        event.setM_tTime(time);

        test_1.setSpeed(600);//���ÿ���ٶ�
        test_1.setClock(event, 1);//����һ������
    }

}
