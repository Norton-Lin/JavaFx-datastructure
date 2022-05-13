package com.example.demo1.Code.entity;

import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;

public class Activity extends Event{
     public Activity(String m_sName, Time m_tTime, Property m_eProperty, Construction m_cPlace,int m_iFloor,int m_iRoom, int m_iPle, int m_iMaxPle, int m_iNum) {
          super(m_sName, m_tTime, m_eProperty, m_cPlace,m_iFloor,m_iRoom, m_iPle, m_iMaxPle, m_iNum);
     }
     public Activity(){

     }
     public boolean exit() {
          return true;
     }
     /**
      * ������Ϣ
      */
     public void printActivity(){
          System.out.println("�γ�����"+this.getM_sName()+"\n");
          System.out.println("�Ͽ�ʱ��Ϊ��"+this.getM_tTime()+"\n");
          System.out.println("�γ����ͣ�"+this.getM_eProperty()+"\n");
          System.out.println("�ڿεص㣺"+this.getM_sConstruction().get_con_name()+
                  this.getM_iFloor()+"��"+this.getM_iRoom()+"��"+"\n");
          System.out.println("��ǰ�μӿγ�������"+this.getM_iPle()+"\n");
          System.out.println("������γ̿γ̣�"+this.getM_iMaxPle()+"\n");
          System.out.println("�γ̱�ţ�"+this.getM_iNum()+"\n");
     }
}