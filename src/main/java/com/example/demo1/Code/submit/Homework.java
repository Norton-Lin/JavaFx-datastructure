package com.example.demo1.Code.submit;

import com.example.demo1.Code.compress.Compress;
import com.example.demo1.Code.recheck.StringCompute;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static javax.swing.JFileChooser.FILES_AND_DIRECTORIES;

/**
 * 作业相关功能图形化
 */
public class Homework {

    public static class HomeworkMenu extends JFrame {

        /**
         * @param storage 作业存储区总目录
         */
        public HomeworkMenu(String storage) {

            super("作业区");

            Compress compressClass = new Compress();

            //创建并设置菜单栏
            MenuBar menuBar = new MenuBar();
            this.setMenuBar(menuBar);

            //创建菜单 Type，添加到菜单栏
            Menu menuType = new Menu("作业", true);
            menuBar.add(menuType);

            //创建菜单 Type的选项
            MenuItem typeView = new MenuItem("查看作业");
            MenuItem typeSubmit = new MenuItem("选择作业");
            MenuItem typeClose = new MenuItem("退       出");

            AtomicInteger flag = new AtomicInteger();

            //查看作业
            typeView.addActionListener(e -> {

                flag.set(1);
                File file = new File(storage);
                JFileChooser fileChooser = new JFileChooser(file);
                fileChooser.setFileSelectionMode(FILES_AND_DIRECTORIES);

                //添加查看文件内容按钮
                JLabel view = new JLabel();
                fileChooser.showDialog(view, "查看作业");

                try {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1);

                    String ViewerPath = null;

                    //根据文件类型选择文件打开方式
                    switch (fileExtension) {

                        // 记事本打开 txt文本
                        case "txt" -> ViewerPath = "notepad.exe";

                        // Word打开docx文件和doc文件
                        case "doc", "docx" -> ViewerPath = "C:\\Program Files\\Microsoft Office\\root\\Office16\\WINWORD.EXE";

                        // MicrosoftEdge打开png,jpg,pdf文件
                        case "png", "jpg", "pdf" -> ViewerPath = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe";
                    }

                    ProcessBuilder viewTxt = new ProcessBuilder(ViewerPath, filePath);
                    try {
                        viewTxt.start();
                    } catch (IOException ioException) {
                        System.out.println("未能成功打开文件...请重试");
                    }
                } catch (Exception ignore) {
                }

            });

            AtomicReference<String> MyHomeworkPath = new AtomicReference<>();//学生作业的位置

            //选择作业
            typeSubmit.addActionListener(e -> {
                flag.set(2);
                //默认打开时为 d盘
                JFileChooser homeworkChooser = new JFileChooser("D:\\Java");
                homeworkChooser.setFileSelectionMode(FILES_AND_DIRECTORIES);
                //添加查看文件内容按钮
                JLabel view = new JLabel();
                homeworkChooser.showDialog(view, "选择");
                try{
                    MyHomeworkPath.set(homeworkChooser.getSelectedFile().getAbsolutePath());//得到学生作业的路径

                }catch (NullPointerException ignore){}
            });

            //退出程序
            typeClose.addActionListener(e -> System.exit(0));

            menuType.add(typeView);
            menuType.add(typeSubmit);
            menuType.add(typeClose);

            //创建菜单 Process的选项，添加到菜单栏
            Menu menuProcess = new Menu("操作", true);
            menuBar.add(menuProcess);

            //创建菜单 Process的选项
            MenuItem fileRecheck = new MenuItem("查重并去重");
            MenuItem fileCompress = new MenuItem("压缩并提交");

            //AtomicReference<String> tempFilePath = new AtomicReference<>();
            //文件查重并去重
            fileRecheck.addActionListener(e -> {

                //此时对要提交的作业进行查重
                if (flag.get() == 2) {

                    flag.set(3);

                    File fileStorage = new File(storage);
                    File[] fs = fileStorage.listFiles();//将 storage下的文件名和 students目录名放在 fs数组中
                    assert fs != null;

                    //先得到所有待比较的作业文件
                    for (File file : fs) {

                        //System.out.println(file.getAbsolutePath());//测试

                        //所有作业文件在存储区子目录下
                        if (file.isDirectory()) {

                            File[] packedHomework = file.listFiles();//保存所有的学生作业文件(压缩文件)

                            //已经有人提交过作业时才进行查重
                            if (packedHomework != null) {

                                //创建临时存放解压文件的文件夹 temp
                                File tempFile = new File(storage + "\\temp");
                                tempFile.mkdir();
                                String tempFilePath = tempFile.getAbsolutePath();

                                //遍历所有已交的作业，将其解压到临时文件夹
                                for (File homework : packedHomework) {

                                    String siFileName = homework.getAbsolutePath();
                                    //防止将码表解压
                                    if (siFileName.endsWith("si")) {
                                        compressClass.uncompress(siFileName, tempFilePath);
                                    }
                                }

                                //保存临时文件夹下所有的作业文件
                                File[] Homeworks = tempFile.listFiles();

                                //开始查重及去重
                                assert Homeworks != null;
                                for (File homeworks : Homeworks) {
                                    try {

                                        String myHomework = new String(Files.readAllBytes(Path.of(MyHomeworkPath.get())));
                                        String otherHomework = new String(Files.readAllBytes(Paths.get(homeworks.getAbsolutePath())));

                                        //重复率过高，需要降重
                                        if (StringCompute.SimilarDegree(otherHomework, myHomework) > 0.4) {
                                            //将去重后的字符串重新写入学生要提交的作业文件中
                                            String lastWord = StringCompute.deleteSubstring(otherHomework, myHomework);
                                            PrintStream stream = new PrintStream(String.valueOf(MyHomeworkPath));
                                            stream.print(lastWord);
                                            stream.close();
                                        }
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }

                                }

                                //删除临时文件夹
                                /*if (deleteDirectory(tempFile.getAbsolutePath())) {
                                    System.out.println("临时文件夹删除成功");
                                } else {
                                    System.out.println("临时文件夹删除未成功");
                                }*/
                            }

                        }
                    }

                }

            });

            //文件压缩并提交
            fileCompress.addActionListener(
                    e -> {
                        //此时要对已经去重的文件压缩
                        if (flag.get() == 3) {
                            compressClass.compress(String.valueOf(MyHomeworkPath), storage + "\\students");
                        }
                    }
            );

            menuProcess.add(fileRecheck);
            menuProcess.add(fileCompress);

            setSize(300, 300);
            setVisible(true);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        }

    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }


    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
            }
            // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath()); //如果目录下还有目录，则反复调用
            }
            if (!flag) {
                break;
            }
        }

        if (!flag) {
            return false;
        }

        // 删除当前目录
        return dirFile.delete();
    }


    public static void SubmitHomework(String storePath) {
        new HomeworkMenu(storePath);
    }

    public static void main(String[] args) {
        SubmitHomework("D:\\Homework");
    }
}
