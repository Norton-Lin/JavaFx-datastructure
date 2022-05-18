package com.example.demo1;

import com.example.demo1.Code.Util.Authority;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.account.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MainViewPort_Controller {

    //控制当前Controller的场景
    private final Stage thisStage;

    //允许我们访问HelloController的方法们
    private final HelloController helloController;

    //添加MainViewPort.fxml中的参数们
    @FXML
    public Button Navigate;
    @FXML
    public Button Stu_Cour;
    @FXML
    public Button Stu_Act;
    @FXML
    public Button Tea_Act;
    @FXML
    public Button Tea_data;
    @FXML
    public Button Cour_Tab;
    @FXML
    public Button Upload;
    @FXML
    public Button Clock;
    @FXML
    public Button Manager;
    @FXML
    public Label buttonStatusText;

    public MainViewPort_Controller(HelloController controller) {

        //收到了hello-view.fxml的Controller
        this.helloController = controller;

        //创建新场景
        thisStage = new Stage();

        //加载FXML文件
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainViewPort.fxml"));

            //将本文件设置为Controller
            loader.setController(this);

            //加载场景
            thisStage.setScene(new Scene(loader.load(), 300, 200));

            //搭建窗口
            thisStage.setTitle("欢迎来到主界面!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        thisStage.show();
    }

    public Account getAccount() {
        return this.helloController.getAccount();
    }

    @FXML
    private void initialize() {
        //处理各种按钮的事件
        Navigate.setOnAction(event -> handleNavButtonAction());
        Stu_Cour.setOnAction(event -> handleStuCourButtonAction());
        Stu_Act.setOnAction(event -> handleStuActButtonAction());
        Tea_Act.setOnAction(event -> handleTeaActButtonAction());
        Cour_Tab.setOnAction(event -> handleCourTabButtonAction());
        Upload.setOnAction(event -> handleUploadButtonAction());
        Clock.setOnAction(event -> handleClockButtonAction());
        Manager.setOnAction(event -> handleManagerButtonAction());
        Tea_data.setOnAction(event -> handleTeaDataButtonAction());
    }

    protected void handleNavButtonAction() {
        NavController navController = new NavController(this);
        thisStage.hide();
        navController.showStage();
    }

    protected void handleStuCourButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Student) {
            buttonStatusText.setText("你不是学生！");
            return;
        }
        CourseChooseController courseChooseController = new CourseChooseController(this);
        thisStage.hide();
        courseChooseController.showStage();

    }

    protected void handleStuActButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Student) {
            buttonStatusText.setText("你不是学生！");
            return;
        }
        ActivityChooseController activityChooseController = new ActivityChooseController(this);
        thisStage.hide();
        activityChooseController.showStage();
    }

    protected void handleTeaActButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Teacher) {
            buttonStatusText.setText("你不是教师！");
            return;
        }
        ActivityChooseController activityChooseController = new ActivityChooseController(this);
        thisStage.hide();
        activityChooseController.showStage();
    }

    protected void handleTeaDataButtonAction() {

    }

    protected void handleCourTabButtonAction() {

    }

    protected void handleUploadButtonAction() {

    }

    protected void handleClockButtonAction() {
        ClockController clockController = new ClockController(this);
    }

    protected void handleManagerButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Manager)
            buttonStatusText.setText("你不是管理员！");
    }
}
