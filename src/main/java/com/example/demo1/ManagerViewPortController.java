package com.example.demo1;

import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Construction;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.Navigate;
import com.example.demo1.Code.entity.Search;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class ManagerViewPortController {
    //控制当前Controller的场景
    private final Stage thisStage;

    //允许我们访问HelloController的方法们
    private final MainViewPort_Controller controller;

    //存储所有课程的ArrayList
    private ArrayList<Course> courses;

    //存储查找到的课程
    Course course;

    @FXML
    public TextField CourseNum;
    @FXML
    public Button SearchAll;
    @FXML
    public TextField Month;
    @FXML
    public TextField Week;
    @FXML
    public TextField Day;
    @FXML
    public TextField StartHour;
    @FXML
    public TextField StartMinute;
    @FXML
    public TextField EndHour;
    @FXML
    public TextField EndMinute;
    @FXML
    public Button CourTime;
    @FXML
    public Button ExamTime;
    @FXML
    public TextField Building;
    @FXML
    public TextField Floor;
    @FXML
    public TextField Room;
    @FXML
    public Button CourCon;
    @FXML
    public Button ExamCon;
    @FXML
    public Button BackToMain;

    public ManagerViewPortController(MainViewPort_Controller controller) {

        //收到了hello-view.fxml的Controller
        this.controller = controller;

        //创建新场景
        thisStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerViewPort.fxml"));

            //将本文件设置为Controller
            loader.setController(this);

            //加载场景
            thisStage.setScene(new Scene(loader.load(), 600, 400));

            //搭建窗口
            thisStage.setTitle("欢迎来到管理员界面!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        thisStage.show();
    }

    @FXML
    private void initialize() {
        this.BackToMain.setOnAction(event -> BackToMainMenu());
        this.SearchAll.setOnAction(event -> SearchForCourse());
        this.CourTime.setOnAction(event -> SetTime(true));
        this.ExamTime.setOnAction(event -> SetTime(false));
        this.CourCon.setOnAction(event -> SetConstruction(true));
        this.ExamCon.setOnAction(event -> SetConstruction(false));
    }

    /**
     * 回到主界面
     */
    protected void BackToMainMenu() {
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    /**
     * 根据管理员输入的课程编号查找课程
     */
    protected void SearchForCourse() {
        //实例化数据库
        CourseDatabase courseDatabase = new CourseDatabase();

        //获取所有课程的列表
        courseDatabase.find(this.courses);

        //获取用户输入的课程编号
        int Num1 = Integer.parseInt(this.CourseNum.getText());

        //实例化查找对象
        Search search = new Search();

        //根据用户输入的编号进行查找
        int Num2 = search.BinaryCourseSearch(Num1, this.courses);

        //分配课程
        this.course = this.courses.get(Num2);
    }

    /**
     * 设置/修改课程上课时间
     */
    protected void SetTime(boolean mark) {
        //从用户输入中读取时间信息
        int month = Integer.parseInt(this.Month.getText());
        int week = Integer.parseInt(this.Week.getText());
        int date = Integer.parseInt(this.Day.getText());
        int sth = Integer.parseInt(this.StartHour.getText());
        int stm = Integer.parseInt(this.StartMinute.getText());
        int enh = Integer.parseInt(this.EndHour.getText());
        int enm = Integer.parseInt(this.EndMinute.getText());

        //构建时间类型对象
        Time time = new Time(sth, stm, enh, enm, date, month, week);

        if (mark)
            this.course.setM_tTime(time);
        else
            this.course.setM_cExamTime(time);
    }

    protected void SetConstruction(boolean mark) {
        //建筑物查找的功能内置在Navigate中
        Navigate navigate = new Navigate();

        //根据建筑物名称找出建筑物
        Construction temp =navigate.getConstruction(this.Building.getText());

        //根据按钮不同设置上课地点或考试地点
        if (mark) {
            this.course.setM_sConstruction(temp);
            this.course.setM_iFloor(Integer.parseInt(this.Floor.getText()));
            this.course.setM_iRoom(Integer.parseInt(this.Room.getText()));
        }
        else {
            this.course.setM_cExamConstruction(temp);
            this.course.setM_iExamFloor(Integer.parseInt(this.Floor.getText()));
            this.course.setM_iExamRoom(Integer.parseInt(this.Room.getText()));
        }
    }
}
