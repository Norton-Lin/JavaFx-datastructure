package com.example.demo1.Code.Mysql;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.entity.Line;

import java.sql.*;
import java.util.ArrayList;

public class MapDatabase {
    // 定义MySQL的数据库驱动程序
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // 定义MySQL数据库的连接地址
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL数据库的连接用户名
    public static final String m_sUser ="root";
    // MySQL数据库的连接密码
    public static final String m_sPassword ="20021213";

    /**
     *向数据库内插入新数据
     * @param line 待插入路径
     */
    public void insert(Line line){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql = "INSERT INTO map(start_id, end_id, name, crowded1, crowded2, crowded3, degree,len)"
                + " VALUES ('" + line.getM_i_stp() + "','" + line.getM_i_enp() + "','" +line.getM_s_name()
                + "','" +line.getM_d_congestion()[0]+ "','" + line.getM_d_congestion()[1]
                + "','" + line.getM_d_congestion()[2]+ "','" +line.getDegree()
                + "','" +line.getM_i_length()+ "')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("MapDatabase","数据库读取错误");
        }
    }

    /**
     * 更新数据库内已有数据的内容
     * @param line 更新路径
     */
    public void update(Line line){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql = "UPDATE map set start_id ='"+line.getM_i_stp()+"',end_id = '"+ line.getM_i_enp()
                +"',crowded1 = '"+line.getM_d_congestion()[0]+"',crowded2 = '"+line.getM_d_congestion()[1]
                +"',crowded3 = '"+line.getM_d_congestion()[2]+"',len = '" + line.getM_i_length()
                + "',degree = '"+line.getDegree()+"'WHERE name =" + line.getM_s_name();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("MapDatabase","数据库读取错误");
        }
    }

    /**
     * 删除数据库内的指定路径
     * @param line 待删除路径
     */
    public void delete(Line line){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql = "DELETE FROM map WHERE name ="+line.getM_s_name();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("MapDatabase","数据库读取错误");
        }
    }

    /**
     * 读取所有路径，创建地图
     * @param lines 路径合集
     */
    public void find(ArrayList<Line> lines){
        ResultSet rs = null; // 保存查询结果
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        String sql = "SELECT * FROM map ";
        Line line = new Line();
        double[] degree=new double[3];
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 执行数据库更新操作
            while(rs.next()){
                line.setM_i_stp(rs.getInt("start_id"));
                line.setM_i_enp(rs.getInt("end_id"));
                line.setM_s_name(rs.getString("name"));
                degree[0] = rs.getDouble("crowded1");
                degree[1] = rs.getDouble("crowded2");
                degree[2] = rs.getDouble("crowded3");
                line.setM_d_congestion(degree);
                line.setM_i_length(rs.getInt("len"));
                lines.add(line);
            }
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("MapDatabase","数据库读取错误");
        }
    }
}
