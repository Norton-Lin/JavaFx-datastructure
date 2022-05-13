package com.example.demo1.Code.Util;

import java.util.Scanner;

public class Time {

    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int startMonth;//��ʼ�·�
    private int startDate;//��ʼ����
    private int week;//����
    public Time() {
        startHour = 0;
        startMinute = 0;
        endHour = 0;
        endMinute = 0;
        startDate = 0;
        startMonth = 0;
        week =0;
    }

    public Time(int hour1, int minute1, int hour2, int minute2, int date, int month,int week) {
        this.startHour = hour1;
        this.startMinute = minute1;
        this.endHour = hour2;
        this.endMinute = minute2;
        this.startDate = date;
        this.startMonth = month;
        this.week = week;

    }

    public boolean checkTime(Time time) {
        boolean result;
        if(time.getWeek()==this.week&&time.getStartMonth()==this.startMonth&&time.getStartDate()==this.startDate) {
            if ((time.endHour < this.startHour) || (time.startHour > this.endHour)) result = true;
            else if ((time.endHour == this.startHour) && (time.endMinute < this.startHour)) result = true;
            else if ((time.startHour == this.endHour) && (time.startMinute > this.endHour)) result = true;
            else result = false;
        }
        else
            result = true;
        return result;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public void setStartDate(int date) {
        this.startDate = date;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    /**
     * ����ʱ�����ݣ��γ̣�
     * �����ڣ�������
     */
    public void scanTime () {
        Scanner in = new Scanner(System.in);
        System.out.println("���������ڣ�");
        this.week = in.nextInt();
        System.out.println("�����뿪ʼʱ�䣨ʱ��");
        this.startHour = in.nextInt();
        System.out.println("�����뿪ʼʱ�䣨�֣�");
        this.startMinute = in.nextInt();
        System.out.println("���������ʱ�䣨ʱ��");
        this.endHour = in.nextInt();
        System.out.println("���������ʱ�䣨ʱ��");
        this.endHour = in.nextInt();
    }

    public void scanDateTime() {
        Scanner in = new Scanner(System.in);
        System.out.println("�������·ݣ�");
        this.startMonth = in.nextInt();
        System.out.println("���������ڣ�");
        this.startDate = in.nextInt();
        System.out.println("�����뿪ʼʱ�䣨ʱ��");
        this.startHour = in.nextInt();
        System.out.println("�����뿪ʼʱ�䣨�֣�");
        this.startMinute = in.nextInt();
        System.out.println("���������ʱ�䣨ʱ��");
        this.endHour = in.nextInt();
        System.out.println("���������ʱ�䣨ʱ��");
        this.endHour = in.nextInt();
    }
    /** public void setsDate(String time){
        int head = 0,tail =0;
        for(int i=0;i<3;i++)
        {
            if(i==1){
                tail = time.indexOf('-',head);
                this.startMonth = Integer.parseInt(time.substring(head,tail));
            }
            if(i==2) {
                tail = time.indexOf(' ', head);
                this.startDate = Integer.parseInt(time.substring(head, tail));
            }
            tail = head +1;
        }
    }
    public void setStartTime(String time){
        int head = 0,tail =0;
        for(int i=0;i<3;i++)
        {
            switch (i) {
                case 0 -> tail = time.indexOf(' ', head);
                case 1 -> {
                    tail = time.indexOf(':', head);
                    this.startHour = Integer.parseInt(time.substring(head, tail));
                }
                case 2 -> {
                    tail = time.indexOf(':', head);
                    this.startMinute = Integer.parseInt(time.substring(head, tail));
                }
            }
            tail = head +1;
        }
    }
    public void setEndTime(String time){
        int head = 0,tail =0;
        for(int i=0;i<3;i++)
        {
            switch (i) {
                case 0 -> tail = time.indexOf(' ', head);
                case 1 -> {
                    tail = time.indexOf(':', head);
                    this.endHour = Integer.parseInt(time.substring(head, tail));
                }
                case 2 -> {
                    tail = time.indexOf(':', head);
                    this.endMinute = Integer.parseInt(time.substring(head, tail));
                }
            }
            tail = head +1;
        }
    }*/
}
