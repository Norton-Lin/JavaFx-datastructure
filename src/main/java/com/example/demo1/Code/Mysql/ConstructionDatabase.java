package com.example.demo1.Code.Mysql;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.entity.Construction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ConstructionDatabase {
    // 定义MySQL的数据库驱动程序
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // 定义MySQL数据库的连接地址
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL数据库的连接用户名
    public static final String m_sUser ="root";
    // MySQL数据库的连接密码
    public static final String m_sPassword ="20021213";
    public void insert(Construction construction){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        ArrayList<ArrayList<Integer>> room = construction.get_con_room();
        int floor = room.size();
        StringBuilder numberList = new StringBuilder();
        for (ArrayList<Integer> integers : room) {
            for (Integer integer : integers) {
                numberList.append(integer).append(' ');
            }
        }
        String sql = "INSERT INTO construction(name,floor,roomnum,campus,id)"
                + " VALUES ('" + construction.get_con_name() +"','" + floor + "','" + numberList
                + "','"+construction.getCampus()+ "','"+construction.get_con_number()+"')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","数据库读取错误");
        }
    }
    public void update(Construction construction){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        ArrayList<ArrayList<Integer>> room = construction.get_con_room();
        int floor = room.size();
        StringBuilder numberList = new StringBuilder();
        for (ArrayList<Integer> integers : room) {
            for (Integer integer : integers) {
                numberList.append(integer).append(' ');
            }
        }
        // 拼凑出一个完整的SQL语句
        String sql = "UPDATE construction SET name='" + construction.get_con_name() + "',floor='"
                + floor+"',roomnum='" + numberList +"', campus='"+construction.getCampus()
                + "'WHERE id=" + construction.get_con_number();
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","数据库读取错误");
        }
    }
    public void delete(Construction construction){
        Connection conn = null ; // 数据库连接
        Statement stmt = null ; // 数据库操作
        // 拼凑出一个完整的SQL语句
        String sql = "DELETE FROM construction WHERE id=" + construction.get_con_number();
        try {
            Class.forName(m_sDriver) ; // 加载驱动程序
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// 实例化Statement对象
            stmt.executeUpdate(sql);// 执行数据库更新操作
            stmt.close() ; // 操作关闭
            conn.close() ; // 数据库关闭
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","数据库读取错误");
        }
    }
    public void find(Construction construction) {
        ResultSet rs = null; // 保存查询结果
        String sql = "SELECT * FROM construction WHERE id = "+ construction.get_con_number();
        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                construction.set_con_name(rs.getString("name"));
                construction.setCampus(rs.getInt("campus"));
                int floorNum = rs.getInt("floor");//获取楼层数
                ArrayList<ArrayList<Integer>> room = new ArrayList<>();//存放房间编号的二维arraylist

                String numberList = rs.getString("roomnum");//获取房间编号字符串
                String[] str = numberList.split(" ");//以空格为界分割字符串
                int j = 0;
                for (int i = 0; i < floorNum; i++)//分楼层存放楼层编号
                {
                    ArrayList<Integer> floor = new ArrayList<>();//存储每层楼房间的编号
                    while (j < str.length)//存放每层楼的房间编号
                    {
                        int num = Integer.parseInt(str[j]);
                        if (num > (i + 2) * 100)//到达下一层
                            break;
                        j++;
                        floor.add(num);
                    }
                    room.add(floor);
                }
                construction.set_con_room(room);
            }
                rs.close();// 关闭结果集
                stmt.close(); // 操作关闭
                conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","数据库读取错误");
        }
    }
    public void find(ArrayList<Construction> constructions) {
        ResultSet rs = null; // 保存查询结果
        String sql = "SELECT * FROM construction ";

        Connection conn = null; // 数据库连接
        Statement stmt = null; // 数据库操作
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// 实例化Statement对象
            rs = stmt.executeQuery(sql);// 实例化ResultSet对象
            while (rs.next()) { // 指针向下移动
                Construction construction = new Construction();
                construction.set_con_number(rs.getInt("id"));
                construction.set_con_name(rs.getString("name"));
                construction.setCampus(rs.getInt("campus"));
                int floorNum = rs.getInt("floor");//获取楼层数
                ArrayList<ArrayList<Integer>> room = new ArrayList<>();//存放房间编号的二维arraylist

                String numberList = rs.getString("roomnum");//获取房间编号字符串
                String[] str = numberList.split(" ");//以空格为界分割字符串
                int j = 0;
                for (int i = 0; i < floorNum; i++)//分楼层存放楼层编号
                {
                    ArrayList<Integer> floor = new ArrayList<>();//存储每层楼房间的编号
                    while (j < str.length)//存放每层楼的房间编号
                    {
                        int num = Integer.parseInt(str[j]);
                        if (num > (i + 2) * 100)//到达下一层
                            break;
                        j++;
                        floor.add(num);
                    }
                    room.add(floor);
                }
                construction.set_con_room(room);
                constructions.add(construction);
            }
            rs.close();// 关闭结果集
            stmt.close(); // 操作关闭
            conn.close(); // 数据库关闭
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","数据库读取错误");
        }
    }
    public static void main(String argc[]){

        ConstructionDatabase constructionDatabase = new ConstructionDatabase();
        Random r  = new Random();
        //construction.set_con_number(3);
       // constructionDatabase.delete(construction);
        Scanner in = new Scanner(System.in);
        for(int i =0;i<10;i++) {
            Construction construction = new Construction();
            construction.set_con_name(in.next());
            construction.set_con_number(i + 1);
            construction.setCampus(1);
            construction.set_con_room(6, new int[]{r.nextInt(10) + 8, r.nextInt(10) + 8, r.nextInt(10) + 8, r.nextInt(10) + 8, r.nextInt(10) + 8, r.nextInt(10) + 8});
            constructionDatabase.insert(construction);
        }
        System.out.println("6666");
    }
}
