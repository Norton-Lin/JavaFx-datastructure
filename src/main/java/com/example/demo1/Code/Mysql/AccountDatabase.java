package com.example.demo1.Code.Mysql;
import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Util.Authority;
import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.account.Account;
import com.example.demo1.Code.entity.account.ManagerAccount;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.entity.account.TeacherAccount;
//import com.mysql.cj.log.Log;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class AccountDatabase {
    // ����MySQL�����ݿ���������
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // ����MySQL���ݿ�����ӵ�ַ
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL���ݿ�������û���
    public static final String m_sUser ="root";
    // MySQL���ݿ����������
    public static final String m_sPassword ="20021213";
    //�����ݿ��в�������
    public void insert(Account account){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String id = account.getID();
        String password = account.getPassword();
        Authority c = account.getAuthority();
        int authority =2;
        if(account.getAuthority()==Authority.Student)
            authority=0;
        else if(account.getAuthority()==Authority.Teacher)
            authority=1;
        String sql = "INSERT INTO account(id,password,authority)"+
                " VALUES ('" + id + "','" + password + "','" + authority+ "')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("AccountDatabase","���ݿ��ȡ����");
        }
    }
    //�������ݿ��е�����
    public void update(Account account){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String id = account.getID();
        String password = account.getPassword();
        Authority authority = account.getAuthority();
        // ƴ�ճ�һ��������SQL���
        String sql = "UPDATE account SET password='" + password + "',authority='"
                + authority + "'WHERE id=" + id ;
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("AccountDatabase","���ݿ��ȡ����");
        }
    }
    //ɾ�����ݿ��е�����
    public void delete(Account account){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String id = account.getID(); // id
// ƴ�ճ�һ��������SQL���
        String sql = "DELETE FROM account WHERE id=" + id;
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("AccountDatabase","���ݿ��ȡ����");
        }
    }
    //��Ѱ���ݿ��е�����
    public void find(Account account) {
        ResultSet rs = null; // �����ѯ���
        String sql = "SELECT id,password,authority FROM account";
        Connection conn = null; // ���ݿ�����
        Statement stmt = null; // ���ݿ����
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                account.setM_sID(rs.getString("id")); // ȡ��id����
                account.setPassword(rs.getString("password"));// ȡ��name����
                account.setM_eAuthority(rs.getInt("authority"));//ȡ��authority
            }
            rs.close();// �رս����
            stmt.close(); // �����ر�
            conn.close(); // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("AccountDatabase","���ݿ��ȡ����");
        }
    }
    //�����˺�������Ѱ��ȡ�˺�Ȩ��
    public boolean findByPassword(Account account){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        ResultSet rs = null; // �����ѯ���
        Account temp = new Account();
        String sql1 = "SELECT id,password,authority FROM account where id = '"+account.getID()+"'and password ="+account.getPassword();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql1);// ʵ����ResultSet����
            if(!rs.next())
                return false;
            else
                account.setM_eAuthority(rs.getInt("authority"));//ȡ��authority
            rs.close();// �رս����
            stmt.close(); // �����ر�
            conn.close(); // ���ݿ�ر�

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
       /* if(temp.getAuthority() != account.getAuthority())
        {
            System.out.println("��Ȩ��");
            return false ;
        }*/
        return true;
    }
    //��ȡѧ����Ӧ������
    public void findStuAccount(StudentAccount studentAccount){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        ResultSet rs = null; // �����ѯ���
        //��ȡ��Ӧ�γ�
        String sql1 = "SELECT id_account,id_course FROM account_course where id_account = "+studentAccount.getID();
        //��ȡ��Ӧ�
        String sql2 = "SELECT id_account,id_activity FROM account_activity where id_account = "+studentAccount.getID();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql1);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                Course course = new Course();
                course.setM_iNum(rs.getInt("id_course"));
                CourseDatabase courseDatabase = new CourseDatabase();
                courseDatabase.find(course);//���Ҫ��course����
                studentAccount.getCourse().add(course);//��ArrayList�����
            }
            rs.close();// �رտγ̽����
            rs = stmt.executeQuery(sql2);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                Activity activity = new Activity();
                activity.setM_iNum(rs.getInt("id_activity"));
                ActivityDatabase activityDatabase = new ActivityDatabase();
                activityDatabase.find(activity);//���Ҫ��course����
                studentAccount.getActivity().add(activity);//��ArrayList�����
            }
            rs.close();// �رջ�����
            stmt.close(); // �����ر�
            conn.close(); // ���ݿ�ر�
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            LogFile.error("AccountDatabase","���ݿ��ȡ����");
        }
    }
    //��ȡ��ʦ��Ӧ������
    public void findTeaAccount(TeacherAccount teacherAccount){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        ResultSet rs = null; // �����ѯ���
        //��ȡ��Ӧ�γ�
        String sql1 = "SELECT id_account,id_course FROM account_course where id_account = "+teacherAccount.getID();
        //��ȡ��Ӧ�
        String sql2 = "SELECT id_account,id_activity FROM account_activity where id_account = "+teacherAccount.getID();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql1);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                Course course = new Course();
                course.setM_iNum(rs.getInt("id_account"));
                CourseDatabase courseDatabase = new CourseDatabase();
                courseDatabase.find(course);//���Ҫ��course����
                teacherAccount.getCourse().add(course);//��ArrayList�����
            }
            rs.close();// �رտγ̽����
            rs = stmt.executeQuery(sql2);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                Activity activity = new Activity();
                activity.setM_iNum(rs.getInt("id_account"));
                ActivityDatabase activityDatabase = new ActivityDatabase();
                activityDatabase.find(activity);//���Ҫ��course����
                teacherAccount.getActivity().add(activity);//��ArrayList�����
            }
            rs.close();// �رս����
            stmt.close(); // �����ر�
            conn.close(); // ���ݿ�ر�
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            LogFile.error("AccountDatabase","���ݿ��ȡ����");
        }
    }
    //����Ա��ȡȫ������
    public void findManAccount(ManagerAccount managerAccount){
        ArrayList<Course> course = new ArrayList<>();
        ArrayList<Activity> activity = new ArrayList<>();
        CourseDatabase courseDatabase = new CourseDatabase();
        ActivityDatabase activityDatabase = new ActivityDatabase();
        courseDatabase.find(course);//���Ҫ��course����
        activityDatabase.find(activity);
        managerAccount.setCourse(course);
        managerAccount.setActivity(activity);

    }
    //��������������
    public static void main(String[] args){
         AccountDatabase accountDatabase = new AccountDatabase();
         ManagerAccount account = new ManagerAccount("1","123456");
         // account = new StudentAccount("1","123456");
         accountDatabase.findManAccount(account);
        // accountDatabase.findStuAccount(account);
         int s = 0;
         s = 1+2;
    }
}
