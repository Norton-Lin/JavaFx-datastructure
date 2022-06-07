package com.example.demo1.Code.Util;

import java.util.Scanner;

public class Time {

    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private int startMonth;//开始月份
    private int startDate;//开始日期
    private int week;//星期
    public Time() {
        startHour = -1;
        startMinute = -1;
        endHour = -1;
        endMinute = -1;
        startDate = -1;
        startMonth = -1;
        week = -1;
    }

    public Time(int startHour, int startMinute, int endHour, int endMinute, int week) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.week = week;
        this.startMonth = 0;
        this.startDate = 0;
    }

    public Time(int hour1, int minute1, int hour2, int minute2, int date, int month) {
        this.startHour = hour1;
        this.startMinute = minute1;
        this.endHour = hour2;
        this.endMinute = minute2;
        this.startDate = date;
        this.startMonth = month;
    }

    public Time(int startHour, int startMinute, int endHour, int endMinute, int startMonth, int startDate, int week) {
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.startMonth = startMonth;
        this.startDate = startDate;
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
     * 输入时间数据（课程）
     * 有星期，无月日
     */
    public void scanTime () {
        Scanner in = new Scanner(System.in);
        System.out.println("请输入星期：");
        this.week = in.nextInt();
        System.out.println("请输入开始时间（时）");
        this.startHour = in.nextInt();
        System.out.println("请输入开始时间（分）");
        this.startMinute = in.nextInt();
        System.out.println("请输入结束时间（时）");
        this.endHour = in.nextInt();
        System.out.println("请输入结束时间（时）");
        this.endMinute = in.nextInt();
    }

    public void scanDateTime() {
        Scanner in = new Scanner(System.in);
        System.out.println("请输入月份：");
        this.startMonth = in.nextInt();
        System.out.println("请输入日期：");
        this.startDate = in.nextInt();
        System.out.println("请输入开始时间（时）");
        this.startHour = in.nextInt();
        System.out.println("请输入开始时间（分）");
        this.startMinute = in.nextInt();
        System.out.println("请输入结束时间（时）");
        this.endHour = in.nextInt();
        System.out.println("请输入结束时间（时）");
        this.endHour = in.nextInt();
    }
    public void printTime(boolean choice){
        if(choice) {
            System.out.println("星期"+this.week+"\t");
        }
        else{
            System.out.println(this.startMonth+"月"+this.startDate+"日\t");
        }
        System.out.println(this.startHour+":"+this.startMinute+"-"+this.endHour+":"+this.endMinute+"\n");
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
