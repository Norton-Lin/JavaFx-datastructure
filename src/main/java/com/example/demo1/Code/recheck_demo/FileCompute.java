
package com.example.demo1.Code.recheck_demo;

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
 * �ļ�����ϵͳ��ͼ�λ�����
 */
public class FileCompute {

    // main��������������
    public static void main(String[] args) {
        FileCompute fileCompute = new FileCompute();
        fileCompute.RepetitionCalculate();
    }

    /**
     * ѡ���ļ����в���
     */
    public void RepetitionCalculate() {

        //awt�ǵ��߳�ģʽ�ģ�����awt�����ֻ�����Ƽ���ʽ���¼������߳��з��ʣ��Ӷ���֤���״̬����ȷ��
        java.awt.EventQueue.invokeLater(() -> {

            final JFrame frame = new JFrame("�ļ������ظ��ʼ���");
            final JLabel tag = new JLabel("��ʾ������ѡ���ļ���ťѡ�����ֱ�����ı����������ļ�");

            //ѡ���ļ�һ
            final JButton load_1 = new JButton("ѡ���ļ�һ:");
            final JLabel filename_1 = new JLabel("");
            final JTextArea textarea_1 = new JTextArea(6, 20);//�ı���
            textarea_1.setLineWrap(true);//����Ϊ�Զ�����
            textarea_1.setWrapStyleWord(true);//�������ڱ߾ദ�Զ�����
            final JScrollPane scroller_1 = new JScrollPane(textarea_1);//������Ч��

            //�����ļ�һ���¼�����
            load_1.addActionListener(new ActionListener() {

                private JFileChooser fileChooser_1 = null;
                private final DefaultEditorKit kit_1 = new DefaultEditorKit();

                public void actionPerformed(ActionEvent e) {

                    if (fileChooser_1 == null) {
                        //����Ĭ���ļ�ѡ��·����������Ҫ�Ķ�
                        fileChooser_1 = new JFileChooser("D:");
                    }

                    //�����ļ����ͣ�֧�ִ� txt�ļ��� Word�ĵ�
                    fileChooser_1.setFileFilter(new FileNameExtensionFilter("text file", "txt", "text", "doc", "docx"));

                    if (fileChooser_1.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {

                        //��ʾ�ļ�·��
                        filename_1.setText(fileChooser_1.getSelectedFile().getAbsolutePath());

                        FileReader reader_1 = null;
                        //���ļ����ݶ�ȡ�� textarea���沢�ҽ����쳣����
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
                        textarea_1.setCaretPosition(0);//��꽹��
                    }
                }
            });

            //�����ļ������¼�����
            final JButton load_2 = new JButton("ѡ���ļ���:");
            final JLabel filename_2 = new JLabel("");
            final JTextArea textarea_2 = new JTextArea(6, 20);
            textarea_2.setLineWrap(true);
            textarea_2.setWrapStyleWord(true);
            final JScrollPane scroller_2 = new JScrollPane(textarea_2);

            //�����ļ������¼�����
            load_2.addActionListener(new ActionListener() {

                private JFileChooser fileChooser_2 = null;
                private final DefaultEditorKit kit_2 = new DefaultEditorKit();

                public void actionPerformed(ActionEvent e) {
                    if (fileChooser_2 == null) {
                        //����Ĭ���ļ�ѡ��·����������Ҫ�Ķ�
                        fileChooser_2 = new JFileChooser("D:");
                    }

                    fileChooser_2.setFileFilter(new FileNameExtensionFilter("text file", "txt", "text", "doc", "docx"));

                    if (fileChooser_2.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        filename_2.setText(fileChooser_2.getSelectedFile().getAbsolutePath());
                        FileReader reader_2 = null;
                        try {
                            reader_2 = new FileReader(fileChooser_2.getSelectedFile());
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

            //��ʾ�����ļ����������ݵĴ���
            final JTextArea textarea_res = new JTextArea(6, 20);
            textarea_res.setLineWrap(true);
            textarea_res.setWrapStyleWord(true);
            final JScrollPane scroller_res = new JScrollPane(textarea_res);

            //����textarea_res͸��
            textarea_res.setOpaque(false);
            scroller_res.setOpaque(false);
            scroller_res.getViewport().setOpaque(false);

            //textarea��textarea2���ݸı��¼�,ɾ���ļ�·������������
            textarea_1.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    filename_1.setText("");
                    textarea_res.setText("");
                }
            });
            textarea_2.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    filename_2.setText("");
                    textarea_res.setText("");
                }
            });

            //���ذ�ť
            final JButton start = new JButton("��        ��");

            //�����¼�
            start.addActionListener(e -> {

                //���ļ��������ַ�����һ���ַ���
                String temp_strA = textarea_1.getText();
                String temp_strB = textarea_2.getText();
                String strA, strB;

                //�������textarea����Ϊ���Ҷ���ȫΪ���ţ���������ƶȼ��㣬������ʾ�û������������ݻ�ѡ���ļ�
                if (!(StringCompute.removeSign(temp_strA).length() == 0 && StringCompute.removeSign(temp_strB).length() == 0)) {

                    if (temp_strA.length() >= temp_strB.length()) {
                        strA = temp_strA;
                        strB = temp_strB;
                    } else {
                        strA = temp_strB;
                        strB = temp_strA;
                    }
                    double result = StringCompute.SimilarDegree(strA, strB);

                    //��ʾ����������textarea_res
                    textarea_res.setText("���Ƶ�����Ϊ��" + StringCompute.longestCommonSubstring(strA, strB));
                    //���ؽ��
                    JOptionPane.showMessageDialog(null, "    ���ƶ�Ϊ��" + StringCompute.similarityResult(result), "�� �� �� ��", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "     ���ã���������ȷ���ݣ� ", "��    ʾ", JOptionPane.ERROR_MESSAGE);
                }
            });

            //�˳���ť
            final JButton cancel = new JButton("��        ��");

            //�˳��¼�
            cancel.addActionListener(e -> {
                frame.dispose();//�ͷŴ�����ռ��Դ
                System.exit(0);//�˳�����
            });

            //�ܲ���
            //�ļ�һnorth
            final Box north = Box.createVerticalBox();//������
            north.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));//�߾�
            north.add(tag);
            north.add(Box.createVerticalStrut(10));
            north.add(load_1);
            north.add(Box.createVerticalStrut(5));
            north.add(filename_1);
            north.add(scroller_1);
            frame.add(north, BorderLayout.NORTH);

            //�ļ���center
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
            south.add(Box.createHorizontalGlue());//��ť������ʾ
            south.add(start);
            south.add(Box.createHorizontalStrut(20));//ˮƽ���
            south.add(cancel);
            south.add(Box.createVerticalStrut(5));
            frame.add(south, BorderLayout.SOUTH);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//����Ĭ���˳���ʽ
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
}