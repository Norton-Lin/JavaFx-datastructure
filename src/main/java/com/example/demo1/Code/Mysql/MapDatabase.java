package com.example.demo1.Code.Mysql;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.entity.Line;

import java.sql.*;
import java.util.ArrayList;

public class MapDatabase {
    // ����MySQL�����ݿ���������
    public static final String m_sDriver ="com.mysql.cj.jdbc.Driver" ;
    // ����MySQL���ݿ�����ӵ�ַ
    public static final String m_sUrl ="jdbc:mysql://localhost:3306/informationmanagement";
    // MySQL���ݿ�������û���
    public static final String m_sUser ="root";
    // MySQL���ݿ����������
    public static final String m_sPassword ="20021213";

    /**
     *�����ݿ��ڲ���������
     * @param line ������·��
     */
    public void insert(Line line){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String sql = "INSERT INTO map(start_id, end_id, name, crowded1, crowded2, crowded3, degree,len)"
                + " VALUES ('" + line.getM_i_stp() + "','" + line.getM_i_enp() + "','" +line.getM_s_name()
                + "','" +line.getM_d_congestion()[0]+ "','" + line.getM_d_congestion()[1]
                + "','" + line.getM_d_congestion()[2]+ "','" +line.getDegree()
                + "','" +line.getM_i_length()+ "')";
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("MapDatabase","���ݿ��ȡ����");
        }
    }

    /**
     * �������ݿ����������ݵ�����
     * @param line ����·��
     */
    public void update(Line line){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String sql = "UPDATE map set start_id ='"+line.getM_i_stp()+"',end_id = '"+ line.getM_i_enp()
                +"',crowded1 = '"+line.getM_d_congestion()[0]+"',crowded2 = '"+line.getM_d_congestion()[1]
                +"',crowded3 = '"+line.getM_d_congestion()[2]+"',len = '" + line.getM_i_length()
                + "',degree = '"+line.getDegree()+"'WHERE name =" + line.getM_s_name();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("MapDatabase","���ݿ��ȡ����");
        }
    }

    /**
     * ɾ�����ݿ��ڵ�ָ��·��
     * @param line ��ɾ��·��
     */
    public void delete(Line line){
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String sql = "DELETE FROM map WHERE name ="+line.getM_s_name();
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            stmt.executeUpdate(sql);// ִ�����ݿ���²���
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("MapDatabase","���ݿ��ȡ����");
        }
    }

    /**
     * ��ȡ����·����������ͼ
     * @param lines ·���ϼ�
     */
    public void find(ArrayList<Line> lines){
        ResultSet rs = null; // �����ѯ���
        Connection conn = null ; // ���ݿ�����
        Statement stmt = null ; // ���ݿ����
        String sql = "SELECT * FROM map ";
        Line line = new Line();
        double[] degree=new double[3];
        try {
            conn = DriverManager.getConnection(m_sUrl, m_sUser, m_sPassword);
            stmt = conn.createStatement() ;// ʵ����Statement����
            rs = stmt.executeQuery(sql);// ִ�����ݿ���²���
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
            stmt.close() ; // �����ر�
            conn.close() ; // ���ݿ�ر�
        } catch (SQLException e) {
            e.printStackTrace();
            LogFile.error("MapDatabase","���ݿ��ȡ����");
        }
    }
}
