package com.example.demo1.Code.Mysql;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.entity.Construction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ConstructionDatabase {
    // ����MySQL�����ݿ���������
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // ����MySQL���ݿ�����ӵ�ַ
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL���ݿ�������û���
    public static final String m_sUser ="root";
    // MySQL���ݿ����������
    public static final String m_sPassword ="20021213";
    public void insert(Construction construction){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
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
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","���ݿ��ȡ����");
        }
    }
    public void update(Construction construction){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        ArrayList<ArrayList<Integer>> room = construction.get_con_room();
        int floor = room.size();
        StringBuilder numberList = new StringBuilder();
        for (ArrayList<Integer> integers : room) {
            for (Integer integer : integers) {
                numberList.append(integer).append(' ');
            }
        }
        // ƴ�ճ�һ��������SQL���
        String sql = "UPDATE construction SET name='" + construction.get_con_name() + "',floor='"
                + floor+"',roomnum='" + numberList +"', campus='"+construction.getCampus()
                + "'WHERE id=" + construction.get_con_number();
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","���ݿ��ȡ����");
        }
    }
    public void delete(Construction construction){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        // ƴ�ճ�һ��������SQL���
        String sql = "DELETE FROM construction WHERE id=" + construction.get_con_number();
        try {
            Class.forName(m_sDriver) ; // ������������
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","���ݿ��ȡ����");
        }
    }
    public void find(Construction construction) {
        ResultSet rs = null; // �����ѯ���
        String sql = "SELECT * FROM construction WHERE id = "+ construction.get_con_number();
        Connection conn = null; // ���ݿ�����
        Statement stmt = null; // ���ݿ����
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                construction.set_con_name(rs.getString("name"));
                construction.setCampus(rs.getInt("campus"));
                int floorNum = rs.getInt("floor");//��ȡ¥����
                ArrayList<ArrayList<Integer>> room = new ArrayList<>();//��ŷ����ŵĶ�άarraylist

                String numberList = rs.getString("roomnum");//��ȡ�������ַ���
                String[] str = numberList.split(" ");//�Կո�Ϊ��ָ��ַ���
                int j = 0;
                for (int i = 0; i < floorNum; i++)//��¥����¥����
                {
                    ArrayList<Integer> floor = new ArrayList<>();//�洢ÿ��¥����ı��
                    while (j < str.length)//���ÿ��¥�ķ�����
                    {
                        int num = Integer.parseInt(str[j]);
                        if (num > (i + 2) * 100)//������һ��
                            break;
                        j++;
                        floor.add(num);
                    }
                    room.add(floor);
                }
                construction.set_con_room(room);
            }
                rs.close();// �رս����
                stmt.close(); // �����ر�
                conn.close(); // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","���ݿ��ȡ����");
        }
    }
    public void find(ArrayList<Construction> constructions) {
        ResultSet rs = null; // �����ѯ���
        String sql = "SELECT * FROM construction ";

        Connection conn = null; // ���ݿ�����
        Statement stmt = null; // ���ݿ����
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement();// ʵ����Statement����
            rs = stmt.executeQuery(sql);// ʵ����ResultSet����
            while (rs.next()) { // ָ�������ƶ�
                Construction construction = new Construction();
                construction.set_con_number(rs.getInt("id"));
                construction.set_con_name(rs.getString("name"));
                construction.setCampus(rs.getInt("campus"));
                int floorNum = rs.getInt("floor");//��ȡ¥����
                ArrayList<ArrayList<Integer>> room = new ArrayList<>();//��ŷ����ŵĶ�άarraylist

                String numberList = rs.getString("roomnum");//��ȡ�������ַ���
                String[] str = numberList.split(" ");//�Կո�Ϊ��ָ��ַ���
                int j = 0;
                for (int i = 0; i < floorNum; i++)//��¥����¥����
                {
                    ArrayList<Integer> floor = new ArrayList<>();//�洢ÿ��¥����ı��
                    while (j < str.length)//���ÿ��¥�ķ�����
                    {
                        int num = Integer.parseInt(str[j]);
                        if (num > (i + 2) * 100)//������һ��
                            break;
                        j++;
                        floor.add(num);
                    }
                    room.add(floor);
                }
                construction.set_con_room(room);
                constructions.add(construction);
            }
            rs.close();// �رս����
            stmt.close(); // �����ر�
            conn.close(); // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("ConstructionDatabase","���ݿ��ȡ����");
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
