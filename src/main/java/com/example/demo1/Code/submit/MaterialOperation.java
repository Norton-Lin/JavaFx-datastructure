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

public class MaterialOperation {
    public static class MaterialMenu extends JFrame {

        /**
         * @param storage 资料存储区总目录
         */
        public MaterialMenu(String storage) {

            super("资料区");

            Compress compressClass = new Compress();

            //创建并设置菜单栏
            MenuBar menuBar = new MenuBar();
            this.setMenuBar(menuBar);

            AtomicInteger flag = new AtomicInteger();//标志

            //创建菜单 Type，添加到菜单栏
            Menu menuType = new Menu("资料", true);
            menuBar.add(menuType);

            MenuItem typeView = new MenuItem("查看资料");
            MenuItem typeChoose = new MenuItem("选择资料");

            //查看资料选项
            menuType.addActionListener(e -> {

                flag.set(0);

                File file = new File(storage);
                JFileChooser fileChooser = new JFileChooser(file);
                fileChooser.setFileSelectionMode(FILES_AND_DIRECTORIES);

                //添加查看文件内容按钮
                JLabel view = new JLabel();
                fileChooser.showDialog(view, "查看");

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

            AtomicReference<String> MyMaterialPath = new AtomicReference<>();//保存待上传资料的位置

            //选择资料选项
            typeChoose.addActionListener(e -> {

                flag.set(1);//设置标志，后续可以对待上传资料文件进行操作：如查重去重、压缩、提交

                //默认打开时为 d盘
                JFileChooser homeworkChooser = new JFileChooser("D:\\Java");
                homeworkChooser.setFileSelectionMode(FILES_AND_DIRECTORIES);

                //添加查看文件内容按钮
                JLabel view = new JLabel();
                homeworkChooser.showDialog(view, "选择");
                try {
                    MyMaterialPath.set(homeworkChooser.getSelectedFile().getAbsolutePath());//得到学生作业的路径

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
            operateRecheck.addActionListener(e -> {

                String MyMaterialPathStr = String.valueOf(MyMaterialPath).substring(String.valueOf(MyMaterialPath).lastIndexOf(".") + 1);
                boolean FileTypeOk = MyMaterialPathStr.equals("txt") || MyMaterialPathStr.equals("doc") || MyMaterialPathStr.equals("docx");

                //此时已经选择了txt文件或者word，对文件进行查重，查重对象是 storage目录下的未压缩文件
                if (flag.get() == 1 && FileTypeOk) {

                    File fileStorage = new File(storage);
                    File[] fs = fileStorage.listFiles();//将 storage下的所有文件放在 fs数组中

                    //资料区已经有文件才进行查重去重
                    if (fs != null) {

                        //创建存放解压文件的临时文件夹 temp
                        File tempFile = new File(storage + "\\temp");
                        tempFile.mkdir();
                        String tempFilePath = tempFile.getAbsolutePath();

                        //遍历所有的文件
                        for (File file : fs) {

                            String siFileName = file.getAbsolutePath();

                            //对以下文本直接进行查重
                            if (siFileName.endsWith("txt") || siFileName.endsWith("dox") || siFileName.endsWith("docx")) {
                                try {

                                    String myMaterial = new String(Files.readAllBytes(Path.of(MyMaterialPath.get())));
                                    String otherMaterial = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

                                    //重复率过高，需要降重
                                    if (StringCompute.SimilarDegree(otherMaterial, myMaterial) > 0.4) {

                                        //将去重后的字符串重新写入学生要提交的资料文件中
                                        String lastWord = StringCompute.deleteSubstring(otherMaterial, myMaterial);
                                        PrintStream stream = new PrintStream(String.valueOf(MyMaterialPath));
                                        stream.print(lastWord);
                                        stream.close();
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            //将压缩文件解压到临时文件夹
                            else if (siFileName.endsWith("si")) {
                                compressClass.uncompress(siFileName, tempFilePath);
                            }
                        }

                        //保存临时文件夹下的所有文件
                        File[] Materials = tempFile.listFiles();

                        if (Materials != null) {

                            //此时遍历临时文件中的txt文件和word文件，继续对待上传资料进行查重去重
                            for (File material : Materials) {

                                String siFileName = material.getAbsolutePath();

                                if (siFileName.endsWith("txt") || siFileName.endsWith("dox") || siFileName.endsWith("docx")) {
                                    try {

                                        String myMaterial = new String(Files.readAllBytes(Path.of(MyMaterialPath.get())));
                                        String otherMaterial = new String(Files.readAllBytes(Paths.get(material.getAbsolutePath())));

                                        //重复率过高，需要降重
                                        if (StringCompute.SimilarDegree(otherMaterial, myMaterial) > 0.4) {
                                            //将去重后的字符串重新写入要上传的资料文件中
                                            String lastWord = StringCompute.deleteSubstring(otherMaterial, myMaterial);
                                            PrintStream stream = new PrintStream(String.valueOf(MyMaterialPath));
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
                        }

                    }
                }
            });
            menuOperate.add(operateRecheck);

            //创建菜单 Upload，添加到菜单栏
            Menu menuUpload = new Menu("上传方式", true);
            menuBar.add(menuUpload);

            MenuItem submitChoose = new MenuItem("直接上传");
            MenuItem submitCompress = new MenuItem("压缩上传");

            //直接上传选项
            submitChoose.addActionListener(e -> {

                //此时已经选择了文件
                if (flag.get() == 1) {
                    //此时直接提交选择的原文件到 storage目录下
                    if (flag.get() == 1) {
                        copyFile(String.valueOf(MyMaterialPath), storage);
                    }
                }
            });

            //压缩上传选项
            submitCompress.addActionListener(e -> {
                //此时已经选择了文件
                if (flag.get() == 1) {
                    //此时先对原文件进行压缩，再保存 students目录下
                    if (flag.get() == 1) {
                        compressClass.compress(String.valueOf(MyMaterialPath), storage);
                    }
                }
            });

            menuUpload.add(submitChoose);
            menuUpload.add(submitCompress);

            setSize(300, 300);
            setVisible(true);
            this.setLocationRelativeTo(null);
            this.setResizable(false);
            this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

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
     * 提供给前端的接口
     *
     * @param storePath 资料区目录
     */
    public static void MaterialPort(String storePath) {
        new MaterialOperation.MaterialMenu(storePath);
    }

    public static void main(String[] args) {
        MaterialPort("D:\\Material");
    }
}
