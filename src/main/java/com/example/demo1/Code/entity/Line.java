
package com.example.demo1.Code.entity;

/**
 * Line类是对道路的基本单位“边”的定义，包含属性及基本操作
 */
public class Line {

    private int m_i_stp;//起始点
    private int m_i_enp;//终点
    private double[] m_d_congestion=new double[3];//拥挤度
    private int m_i_length;//长度
    private String m_s_name;//名字
    private int degree;//道路等级



    public Line() {
        m_i_enp = 0;
        m_i_stp = 0;
        m_d_congestion = new double[]{0, 0, 0};
        m_i_length = 0;
        m_s_name = null;
    }

    /**
     * 查询起点
     *
     * @return 起点
     */
    public int getM_i_stp() {
        return this.m_i_stp;
    }

    /**
     * 查询终点
     *
     * @return 终点
     */
    public int getM_i_enp() {
        return this.m_i_enp;
    }

    /**
     * 查询拥挤度
     *
     * @return 拥挤度
     */
    public double[] getM_d_congestion() {
        return this.m_d_congestion;
    }

    /**
     * 查询边长
     *
     * @return 边长
     */
    public int getM_i_length() {
        return this.m_i_length;
    }

    /**
     * 查询路的名字
     *
     * @return name
     */
    public String getM_s_name() {
        return m_s_name;
    }

    /**
     * 编辑起点
     *
     * @param m_i_stp 起点
     */
    public void setM_i_stp(int m_i_stp) {
        this.m_i_stp = m_i_stp;
    }

    /**
     * 编辑终点
     *
     * @param m_i_enp 终点
     */
    public void setM_i_enp(int m_i_enp) {
        this.m_i_enp = m_i_enp;
    }

    /**
     * 编辑拥挤度
     *
     * @param m_d_congestion 拥挤度
     */
    public void setM_d_congestion(double[] m_d_congestion) {
        this.m_d_congestion = m_d_congestion;
    }

    /**
     * 编辑边长
     *
     * @param m_i_length 长度
     */
    public void setM_i_length(int m_i_length) {
        this.m_i_length = m_i_length;
    }

    /**
     * 编辑名字
     *
     * @param m_s_name 名字
     */
    public void setM_s_name(String m_s_name) {
        this.m_s_name = m_s_name;
    }
    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

}
