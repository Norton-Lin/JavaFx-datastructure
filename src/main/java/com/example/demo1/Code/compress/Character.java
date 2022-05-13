package com.example.demo1.Code.compress;

import java.io.Serializable;

/**
 * 哈夫曼树字符信息类
 * 实现Serializable接口，以实现序列化，为了将类数据写入文件和读出
 */
public class Character implements Serializable {
    private char m_cCha ; //结点的字符
    private int m_iWeight; //字符权值
    private String m_sHuffmanCode; //结点的哈夫曼编码

    /**
     * 构造函数
     * @param c 结点的字符
     * @param i1 字符权值
     */
    public Character(char c, int i1) {
        m_cCha = c;
        m_iWeight = i1;
    }

    /**
     *  获取结点字符
     * @return 结点字符
     */
    public char getM_cCha() {
        return m_cCha;
    }

    /**
     * 修改结点字符
     * @param m_cCha 新结点字符
     */
    public void setM_cCha(char m_cCha) {
        this.m_cCha = m_cCha;
    }

    /**
     * 获取结点字符权值
     * @return 结点字符权值
     */
    public int getM_iWeight() {
        return m_iWeight;
    }

    /**
     * 修改结点字符权值
     * @param m_iWeight 新结点字符权值
     */
    public void setM_iWeight(int m_iWeight) {
        this.m_iWeight = m_iWeight;
    }

    /**
     * 结点字符权值＋1
     */
    public void addM_iWeight() {
        this.m_iWeight++;
    }

    /**
     * 获取结点字符哈夫曼编码
     * @return 结点字符哈夫曼编码
     */
    public String getM_sHuffmanCode() {
        return m_sHuffmanCode;
    }

    /**
     * 修改结点字符的哈夫曼编码
     * @param m_sHuffmanCode 新哈夫曼编码
     */
    public void setM_sHuffmanCode(String m_sHuffmanCode) {
        this.m_sHuffmanCode = m_sHuffmanCode;
    }
}
