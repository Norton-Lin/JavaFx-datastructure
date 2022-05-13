package com.example.demo1.Code.Mysql;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Util.Authority;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.account.Account;

import java.sql.*;
import java.util.ArrayList;

public class CourseDatabase {
    // ����MySQL�����ݿ���������
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // ����MySQL���ݿ�����ӵ�ַ
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL���ݿ�������û���
    public static final String m_sUser ="root";
    // MySQL���ݿ����������
    public static final String m_sPassword ="20021213";
    /**
     * �����ݿ�����µĿγ�����
     * @param course �¿γ�
     */
    public void insert(Course course){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String sql = "INSERT INTO course(id,name,weeks,starthour,startmin,endhour,endmin,property,num,maxnum," +
                "connectgroup,tstartmonth,tstartdate,tstarthour,tstartmin,tendhour,tendmin)"
                + " VALUES ('" + course.getM_iNum() + "','" + course.getM_sName() + "','" +course.getM_tTime().getWeek()
                + "','" + course.getM_tTime().getStartHour() + "','" +course.getM_tTime().getStartMinute()
                + "','" + course.getM_tTime().getEndHour() + "','" + course.getM_tTime().getEndMinute()
                + "','" + course.getM_eProperty(0)+ "','" +course.getM_iPle()
                + "','" +course.getM_iMaxPle()+ "','" +course.getM_sCurGroup()
                + "','" +course.getM_cExamTime().getStartMonth()+"','" +course.getM_cExamTime().getStartDate()
                + "','" +course.getM_cExamTime().getStartHour()+"','" +course.getM_cExamTime().getStartMinute()
                + "','" +course.getM_cExamTime().getEndHour() + "','" +course.getM_cExamTime().getEndMinute()+"')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","���ݿ��ȡ����");
        }
    }
    /**
     * ���û��������в������ݣ�ʵ���û���ѡ�γ̵����
     * @param course ����ӿγ�
     * @param account ����ӿγ̵Ķ�Ӧ�˺�
     */
    public void insert(Course course,Account account){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String sql = "INSERT INTO account_course(id_account, id_course)"
                + " VALUES ('" + account.getID() + "','" + course.getM_iNum() + "')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�f
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","���ݿ��ȡ����");
        }

    }
    /**
     * �޸����ݿ��еĿγ�����
     * @param course ���޸Ŀγ�
     */
    public void update(Course course){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        // ƴ�ճ�һ��������SQL���
        String sql = "UPDATE course SET name='" + course.getM_sName() +"',weeks'"+
                course.getM_tTime().getWeek() + "',starthour='"
                + course.getM_tTime().getStartHour()+"',startmin='"+course.getM_tTime().getStartMinute()
                + "',endhour='" + course.getM_tTime().getEndHour() + "',endmin='" + course.getM_tTime().getEndMinute()
                + "',property='" + course.getM_eProperty() +"',num='" + course.getM_iPle() + "',maxnum='"
                + course.getM_iMaxPle() + "',connectgroup='" + course.getM_sCurGroup() + "',tstartmonth='"
                + course.getM_cExamTime().getStartMonth() +"',tstartdate='" +course.getM_cExamTime().getStartDate()
                + "',tstarthour='" + course.getM_cExamTime().getStartHour() +"',tstartmin='"
                + course.getM_cExamTime().getStartMinute() +"',property='"+ course.getM_cExamTime().getEndHour()
                +"',property='"+course.getM_cExamTime().getEndMinute() + "'WHERE id=" + course.getM_iNum() ;
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","���ݿ��ȡ����");
        }
    }
    /**
     * ɾ�����ݿ��е�ָ���γ�
     * @param course ��ɾ���γ�
     */
    public void delete(Course course){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        // ƴ�ճ�һ��������SQL���
        String sql1 = "DELETE FROM course WHERE id=" + course.getM_iNum();//ɾ���γ̱��е�����
        String sql2 = "DELETE FROM account_course WHERE id_course = "+course.getM_iNum();//ɾ���û�_�γ̹�����еĶ�Ӧ����
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql1);// ִ�����ݿ���²���
            stmt.executeUpdate(sql2);
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","���ݿ��ȡ����");
        }


    }
    /**
     * ���û��γ̹�������ɾ�����ݣ�ʵ���û���ѡ�γ̵�ɾ��
     * @param course ��ɾ���γ�
     * @param account �������˺�
     */
    public void delete(Course course,Account account){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        // ƴ�ճ�һ��������SQL���
        String sql = "DELETE FROM account_course WHERE id_account = '"+ account.getID()+"'and id_course=" + course.getM_iNum();
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","���ݿ��ȡ����");
        }
    }
    /**
     * �����ݿ��ڶ�ȡ�γ�
     * @param course ����ȡ�γ�����
     */
    public void find(Course course) {
        ResultSet rs = null; // �����ѯ���
        String sql = "SELECT * FROM course WHERE id = " + course.getM_iNum();
       /* String sql = "SELECT id, name, starthour, startmin, endhour, endmin, property, " +
                "num, maxnum, connectgroup, tstarthour, tstartmonth,tstartdate," +
                "tstartmin, tendhour, tendmin FROM course WHERE id = "+ course.getM_iNum();*/
        Connection conn = null; // ���ݿ�����
        Statement stmt = null; // ���ݿ����
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                course.setM_sName(rs.getString("name"));
                course.getM_tTime().setStartHour(rs.getInt("starthour"));
                course.getM_tTime().setStartMinute(rs.getInt("startmin"));
                course.getM_tTime().setEndHour(rs.getInt("endhour"));
                course.getM_tTime().setEndMinute(rs.getInt("endmin"));
                course.getM_tTime().setWeek(rs.getInt("weeks"));
                course.setM_eProperty(rs.getInt("property"));
                course.setM_iPle(rs.getInt("num"));
                course.setM_iMaxPle(rs.getInt("maxnum"));
                course.setM_sCurGroup(rs.getString("connectgroup"));
                course.getM_cExamTime().setStartMonth(rs.getInt("tstartmonth"));
                course.getM_cExamTime().setStartDate(rs.getInt("tstartdate"));
                course.getM_cExamTime().setStartHour(rs.getInt("tstarthour"));
                course.getM_cExamTime().setStartMinute(rs.getInt("tstartmin"));
                course.getM_cExamTime().setEndHour(rs.getInt("tendhour"));
                course.getM_cExamTime().setEndMinute(rs.getInt("tendmin"));
            }
            rs.close();// �رս����
            stmt.close(); // �����ر�
            conn.close(); // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","���ݿ��ȡ����");
        }
    }
    /**
     * �����ݿ��ж�ȡ�γ̼�
     * @param course �γ̼�
     */
    public void find(ArrayList<Course> course){
        ResultSet rs = null; // �����ѯ���
        String sql = "SELECT * FROM course";
      /*  String sql = "SELECT id, name, starthour, startmin, endhour," +
                " endmin, property, num, maxnum, connectgroup, tstarthour, " +
                "tstartmin, tendhour, tendmin FROM course ";*/
        Connection conn = null; // ���ݿ�����
        Statement stmt = null; // ���ݿ����
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                Course c = new Course();
                c.setM_iNum(rs.getInt("id"));
                c.setM_sName(rs.getString("name"));
                c.getM_tTime().setStartHour(rs.getInt("starthour"));
                c.getM_tTime().setStartMinute(rs.getInt("startmin"));
                c.getM_tTime().setEndHour(rs.getInt("endhour"));
                c.getM_tTime().setEndMinute(rs.getInt("endmin"));
                c.getM_tTime().setWeek(rs.getInt("weeks"));
                c.setM_eProperty(rs.getInt("property"));
                c.setM_iPle(rs.getInt("num"));
                c.setM_iMaxPle(rs.getInt("maxnum"));
                c.setM_sCurGroup(rs.getString("connectgroup"));
                c.getM_cExamTime().setStartMonth(rs.getInt("tstartmonth"));
                c.getM_cExamTime().setStartDate(rs.getInt("tstartdate"));
                c.getM_cExamTime().setStartHour(rs.getInt("tstarthour"));
                c.getM_cExamTime().setStartMinute(rs.getInt("tstartmin"));
                c.getM_cExamTime().setEndHour(rs.getInt("tendhour"));
                c.getM_cExamTime().setEndMinute(rs.getInt("tendmin"));
                course.add(c);
            }
            rs.close();// �رս����
            stmt.close(); // �����ر�
            conn.close(); // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","���ݿ��ȡ����");
        }
    }
    public static void main(String args[])
    {
         CourseDatabase courseDatabase = new CourseDatabase();
         Course course = new Course();
        course.setM_sName("��������ԭ��");
        course.getM_tTime().setStartHour(9);
        course.getM_tTime().setStartMinute(50);
        course.getM_tTime().setEndHour(11);
        course.getM_tTime().setEndMinute(25);
        course.getM_tTime().setWeek(1);
        course.setM_eProperty(0);
        course.setM_iPle(67);
        course.setM_iMaxPle(100);
        course.setM_sCurGroup("54555232234");
        course.getM_cExamTime().setStartMonth(6);
        course.getM_cExamTime().setStartDate(28);
        course.getM_cExamTime().setStartHour(10);
        course.getM_cExamTime().setStartMinute(0);
        course.getM_cExamTime().setEndHour(12);
        course.getM_cExamTime().setEndMinute(0);
        courseDatabase.insert(course);
    }
}
