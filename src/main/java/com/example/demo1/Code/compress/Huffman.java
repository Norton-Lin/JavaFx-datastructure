package com.example.demo1.Code.compress;

/**
 * 哈夫曼树结点类
 */
public class Huffman {
    private Huffman m_cParent;//哈夫曼树双亲结点
    private Huffman m_cLeftChild;//哈夫曼树左子结点
    private Huffman m_cRightChild;//哈夫曼树右子结点
    private int m_iIndex;////该结点在全文件字符信息结构类中的位置
    private Character m_cElement;//哈夫曼树结点

    /**
     * 获取哈夫曼树双亲结点
     * @return 哈夫曼树双亲结点
     */
    public Huffman getM_cParent() {
        return m_cParent;
    }

    /**
     * 修改哈夫曼树双亲结点
     * @param m_cParent 新哈夫曼树双亲结点
     */
    public void setM_cParent(Huffman m_cParent) {
        this.m_cParent = m_cParent;
    }

    /**
     * 获取哈夫曼树左子结点
     * @return 哈夫曼树左子结点
     */
    public Huffman getM_cLeftChild() {
        return m_cLeftChild;
    }

    /**
     * 修改哈夫曼树左子结点
      * @param m_cLeftChild 新哈夫曼树左子结点
     */
    public void setM_cLeftChild(Huffman m_cLeftChild) {
        this.m_cLeftChild = m_cLeftChild;
    }

    /**
     * 获取哈夫曼树右子结点
     * @return 哈夫曼树右子结点
     */
    public Huffman getM_cRightChild() {
        return m_cRightChild;
    }

    /**
     * 修改哈夫曼树右子结点
     * @param m_cRightChild 新哈夫曼树右子结点
     */
    public void setM_cRightChild(Huffman m_cRightChild) {
        this.m_cRightChild = m_cRightChild;
    }

    /**
     * 获取该结点在全文件字符信息结构类中的位置
     * @return 该结点在全文件字符信息结构类中的位置
     */
    public int getM_iIndex() {
        return m_iIndex;
    }

    /**
     * 修改结点在全文件信息结构类中的位置
     * @param m_iIndex 新位置
     */
    public void setM_iIndex(int m_iIndex) {
        this.m_iIndex = m_iIndex;
    }
    /**
     * 获取当前结点信息
     * @return 当前结点信息
     */
    public Character getM_cElement() {
        return m_cElement;
    }

    /**
     * 修改该结点内部信息
     * @param m_cElement 新的结点内部信息
     */
    public void setM_cElement(Character m_cElement) {
        this.m_cElement = m_cElement;
    }

}
