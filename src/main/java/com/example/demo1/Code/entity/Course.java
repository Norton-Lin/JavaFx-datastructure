package com.example.demo1.Code.entity;

import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;

public class Course extends Event{
    private String m_sCurGroup;//�γ�Ⱥ
    private Time m_cExamTime = new Time();//����ʱ��
    private Construction m_cExamConstruction = new Construction();//�������ڽ���
    private int m_iExamFloor;//��������¥��
    private int m_iExamRoom;//�������ڷ���


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
     * ��ȡ�γ�Ⱥ
     * @return �γ�Ⱥ��
     */
    public String getM_sCurGroup() {
        return m_sCurGroup;
    }

    /**
     * �޸Ŀγ�Ⱥ
     * @param m_sCurGroup �¿γ�Ⱥ��
     */
    public void setM_sCurGroup(String m_sCurGroup) {
        this.m_sCurGroup = m_sCurGroup;
    }

    /**
     * ��ȡ����ʱ��
     * @return �¿���ʱ��
     */
    public Time getM_cExamTime() {
        return m_cExamTime;
    }

    /**
     * �޸Ŀ���ʱ��
     * @param m_cExamTime �¿���ʱ��
     */
    public void setM_cExamTime(Time m_cExamTime) {
        this.m_cExamTime = m_cExamTime;
    }

    /**
     * ��ȡ���Եص�
     * @return ���Եص�
     */
    public Construction getM_cExamConstruction() {
        return m_cExamConstruction;
    }

    /**
     * �޸Ŀ��Եص�
     * @param m_cExamConstruction �¿��Եص�
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
    * ����γ���Ϣ
    */
    public void printCourse(){
        System.out.println("�γ�����"+this.getM_sName()+"\n");
        System.out.println("�Ͽ�ʱ��Ϊ��"+this.getM_tTime()+"\n");
        System.out.println("�γ����ͣ�"+this.getM_eProperty()+"\n");
        System.out.println("�ڿεص㣺"+this.getM_sConstruction().get_con_name()+
                this.getM_iFloor()+"��"+this.getM_iRoom()+"��"+"\n");
        System.out.println("��ǰ�μӿγ�������"+this.getM_iPle()+"\n");
        System.out.println("������γ̿γ̣�"+this.getM_iMaxPle()+"\n");
        System.out.println("�γ̱�ţ�"+this.getM_iNum()+"\n");
        System.out.println("�γ�Ⱥ��"+this.getM_sCurGroup()+"\n");
        System.out.println("�γ̿���ʱ�䣺"+this.getM_cExamTime()+"\n");
        System.out.println("�γ̿��Եص㣺"+this.getM_cExamConstruction().get_con_name()+
                this.getM_iExamFloor()+"��"+this.getM_iExamRoom()+"��"+"\n");
    }


}
