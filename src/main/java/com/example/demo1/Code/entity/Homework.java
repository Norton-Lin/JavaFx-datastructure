package com.example.demo1.Code.entity;

public class Homework {
    private int m_iTag=0;//作业提交标记
    private String m_iPath;//作业目的提交路径
    private String m_iName;//作业名

    public int getM_iTag() {
        return m_iTag;
    }

    public void setM_iTag(int m_iTag) {
        this.m_iTag = m_iTag;
    }

    public String getM_iPath() {
        return m_iPath;
    }

    public void setM_iPath(String m_iPath) {
        this.m_iPath = m_iPath;
    }

    public String getM_iName() {
        return m_iName;
    }

    public void setM_iName(String m_iName) {
        this.m_iName = m_iName;
    }
}
