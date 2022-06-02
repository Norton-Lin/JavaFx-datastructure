package com.example.demo1;

import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.FuzzySearch;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.StudentAccount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CourseChooseController {

    private final Stage thisStage;
    private final MainViewPort_Controller controller;


    @FXML
    public Button Search1;
    @FXML
    public Button Assure1;
    @FXML
    public Button Search2;
    @FXML
    public Button Assure2;
    @FXML
    public Button backToMain;
    @FXML
    public Label SearchResult_Name;
    @FXML
    public Label SearchResult_Boolean;
    @FXML
    public TextField ToBeFuzzySearched;
    @FXML
    public TextField ToBeBinarySearched;
    @FXML
    public TextArea ResOfSearch;

    StudentAccount studentAccount;

    //存放本账号的课程表
    ArrayList<Course> courses = new ArrayList<>();

    public CourseChooseController(MainViewPort_Controller mainViewPort_controller) {
        //得到新Controller
        this.controller = mainViewPort_controller;
        //获取学生账户信息
        this.studentAccount = new StudentAccount(this.controller.getAccount());
        //获得课程列表
        this.courses = this.studentAccount.getCourse();

        //创建新场景
        thisStage = new Stage();

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseChoose.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 650, 400));
            thisStage.setTitle("欢迎来到选课界面~");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        //创建新场景
        this.thisStage.show();
    }

    @FXML
    private void initialize() {
        //点击查询所有课程的反应
        this.Search1.setOnAction(event -> Search1ButtonClicked());
        //点击添加未选课程后的反应
        this.Assure1.setOnAction(event -> Assure1ButtonClicked());
        //点击查询自己课程后的反应
        this.Search2.setOnAction(event -> Search2ButtonClicked());
        //点击删除已有课程之后的反应
        this.Assure2.setOnAction(event -> Assure2ButtonClicked());
        //点击回到主菜单后的反应
        this.backToMain.setOnAction(event -> backToMainClicked());
    }

    /**
     * 将在所有课程库中查找到的结果显示在文本框中
     */
    private ArrayList<Course> Search1ButtonClicked() {
        //用来存储课程列表的空表
        ArrayList<Course> tool = new ArrayList<>();
        //实例化数据库
        CourseDatabase courseDatabase = new CourseDatabase();
        //从数据库中得到课程列表
        courseDatabase.find(tool);
        //实例化一个模糊查找对象
        FuzzySearch fuzzySearch = new FuzzySearch();
        //建立一个空表用于存储模糊查找结果
        ArrayList<Course> primaryResults = new ArrayList<>();
        //实例化查找类
        Search search = new Search();
        //查询结果
        StringBuilder texts = new StringBuilder();
        //若精确查找框内有内容，优先查找此部分
        try {
            if (this.ToBeBinarySearched.getText() != null) {
                int Num = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), tool);
                Course course_ex = tool.get(Num);
                texts.append(course_ex.getM_sName()).append("\t")
                        .append("教师为：").append(course_ex.getM_sTeacher()).append("\t")
                        .append("编号为：").append(course_ex.getM_iNum()).append("\t")
                        .append("上课地点为：").append(course_ex.getM_sConstruction())
                        .append(course_ex.getM_iFloor()).append("层")
                        .append(course_ex.getM_iRoom()).append("室").append("\t")
                        .append("课程群号：").append(course_ex.getM_sCurGroup()).append("\n")
                        .append("考试时间：").append(course_ex.getM_cExamTime())
                        .append("考试地点：").append(course_ex.getM_cExamConstruction())
                        .append(course_ex.getM_iExamFloor()).append("层")
                        .append(course_ex.getM_iExamRoom()).append("室").append("\t");
                this.ResOfSearch.setText(texts.toString());
                //若精确查找框内不存在内容，则进行模糊查找，否则不进行模糊查找
            } else if (this.ToBeFuzzySearched.getText() != null) {
                //将模糊查找结果存入 list
                primaryResults = fuzzySearch.get_FS_result(ToBeFuzzySearched.getText(), tool);
                if (primaryResults != null) {
                    for (Course course : primaryResults)
                        texts.append(course.getM_sName()).append("\t")
                                .append("教师为：").append(course.getM_sTeacher()).append("\t")
                                .append("编号为：").append(course.getM_iNum())
                                .append("上课地点为：").append(course.getM_sConstruction())
                                .append(course.getM_iFloor()).append("层").append(course.getM_iRoom()).append("室")
                                .append("\n");
                    //将查询得到的结果显示在文本框中
                    ResOfSearch.setText(texts.toString());
                } else
                    ResOfSearch.setText("查找失败，请重新输入");
            }
        } catch (Exception e) {
            System.out.println("用户输入异常");
            SearchResult_Name.setText("右侧输入框中应该输入数字哦~");
        }
        return primaryResults;
    }

    /**
     * 点击此按钮选定课程
     */
    private void Assure1ButtonClicked() {
        //用来存储课程列表的空表
        ArrayList<Course> tool = new ArrayList<>();
        //实例化数据库
        CourseDatabase courseDatabase = new CourseDatabase();
        //从数据库中得到课程列表
        courseDatabase.find(tool);
        //实例化查找类型对象
        Search search = new Search();
        //标记查找是否成功
        boolean mark = false;
        try {
            //存储精确查询结果
            int fin = 0;

            if (Search1ButtonClicked() == null) {
                search.QuickSort(tool, 0, tool.size() - 1);
                //模糊查找失败或未进行模糊查找，在所有课程列表中查询
                int Num1 = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), tool);
                if (Num1 != tool.size()) {
                    mark = true;
                    fin = Num1;
                }
            }
            else {
                search.QuickSort(Search1ButtonClicked(), 0, Search2ButtonClicked().size() - 1);
                //查找成功后在模糊查找得到的列表中精确查找
                int Num2 = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), Search1ButtonClicked());
                if (Num2 != Search1ButtonClicked().size()) {
                    mark = true;
                    fin = Num2;
                }
            }

            if (mark) {
                //展示查找到的课程名称
                SearchResult_Name.setText(tool.get(fin).getM_sName() + " 编号为：" + tool.get(fin).getM_iNum());
                //将课程添加到课表
                SearchResult_Boolean.setText(this.studentAccount.registerCourse(tool.get(fin)));
            }
            else
                SearchResult_Boolean.setText("查找失败……检查一下操作吧！");

        } catch (Exception e) {
            System.out.println("用户输入异常");
            SearchResult_Name.setText("右侧输入框中应该输入数字哦~");
        }
    }

    private ArrayList<Course> Search2ButtonClicked() {
        //实例化模糊查找对象
        FuzzySearch fuzzySearch = new FuzzySearch();
        //建立一个空表用于存储模糊查找结果
        ArrayList<Course> primaryResults = new ArrayList<>();
        //查询结果的list
        StringBuilder texts = new StringBuilder();
        //实例化查找类
        Search search = new Search();

        //若精确查找框内有内容，优先查找此部分
        try {
            if (this.ToBeBinarySearched.getText() != null) {
                int Num = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), this.courses);
                Course course_ex = this.courses.get(Num);
                texts.append(course_ex.getM_sName()).append("\t")
                        .append("教师为：").append(course_ex.getM_sTeacher()).append("\t")
                        .append("编号为：").append(course_ex.getM_iNum()).append("\t")
                        .append("上课地点为：").append(course_ex.getM_sConstruction())
                        .append(course_ex.getM_iFloor()).append("层")
                        .append(course_ex.getM_iRoom()).append("室").append("\t")
                        .append("课程群号：").append(course_ex.getM_sCurGroup()).append("\n")
                        .append("考试时间：").append(course_ex.getM_cExamTime())
                        .append("考试地点：").append(course_ex.getM_cExamConstruction())
                        .append(course_ex.getM_iExamFloor()).append("层")
                        .append(course_ex.getM_iExamRoom()).append("室").append("\t");
                this.ResOfSearch.setText(texts.toString());
                //若精确查找框内不存在内容，则进行模糊查找，否则不进行模糊查找
            } else if (this.ToBeFuzzySearched.getText() != null) {
                //将模糊查找结果存入 list
                primaryResults = fuzzySearch.get_FS_result(ToBeFuzzySearched.getText(), this.courses);
                if (primaryResults != null) {
                    for (Course course : primaryResults)
                        texts.append(course.getM_sName()).append("\t")
                                .append("教师为：").append(course.getM_sTeacher()).append("\t")
                                .append("编号为：").append(course.getM_iNum())
                                .append("上课地点为：").append(course.getM_sConstruction())
                                .append(course.getM_iFloor()).append("层").append(course.getM_iRoom()).append("室")
                                .append("\n");
                    //将查询得到的结果显示在文本框中
                    ResOfSearch.setText(texts.toString());
                } else
                    ResOfSearch.setText("查找失败，请重新输入");
            }
        } catch (Exception e) {
            System.out.println("用户输入异常");
            SearchResult_Name.setText("右侧输入框中应该输入数字哦~");
        }
        return primaryResults;
    }

    private void Assure2ButtonClicked() {
        //实例化查找类型对象
        Search search = new Search();
        boolean mark = false;
        try {
            //存储精确查询结果
            int fin = 0;

            if (Search2ButtonClicked() == null) {
                //模糊查找失败或未进行模糊查找，在所有课程列表中查询
                int Num1 = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), this.courses);
                if (Num1 != this.courses.size()) {
                    mark = true;
                    fin = Num1;
                }
            }
            else {
                //查找成功后在模糊查找得到的列表中精确查找
                int Num2 = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), Search2ButtonClicked());
                if (Num2 != Search2ButtonClicked().size()) {
                    mark = true;
                    fin = Num2;
                }
            }

            if (mark) {
                //展示查找到的课程名称
                SearchResult_Name.setText(this.courses.get(fin).getM_sName() + " 编号为：" + this.courses.get(fin).getM_iNum());
                //根据账号类型将课程从课表中删除
                this.studentAccount.decCourse(this.courses.get(fin));
                SearchResult_Boolean.setText("查找并删除成功！好耶！");
            }
            else
                SearchResult_Boolean.setText("查找失败……检查一下操作吧！");

        } catch (Exception e) {
            System.out.println("用户输入异常");
            SearchResult_Name.setText("右侧输入框中应该输入数字哦~");
        }
    }

    private void backToMainClicked() {
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }
}