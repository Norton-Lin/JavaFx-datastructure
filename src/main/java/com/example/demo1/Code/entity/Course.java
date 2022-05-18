package com.example.demo1.Code.entity;

import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;

public class Course extends Event{
    private String m_sCurGroup;//课程群
    private Time m_cExamTime = new Time();//考试时间
    private Construction m_cExamConstruction = new Construction();//考试所在建筑
    private int m_iExamFloor;//考试所在楼层
    private int m_iExamRoom;//考试所在房间


    public Course() {
    }

    public Course(String m_sName, Time m_tTime, Property m_eProperty, Construction m_sConstruction,int m_iFloor,int m_iRoom, int m_iPle, int m_iMaxPle, int m_iNum) {
        super(m_sName, m_tTime, m_eProperty, m_sConstruction,m_iFloor,m_iRoom, m_iPle, m_iMaxPle, m_iNum);
    }

    public Course(String m_sCurGroup, Time m_cExamTime, Construction m_cExamPlace) {
        this.m_sCurGroup = m_sCurGroup;
        this.m_cExamTime = m_cExamTime;
        this.m_cExamConstruction = m_cExamPlace;
    }

    /**
     * 获取课程群
     * @return 课程群号
     */
    public String getM_sCurGroup() {
        return m_sCurGroup;
    }

    /**
     * 修改课程群
     * @param m_sCurGroup 新课程群号
     */
    public void setM_sCurGroup(String m_sCurGroup) {
        this.m_sCurGroup = m_sCurGroup;
    }

    /**
     * 获取考试时间
     * @return 新考试时间
     */
    public Time getM_cExamTime() {
        return m_cExamTime;
    }

    /**
     * 修改考试时间
     * @param m_cExamTime 新考试时间
     */
    public void setM_cExamTime(Time m_cExamTime) {
        this.m_cExamTime = m_cExamTime;
    }

    /**
     * 获取考试地点
     * @return 考试地点
     */
    public Construction getM_cExamConstruction() {
        return m_cExamConstruction;
    }

    /**
     * 修改考试地点
     * @param m_cExamConstruction 新考试地点
     */
    public void setM_cExamConstruction(Construction m_cExamConstruction) {
        this.m_cExamConstruction = m_cExamConstruction;
    }

    public int getM_iExamFloor() {
        return m_iExamFloor;
    }

    public void setM_iExamFloor(int m_iExamFloor) {
        this.m_iExamFloor = m_iExamFloor;
    }

    public int getM_iExamRoom() {
        return m_iExamRoom;
    }

    public void setM_iExamRoom(int m_iExamRoom) {
        this.m_iExamRoom = m_iExamRoom;
    }

    public boolean joinCourse() {
        return true;
    }

    public boolean exitCourse() {
        return true;
    }
    /**
    * 输出课程信息
    */
    public void printCourse(){
        System.out.println("课程名："+this.getM_sName()+"\n");
        System.out.println("上课时间为："+this.getM_tTime()+"\n");
        System.out.println("课程类型："+this.getM_eProperty()+"\n");
        System.out.println("授课地点："+this.getM_sConstruction().get_con_name()+
                this.getM_iFloor()+"层"+this.getM_iRoom()+"室"+"\n");
        System.out.println("当前参加课程人数："+this.getM_iPle()+"\n");
        System.out.println("最大参与课程课程："+this.getM_iMaxPle()+"\n");
        System.out.println("课程编号："+this.getM_iNum()+"\n");
        System.out.println("课程群："+this.getM_sCurGroup()+"\n");
        System.out.println("课程考试时间："+this.getM_cExamTime()+"\n");
        System.out.println("课程考试地点："+this.getM_cExamConstruction().get_con_name()+
                this.getM_iExamFloor()+"层"+this.getM_iExamRoom()+"室"+"\n");
    }


}
