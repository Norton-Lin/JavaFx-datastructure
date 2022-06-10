package com.example.demo1;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.clock.ClockOperation;
import com.example.demo1.Code.clock.EventClock;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.systemtime.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ClockController {
    //控制当前Controller的场景
    private final Stage thisStage;

    //允许我们访问HelloController的方法们
    private final MainViewPort_Controller controller;

    //建立闹钟的实例化
    ClockOperation clockOperation;

    StudentAccount studentAccount;

    @FXML
    public Label info;
    @FXML
    public Button setClock;
    @FXML
    public Button deleteClock;
    @FXML
    public Button lookClock;
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
    public TextArea Result;

    @FXML
    public RadioButton Once;
    @FXML
    public RadioButton PerDay;
    @FXML
    public RadioButton PerWeek;

    public ClockController(MainViewPort_Controller mainViewPortController) {
        //收到了hello-view.fxml的Controller
        this.controller = mainViewPortController;

        //创建新场景
        this.thisStage = new Stage();

        //创建按钮

        //创建当前闹钟的学生账户
        this.studentAccount = new StudentAccount(this.controller.getAccount());
        //获取闹钟信息
        this.clockOperation = new ClockOperation(this.studentAccount);

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
        this.setClock.setOnAction(event -> SetOnClock());
        this.lookClock.setOnAction(event -> handleLook());
        this.deleteClock.setOnAction(event -> handleDelete());
        this.backToMain.setOnAction(event -> BackToMainMenu());
    }


    public void showStage() {
        thisStage.show();
    }

    /**
     * 设置闹钟
     */
    protected void SetOnClock() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生设置闹钟");
        int week, month, date, hour, minute;
        Time time = new Time();
        try {
            int type = 0;
            //根据不同的闹钟类型获得不同的参数以构造时间类
            //只响一次的闹钟需要输入全部信息
            if (this.Once.isSelected()) {
                type = 0;
                month = Integer.parseInt(this.Month.getText());
                week = Integer.parseInt(this.Week.getText());
                date = Integer.parseInt(this.Date.getText());
                hour = Integer.parseInt(this.Hour1.getText());
                minute = Integer.parseInt(this.Minute1.getText());
                time.setStartMonth(month);
                time.setStartDate(date);
                time.setWeek(week);
                time.setStartHour(hour);
                time.setStartMinute(minute);
            //每天一次的闹钟只需输入时间和分钟信息
            } else if (this.PerDay.isSelected()) {
                type = 1;
                hour = Integer.parseInt(this.Hour1.getText());
                minute = Integer.parseInt(this.Minute1.getText());
                time.setStartHour(hour);
                time.setStartMinute(minute);
            //每周一次的闹钟需要输入星期、小时和分钟信息
            } else if (this.PerWeek.isSelected()) {
                type = 7;
                week = Integer.parseInt(this.Week.getText());
                hour = Integer.parseInt(this.Hour1.getText());
                minute = Integer.parseInt(this.Minute1.getText());
                time.setWeek(week);
                time.setStartHour(hour);
                time.setStartMinute(minute);
            }

            String Name = this.NameOfClock.getText();

            this.info.setText(clockOperation.setClock(time, Name, type).toString());
            this.studentAccount.getM_CaEventClock().add(new EventClock(time, Name, type));
        } catch (Exception e) {
            this.info.setText("输入有误，请重试");
        }
    }

    protected void BackToMainMenu() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生返回主界面");
        SystemTime.restartTime();
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    protected void handleDelete() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生删除闹钟");
        String Name = this.NameOfClock.getText();
        if (!Name.isEmpty()) {
            this.info.setText(clockOperation.deleteClock(Name).toString());
        } else {
            this.info.setText("请输入要删除的闹钟名！");
        }
    }

    protected void handleLook() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生查看闹钟");
        ArrayList<EventClock> allClock = clockOperation.LookClock();
        StringBuilder text = new StringBuilder();
        for (EventClock tool : allClock) {
            text.append(tool.toString()).append("\n");
        }
        this.Result.setText(text.toString());
    }
}
