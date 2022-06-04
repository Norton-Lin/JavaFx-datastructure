package com.example.demo1;

import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.Construction;
import com.example.demo1.Code.entity.FuzzySearch;
import com.example.demo1.Code.entity.account.StudentAccount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

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
    public TextField ToBe;
    @FXML
    public Button Search;
    @FXML
    public Button Delete;
    @FXML
    public TextArea Info;
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
            thisStage.setScene(new Scene(loader.load(), 1000, 350));
            thisStage.setTitle("欢迎来到活动界面~");
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
        //点击添加活动按钮后的反应
        this.Assure.setOnAction(event -> AssureClicked());
        //点击回到主菜单后的反应
        this.BackToMain.setOnAction(event -> BackToMainClicked());
        //点击查找按钮后的反应
        this.Search.setOnAction(event -> SearchClicked());
        //点击删除按钮后的反应
        this.Delete.setOnAction(event -> DeleteClicked());
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

    /**
     * 查找功能
     */
    private void SearchClicked() {
        //从学生账户中获得所有活动信息
        ArrayList<Activity> activities = this.studentAccount.getActivity();
        //存储要输出在文本框中的信息的字符串
        StringBuilder text = new StringBuilder();

        if (this.ToBe.getText().isEmpty()) {
            if (activities != null)
                for (Activity activity : activities) {
                    Time time = activity.getM_tTime();
                    text.append(activity.getM_sName()).append("\t")
                        .append("时间为：").append(time.getStartMonth()).append("月")
                        .append(time.getStartDate()).append("日").append("星期").append(time.getWeek())
                        .append("\t");

                    if (activity.getM_eProperty() == Property.SELF) {
                        text.append("课程属性：").append("个人活动").append("\t");
                    } else if (activity.getM_eProperty() == Property.GROUP) {
                        text.append("课程属性：").append("集体活动").append("\t");
                    }

                    text.append("活动地点：");

                    if (activity.getM_sConstruction().getCampus() == 0) {
                        text.append("校区为：").append("沙河校区").append("\t");
                    } else if (activity.getM_sConstruction().getCampus() == 1) {
                        text.append("校区为：").append("西土城校区").append("\t");
                    }

                    text.append(activity.getM_sConstruction().get_con_name())
                        .append(activity.getM_iFloor()).append("层")
                        .append(activity.getM_iRoom()).append("室");

                    text.append("\n");
                }
            else {
                text.append("活动列表为空~");
            }
        } else {
            //实例化模糊查找对象
            FuzzySearch fuzzySearch = new FuzzySearch();
            //根据输入框中内容查找获得结果
            ArrayList<Activity> results = fuzzySearch.get_FS_result(this.ToBe.getText(), activities);
            if (!results.isEmpty()) {
                for (Activity activity : activities) {
                    Time time = activity.getM_tTime();
                    text.append(activity.getM_sName()).append("\t")
                            .append("时间为：").append(time.getStartMonth()).append("月")
                            .append(time.getStartDate()).append("日").append("星期").append(time.getWeek())
                            .append("\t");

                    if (activity.getM_eProperty() == Property.SELF) {
                        text.append("课程属性：").append("个人活动").append("\t");
                    } else if (activity.getM_eProperty() == Property.GROUP) {
                        text.append("课程属性：").append("集体活动").append("\t");
                    }

                    text.append("活动地点：");

                    if (activity.getM_sConstruction().getCampus() == 0) {
                        text.append("校区为：").append("沙河校区").append("\t");
                    } else if (activity.getM_sConstruction().getCampus() == 1) {
                        text.append("校区为：").append("西土城校区").append("\t");
                    }

                    text.append(activity.getM_sConstruction().get_con_name())
                            .append(activity.getM_iFloor()).append("层")
                            .append(activity.getM_iRoom()).append("室");

                    text.append("\n");
                }
            } else {
                text.append("查找失败，请重新输入");
            }
        }
        this.Info.setText(text.toString());
    }

    private void DeleteClicked() {
        if (this.ToBe.getText().isEmpty())
            this.ErrorInfo.setText("请输入要删除的课程信息！");
        else {
            FuzzySearch fuzzySearch = new FuzzySearch();
            ArrayList<Activity> results = fuzzySearch.get_FS_result(this.ToBe.getText(), this.studentAccount.getActivity());
            Activity activity = results.get(0);
            this.ErrorInfo.setText(this.studentAccount.exitActivity(activity));
        }
    }
}
