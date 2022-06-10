package com.example.demo1;

import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.systemtime.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class ClockController {
    //控制当前Controller的场景
    private final Stage thisStage;

    //允许我们访问HelloController的方法们
    private final MainViewPort_Controller controller;

    //建立闹钟的实例化
    SystemTime systemTime;

    StudentAccount studentAccount;

    @FXML
    public Button pause;
    @FXML
    public Button setClock;
    @FXML
    public Button setSpeed;
    @FXML
    public Button backToMain;

    @FXML
    public TextField NameOfClock;
    @FXML
    public TextField Week;
    @FXML
    public TextField Month;
    @FXML
    public TextField Date;
    @FXML
    public TextField Minute1;
    @FXML
    public TextField Hour1;

    @FXML
    public ToggleGroup toggleGroup1;
    @FXML
    public ToggleGroup toggleGroup2;
    @FXML
    public RadioButton Once;
    @FXML
    public RadioButton PerDay;
    @FXML
    public RadioButton PerWeek;
    @FXML
    public RadioButton One = new RadioButton();
    @FXML
    public RadioButton Hundred = new RadioButton();
    @FXML
    public RadioButton SixHundred = new RadioButton();
    @FXML
    public RadioButton Thousand = new RadioButton();

    public ClockController(MainViewPort_Controller mainViewPortController) {
        //收到了hello-view.fxml的Controller
        this.controller = mainViewPortController;

        //创建新场景
        this.thisStage = new Stage();

        //创建按钮

        //创建当前闹钟的学生账户
        this.studentAccount = new StudentAccount(this.controller.getAccount());
        //获取系统时间
//        this.systemTime = new SystemTime(this.studentAccount);
        this.systemTime.SystemTimeStart();
        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Clock.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 550, 150));
            thisStage.setTitle("欢迎来到闹钟界面~");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        this.pause.setOnAction(event -> ClockPause());
        this.setClock.setOnAction(event -> SetOnClock());
        this.setSpeed.setOnAction(event -> SetFastSpeed());
        this.backToMain.setOnAction(event -> BackToMainMenu());
    }


    public void showStage() {
        thisStage.show();
    }

    protected void ClockPause() {
        this.systemTime.stopTime();
    }

    protected void SetOnClock() {
        Time time = new Time(Integer.parseInt(Hour1.getText()), Integer.parseInt(Minute1.getText()), 0, 0,
                Integer.parseInt(Date.getText()), Integer.parseInt(Month.getText()), Integer.parseInt(Week.getText()));

        toggleGroup2 = new ToggleGroup();
        final int[] type = new int[1];

        toggleGroup2.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton r = (RadioButton)t1;
            switch (r.getText()) {
                case "只响一次" -> type[0] = 0;
                case "一天一次" -> type[0] = 1;
                case "一周一次" -> type[0] = 7;
            }
        });

 //       this.systemTime.setClock(time, this.NameOfClock.getText(), type[0], this.studentAccount.getM_CaEventClock());
//        this.systemTime.setClock(time, this.NameOfClock.getText(), type[0]);
    }

    protected void SetFastSpeed() {
        toggleGroup1 = new ToggleGroup();
        int speed = 0;

        if (One.isSelected()) {
            speed = 1;
        } else if (Hundred.isSelected()) {
            speed = 100;
        } else if (SixHundred.isSelected()) {
            speed = 600;
        } else if (Thousand.isSelected()) {
            speed = 1000;
        }

        this.systemTime.setSpeed(speed);
    }

    protected void BackToMainMenu() {
        //将第二个界面展示出来
        this.controller.showStage();

        //停止时钟
        this.systemTime.stopTime();

        //本页面隐藏
        this.thisStage.hide();
    }
}
