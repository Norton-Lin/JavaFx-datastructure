package com.example.demo1.Code.submit;

import com.example.demo1.Code.compress.Compress;
import com.example.demo1.Code.recheck.StringCompute;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static javax.swing.JFileChooser.FILES_AND_DIRECTORIES;

/**
 * 作业相关功能图形化
 * 与前端的接口为HomeworkOperation()方法
 * 注意事项：小组其他成员在测试时可能需要修改代码第70-85行中部分exe文件的内存地址
 */
public class HomeworkOperation {

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
            MenuItem typeChoose = new MenuItem("提交作业");

            AtomicInteger flag = new AtomicInteger();//设置标志，用于判断后续执行何种操作

            //查看作业选项
            typeView.addActionListener(e -> {

                flag.set(0);//设置标志，防止后续存在空操作导致抛出异常
                File file = new File(storage);
                JFileChooser fileChooser = new JFileChooser(file);
                fileChooser.setFileSelectionMode(FILES_AND_DIRECTORIES);

                //添加查看文件内容按钮
                JLabel view = new JLabel();
                fileChooser.showDialog(view, "查看");

                try {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();//要查看的文件的路径
                    String fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1);//要查看的文件的拓展名

                    String ViewerPath;//文件查看器的内存地址

                    //根据文件类型选择文件打开方式
                    switch (fileExtension) {

                        // 记事本打开 txt文本
                        case "txt" -> ViewerPath = "notepad.exe";

                        // Word打开docx文件和doc文件
                        case "doc", "docx" -> ViewerPath = "C:\\Program Files\\Microsoft Office\\root\\Office16\\WINWORD.EXE";

                        // MicrosoftEdge打开png,jpg,pdf文件
                        case "png", "jpg", "pdf" -> ViewerPath = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe";

                        //其他格式文件则使用 MicrosoftEdge打开
                        default -> ViewerPath = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe";
                    }

                    ProcessBuilder viewTxt = new ProcessBuilder(ViewerPath, filePath);//创建文件查看进程

                    try {
                        viewTxt.start();
                    } catch (IOException ioException) {
                        System.out.println("未能成功打开文件...请重试");
                    }
                } catch (Exception ignore) {
                }

            });

            AtomicReference<String> MyHomeworkPath = new AtomicReference<>();//保存学生待交作业的位置

            //选择作业选项
            typeChoose.addActionListener(e -> {

                flag.set(1);//设置标志，后续可以对待交作业文件进行操作：如查重去重、压缩、提交

                //默认打开时为 d盘
                JFileChooser homeworkChooser = new JFileChooser("D:\\Java");
                homeworkChooser.setFileSelectionMode(FILES_AND_DIRECTORIES);

                //添加查看文件内容按钮
                JLabel view = new JLabel();
                homeworkChooser.showDialog(view, "选择");
                try {
                    MyHomeworkPath.set(homeworkChooser.getSelectedFile().getAbsolutePath());//得到学生作业的路径

                } catch (NullPointerException ignore) {
                }
            });

            menuType.add(typeView);
            menuType.add(typeChoose);

            //创建菜单 Operate，添加到菜单栏
            Menu menuOperate = new Menu("去重[可选]", true);
            menuBar.add(menuOperate);

            //创建菜单 Operate的选项
            MenuItem operateRecheck = new MenuItem("查重并去重");

            //AtomicReference<String> tempFilePath = new AtomicReference<>();
            //文件查重并去重选项
            operateRecheck.addActionListener(e -> {

                String MyHomeworkPathStr = String.valueOf(MyHomeworkPath).substring(String.valueOf(MyHomeworkPath).lastIndexOf(".") + 1);
                boolean FileTypeOk = MyHomeworkPathStr.equals("txt") || MyHomeworkPathStr.equals("doc") || MyHomeworkPathStr.equals("docx");

                //此时已经选择了txt文件或者word，对文件进行查重，查重对象是 students目录下的未压缩文件
                if (flag.get() == 1 && FileTypeOk) {

                    File fileStorage = new File(storage);
                    File[] fs = fileStorage.listFiles();//将 storage下的文件和 students目录放在 fs数组中
                    assert fs != null;

                    //先得到所有待比较的作业文件
                    for (File file : fs) {

                        //所有作业文件在 students子目录下
                        if (file.isDirectory()) {

                            File[] packedHomework = file.listFiles();//保存所有的学生作业文件(包括未压缩文件和压缩文件)

                            //已经有人提交过作业时才进行查重
                            if (packedHomework != null) {

                                //创建存放解压文件的临时文件夹 temp
                                File tempFile = new File(storage + "\\temp");
                                tempFile.mkdir();
                                String tempFilePath = tempFile.getAbsolutePath();
                                //新建临时文件夹保存作业源文件的副本，防止查重时修改源文件
                                File tempMyMaterial = new File(storage + "\\" + "duplicate");
                                tempMyMaterial.mkdir();
                                String duplicateMaterial = tempMyMaterial.getAbsolutePath();
                                copyFile(String.valueOf(MyHomeworkPath), duplicateMaterial);
                                //将上传文件的地方更新为副本
                                MyHomeworkPath.set(duplicateMaterial + "\\" + new File(String.valueOf(MyHomeworkPath)).getName());


                                //先遍历所有的txt文件和word文件，对待交作业进行查重去重
                                for (File homework : packedHomework) {

                                    String siFileName = homework.getAbsolutePath();
                                    if (siFileName.endsWith("txt") || siFileName.endsWith("dox") || siFileName.endsWith("docx")) {
                                        try {

                                            String myHomework = new String(Files.readAllBytes(Path.of(MyHomeworkPath.get())));
                                            String otherHomework = new String(Files.readAllBytes(Paths.get(homework.getAbsolutePath())));

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
                                }

                                //再遍历所有已交的作业，将压缩文件解压到临时文件夹
                                for (File homework : packedHomework) {

                                    String siFileName = homework.getAbsolutePath();
                                    //防止将码表解压
                                    if (siFileName.endsWith("si")) {
                                        compressClass.uncompress(siFileName, tempFilePath);
                                    }
                                }

                                //保存临时文件夹下所有的解压出来作业文件
                                File[] Homeworks = tempFile.listFiles();

                                //此时遍历临时文件中的txt文件和word文件，继续对待交作业进行查重去重
                                assert Homeworks != null;
                                for (File homeworks : Homeworks) {
                                    String siFileName = homeworks.getAbsolutePath();

                                    if (siFileName.endsWith("txt") || siFileName.endsWith("dox") || siFileName.endsWith("docx")) {
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


                                }

                                //删除查重时产生的临时文件夹
                                if (tempFile.exists()) {
                                    deleteDirectory(tempFile.getAbsolutePath());
                                }

                                //删除去重时产生的保存原文件副本的文件夹
                                if (tempMyMaterial.exists()) {
                                    deleteDirectory(tempMyMaterial.getAbsolutePath());
                                }
                            }

                        }
                    }

                }

            });
            menuOperate.add(operateRecheck);

            //创建菜单 Submit的选项，添加到菜单栏
            Menu menuSubmit = new Menu("提交方式", true);
            menuBar.add(menuSubmit);

            //创建菜单 Submit的选项
            MenuItem submitDirect = new MenuItem("直接提交");
            MenuItem submitCompress = new MenuItem("压缩提交");

            String StudentFileName = storage + "\\students";//学生作业提交目录

            //提交原始文件选项
            submitDirect.addActionListener(
                    e -> {
                        //此时已经选择了文件
                        if (flag.get() == 1) {
                            //此时直接提交选择的原文件到 students目录
                            if (flag.get() == 1) {
                                copyFile(String.valueOf(MyHomeworkPath), StudentFileName);
                            }
                        }
                    });

            //提交压缩文件选项
            submitCompress.addActionListener(
                    e -> {
                        //此时已经选择了文件
                        if (flag.get() == 1) {
                            //此时先对原文件进行压缩，再保存 students目录下
                            if (flag.get() == 1) {
                                compressClass.compress(String.valueOf(MyHomeworkPath), StudentFileName);
                            }
                        }
                    }
            );

            menuSubmit.add(submitDirect);
            menuSubmit.add(submitCompress);

            setSize(300, 300);
            setVisible(true);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        }

    }

    /**
     * 把文件复制到指定目录下
     *
     * @param originalFile    源文件名
     * @param destinationFile 目标目录名
     */
    private static void copyFile(String originalFile, String destinationFile) {

        //在目标目录下创建同名文件
        destinationFile = destinationFile + "\\" + new File(originalFile).getName();

        try {
            //创建输入输出流对象
            FileInputStream fis = new FileInputStream(originalFile);
            FileOutputStream fos = new FileOutputStream(destinationFile);

            byte[] dataByte = new byte[1024 * 8];
            int len;

            //将原文件内容写入指定目录下的同名文件中，完成文件复制
            while ((len = fis.read(dataByte)) != -1) {
                fos.write(dataByte, 0, len);
            }

            fis.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 文件名
     * @return 删除结果
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除目录下的所有文件及子目录
     *
     * @param directoryName 目录名
     * @return 删除结果
     */
    public static boolean deleteDirectory(String directoryName) {

        //如果directoryName不以文件分隔符结尾，自动添加文件分隔符
        if (!directoryName.endsWith(File.separator)) {
            directoryName = directoryName + File.separator;
        }
        File dirFile = new File(directoryName);

        //如果directoryName对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;

        //删除文件夹下的所有文件及子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            //删除文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
            }
            //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath()); //如果目录下还有目录，则递归调用
            }
            if (!flag) {
                break;
            }
        }

        if (!flag) {
            return false;
        }

        //删除当前目录自身
        return dirFile.delete();
    }


    /**
     * 提供给前端的接口
     *
     * @param storePath 作业区目录
     */
    public static void HomeworkPort(String storePath) {
        new HomeworkMenu(storePath);
    }

    //main方法仅作测试
    public static void main(String[] args) {
        HomeworkPort("D:\\Homework");
    }
}
