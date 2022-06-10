package com.example.demo1;

import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.systemtime.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.demo1.Code.entity.*;
import javafx.stage.Stage;
import com.example.demo1.Code.entity.Course;

import java.io.IOException;
import java.util.ArrayList;

public class NavController {
    //从主界面继承而来的信息
    private final MainViewPort_Controller mainViewPort_controller;

    //本界面
    private final Stage thisStage;

    //本页面中的各种元素
    @FXML
    public TextField StartPoint;
    @FXML
    public TextField EndPoint;
    @FXML
    public TextField Course_Name;
    @FXML
    public TextField Week;
    @FXML
    public TextField Hour;
    @FXML
    public TextField Minute;
    @FXML
    public RadioButton Walk = new RadioButton();
    @FXML
    public RadioButton Bicycle = new RadioButton();
    @FXML
    public RadioButton Electric = new RadioButton();
    @FXML
    public RadioButton Car = new RadioButton();
    @FXML
    public Button submitButton;
    @FXML
    public TextArea ResOfNav;
    @FXML
    public Button backToMain;

    //某账户全部课程信息
    ArrayList<Course> courses;

    //学生账户
    StudentAccount studentAccount;

    public NavController(MainViewPort_Controller mainController) {
        //将主界面的信息继承来
        this.mainViewPort_controller = mainController;

        studentAccount = new StudentAccount(this.mainViewPort_controller.getAccount());

        //本界面的展开
        thisStage = new Stage();

        //加载FXML文件
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Nav.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 1500, 400));
            thisStage.setTitle("欢迎使用导航系统~");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.courses = this.studentAccount.getCourse();
    }

    /**
     * 跳出页面
     */
    public void showStage() {
        thisStage.show();
    }

    @FXML
    private void initialize() {
        submitButton.setOnAction(event -> handleSubmitButtonAction());
        backToMain.setOnAction(event -> handleBackAction());
    }

    protected void handleSubmitButtonAction() {

        //默认交通方式为步行
        int traffic = -1;

        if (Walk.isSelected()) {
            traffic = 0;
        } else if (Bicycle.isSelected()) {
            traffic = 1;
        } else if (Electric.isSelected()) {
            traffic = 2;
        } else if (Car.isSelected()) {
            traffic = 3;
        }

        //起点信息
        String start = this.StartPoint.getText();
        //终点信息
        String end = this.EndPoint.getText();
        //实例化导航类
        Navigate navigate = new Navigate();
        String AppendingInfo = "";
        if (end.isEmpty()) {
            if (!this.Course_Name.getText().isEmpty()) {
                //课程信息
                String course = this.Course_Name.getText();
                try {
                    //根据课程编号精确查找
                    int Num = Integer.parseInt(course);
                    Search search = new Search();
                    Course ACourse;
                    int tool = search.BinaryCourseSearch(Num, this.courses);
                    if (tool != this.courses.size()) {
                        ACourse = this.courses.get(tool);
                        end = ACourse.getM_sConstruction().get_con_name();
                        AppendingInfo = "您输入的起点到所选课程所在地的最佳路径为" + "\n";
                        ResOfNav.setText(AppendingInfo + navigate.toNavigate(traffic, start, end, 0).toString());
                    }
                    else
                        ResOfNav.setText("查找失败");
                } catch (NumberFormatException e) {
                    //根据课程名称模糊查找
                    String course_info = this.Course_Name.getText();
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    ArrayList<Course> results = fuzzySearch.get_FS_result(course_info, this.courses);
                    if (results != null) {
                        end = results.get(0).getM_sConstruction().get_con_name();
                        AppendingInfo = "您输入的起点到所选课程所在地的最佳路径为" + "\n";
                        ResOfNav.setText(AppendingInfo + navigate.toNavigate(traffic, start, end, 0).toString());
                    } else {
                        ResOfNav.setText("查找失败");
                    }
                }
            } else if (!this.Week.getText().isEmpty() && !this.Hour.getText().isEmpty() && !this.Minute.getText().isEmpty()) {
                //存储距离输入时间最短的课程
                Course min = new Course();
                int week = Integer.parseInt(this.Week.getText());
                int hour = Integer.parseInt(this.Hour.getText());
                int minute = Integer.parseInt(this.Minute.getText());
                Time linshi = new Time(week, 0, 0);
                min.setM_tTime(linshi);
                //遍历课程列表，寻找与输入时间差距最小的课程
                for (Course temp : this.courses) {
                    //当前的课程如果与输入的星期不同则舍弃
                    if (temp.getM_tTime().getWeek() == week) {
                        int hour0 = temp.getM_tTime().getStartHour();
                        int minute0 = temp.getM_tTime().getStartMinute();
                        //如果当前课程上课时间在输入时间前则舍弃
                        if (hour0 > hour) {
                            if ((hour0 - hour) < Math.abs(min.getM_tTime().getStartHour() - hour))
                                min = temp;
                            else if ((hour0 - hour) == Math.abs(min.getM_tTime().getStartHour() - hour))
                                if ((minute0 - minute) <= Math.abs(min.getM_tTime().getStartMinute() - minute))
                                    min = temp;
                        }
                    }
                }
                if (this.courses.contains(min)) {
                    end = min.getM_sConstruction().get_con_name();
                    AppendingInfo = "您输入的起点到距离您输入时间最近的课程所在地点的最佳路径为：" + "\n";
                    ResOfNav.setText(AppendingInfo + navigate.toNavigate(traffic, start, end, 0).toString());
                } else
                    ResOfNav.setText("您输入的时间并不符合您的任何一门课程~无法导航");
            } else {
                ResOfNav.setText("输入异常，请重新输入");
            }
        } else
            ResOfNav.setText(AppendingInfo + navigate.toNavigate(traffic, start, end, 0).toString());
    }

    protected void handleBackAction() {
        SystemTime.restartTime();

        //将第二个界面展示出来
        mainViewPort_controller.showStage();

        //本页面隐藏
        thisStage.hide();
    }
}
