package com.example.demo1.Code.compress;

import java.io.*;
import java.util.Scanner;

/**
 * 压缩类
 * 实现压缩与解压缩功能
 */
public class Compress {

    FileState m_cFileState = new FileState();//全文件信息结构类

    /**
     * 目的:树结点初始化
     *
     * @param element 结点内存储字符
     * @param index   结点在全文件字符信息结构体中的位置
     * @param left    左子树
     * @param right   右子树
     * @param parent  双亲
     * @return Huffman类型，初始化后的树结点
     */
    private Huffman initNode(Character element, int index, Huffman left, Huffman right, Huffman parent) {
        Huffman tmp = new Huffman(); //申请一个哈夫曼树结点空间
        tmp.setM_cElement(element);
        tmp.setM_cParent(parent);
        tmp.setM_cLeftChild(left);
        tmp.setM_cRightChild(right);
        tmp.setM_iIndex(index);
        return tmp;
    }

    /**
     * 创建哈夫曼树
     *
     * @return Huffman类型，哈夫曼树根结点
     */
    private Huffman creatHuffmanTree() {
        Huffman[] array = new Huffman[m_cFileState.getM_iCount()];//构建一个用来存储树节点的数组
        Huffman root = null;
        int j;//循环变量
        int min1, min2; //min1,min2为两个最小权值结点的对应编号
        for (int i = 0; i < m_cFileState.getM_iCount(); i++)//遍历所有叶子结点
            array[i] = initNode(m_cFileState.getM_Carray().get(i), i, null, null, null);//叶子结点初始化
        for (int i = 0; i < m_cFileState.getM_iCount() - 1; i++)//边界条件M_iCount-1，只剩一个结点时不存在两个最小权值结点
        {
            j = 0;
            while (array[j] == null)//寻找第一个非空结点
                j++;
            min1 = j;
            for (j = min1; j < m_cFileState.getM_iCount(); j++)
                if ((array[j] != null) && (array[j].getM_cElement().getM_iWeight() < array[min1].getM_cElement().getM_iWeight()))
                    min1 = j; //更新第一个最小权值结点编号
            j = 0;
            while (array[j] == null || j == min1) //寻找第二个非空结点
                j++;
            min2 = j;
            for (j = min2; j < m_cFileState.getM_iCount(); j++)
                if ((array[j] != null) && (j != min1) && (array[j].getM_cElement().getM_iWeight() < array[min2].getM_cElement().getM_iWeight()))
                    min2 = j; //更新最第二个小权值结点编号
            //创建两个结点的双亲结点的字符类对象
            Character element = new Character((char) 0, array[min1].getM_cElement().getM_iWeight() + array[min2].getM_cElement().getM_iWeight());
            root = initNode(element, -1, array[min1], array[min2], null);//建立两个最小权结点的双亲结点
            array[min1].setM_cParent(root);
            array[min2].setM_cParent(root);//对应双亲结点
            array[min1] = root;
            array[min2] = null;//更新该位置的权值，防止重复使用最小权结点
        }
        //array = null;
        return root; //返回根结点
    }

    /**
     * 从叶子到根逆序获取哈夫曼编码
     *
     * @param leaf 待获取哈夫曼编码的叶子结点
     * @return String.valueOf(code) 用于存储哈夫曼编码的字符串，由StringBuilder转为String
     */
    private String getHuffmanCode(Huffman leaf) {
        StringBuilder code = new StringBuilder();//创建一个可变String
        Huffman tmp = leaf;
        while (tmp.getM_cParent() != null)//左子树记为0，右子树记为1
        {
            if (tmp.getM_cParent().getM_cLeftChild() == tmp)
                code.append('0');
            else
                code.append('1');
            tmp = tmp.getM_cParent();//向上寻找跟结点
        }
        code.reverse();////逆置字符串，得到正确的哈夫曼编码
        return String.valueOf(code);
    }

    /**
     * 从哈夫曼树的根开始，获取所有叶子结点存储数据对应的哈夫曼编码
     *
     * @param root 哈夫曼树根结点
     */
    private void huffmanCode(Huffman root) {
        if (root != null) {
            if (root.getM_cLeftChild() != null)//存在左孩子
                huffmanCode(root.getM_cLeftChild());
            if (root.getM_cRightChild() != null)//存在右孩子
                huffmanCode(root.getM_cRightChild());
            if (root.getM_cRightChild() == null && root.getM_cLeftChild() == null) //该结点是叶子结点
            {
                // index = root.getM_iIndex();
                root.getM_cElement().setM_sHuffmanCode(getHuffmanCode(root));//得到当前叶子结点的哈夫曼编码
            }

        }
    }

    /**
     * 记录码表数据
     *
     * @param c 从文件内读取的一个字符
     */
    private void CreatFileState(char c) {
        boolean flag = false;//标记该字符是否在文件中出现过
        for (int i = 0; i < m_cFileState.getM_iCount(); i++) {
            if (c == m_cFileState.getM_Carray().get(i).getM_cCha()) {
                m_cFileState.getM_Carray().get(i).addM_iWeight(); //该字符权值+1
                flag = true; //该字符已在文件中出现过
            }
        }
        if (!flag) {//该字符未出现过
            m_cFileState.getM_Carray().add(new Character(c, 1));//将该字符加入字符信息数组
            m_cFileState.setM_iCount(m_cFileState.getM_iCount() + 1);//记录该字符的出现
        }
    }

    /**
     * 检索码表获取字符的对应哈夫曼编码
     *
     * @param c 从文件内读取的一个字符
     * @return 用于存储哈夫曼编码的字符串
     */
    private String searchDictionary(char c) {
        String huffmanCode = null;
        for (int i = 0; i < m_cFileState.getM_iCount(); i++)
            if (c == m_cFileState.getM_Carray().get(i).getM_cCha()) {//找到对应字符
                huffmanCode = m_cFileState.getM_Carray().get(i).getM_sHuffmanCode();//获取哈夫曼编码
                break;
            }
        return huffmanCode;
    }

    /**
     * 统计文件中各个字符数量
     *
     * @param filename 文件名
     */
    private void analyse(String filename) {
        File file = new File(filename);
        Scanner x = new Scanner(System.in);//用于获取输入指令
        if (!file.exists()) {//若文件不存在
            System.out.println("Cannot open the file ");
            System.out.println(filename);
            return;
        }
        FileInputStream in;//文字字节输入流，用于按字节读取信息
        try {
            in = new FileInputStream(file);
            for (int i = 0; i < file.length(); i++)
                CreatFileState((char) in.read());//根据从文件中读取的数据统计字符信息
            in.close();//关闭文件流
        } catch (IOException e) {//异常处理
            e.printStackTrace();
        }
        /*System.out.println("是否显示文件信息？\n");
        System.out.println("1.是\n2.按任意键退出\n");
        int i = x.nextInt();
        if (i == 1)//格式化输出文件各字符信息
            for (i = 0; i < m_cFileState.getM_iCount(); i++) {
                System.out.format("%s %c %s %d %s %d\n", "char is", m_cFileState.getM_Carray().get(i).getM_cCha(),
                        "ascii", (int) m_cFileState.getM_Carray().get(i).getM_cCha(), "number is", m_cFileState.getM_Carray().get(i).getM_iWeight());
            }*/
    }

    /**
     * 将源文件数据压缩后写入压缩文件
     *
     * @param filename  文件名
     * @param storePath 压缩文件存储地址：students目录
     */
    private void writeToFile(String filename, String storePath) {
        File fr = new File(filename);//待压缩文件
        File temp = new File(filename);
        String newFileName = storePath +"\\"+ temp.getName().replace("txt","si");//压缩文件名
        /*Scanner x = new Scanner(System.in);//读取输入指令
        int order ;*/
        if (!fr.exists()) {
            System.out.println("Cannot open the file ");
            System.out.println(filename);
            return;
        }
        /*System.out.println("请决定压缩文件输出路径：\\n");
        System.out.println("1.自定义输入\\n");
        System.out.println("2.默认路径，按除1外任意键将生成同名压缩文件于同目录下\\n");
        order = x.nextInt();
        if(order == 1) {
            System.out.println("请输入输出路径：\n");
            newFileName = x.nextLine();
        }
        else
            newFileName = filename.replace(".png",".si");*/
        File fw = new File(newFileName);//压缩文件
        if (!fr.exists()) {
            try {
                fw.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String codeFile = newFileName.replace(".si", ".code");//压缩编码文件名
        File fc = new File(codeFile);//压缩编码文件
        ObjectOutputStream out;//对象输出流，用于将对象写入文件
        try {
            out = new ObjectOutputStream(new FileOutputStream(fc));
            out.writeObject(m_cFileState);//将文件字符信息类写入文件
            out.flush();//清空缓冲区的数据流
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeCompressCode(fr, fw); //进一步写入操作
    }

    private void writeCompressCode(File fr, File fw) {
        String code;//临时存放哈夫曼编码
        char tmp, buffer = 0;
        int index = 0;     //字节索引
        int codeIndex = 0; //检测编码是否读完
        boolean flag = false;          //标记变量，防止有剩余数据未被写入压缩文件
        try {
            FileInputStream in = new FileInputStream(fr);//文件字节输入流
            FileOutputStream out = new FileOutputStream(fw);//文件字节输出流
            if (fr.exists())
                flag = true;
            tmp = (char) in.read();//读入一个字符
            code = searchDictionary(tmp); //从字典里查出字符的哈夫曼编码
            while (flag) {
                while (index < 8)//8个比特（1字节）一循环写入压缩数据
                {
                    if (code.length() > codeIndex) {
                        if (code.charAt(codeIndex) == '0') //标记位为0
                            buffer &= ~(1 << index); //对应的位置为0
                        else// if (code.charAt(codeIndex) == '1') //标记位为1
                            buffer |= (1 << index); //对应的位置为1
                        codeIndex++;
                        index++;
                    } else //字符已读完，跳出循环，读取下一个字符
                    {
                        tmp = (char) in.read(); //从源文件读一个字符
                        codeIndex = 0;                    //字符检索位归0
                        code = searchDictionary(tmp); //从字典里查出字符的哈夫曼编码
                        if (tmp == 65535) {
                            flag = false;//文件已读完
                            break;
                        }
                    }
                }
                if (index != 0) {
                    out.write(buffer); //将转换后的内容写入压缩文件
                    buffer = 0;
                }
                index = 0;
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //getRatio(fr, fw);//计算压缩率
    }

    /**
     * 压缩文件
     *
     * @param filename 待压缩文件名
     */
    public void compress(String filename, String storePath) {
        Huffman root;
        m_cFileState.setM_iCount(0);
        analyse(filename);       //从待文件中获取字符种类和出现次数
        root = creatHuffmanTree(); //建立哈夫曼树
        huffmanCode(root);       //获取哈夫曼编码表
        writeToFile(filename, storePath);   //将文件压缩存入压缩文档
    }

    /**
     * 获取文件压缩比
     *
     * @param before 原文件
     * @param after  压缩文件
     */
    private void getRatio(File before, File after) {
        long size1 = before.length();//待压缩文件大小
        long size2 = after.length();//压缩文件大小
        double answer = (double) size2 / (double) size1;//压缩率
        System.out.println(answer);
    }

    /**
     * 文件解压
     *
     * @param filename  待解压文件名
     * @param storePath 已解压文件的存储地址
     */
    public void uncompress(String filename, String storePath) {
        writeToDeFile(filename, storePath);
    }

    public void writeToDeFile(String filename, String storePath) {
        File fr = new File(filename);//待解压文件
        String newFileName = storePath+"\\"+fr.getName().replace("si", getFileExtension(fr));//解压文件名
        System.out.println(newFileName);
        //int order;
        if (!fr.exists()) {
            System.out.println("Cannot open the file ");
            System.out.println(filename);
            return;
        }
        /*Scanner x = new Scanner(System.in);
        System.out.println("请决定解压缩文件输出路径：\\n");
        System.out.println("1.自定义输入\\n");
        System.out.println("2.默认路径，按除1外任意键将生成同名解压缩文件于同目录下\\n");
        order = x.nextInt();
        if(order ==1){
            System.out.println("请输入输出路径：\n");
            newFileName = x.nextLine();
        }
        else
            newFileName = filename.replace(".si",".si.png");*/
        File fw = new File(newFileName);
        if (!fr.exists()) {
            try {
                fw.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String codeFile = filename.replace(".si", ".code");//压缩编码文件名
        File fc = new File(codeFile);//压缩编码文件
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fc));
            m_cFileState = (FileState) in.readObject();//获取压缩编码
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Huffman root = creatHuffmanTree();//构建哈夫曼树用于解码
        writeUncompressCode(fr, fw, root);//解压文件将数据写入对应文件
    }

    /**
     * 将文件解压后写入一个新建文本文件
     *
     * @param fr   待解压缩文件
     * @param fw   解压缩数据输出文件
     * @param root 对应编码哈夫曼树
     */
    private void writeUncompressCode(File fr, File fw, Huffman root) {
        Huffman tmproot = root; // tmproot为当前根节点
        char tmp;
        int index;
        try {
            FileInputStream in = new FileInputStream(fr);//文件字节输入流
            FileOutputStream out = new FileOutputStream(fw);//文件字节输出流
            tmp = (char) in.read();//fr已经过判断，不为空
            while (tmp != 65535)//文件到结尾时将读入65535 /2^16-1
            {
                index = 0;
                while (index < 8) {
                    if ((tmp & (1 << index)) != 0) //按位判断，当位值为1，进入右子树
                        tmproot = tmproot.getM_cRightChild();
                    else //当位值为0，进入左子树
                        tmproot = tmproot.getM_cLeftChild();
                    if (tmproot.getM_cLeftChild() == null && tmproot.getM_cRightChild() == null) //到达叶子结点，得到原字符
                    {
                        out.write(tmproot.getM_cElement().getM_cCha());
                        tmproot = root; // tmproot重新指向根结点
                    }
                    index++;
                }
                tmp = (char) in.read();//读下一个字符
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

}
