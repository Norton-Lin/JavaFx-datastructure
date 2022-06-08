package com.example.demo1;

import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.FuzzySearch;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.TeacherAccount;
import com.example.demo1.Code.systemtime.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class TeaCourController {
    private final Stage thisStage;
    private final MainViewPort_Controller controller;

    //存储教师自己课程的课程列表
    ArrayList<Course> courses;

    //存储搜索结果的课程
    Course result;

    //创建数据库
    CourseDatabase database;

    TeacherAccount teacherAccount;

    @FXML
    public TextField Course_Name;
    @FXML
    public Label Info;
    @FXML
    public Button Search;
    @FXML
    public Button Assure;
    @FXML
    public Button BackToMain;

    public TeaCourController(MainViewPort_Controller controller) {
        //得到新Controller
        this.controller = controller;

        //获得教师账户的信息
        teacherAccount = new TeacherAccount(this.controller.getAccount());

        //创建新场景
        thisStage = new Stage();

        //获取教师名下所有课程
        this.courses = this.teacherAccount.getCourse();

        database = new CourseDatabase();

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TeaCour.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 500, 200));
            thisStage.setTitle("欢迎来到教师课程进度管理界面~");
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
        this.Search.setOnAction(event -> handleSearch());
        this.Assure.setOnAction(event -> handleAdd());
        this.BackToMain.setOnAction(event -> handleBack());
    }

    protected void handleBack() {
        SystemTime.restartTime();
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    protected void handleSearch() {
        if (!this.Course_Name.getText().isEmpty()) {
            try {
                int Num0 = Integer.parseInt(this.Course_Name.getText());
                Search search = new Search();
                int Num1 = search.BinaryCourseSearch(Num0, this.courses);
                if (Num1 != this.courses.size()) {
                    this.result = this.courses.get(Num1);
                    this.Info.setText("您要操作的课程是否是：" + this.result.toString() + "\n" + "当前进度为" +
                            this.result.getM_iCurrentClass() + "/" + this.result.getM_iTotalClass());
                } else {
                    this.Info.setText("查找失败！");
                }
            } catch (NumberFormatException e) {
                String name = this.Course_Name.getText();
                FuzzySearch fuzzySearch = new FuzzySearch();
                ArrayList<Course> temp = fuzzySearch.get_FS_result(name, this.courses);
                if (temp != null) {
                    this.result = temp.get(0);
                    this.Info.setText("您要操作的课程是否是：" + this.result.toString() + "\n" +  "当前进度为" +
                            this.result.getM_iCurrentClass() + "/" + this.result.getM_iTotalClass());
                } else {
                    this.Info.setText("查找失败");
                }
            }
        } else {
            StringBuilder text = new StringBuilder();
            if (this.courses != null) {
                text.append("您名下的课程有：\n");
                for (Course course : this.courses) {
                    text.append(course.toString()).append("\n");
                }
                text.append("请选择某一门精确的课程");
            } else {
                text.append("您名下没有课程！");
            }
            this.Info.setText(text.toString());
        }
    }

    protected void handleAdd() {
        if (result != null) {
            int Num = result.getM_iCurrentClass();
            if ((Num + 1) <= result.getM_iTotalClass()) {
                result.setM_iCurrentClass(Num + 1);
                database.update(this.result);
                this.Info.setText("进度已成功加一！");
            } else {
                this.Info.setText("进度已达最大值！");
            }
        } else {
            this.Info.setText("未查找到课程，无法操作！");
        }
    }
}
