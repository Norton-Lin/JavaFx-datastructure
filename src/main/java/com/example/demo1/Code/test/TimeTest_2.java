
package com.example.demo1.Code.test;

import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Event;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TimeTest_2 {

    private int speed;//ʱ�����ٶ�

    /**
     * ����ģ��ϵͳʱ�����ٶ�
     *
     * @param speed Ŀ�����ٶ�
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * ��ȡģ��ϵͳʱ�����ٶ�
     *
     * @return speed ��ǰ����ٶ�
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * ��ȡϵͳ��ǰʱ��
     *
     * @return ϵͳ��ǰʱ��
     */
    public Calendar getSystemTime() {
        return Calendar.getInstance();
    }

    /**
     * ��ȡģ��ϵͳ��ǰʱ��
     *
     * @param initial_time ģ��ϵͳ��ʼʱ��
     * @return ������ʽ��ģ��ϵͳ��ǰʱ��
     */
    public long showSimulateTime(Calendar initial_time) {

        Calendar current_time = getSystemTime();//��ǰϵͳʱ��

        long interval = (current_time.getTimeInMillis() - initial_time.getTimeInMillis()) * this.getSpeed();

        return initial_time.getTimeInMillis() + interval;// show_timeΪģ��ϵͳ�ĵ�ǰʱ��

    }

    /**
     * ���涨��ʽ��ʱ���ַ���ת��ΪCalendar����
     *
     * @param t �ַ������͵�ʱ��
     * @return Calendar���͵�ʱ��
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
     * �������Ӳ�����
     *
     * @param cycle_type ��������
     * @param event      ��Ҫ������ӵ�����
     */
    public void setAlarmClock(int cycle_type, Event event) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //���ʱ��Ϊģ��ϵͳ��ʼʹ��ʱ�������ʱ�䣬����Ľ��������ã�����������ʽ
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

                //��������
                case 0 -> {

                    if (d1 == d2 && h1 == h2 && m1 == m2) {
                        System.out.println("Time for activity :-)");
                    }

                }

                //ÿ��һ������
                case 1 -> {

                    if (h1 == h2 && m1 == m2) {
                        System.out.println("Time for activity :-)");
                    }

                }

                //ÿ��һ������
                case 7 -> {

                    if ((d2 - d1) % 7 == 0 && h1 == h2 && m1 == m2) {
                        System.out.println("Time for activity :-)");
                    }

                }
            }
        }
    }


    //��������ʹ��
    public static void main(String[] args) {

        TimeTest_2 st = new TimeTest_2();
        Event event = new Event();
        Time time=new Time();
        time.setStartMonth(5);
        time.setStartDate(6);
        time.setStartHour(13);
        time.setStartMinute(30);
        event.setM_tTime(time);

        st.setSpeed(600);//���ÿ���ٶ�

        st.setAlarmClock(1, event);//����һ��ÿ�����ѵ�����

    }

}
