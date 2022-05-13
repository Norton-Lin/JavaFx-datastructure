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
      * 输出活动信息
      */
     public void printActivity(){
          System.out.println("课程名："+this.getM_sName()+"\n");
          System.out.println("上课时间为："+this.getM_tTime()+"\n");
          System.out.println("课程类型："+this.getM_eProperty()+"\n");
          System.out.println("授课地点："+this.getM_sConstruction().get_con_name()+
                  this.getM_iFloor()+"层"+this.getM_iRoom()+"室"+"\n");
          System.out.println("当前参加课程人数："+this.getM_iPle()+"\n");
          System.out.println("最大参与课程课程："+this.getM_iMaxPle()+"\n");
          System.out.println("课程编号："+this.getM_iNum()+"\n");
     }
}