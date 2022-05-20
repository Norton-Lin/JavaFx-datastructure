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
     * @param course
     * @param homeworks
     * @param studentAccount
     */
    public void find(Course course, ArrayList<Homework> homeworks, StudentAccount studentAccount) {
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        ResultSet rs = null; // 保存查询结果
        String sql = "SELECT * FROM course_homework where course_id = " + course.getM_iNum();
        String sql1 = "SELECT * FROM account_homework where account_id = " + studentAccount.getID();
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

}
