package com.example.demo1;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Util.Authority;
import com.example.demo1.Code.clock.ClockOperation;
import com.example.demo1.Code.entity.account.Account;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.systemtime.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.io.File;
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
    public Button Tea_Cour;
    @FXML
    public Button Cour_Tab;
    @FXML
    public Button Upload;
    @FXML
    public Button Clock;
    @FXML
    public Button Manager;
    @FXML
    public Button Manager_Act;
    @FXML
    public Button Exit;
    @FXML
    public Button StartClock;
    @FXML
    public Label buttonStatusText;
    @FXML
    public RadioButton One;
    @FXML
    public RadioButton Sixty;
    @FXML
    public RadioButton ThreeHundredSixty;
    @FXML
    public RadioButton SixHundred;
    @FXML
    public Button SetSpeed;

    SystemTime systemTime;

    ClockOperation clockOperation;

    public MainViewPort_Controller(HelloController controller) {

        //收到了hello-view.fxml的Controller
        this.helloController = controller;

        //实例化系统时间以便系统时间推进
        systemTime = new SystemTime();

        //启动系统时间
        systemTime.SystemTimeStart();

        SystemTime.setSpeed(1);

        //创建新场景
        thisStage = new Stage();

        //加载FXML文件
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainViewPort.fxml"));

            //将本文件设置为Controller
            loader.setController(this);

            //加载场景
            thisStage.setScene(new Scene(loader.load(), 400, 400));

            //搭建窗口
            thisStage.setTitle("欢迎来到主界面!");
            final String TAG = "Main";
            // (可选) 设置日志输出级别, 默认为 INFO 级别
            LogFile.setLogOutLevel(LogFile.Level.DEBUG);
            // (可选) 设置日志输出文件(追加到文件尾部)
            try {
                LogFile.setLogOutFile(new File("MyLog.log"));
            } catch (IOException e) {
                LogFile.error(TAG, "The error log.无法打开日志输出文件");
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        thisStage.show();
    }

    /**
     * 获取账号信息
     * @return 账号信息
     */
    public Account getAccount() {
        return this.helloController.getAccount();
    }

    @FXML
    private void initialize() {
        //处理各种按钮的事件
        Navigate.setOnAction(event -> handleNavButtonAction());
        Stu_Cour.setOnAction(event -> handleStuCourButtonAction());
        Stu_Act.setOnAction(event -> handleStuActButtonAction());
        Tea_Cour.setOnAction(event -> handleTeaCourButtonAction());
        Cour_Tab.setOnAction(event -> handleCourTabButtonAction());
        Upload.setOnAction(event -> handleUploadButtonAction());
        Clock.setOnAction(event -> handleClockButtonAction());
        Manager.setOnAction(event -> handleManagerButtonAction());
        SetSpeed.setOnAction(event -> handleSpeed());
        Manager_Act.setOnAction(event -> handleManagerAct());
        Exit.setOnAction(event -> handleExit());
        StartClock.setOnAction(event -> StartTheClock());
    }

    /**
     * 跳转到导航界面
     */
    protected void handleNavButtonAction() {
        SystemTime.stopTime();
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户进入导航页面");
        NavController navController = new NavController(this);
        thisStage.hide();
        navController.showStage();
    }

    /**
     * 跳转到学生课程管理界面
     */
    protected void handleStuCourButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Student) {
            buttonStatusText.setText("你不是学生！");
            return;
        }
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户进入学生课程管理");
        SystemTime.stopTime();
        CourseChooseController courseChooseController = new CourseChooseController(this);
        thisStage.hide();
        courseChooseController.showStage();

    }

    /**
     * 跳转到学生活动管理界面
     */
    protected void handleStuActButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Student) {
            buttonStatusText.setText("你不是学生！");
            return;
        }
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户进入学生活动管理");
        SystemTime.stopTime();
        ActivityController activityController = new ActivityController(this);
        thisStage.hide();
        activityController.showStage();
    }

    /**
     * 跳转教师课程管理界面
     */
    protected void handleTeaCourButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Teacher) {
            buttonStatusText.setText("你不是教师！");
            return;
        }
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户进入教师课程管理");
        SystemTime.stopTime();
        TeaCourController teaCourController = new TeaCourController(this);
        thisStage.hide();
        teaCourController.showStage();
    }

    /**
     * 跳转到课程表界面
     */
    protected void handleCourTabButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Student) {
            buttonStatusText.setText("只有学生可以查看课程表！");
            return;
        }
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户进入课程表界面");
        SystemTime.stopTime();
        CourseTableController courseTableController = new CourseTableController(this);
        thisStage.hide();
        courseTableController.showStage();
    }

    /**
     * 跳转到上传作业与资料界面
     */
    protected void handleUploadButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Student) {
            buttonStatusText.setText("只有学生可以上传作业！");
            return;
        }
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户进入学生作业上传界面");
        SystemTime.stopTime();
        HomeAndMaterialController homeAndMaterialController = new HomeAndMaterialController(this);
        this.thisStage.hide();
        homeAndMaterialController.showStage();
    }

    /**
     * 跳转到闹钟设置界面
     */
    protected void handleClockButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Student) {
            buttonStatusText.setText("只有学生可以设置闹钟！");
            return;
        }
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户进入闹钟设置界面");
        SystemTime.stopTime();
        ClockController clockController = new ClockController(this);
        thisStage.hide();
        clockController.showStage();
    }

    /**
     * 跳转到管理员管理课程界面
     */
    protected void handleManagerButtonAction() {
        if (this.helloController.getAccount().getAuthority() != Authority.Manager) {
            buttonStatusText.setText("你不是管理员！");
            return;
        }
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户进入管理员课程管理界面");
        SystemTime.stopTime();
        ManagerViewPortController managerViewPortController
                = new ManagerViewPortController(this);
        thisStage.hide();
        managerViewPortController.showStage();
    }

    /**
     * 跳转到管理员管理活动界面
     */
    protected void handleManagerAct() {
        if (this.helloController.getAccount().getAuthority() != Authority.Manager) {
            buttonStatusText.setText("你不是管理员！");
            return;
        }
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户进入管理员活动管理界面");
        SystemTime.stopTime();
        ManagerActController controller = new ManagerActController(this);
        thisStage.hide();
        controller.showStage();
    }

    /**
     * 退出系统
     */
    protected void handleExit() {
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户退出程序");
        System.exit(0);
    }

    /**
     * 设置快进速度
     */
    protected void handleSpeed() {
        int speed = 360;
        if (this.One.isSelected()) {
            speed = 1;
        } else if (this.Sixty.isSelected()) {
            speed = 60;
        } else if (this.ThreeHundredSixty.isSelected()) {
            speed = 360;
        } else if (this.SixHundred.isSelected()) {
            speed = 600;
        }
        LogFile.info("User" + this.helloController.getAccount().getID(),"用户设置系统快进时间");
        SystemTime.setSpeed(speed);
    }

    /**
     * 启动闹钟
     */
    protected void StartTheClock() {
        if (this.helloController.getAccount().getAuthority() == Authority.Student) {
            StudentAccount studentAccount = new StudentAccount(this.helloController.getAccount());
            clockOperation = new ClockOperation(studentAccount);
        } else
            this.buttonStatusText.setText("你不是学生！");
    }
}
