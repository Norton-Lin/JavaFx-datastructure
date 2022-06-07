package com.example.demo1;

import com.example.demo1.Code.entity.account.StudentAccount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.demo1.Code.entity.*;
import javafx.stage.Stage;

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
    public TextField Course;
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
            thisStage.setScene(new Scene(loader.load(), 600, 400));
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
        int traffic = 0;

        if (Bicycle.isSelected()) {
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
        if (end.isEmpty()) {
            if (!this.Course.getText().isEmpty()) {
                //课程信息
                String course = this.Course.getText();
                try {
                    int Num = Integer.parseInt(course);

                } catch (NumberFormatException e) {
                    String course_info = this.Course.getText();
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    ArrayList<Course> results = fuzzySearch.get_FS_result(course_info, this.courses);
                    end = results.get(0).getM_sConstruction().get_con_name();
                }
            } else if (!this.Week.getText().isEmpty() && !this.Hour.getText().isEmpty() && !this.Minute.getText().isEmpty()) {

            } else {
                ResOfNav.setText("输入异常，请重新输入");
            }
        }
        ResOfNav.setText(navigate.toNavigate(traffic, start, end, 0).toString());
    }

    protected void handleBackAction() {

        //将第二个界面展示出来
        mainViewPort_controller.showStage();

        //本页面隐藏
        thisStage.hide();
    }
}
