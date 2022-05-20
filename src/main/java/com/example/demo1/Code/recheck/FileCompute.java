
package com.example.demo1.Code.recheck;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.IOException;

/**
 * 文件查重系统的图形化界面
 */
public class FileCompute {

    // main方法仅作测试用
    public static void main(String[] args) {
        FileCompute fileCompute = new FileCompute();
        fileCompute.RepetitionCalculate();
    }

    /**
     * 选择文件进行查重
     */
    public void RepetitionCalculate() {

        //awt是单线程模式的，所有awt的组件只能在推荐方式的事件处理线程中访问，从而保证组件状态的正确性
        java.awt.EventQueue.invokeLater(() -> {

            final JFrame frame = new JFrame("文件内容重复率计算");
            final JLabel tag = new JLabel("提示：请点击选择文件按钮选择或者直接在文本框中输入文件");

            //选择文件一
            final JButton load_1 = new JButton("选择文件一:");
            final JLabel filename_1 = new JLabel("");
            final JTextArea textarea_1 = new JTextArea(6, 20);//文本框
            textarea_1.setLineWrap(true);//设置为自动换行
            textarea_1.setWrapStyleWord(true);//超长行在边距处自动换行
            final JScrollPane scroller_1 = new JScrollPane(textarea_1);//滚动条效果

            //加载文件一的事件监听
            load_1.addActionListener(new ActionListener() {

                private JFileChooser fileChooser_1 = null;
                private final DefaultEditorKit kit_1 = new DefaultEditorKit();

                public void actionPerformed(ActionEvent e) {

                    if (fileChooser_1 == null) {
                        //设置默认文件选择路径，后续需要改动
                        fileChooser_1 = new JFileChooser("D://桌面");
                    }

                    //过滤文件类型，支持打开 txt文件和 Word文件
                    fileChooser_1.setFileFilter(new FileNameExtensionFilter("text file", "txt", "text", "doc", "docx"));

                    if (fileChooser_1.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {

                        //显示文件路径
                        filename_1.setText(fileChooser_1.getSelectedFile().getAbsolutePath());

                        FileReader reader_1 = null;
                        //将文件内容读取到 textarea里面并且进行异常处理
                        try {
                            reader_1 = new FileReader(fileChooser_1.getSelectedFile());
                            textarea_1.setText("");
                            kit_1.read(reader_1, textarea_1.getDocument(), 0);
                        } catch (Exception xe) {
                            System.err.println(xe.getMessage());
                        } finally {
                            if (reader_1 != null) {
                                try {
                                    reader_1.close();
                                } catch (IOException ioe) {
                                    System.err.println(ioe.getMessage());
                                }
                            }
                        }
                        textarea_1.setCaretPosition(0);//鼠标焦点
                    }
                }
            });

            //加载文件二的事件监听
            final JButton load_2 = new JButton("选择文件二:");
            final JLabel filename_2 = new JLabel("");
            final JTextArea textarea_2 = new JTextArea(6, 20);
            textarea_2.setLineWrap(true);
            textarea_2.setWrapStyleWord(true);
            final JScrollPane scroller_2 = new JScrollPane(textarea_2);

            //加载文件二的事件监听
            load_2.addActionListener(new ActionListener() {

                private JFileChooser fileChooser_2 = null;
                private final DefaultEditorKit kit_2 = new DefaultEditorKit();

                public void actionPerformed(ActionEvent e) {
                    if (fileChooser_2 == null) {
                        //设置默认文件选择路径，后续需要改动
                        fileChooser_2 = new JFileChooser("D://桌面");
                    }

                    fileChooser_2.setFileFilter(new FileNameExtensionFilter("text file", "txt", "text", "doc", "docx"));

                    if (fileChooser_2.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        filename_2.setText(fileChooser_2.getSelectedFile().getAbsolutePath());
                        FileReader reader_2 = null;
                        try {
                            reader_2 = new FileReader(fileChooser_2.getSelectedFile());
                            //String content = new String(Files.readAllBytes(Path.of(fileChooser_2.getSelectedFile().getAbsolutePath())));
                            textarea_2.setText("");
                            kit_2.read(reader_2, textarea_2.getDocument(), 0);
                        } catch (Exception xe2) {
                            System.err.println(xe2.getMessage());
                        } finally {
                            if (reader_2 != null) {
                                try {
                                    reader_2.close();
                                } catch (IOException ioe2) {
                                    System.err.println(ioe2.getMessage());
                                }
                            }
                        }
                        textarea_2.setCaretPosition(0);
                    }
                }
            });

            //显示两个文件的相似内容的窗口
            final JTextArea textarea_repetition = new JTextArea(6, 20);
            textarea_repetition.setLineWrap(true);
            textarea_repetition.setWrapStyleWord(true);
            final JScrollPane scroller_res = new JScrollPane(textarea_repetition);

            //设置textarea_res透明
            textarea_repetition.setOpaque(false);
            scroller_res.setOpaque(false);
            scroller_res.getViewport().setOpaque(false);

            //textarea和textarea2内容改变事件,删除文件路径和相似内容
            textarea_1.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    filename_1.setText("");
                    textarea_repetition.setText("");
                }
            });
            textarea_2.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    filename_2.setText("");
                    textarea_repetition.setText("");
                }
            });

            //查重按钮
            final JButton start = new JButton("查        重");

            //查重事件
            start.addActionListener(e -> {

                //将文件的所有字符存入一个字符串
                String temp_strA = textarea_1.getText();
                String temp_strB = textarea_2.getText();
                String strA, strB;

                //如果两个textarea都不为空且都不全为符号，则进行相似度计算，否则提示用户进行输入数据或选择文件
                if (!(StringCompute.removeSign(temp_strA).length() == 0 && StringCompute.removeSign(temp_strB).length() == 0)) {

                    if (temp_strA.length() >= temp_strB.length()) {
                        strA = temp_strA;
                        strB = temp_strB;
                    } else {
                        strA = temp_strB;
                        strB = temp_strA;
                    }
                    double result = StringCompute.SimilarDegree(strA, strB);

                    //显示相似内容于textarea_res
                    textarea_repetition.setText("相似的内容为：\n" + StringCompute.longestCommonSubstring(strA, strB));
                    //查重结果.
                    JOptionPane.showMessageDialog(null, "    动态规划：" + StringCompute.similarityResult(result) ,"计 算 结 果", JOptionPane.PLAIN_MESSAGE);
                    //+" 余弦1："+StringCompute.similarityResult(StringCompute.getSimilarity(strA,strB))+" 余弦3："+StringCompute.similarityResult(StringCompute.getCosineSimilarity(strA,strB)),
                } else {
                    JOptionPane.showMessageDialog(null, "     请输入正确内容...", "提    示", JOptionPane.ERROR_MESSAGE);
                }
            });

            //退出按钮
            final JButton cancel = new JButton("退        出");

            //退出事件
            cancel.addActionListener(e -> {
                frame.dispose();//释放窗体所占资源
                System.exit(0);//退出程序
            });

            //去重按钮
            final JButton RemoveRepetition = new JButton("去        重");
            RemoveRepetition.addActionListener(e ->{

            });
            //总布局
            //文件一north
            final Box north = Box.createVerticalBox();//竖排列
            north.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));//边距
            north.add(tag);
            north.add(Box.createVerticalStrut(10));
            north.add(load_1);
            north.add(Box.createVerticalStrut(5));
            north.add(filename_1);
            north.add(scroller_1);
            frame.add(north, BorderLayout.NORTH);

            //文件二center
            final Box center = Box.createVerticalBox();
            center.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            center.add(load_2);
            center.add(Box.createVerticalStrut(5));
            center.add(filename_2);
            center.add(scroller_2);
            center.add(scroller_res);
            frame.add(center, BorderLayout.CENTER);

            //south
            final Box south = Box.createHorizontalBox();
            south.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            //south.add(Box.createHorizontalGlue());//按钮居中显示
            south.add(start);
            south.add(Box.createHorizontalStrut(20));//水平间距
            south.add(RemoveRepetition);
            south.add(Box.createVerticalStrut(15));
            south.add(cancel);
            south.add(Box.createVerticalStrut(10));

            frame.add(south, BorderLayout.SOUTH);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//窗体默认退出形式
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
}
