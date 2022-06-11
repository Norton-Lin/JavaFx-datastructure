package com.example.demo1.Code.Mysql;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.Homework;
import com.example.demo1.Code.entity.account.StudentAccount;

import java.sql.*;
import java.util.ArrayList;

public class HomeWorkDatabase {
    // 定义MySQL的数据库驱动程序
    public static final String m_sDriver = "com.mysql.cj.jdbc.Driver";
    // 定义MySQL数据库的连接地址
    public static final String m_sUrl = "jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL数据库的连接用户名
    public static final String m_sUser = "root";
    // MySQL数据库的连接密码
    public static final String m_sPassword = "20021213";

    /**
     * 查询课程名下的作业
     *
     * @param course    课程
     * @param homeworks 作业
     */
    public void find(Course course, ArrayList<Homework> homeworks) {
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        ResultSet rs = null; // 保存查询结果
        String sql = "SELECT * FROM course_homework where course_id = " + course.getM_iNum();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                Homework h = new Homework();
                h.setM_iName(rs.getString("work_name"));
                h.setM_iPath(rs.getString("path"));
                homeworks.add(h);
            }
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("HomeWorkDatabase", "数据库读取错误");
        }
    }

    /**
     * 查询学生课程名下的作业
     *
     * @param course         课程
     * @param homeworks      作业
     * @param studentAccount 学生账号
     */
    public void find(Course course, ArrayList<Homework> homeworks, StudentAccount studentAccount) {
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        ResultSet rs = null; // 保存查询结果
        String sql = "SELECT * FROM course_homework where course_id = " + course.getM_iNum();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                Homework h = new Homework();
                h.setM_iName(rs.getString("work_name"));
                h.setM_iPath(rs.getString("path"));
                homeworks.add(h);
            }
            rs.close();// 关闭结果集
            if (homeworks.size() > 0)//作业列表不为空
                for (Homework homework : homeworks) {
                    sql = "SELECT * FROM account_homework where account_id = '" + studentAccount.getID()
                            + "'and course_id ='" + course.getM_iNum() + "'and work_name='" + homework.getM_iName() + "'";
                    rs = stmt.executeQuery(sql);
                    while (rs.next())
                        homework.setM_iTag(rs.getInt("tag"));
                    rs.close();
                }
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("HomeWorkDatabase", "数据库读取错误");
        }
    }

    /**
     * 修改指定作业信息
     *
     * @param course   课程
     * @param homework 作业
     */
    public void update(Course course, Homework homework) {
        String sql = "Update course_homework set path='" + homework.getM_iPath()
                + "'where course_id = '" + course.getM_iNum() + "'and work_name ='"
                + homework.getM_iName() + "'";
        executeSql(sql);
    }

    /**
     * 修改指定作业信息
     *
     * @param course         课程
     * @param homework       作业
     * @param studentAccount 学生账号
     */
    public void update(Course course, Homework homework, StudentAccount studentAccount) {
        String sql = "Update account_homework set tag='" + homework.getM_iTag()
                + "'where course_id = '" + course.getM_iNum() + "'and work_name ='"
                + homework.getM_iName() + "'and account_id ='" + studentAccount.getID() + "'";
        executeSql(sql);
    }

    /**
     * 删除指定作业信息
     *
     * @param course   课程
     * @param homework 作业
     */
    public void delete(Course course, Homework homework) {
        String sql = "DELETE FROM course_homework WHERE work_name ='" + homework.getM_iName()
                + "'and course_id=" + course.getM_iNum();
        String sql1 = "DELETE FROM account_homework WHERE work_name ='" + homework.getM_iName()
                + "'and course_id=" + course.getM_iNum();
        executeSql(sql);
        executeSql(sql1);
    }

    /**
     * 插入课程作业信息
     * @param course 课程
     * @param homework 作业
     */
    public void insert(Course course, Homework homework) {
        String sql = "INSERT INTO course_homework(course_id,work_name,path)"
                + "VALUES('" + course.getM_iNum() + "','" + homework.getM_iName()
                + "','" + homework.getM_iPath() + "')";
        executeSql(sql);
    }

    /**
     * sql语句执行
     * @param sql sql语句
     */
    public void executeSql(String sql) {
        Connection conn;
        Statement stmt;
        try {
            Class.forName(m_sDriver); // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("HomeworkDatabase", "数据库读取错误");
        }
    }

    /**
     * 插入用户课程作业信息
     * @param course 课程
     * @param homework 作业
     * @param studentAccount 学生账户
     */
    public void insert(Course course, Homework homework, StudentAccount studentAccount) {
        String sql = "INSERT INTO account_homework(account_id,course_id,work_name,tag)"
                + "VALUES('" + studentAccount.getID() + "','" + course.getM_iNum() + "','"
                + homework.getM_iName() + "','" + homework.getM_iTag() + "')";
        executeSql(sql);
    }
}
