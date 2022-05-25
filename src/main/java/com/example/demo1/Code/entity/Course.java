package com.example.demo1.Code.entity;

import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;

import java.util.ArrayList;

public class Course extends Event{
    private String m_sCurGroup;//课程群
    private Time m_cExamTime = new Time();//考试时间
    private Construction m_cExamConstruction = new Construction();//考试所在建筑
    private int m_iExamFloor;//考试所在楼层
    private int m_iExamRoom;//考试所在房间
    private String m_sData;//课程资料
    private String m_sTeacher;//任课老师
    private int m_iTotalClass;//总课时
    private int m_iCurrentClass;//当前进度
    private ArrayList<Homework> m_CaHomework = new ArrayList<>();//课程作业列表

    public Course() {
    }

    public Course(String m_sName, Time m_tTime, Property m_eProperty, Construction m_sConstruction,int m_iFloor,int m_iRoom, int m_iPle, int m_iMaxPle, int m_iNum) {
        super(m_sName, m_tTime, m_eProperty, m_sConstruction,m_iFloor,m_iRoom, m_iPle, m_iMaxPle, m_iNum);
    }

    public Course(String m_sCurGroup, Time m_cExamTime, Construction m_cExamPlace,int m_iExamFloor, int m_iExamRoom,String m_sData,
                  int m_iTotalClass,int m_iCurrentClass,String teacher,ArrayList<Homework> m_CaHomework) {
        this.m_sCurGroup = m_sCurGroup;
        this.m_cExamTime = m_cExamTime;
        this.m_cExamConstruction = m_cExamPlace;
        this.m_iExamFloor = m_iExamFloor;
        this.m_iExamRoom=m_iExamRoom;
        this.m_sData = m_sData;
        this.m_sTeacher  = teacher;
        this.m_iTotalClass = m_iTotalClass;
        this.m_iCurrentClass = m_iCurrentClass;
        this.m_CaHomework = m_CaHomework;
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
    /*
    * 获取课程总课时
     */
    public int getM_iTotalClass() {
        return m_iTotalClass;
    }

    /**
     * 设置课程总课时
     * @param m_iTotalClass
     */
    public void setM_iTotalClass(int m_iTotalClass) {
        this.m_iTotalClass = m_iTotalClass;
    }

    public int getM_iCurrentClass() {
        return m_iCurrentClass;
    }

    public void setM_iCurrentClass(int m_iCurrentClass) {
        this.m_iCurrentClass = m_iCurrentClass;
    }

    public boolean joinCourse() {
        return true;
    }

    public boolean exitCourse() {
        return true;
    }

    public String getM_sData() {
        return m_sData;
    }

    public void setM_sData(String m_sData) {
        this.m_sData = m_sData;
    }

    public ArrayList<Homework> getM_CaHomework() {
        return m_CaHomework;
    }

    public void setM_CaHomework(ArrayList<Homework> m_CaHomework) {
        this.m_CaHomework = m_CaHomework;
    }

    public String getM_sTeacher() {
        return m_sTeacher;
    }

    public void setM_sTeacher(String m_sTeacher) {
        this.m_sTeacher = m_sTeacher;
    }
    /**
    * 输出课程信息
    */
    public void printCourse(){
        System.out.println("课程名："+this.getM_sName()+"\n");
        System.out.println("上课时间为：");
        this.getM_tTime().printTime(true);
        System.out.println("课程类型："+this.getM_eProperty()+"\n");
        System.out.println("授课地点："+this.getM_sConstruction().get_con_name()+
                this.getM_iFloor()+"层"+this.getM_iRoom()+"室"+"\n");
        System.out.println("当前参加课程人数："+this.getM_iPle()+"\n");
        System.out.println("最大参与课程课程："+this.getM_iMaxPle()+"\n");
        System.out.println("课程编号："+this.getM_iNum()+"\n");
        System.out.println("课程群："+this.getM_sCurGroup()+"\n");
        System.out.println("课程考试时间：");
        this.getM_cExamTime().printTime(false);
        System.out.println("课程考试地点："+this.getM_cExamConstruction().get_con_name()+
                this.getM_iExamFloor()+"层"+this.getM_iExamRoom()+"室"+"\n");
        System.out.println("课程资料区路径："+this.getM_sData());
    }

}
