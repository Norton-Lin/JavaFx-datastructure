package com.example.demo1.Code.compress;

import java.io.*;
import java.util.ArrayList;

/**
 * 文件字符信息类
 * 实现Serializable接口，以实现序列化，为了将类数据写入文件和读出
 */
public class FileState implements Serializable{
    private int m_iCount; //有效字符数量
    private ArrayList<Character> m_Carray =new ArrayList<Character>();//字符信息数组

    /**
     * 获取有效字符数量
     * @return 有效字符数量
     */
    public int getM_iCount() {
        return m_iCount;
    }

    /**
     * 修改有效字符数量
     * @param m_iCount
     */
    public void setM_iCount(int m_iCount) {
        this.m_iCount = m_iCount;
    }

    /**
     * 获取字符信息数组
     * @return 字符信息数组
     */
    public ArrayList<Character> getM_Carray() {
        return m_Carray;
    }

    /**
     * 修改字符信息数组
     * @param m_Carray 新字符信息数组
     */
    public void setM_Carray(ArrayList<Character> m_Carray) {
        this.m_Carray = m_Carray;
    }






}
