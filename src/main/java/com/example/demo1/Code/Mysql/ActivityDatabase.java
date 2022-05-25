package com.example.demo1.Code.Mysql;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.account.Account;

import java.sql.*;
import java.util.ArrayList;

public class ActivityDatabase {
    // ����MySQL�����ݿ���������
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // ����MySQL���ݿ�����ӵ�ַ
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL���ݿ�������û���
    public static final String m_sUser ="root";
    // MySQL���ݿ����������
    public static final String m_sPassword ="20021213";

    /**
     * �����ݿ�����µĻ����
     * @param activity �»
     */
    public void insert(Activity activity){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String sql = "INSERT INTO activity(id,name,startmonth,startdate,starthour,startmin,endhour,endmin,property,num,maxnum,)"
                + " VALUES ('" + activity.getM_iNum() + "','" + activity.getM_sName() + "','" +
                activity.getM_tTime().getStartMonth()+ "','" +activity.getM_tTime().getStartDate()+ "','" +
                activity.getM_tTime().getStartHour()+ "','" +activity.getM_tTime().getStartMinute()+ 
                "','" + activity.getM_tTime().getEndHour()+ "','" + activity.getM_tTime().getEndMinute()
                + "','" + activity.getM_eProperty()+ "','" +activity.getM_iPle()+ "','" 
                +activity.getM_iMaxPle()+ "')";
        String sql1 = "INSERT INTO activity_construction(activity_id,construction_id,floor,room)"+
                "VALUES('"+activity.getM_iNum()+"','"+activity.getM_sConstruction().get_con_number()+
                "','"+activity.getM_iFloor()+"','"+activity.getM_iRoom()+"')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.executeUpdate(sql1);
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","���ݿ��ȡ����");
        }
    }
    /**
     * ���û��������в������ݣ�ʵ���û���ѡ�γ̵����
     * @param activity ����ӿγ�
     * @param account ����ӿγ̵Ķ�Ӧ�˺�
     */
    public void insert(Activity activity, Account account){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String sql = "INSERT INTO account_activity(id_account, id_activity)"
                + " VALUES ('" + account.getID() + "','" + activity.getM_iNum() + "')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�f
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","���ݿ��ȡ����");
        }

    }
    /**
     * �޸����ݿ��еĻ����
     * @param activity ���޸Ļ
     */
    public void update(Activity activity){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        // ƴ�ճ�һ��������SQL���
        String sql = "UPDATE activity SET name='" + activity.getM_sName() +"',startmonth='"
                + activity.getM_tTime().getStartMonth() + "',startdate='"+activity.getM_tTime().getStartDate()
                + "',starthour='" + activity.getM_tTime().getStartHour()
                +"',startmin='"+activity.getM_tTime().getStartMinute()
                + "',endhour='" + activity.getM_tTime().getEndHour() + "',endmin='" + 
                activity.getM_tTime().getEndMinute() + "',property='" + activity.getM_eProperty() 
                +"',num='" + activity.getM_iPle() + "',maxnum='" + activity.getM_iMaxPle() 
                 + "'WHERE id=" + activity.getM_iNum() ;
        String sql1 = "UPDATE activity_construction SET construction_id='"+activity.getM_sConstruction().get_con_number()
                +"',floor = '"+activity.getM_iFloor()+"',room = '"+activity.getM_iRoom()+"'WHERE activity_id ="
                +activity.getM_iNum();
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.executeUpdate(sql1);
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","���ݿ��ȡ����");
        }
    }
    /**
     * ɾ�����ݿ��е�ָ���
     * @param activity ��ɾ���
     */
    public void delete(Activity activity){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String sql1 = "DELETE FROM account WHERE id=" + activity.getM_iNum();
        String sql2 = "DELETE FROM account_activity WHERE id_activity = "+activity.getM_iNum();//ɾ���û�_�������еĶ�Ӧ����
        String sql3 = "DELETE FROM activity_construction WHERE activity_id="+activity.getM_iNum();//ɾ���_�����������еĶ�Ӧ����
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql1);// ִ�����ݿ���²���
            stmt.executeUpdate(sql2);// ִ�����ݿ���²���
            stmt.executeUpdate(sql3);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","���ݿ��ȡ����");
        }
    }
    /**
     * ���û��γ̹�������ɾ�����ݣ�ʵ���û���ѡ�γ̵�ɾ��
     * @param activity ��ɾ���γ�
     * @param account �������˺�
     */
    public void delete(Activity activity,Account account){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        // ƴ�ճ�һ��������SQL���
        String sql = "DELETE FROM account_activity WHERE id_account = '"+ account.getID()+"'and id_activity=" + activity.getM_iNum();
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","���ݿ��ȡ����");
        }
    }
    /**
     * �����ݿ��ڶ�ȡ�
     * @param activity ����ȡ�����
     */
    public void find(Activity activity) {
        ResultSet rs = null; // �����ѯ���
        String sql = "SELECT id, name, startmonth,startdate,starthour, startmin, endhour," +
                " endmin, property, num, maxnum FROM activity WHERE id =" + activity.getM_iNum();
        Connection conn = null; // ���ݿ�����
        Statement stmt = null; // ���ݿ����
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                activity.setM_sName(rs.getString("name"));
                activity.getM_tTime().setStartMonth(rs.getInt("startmonth"));
                activity.getM_tTime().setStartDate(rs.getInt("startdate"));
                activity.getM_tTime().setStartHour(rs.getInt("starthour"));
                activity.getM_tTime().setStartMinute(rs.getInt("startmin"));
                activity.getM_tTime().setEndHour(rs.getInt("endhour"));
                activity.getM_tTime().setEndMinute(rs.getInt("endmin"));
                activity.setM_eProperty(rs.getInt("property"));
                activity.setM_iPle(rs.getInt("num"));
                activity.setM_iMaxPle(rs.getInt("maxnum"));
            }
            rs.close();// �رս����
            stmt.close(); // �����ر�
            conn.close(); // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","���ݿ��ȡ����");
        }
    }
    public void find(ArrayList<Activity> activity) {
        ResultSet rs = null; // �����ѯ���
        String sql = "SELECT id, name,startmonth,startdate ,starthour, startmin, endhour," +
                " endmin, property, num, maxnum FROM activity" ;
        Connection conn = null; // ���ݿ�����
        Statement stmt = null; // ���ݿ����
        ConstructionDatabase constructionDatabase = new ConstructionDatabase();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                Activity a = new Activity();
                a.setM_sName(rs.getString("name"));
                a.getM_tTime().setStartMonth(rs.getInt("startmonth"));
                a.getM_tTime().setStartDate(rs.getInt("startdate"));
                a.getM_tTime().setStartHour(rs.getInt("starthour"));
                a.getM_tTime().setStartMinute(rs.getInt("startmin"));
                a.getM_tTime().setEndHour(rs.getInt("endhour"));
                a.getM_tTime().setEndMinute(rs.getInt("endmin"));
                a.setM_eProperty(rs.getInt("property"));
                a.setM_iPle(rs.getInt("num"));
                a.setM_iMaxPle(rs.getInt("maxnum"));
                activity.add(a);
            }
            rs.close();// �رս����
            stmt.close(); // �����ر�
            conn.close(); // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","���ݿ��ȡ����");
        }
        for(int i =0;i<activity.size();i++) {
            Activity activity1 = activity.get(i);
            constructionDatabase.findByActivity(activity1);
            constructionDatabase.findByActivity(activity1);
            activity.set(i,activity1);
        }

    }
}
