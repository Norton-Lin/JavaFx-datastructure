package com.example.demo1.Code.Mysql;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.account.Account;

import java.sql.*;
import java.util.ArrayList;

public class ActivityDatabase {
    // 定义MySQL的数据库驱动程序
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // 定义MySQL数据库的连接地址
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL数据库的连接用户名
    public static final String m_sUser ="root";
    // MySQL数据库的连接密码
    public static final String m_sPassword ="20021213";

    /**
     * 向数据库插入新的活动内容
     * @param activity 新活动
     */
    public void insert(Activity activity){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
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
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.executeUpdate(sql1);
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }
    }
    /**
     * 向用户关联表中插入数据，实现用户已选课程的添加
     * @param activity 待添加课程
     * @param account 待添加课程的对应账号
     */
    public void insert(Activity activity, Account account){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql = "INSERT INTO account_activity(id_account, id_activity)"
                + " VALUES ('" + account.getID() + "','" + activity.getM_iNum() + "')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭f
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }

    }
    /**
     * 修改数据库中的活动内容
     * @param activity 待修改活动
     */
    public void update(Activity activity){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        // 拼凑出一个完整的SQL语句
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
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.executeUpdate(sql1);
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }
    }
    /**
     * 删除数据库中的指定活动
     * @param activity 待删除活动
     */
    public void delete(Activity activity){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql1 = "DELETE FROM account WHERE id=" + activity.getM_iNum();
        String sql2 = "DELETE FROM account_activity WHERE id_activity = "+activity.getM_iNum();//删除用户_活动管理表中的对应数据
        String sql3 = "DELETE FROM activity_construction WHERE activity_id="+activity.getM_iNum();//删除活动_建筑关联表中的对应数据
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql1);// 执行数据库更新操作
            stmt.executeUpdate(sql2);// 执行数据库更新操作
            stmt.executeUpdate(sql3);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }
    }
    /**
     * 从用户课程关联表中删除数据，实现用户已选课程的删除
     * @param activity 待删除课程
     * @param account 待操作账号
     */
    public void delete(Activity activity,Account account){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        // 拼凑出一个完整的SQL语句
        String sql = "DELETE FROM account_activity WHERE id_account = '"+ account.getID()+"'and id_activity=" + activity.getM_iNum();
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }
    }
    /**
     * 从数据库内读取活动
     * @param activity 待读取活动内容
     */
    public void find(Activity activity) {
        ResultSet rs = null; // 保存查询结果
        String sql = "SELECT id, name, startmonth,startdate,starthour, startmin, endhour," +
                " endmin, property, num, maxnum FROM activity WHERE id =" + activity.getM_iNum();
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
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
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }
    }
    public void find(ArrayList<Activity> activity) {
        ResultSet rs = null; // 保存查询结果
        String sql = "SELECT id, name,startmonth,startdate ,starthour, startmin, endhour," +
                " endmin, property, num, maxnum FROM activity" ;
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        ConstructionDatabase constructionDatabase = new ConstructionDatabase();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
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
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }
        for(int i =0;i<activity.size();i++) {
            Activity activity1 = activity.get(i);
            constructionDatabase.findByActivity(activity1);
            constructionDatabase.findByActivity(activity1);
            activity.set(i,activity1);
        }

    }
}
