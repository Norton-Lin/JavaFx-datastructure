package com.example.demo1.Code.entity;

import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;

public class Event {
    private String m_sName;//事项名
    private Time m_tTime = new Time();//事项时间
    private Property m_eProperty;//事项属性
    private Construction m_sConstruction = new Construction();//事项所在建筑
    private int m_iFloor;//事件所在楼层
    private int m_iRoom;//事件所在房间号
    private int m_iPle;//参与事项人数
    private int m_iMaxPle;//允许参与事项的最大人数
    private int m_iNum;//事项编号，四位数字，课程类与活动类的首位不同
    private Integer m_iWeight;//事项排序权重，由FuzzySearch算法产生

    public Event() {
        super();
    }

    public Event(String m_sName, Time m_tTime, Property m_eProperty, Construction m_sConstruction,
                 int m_iFloor,int m_iRoom, int m_iPle, int m_iMaxPle, int m_iNum) {
        this.m_sName = m_sName;
        this.m_tTime = m_tTime;
        this.m_eProperty = m_eProperty;
        this.m_sConstruction = m_sConstruction;
        this.m_iFloor=m_iFloor;
        this.m_iRoom=m_iRoom;
        this.m_iPle = m_iPle;
        this.m_iMaxPle = m_iMaxPle;
        this.m_iNum = m_iNum;
    }

    public Event(String m_sName, Time m_tTime, Property m_eProperty, Construction m_sConstruction) {
        this.m_sName = m_sName;
        this.m_tTime = m_tTime;
        this.m_eProperty = m_eProperty;
        this.m_sConstruction = m_sConstruction;
    }

    /**
     * 获取课程名
     * @return 课程名
     */
    public String getM_sName() {
        return m_sName;
    }

    /**
     * 修改事项名
     * @param m_sName 新事项名
     */
    public void setM_sName(String m_sName) {
        this.m_sName = m_sName;
    }

    /**
     * 获取事项时间
     * @return 事项时间
     */
    public Time getM_tTime() {
        return m_tTime;
    }

    /**
     * 修改事项时间
     * @param m_tTime 新事项时间
     */
    public void setM_tTime(Time m_tTime) {
        this.m_tTime = m_tTime;
    }

    /**
     * 获取事项属性
     * @return 事项属性
     */
    public Property getM_eProperty() {
        return m_eProperty;
    }
    public int getM_eProperty(int num){
        switch (m_eProperty)
        {
            case Theory -> num =0;
            case Practice -> num=1;
            case Sports -> num=2;
            case SELF -> num=3;
            case GROUP -> num=4;
            default -> num =5;
        }
        return num;
    }
    /**
     * 修改事项属性
     * @param m_eProperty 新事项属性
     */
    public void setM_eProperty(Property m_eProperty) {
        this.m_eProperty = m_eProperty;
    }
    public void setM_eProperty(int property) {
        switch (property) {
            case 0 -> this.m_eProperty = Property.Theory;
            case 1 -> this.m_eProperty = Property.Practice;
            case 2 -> this.m_eProperty = Property.Sports;
            case 3 -> this.m_eProperty = Property.SELF;
            case 4 -> this.m_eProperty = Property.GROUP;
            default -> this.m_eProperty = null;
        }
    }
    /**
     * 获取事项地点
     * @return 事项场所
     */
    public Construction getM_sConstruction() {
        return m_sConstruction;
    }

    /**
     * 修改事项地点
     * @param m_sConstruction 新事项地点
     */
    public void setM_sConstruction(Construction m_sConstruction) {
        this.m_sConstruction = m_sConstruction;
    }

    /**
     * 获取事项所在楼层
     * @return 事项所在楼层
     */
    public int getM_iFloor() {
        return m_iFloor;
    }

    /**
     * 修改事项所在楼层
     * @param m_iFloor 新楼层
     */
    public void setM_iFloor(int m_iFloor) {
        this.m_iFloor = m_iFloor;
    }

    /**
     * 获取事项所在房间
     * @return 事项所在房间
     */
    public int getM_iRoom() {
        return m_iRoom;
    }

    /**
     * 修改事项所在房间
     * @param m_iRoom 新房间
     */
    public void setM_iRoom(int m_iRoom) {
        this.m_iRoom = m_iRoom;
    }

    /**
     * 获取当前事项人数
     * @return 当前事项人数
     */
    public int getM_iPle() {
        return m_iPle;
    }

    public void setM_iPle(int m_iPle) {
        this.m_iPle = m_iPle;
    }

    /**
     * 获取事项最大人数
     * @return 事项最大人数
     */
    public int getM_iMaxPle() {
        return this.m_iMaxPle;
    }

    /**
     * 修改事项最大人数
     * @param m_iMaxPle 事项最大人数
     */
    public void setM_iMaxPle(int m_iMaxPle) {
        this.m_iMaxPle = m_iMaxPle;
    }

    /**
     * 获取事项编号
     * @return 事项编号
     */
    public int getM_iNum() {
        return m_iNum;
    }

    /**
     *修改事项编号
     * @param m_iNum 新事项编号
     */
    public void setM_iNum(int m_iNum) {
        this.m_iNum = m_iNum;
    }

    /**
     * 添加事项成员
     * @return 添加是否成功
     */
    public boolean addM_iPle() {
        boolean mark;//用来标记添加是否成功的标记变量
        if (m_iPle < m_iMaxPle) {
            m_iPle++;
            mark = true;
        }
        else
            mark = false;
        return mark;
    }

    /**
     * 删除事项成员
     * @return 删除是否成功
     */
    public boolean delM_iPle() {
        boolean mark;//用来标记是否删除成功的标记变量
        if (m_iPle > 0) {
            m_iPle--;
            mark = true;
        }
        else
            mark = false;
        return mark;
    }

    /**
     * 修改事项排序权重
     * @param weight 权值
     */
    public void setM_iWeight(int weight){
        this.m_iWeight=weight;
    }

    /**
     * 获取事项排序权重
     * @return 权值
     */
    public Integer getM_iWeight() {
        return m_iWeight;
    }

    public Integer getM_iStartTime(){
        return m_tTime.getStartHour();
    }
}
