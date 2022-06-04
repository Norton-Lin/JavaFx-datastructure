package com.example.demo1;

import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.Construction;
import com.example.demo1.Code.entity.account.StudentAccount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ActivityController {

    private final Stage thisStage;
    private final MainViewPort_Controller controller;

    StudentAccount studentAccount;

    @FXML
    public Label ErrorInfo;
    @FXML
    public TextField Name;
    @FXML
    public TextField Month;
    @FXML
    public TextField Date;
    @FXML
    public TextField Week;
    @FXML
    public TextField STH;
    @FXML
    public TextField STM;
    @FXML
    public TextField ENH;
    @FXML
    public TextField ENM;
    @FXML
    public RadioButton Solo = new RadioButton();
    @FXML
    public RadioButton Group = new RadioButton();
    @FXML
    public RadioButton ShaHe = new RadioButton();
    @FXML
    public RadioButton XiTuCheng = new RadioButton();
    @FXML
    public TextField Building;
    @FXML
    public TextField Floor;
    @FXML
    public TextField Room;
    @FXML
    public Button Assure;
    @FXML
    public Button BackToMain;

    public ActivityController(MainViewPort_Controller mainViewPort_controller) {
        //得到新Controller
        this.controller = mainViewPort_controller;

        //创建新场景
        thisStage = new Stage();

        this.studentAccount = new StudentAccount(this.controller.getAccount());

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Activity.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 450, 350));
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
        this.Assure.setOnAction(event -> AssureClicked());
        //点击回到主菜单后的反应
        this.BackToMain.setOnAction(event -> BackToMainClicked());
    }

    private void BackToMainClicked() {
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    private void AssureClicked() {
        try {
            String Name = this.Name.getText();

            int Month = Integer.parseInt(this.Month.getText());
            int Date = Integer.parseInt(this.Date.getText());
            int Week = Integer.parseInt(this.Week.getText());
            int STH = Integer.parseInt(this.STH.getText());
            int STM = Integer.parseInt(this.STM.getText());
            int ENH = Integer.parseInt(this.ENH.getText());
            int ENM = Integer.parseInt(this.ENM.getText());
            if (Month <= 0 || Month > 12 || Date <= 0 || Date > 31 || Week <= 0 || Week > 7)
                throw new Exception();
            Time time = new Time(STH, STM, ENH, ENM, Date, Month, Week);

            Property type = Property.SELF;
            int Num = -1000;
            if (this.Solo.isSelected()) {
                Num = 2000;
            } else if (this.Group.isSelected()) {
                type = Property.GROUP;
                Num = 3000;
            }

            int campus = 0;
            if (this.ShaHe.isSelected()) {
                campus = 0;
            } else if (this.XiTuCheng.isSelected()) {
                campus = 1;
            }
            String Building = this.Building.getText();
            Construction construction = new Construction(Building, campus);

            int Floor = Integer.parseInt(this.Floor.getText());
            int Room = Integer.parseInt(this.Room.getText());

            Activity activity = new Activity(Name, time, type, construction, Floor, Room, Num);

            //根据上面的参数构建活动并传入
            this.ErrorInfo.setText(this.studentAccount.registerActivity(activity));
        } catch (Exception e) {
            this.ErrorInfo.setText("输入错误！\n月日周楼层房间都请输入数字！");
        }
    }
}
