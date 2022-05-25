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
    // 定义MySQL的数据库驱动程序
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // 定义MySQL数据库的连接地址
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL数据库的连接用户名
    public static final String m_sUser ="root";
    // MySQL数据库的连接密码
    public static final String m_sPassword ="20021213";
    //向数据库中插入数据
    public void insert(Account account){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
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
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("AccountDatabase","数据库读取错误");
        }
    }
    //更新数据库中的数据
    public void update(Account account){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String id = account.getID();
        String password = account.getPassword();
        Authority authority = account.getAuthority();
        // 拼凑出一个完整的SQL语句
        String sql = "UPDATE account SET password='" + password + "',authority='"
                + authority + "'WHERE id=" + id ;
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("AccountDatabase","数据库读取错误");
        }
    }
    //删除数据库中的数据
    public void delete(Account account){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String id = account.getID(); // id
// 拼凑出一个完整的SQL语句
        String sql = "DELETE FROM account WHERE id=" + id;
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("AccountDatabase","数据库读取错误");
        }
    }
    //搜寻数据库中的数据
    public void find(Account account) {
        ResultSet rs = null; // 保存查询结果
        String sql = "SELECT id,password,authority FROM account";
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                account.setM_sID(rs.getString("id")); // 取得id内容
                account.setPassword(rs.getString("password"));// 取得name内容
                account.setM_eAuthority(rs.getInt("authority"));//取得authority
            }
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("AccountDatabase","数据库读取错误");
        }
    }
    //根据账号密码搜寻获取账号权限
    public boolean findByPassword(Account account){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        ResultSet rs = null; // 保存查询结果
        Account temp = new Account();
        String sql1 = "SELECT id,password,authority FROM account where id = '"+account.getID()+"'and password ="+account.getPassword();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql1);// 实例化ResultSet对象
            if(!rs.next())
                return false;
            else
                account.setM_eAuthority(rs.getInt("authority"));//取得authority
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
       /* if(temp.getAuthority() != account.getAuthority())
        {
            System.out.println("无权限");
            return false ;
        }*/
        return true;
    }
    //读取学生对应的数据
    public void findStuAccount(StudentAccount studentAccount){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        ResultSet rs = null; // 保存查询结果
        //读取对应课程
        String sql1 = "SELECT id_account,id_course FROM account_course where id_account = '"+studentAccount.getID()+"'";
        //读取对应活动
        String sql2 = "SELECT id_account,id_activity FROM account_activity where id_account = '"+studentAccount.getID()+"'";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql1);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                Course course = new Course();
                course.setM_iNum(rs.getInt("id_course"));
                CourseDatabase courseDatabase = new CourseDatabase();
                courseDatabase.find(course);//这边要读course数据
                studentAccount.getCourse().add(course);//向ArrayList中添加
            }
            rs.close();// 关闭课程结果集
            rs = stmt.executeQuery(sql2);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                Activity activity = new Activity();
                activity.setM_iNum(rs.getInt("id_activity"));
                ActivityDatabase activityDatabase = new ActivityDatabase();
                activityDatabase.find(activity);//这边要读course数据
                studentAccount.getActivity().add(activity);//向ArrayList中添加
            }
            rs.close();// 关闭活动结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            LogFile.error("AccountDatabase","数据库读取错误");
        }
    }
    //读取教师对应的数据
    public void findTeaAccount(TeacherAccount teacherAccount){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        ResultSet rs = null; // 保存查询结果
        //读取对应课程
        String sql1 = "SELECT id_account,id_course FROM account_course where id_account = "+teacherAccount.getID();
        //读取对应活动
        String sql2 = "SELECT id_account,id_activity FROM account_activity where id_account = "+teacherAccount.getID();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql1);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                Course course = new Course();
                course.setM_iNum(rs.getInt("id_account"));
                CourseDatabase courseDatabase = new CourseDatabase();
                courseDatabase.find(course);//这边要读course数据
                teacherAccount.getCourse().add(course);//向ArrayList中添加
            }
            rs.close();// 关闭课程结果集
            rs = stmt.executeQuery(sql2);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                Activity activity = new Activity();
                activity.setM_iNum(rs.getInt("id_account"));
                ActivityDatabase activityDatabase = new ActivityDatabase();
                activityDatabase.find(activity);//这边要读course数据
                teacherAccount.getActivity().add(activity);//向ArrayList中添加
            }
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            LogFile.error("AccountDatabase","数据库读取错误");
        }
    }
    //管理员读取全部数据
    public void findManAccount(ManagerAccount managerAccount){
        ArrayList<Course> course = new ArrayList<>();
        ArrayList<Activity> activity = new ArrayList<>();
        CourseDatabase courseDatabase = new CourseDatabase();
        ActivityDatabase activityDatabase = new ActivityDatabase();
        courseDatabase.find(course);//这边要读course数据
        activityDatabase.find(activity);
        managerAccount.setCourse(course);
        managerAccount.setActivity(activity);

    }
    //主函数，测试用
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
