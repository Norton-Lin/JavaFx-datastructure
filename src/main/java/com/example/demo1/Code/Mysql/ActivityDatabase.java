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
     * @param ID 对应账号Id
     */
    public void insert(Activity activity,String ID,int choice){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql;
        //个人活动
        if(choice==0){
            sql = "INSERT INTO activity(id,account_id,name,startmonth,startdate,starthour,startmin" +
                    ",endhour,endmin,property,num,maxnum,floor,room,construction)"
                    + " VALUES ('" + activity.getM_iNum() + "','" + ID +"','" +activity.getM_sName() + "','" +
                    activity.getM_tTime().getStartMonth()+ "','" +activity.getM_tTime().getStartDate()+ "','" +
                    activity.getM_tTime().getStartHour()+ "','" +activity.getM_tTime().getStartMinute()+
                    "','" + activity.getM_tTime().getEndHour()+ "','" + activity.getM_tTime().getEndMinute()
                    + "','" + choice+ "','" +activity.getM_iPle()+ "','"
                    +activity.getM_iMaxPle()+ "','" +activity.getM_iFloor() +"','"+activity.getM_iRoom()+"','"
                    +activity.getM_sConstruction().get_con_name()+"')";
        }
        //班级活动
        else{
            sql = "INSERT INTO class_activity(id,class_id,name,startmonth,startdate,starthour,startmin" +
                ",endhour,endmin,property,num,maxnum,floor,room,construction)"
                + " VALUES ('" + activity.getM_iNum() + "','" + ID +"','" +activity.getM_sName() + "','" +
                activity.getM_tTime().getStartMonth()+ "','" +activity.getM_tTime().getStartDate()+ "','" +
                activity.getM_tTime().getStartHour()+ "','" +activity.getM_tTime().getStartMinute()+ 
                "','" + activity.getM_tTime().getEndHour()+ "','" + activity.getM_tTime().getEndMinute()
                    + "','" + choice+ "','" +activity.getM_iPle()+ "','"
                +activity.getM_iMaxPle()+ "','" +activity.getM_iFloor() +"','"+activity.getM_iRoom()+"','"
                +activity.getM_sConstruction().get_con_name()+"')";
        }
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
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
    public void update(Activity activity,String ID){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        // 拼凑出一个完整的SQL语句
        String sql = "UPDATE activity SET startmonth='" + activity.getM_tTime().getStartMonth()
                + "',startdate='"+activity.getM_tTime().getStartDate()
                + "',starthour='" + activity.getM_tTime().getStartHour()
                +"',startmin='"+activity.getM_tTime().getStartMinute()
                + "',endhour='" + activity.getM_tTime().getEndHour() + "',endmin='" + 
                activity.getM_tTime().getEndMinute() + "',property='" + activity.getM_eProperty() 
                +"',num='" + activity.getM_iPle() + "',maxnum='" + activity.getM_iMaxPle()
                +"',floor='" + activity.getM_iFloor()+"',room='" +activity.getM_iRoom()
                +"',construction'"+activity.getM_sConstruction().get_con_name()
                + "'WHERE name='" + activity.getM_sName() +"'and account_id ='"+ID+"'" ;
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
     * 删除数据库中的指定活动
     * @param activity 待删除活动
     */
    public void delete(Activity activity,String ID){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql1 = "DELETE FROM activity WHERE name='" + activity.getM_sName()
                +"'and account_id ='"+ID+"'";
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql1);// 执行数据库更新操作
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
    public void find(Activity activity,String ID) {
        ResultSet rs = null; // 保存查询结果
        String sql = "SELECT id, name, startmonth,startdate,starthour, startmin, endhour," +
                " endmin, property, num, maxnum,floor,room,construction " +
                "FROM activity WHERE name='"+activity.getM_sName()+"'and account_id ='" + ID+"'";
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        ConstructionDatabase constructionDatabase = new ConstructionDatabase();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                activity.setM_iNum(rs.getInt("id"));
                activity.getM_tTime().setStartMonth(rs.getInt("startmonth"));
                activity.getM_tTime().setStartDate(rs.getInt("startdate"));
                activity.getM_tTime().setStartHour(rs.getInt("starthour"));
                activity.getM_tTime().setStartMinute(rs.getInt("startmin"));
                activity.getM_tTime().setEndHour(rs.getInt("endhour"));
                activity.getM_tTime().setEndMinute(rs.getInt("endmin"));
                activity.setM_eProperty(rs.getInt("property"));
                activity.setM_iPle(rs.getInt("num"));
                activity.setM_iMaxPle(rs.getInt("maxnum"));
                activity.setM_iFloor(rs.getInt("floor"));
                activity.setM_iRoom(rs.getInt("room"));
                activity.getM_sConstruction().set_con_name(rs.getString("construction"));
            }
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }
    }
    public void find(ArrayList<Activity> activity,String ID,int choice) {
        ResultSet rs = null; // 保存查询结果
        String sql;
        if(choice==1){
            sql = "SELECT id, name, startmonth,startdate,starthour, startmin, endhour," +
                " endmin, property, num, maxnum,floor,room,construction " +
                "FROM activity WHERE account_id ='" + ID+"'";
        }
        else{
            sql = "SELECT id, name, startmonth,startdate,starthour, startmin, endhour," +
                    " endmin, property, num, maxnum,floor,room,construction " +
                    "FROM class_activity WHERE class_id ='" + ID+"'";
        }
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
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
                a.setM_iFloor(rs.getInt("floor"));
                a.setM_iRoom(rs.getInt("room"));
                a.getM_sConstruction().set_con_name(rs.getString("construction"));
                activity.add(a);
            }
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }
    }
    public void find(ArrayList<Activity> activities){
        ResultSet rs = null; // 保存查询结果
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        String sql="SELECT * FROM class_activity";
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
                a.setM_iFloor(rs.getInt("floor"));
                a.setM_iRoom(rs.getInt("room"));
                a.getM_sConstruction().set_con_name(rs.getString("construction"));
                activities.add(a);
            }
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ActivityDatabase","数据库读取错误");
        }
    }

}
