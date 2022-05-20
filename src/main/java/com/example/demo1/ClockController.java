package com.example.demo1;

import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class ClockController {
    //控制当前Controller的场景
    private final Stage thisStage;

    //允许我们访问HelloController的方法们
    private final MainViewPort_Controller controller;

    //建立闹钟的实例化
    SystemTime systemTime;

    @FXML
    public Button Pause;
    @FXML
    public Button SetClock;
    @FXML
    public Button SetSpeed;
    @FXML
    public Button BackToMain;

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
    public RadioButton One;
    @FXML
    public RadioButton Hundred;
    @FXML
    public RadioButton SixHundred;
    @FXML
    public RadioButton Thousand;

    public ClockController(MainViewPort_Controller mainViewPortController) {
        //收到了hello-view.fxml的Controller
        this.controller = mainViewPortController;

        //创建新场景
        this.thisStage = new Stage();

        //获取系统时间
//        this.systemTime = new SystemTime();
//        this.systemTime.start();

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

    private void initialize() {
        this.Pause.setOnAction(event -> ClockPause());
        this.SetClock.setOnAction(event -> SetOnClock());
        this.SetSpeed.setOnAction(event -> SetFastSpeed());
        this.BackToMain.setOnAction(event -> BackToMainMenu());
    }


    public void showStage() {
        thisStage.show();
    }

    private void ClockPause() {
        this.systemTime.stopTime();
    }

    private void SetOnClock() {
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

        this.systemTime.setClock(time, this.NameOfClock.getText(), type[0], null);
//        this.systemTime.setClock(time, this.NameOfClock.getText(), type[0]);
    }

    private void SetFastSpeed() {
        toggleGroup1 = new ToggleGroup();
        final int[] Speed = new int[1];

        toggleGroup1.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            RadioButton r = (RadioButton)t1;
            switch (r.getText()) {
                case "1" -> Speed[0] = 1;
                case "100" -> Speed[0] = 100;
                case "600" -> Speed[0] = 600;
                case "1000" -> Speed[0] = 1000;
            }
        });

        System.out.println(Speed[0]);
        this.systemTime.setSpeed(Speed[0]);
//        this.systemTime.start();
    }

    private void BackToMainMenu() {
        //将第二个界面展示出来
        this.controller.showStage();

        //停止时钟
        this.systemTime.stopTime();

        //本页面隐藏
        this.thisStage.hide();
    }
}
