package com.example.demo1.Code.Mysql;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Util.Authority;
import com.example.demo1.Code.entity.Construction;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.account.Account;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;

public class CourseDatabase {
    // 定义MySQL的数据库驱动程序
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // 定义MySQL数据库的连接地址
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL数据库的连接用户名
    public static final String m_sUser ="root";
    // MySQL数据库的连接密码
    public static final String m_sPassword ="20021213";
    /**
     * 向数据库插入新的课程内容
     * @param course 新课程
     */
    public void insert(Course course){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql = "INSERT INTO course(id,name,weeks,starthour,startmin,endhour,endmin,property,num,maxnum," +
                "connectgroup,tstartmonth,tstartdate,tstarthour,tstartmin,tendhour,tendmin,data,teacher)"
                + " VALUES ('" + course.getM_iNum() + "','" + course.getM_sName() + "','" +course.getM_tTime().getWeek()
                + "','" + course.getM_tTime().getStartHour() + "','" +course.getM_tTime().getStartMinute()
                + "','" + course.getM_tTime().getEndHour() + "','" + course.getM_tTime().getEndMinute()
                + "','" + course.getM_eProperty(0)+ "','" +course.getM_iPle()
                + "','" + course.getM_iMaxPle()+ "','" +course.getM_sCurGroup()
                + "','" + course.getM_cExamTime().getStartMonth()+"','" +course.getM_cExamTime().getStartDate()
                + "','" + course.getM_cExamTime().getStartHour()+"','" +course.getM_cExamTime().getStartMinute()
                + "','" + course.getM_cExamTime().getEndHour() + "','" +course.getM_cExamTime().getEndMinute()
                + "','" + course.getM_iTotalClass()+ "','" + course.getM_iCurrentClass()
                + "','" +course.getM_sData()+ "','" +course.getM_sTeacher()+"')";
        String sql1 = "INSERT INTO course_construction(course_id,construction_id,floor,room,type)"+
                "VALUES ('"+course.getM_iNum()+ "','" +course.getM_sConstruction().get_con_number()
                + "','" +course.getM_iFloor()+ "','" +course.getM_iRoom()+"','"+0+"')";//普通教室数据插入
        String sql2 = "INSERT INTO course_construction(course_id,construction_id,floor,room,type)"+
                "VALUES ('"+course.getM_iNum()+ "','" +course.getM_cExamConstruction().get_con_number()
                + "','" +course.getM_iExamFloor()+ "','" +course.getM_iExamRoom()+"','"+1+"')";//考试教室数据插入
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","数据库读取错误");
        }
    }
    /**
     * 向用户关联表中插入数据，实现用户已选课程的添加
     * @param course 待添加课程
     * @param account 待添加课程的对应账号
     */
    public void insert(Course course,Account account){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql = "INSERT INTO account_course(id_account, id_course)"
                + " VALUES ('" + account.getID() + "','" + course.getM_iNum() + "')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭f
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","数据库读取错误");
        }

    }
    /**
     * 向用户关联表中插入数据，实现用户已选课程的添加
     * @param course 待添加课程
     * @param ID 用户Id
     */
    public void insert(Course course,String ID)
    {
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql = "INSERT INTO account_course(id_account, id_course)"
                + " VALUES ('" + ID + "','" + course.getM_iNum() + "')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭f
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","数据库读取错误");
        }
    }
    /**
     * 修改数据库中的课程内容
     * @param course 待修改课程
     */
    public void update(Course course){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        // 拼凑出一个完整的SQL语句
        String sql = "UPDATE course SET name='" + course.getM_sName() +"',weeks='"+
                course.getM_tTime().getWeek() + "',starthour='"
                + course.getM_tTime().getStartHour()+"',startmin='"+course.getM_tTime().getStartMinute()
                + "',endhour='" + course.getM_tTime().getEndHour() + "',endmin='" + course.getM_tTime().getEndMinute()
                + "',property='" + course.getM_eProperty(1) +"',num='" + course.getM_iPle() + "',maxnum='"
                + course.getM_iMaxPle() + "',connectgroup='" + course.getM_sCurGroup() + "',tstartmonth='"
                + course.getM_cExamTime().getStartMonth() +"',tstartdate='" +course.getM_cExamTime().getStartDate()
                + "',tstarthour='" + course.getM_cExamTime().getStartHour() +"',tstartmin='"
                + course.getM_cExamTime().getStartMinute() +"',tendhour='"+ course.getM_cExamTime().getEndHour()
                +"',tendmin='"+course.getM_cExamTime().getEndMinute()+"',data='"+course.getM_sData()
                + "',totalClass='" + course.getM_iTotalClass()+ "',currentClass='" + course.getM_iCurrentClass()
                +"',teacher='"+course.getM_sTeacher()
                + "'WHERE id=" + course.getM_iNum() ;
        String sql1 = "UPDATE course_construction SET construction_id='"+course.getM_sConstruction().get_con_number()
                +"',floor='"+course.getM_iFloor()+"',room='"+course.getM_iRoom()+"'WHERE course_id='"+course.getM_iNum()
                +"'and type ="+0;
        String sql2 = "UPDATE course_construction SET construction_id='"
                +course.getM_cExamConstruction().get_con_number()
                +"',floor='"+course.getM_iExamFloor()+"',room='"+course.getM_iExamRoom()
                +"'WHERE course_id='"+course.getM_iNum() +"'and type ="+1;
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","数据库读取错误");
        }
    }
    /**
     * 删除数据库中的指定课程
     * @param course 待删除课程
     */
    public void delete(Course course){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        // 拼凑出一个完整的SQL语句
        String sql1 = "DELETE FROM course WHERE id=" + course.getM_iNum();//删除课程表中的数据
        String sql2 = "DELETE FROM account_course WHERE id_course = "+course.getM_iNum();//删除用户_课程管理表中的对应数据
        String sql3 = "DELETE FROM course_construction WHERE course_id="+course.getM_iNum();//删除课程_建筑关联表中的对应数据
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql1);// 执行数据库更新操作
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","数据库读取错误");
        }


    }
    /**
     * 从用户课程关联表中删除数据，实现用户已选课程的删除
     * @param course 待删除课程
     * @param account 待操作账号
     */
    public void delete(Course course,Account account){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        // 拼凑出一个完整的SQL语句
        String sql = "DELETE FROM account_course WHERE id_account = '"+ account.getID()+"'and id_course=" + course.getM_iNum();
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","数据库读取错误");
        }
    }
    /**
     * 从数据库内读取课程
     * @param course 待读取课程内容
     */
    public void find(Course course) {
        ResultSet rs = null; // 保存查询结果
        ConstructionDatabase constructionDatabase = new ConstructionDatabase();
        String sql = "SELECT * FROM course WHERE id = " + course.getM_iNum();
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
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
                course.setM_iTotalClass(rs.getInt("totalClass"));
                course.setM_iCurrentClass(rs.getInt("currentClass"));
                course.setM_sData(rs.getString("data"));
                course.setM_sTeacher(rs.getString("teacher"));
            }
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","数据库读取错误");
        }
        constructionDatabase.findByCourse(course);
    }
    /**
     * 从数据库中读取课程集
     * @param course 课程集
     */
    public void find(ArrayList<Course> course){
        ResultSet rs = null; // 保存查询结果
        ConstructionDatabase constructionDatabase = new ConstructionDatabase();
        String sql = "SELECT * FROM course";
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
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
                c.setM_iTotalClass(rs.getInt("totalClass"));
                c.setM_iCurrentClass(rs.getInt("currentClass"));
                c.setM_sData(rs.getString("data"));
                c.setM_sTeacher(rs.getString("teacher"));
                course.add(c);
            }
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("CourseDatabase","数据库读取错误");
        }
        for(int i =0;i<course.size();i++) {
            Course course1 = course.get(i);
            constructionDatabase.findByCourse(course1);
            course.set(i,course1);
        }
    }
    public static void main(String[] args)
    {
        CourseDatabase courseDatabase = new CourseDatabase();
        Course course = new Course();
        course.setM_sName("计算机组成原理");
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
