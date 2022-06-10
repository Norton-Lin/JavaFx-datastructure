package com.example.demo1;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Mysql.AccountDatabase;
import com.example.demo1.Code.Mysql.ActivityDatabase;
import com.example.demo1.Code.Util.MyException;
import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.Construction;
import com.example.demo1.Code.entity.FuzzySearch;
import com.example.demo1.Code.entity.account.ManagerAccount;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.systemtime.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ManagerActController {
    private final Stage thisStage;
    private final MainViewPort_Controller controller;

    ManagerAccount managerAccount;

    ActivityDatabase activityDatabase;

    @FXML
    public Label ErrorInfo;
    @FXML
    public TextField Name;
    @FXML
    public TextField Month;
    @FXML
    public TextField Date;
    @FXML
    public TextField STH;
    @FXML
    public TextField STM;
    @FXML
    public TextField ENH;
    @FXML
    public TextField ENM;
    @FXML
    public TextField ClassNum;
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
    public TextField DClass;
    @FXML
    public Button Delete;
    @FXML
    public TextArea Info;
    @FXML
    public Button BackToMain;

    public ManagerActController(MainViewPort_Controller mainViewPort_controller) {
        //得到新Controller
        this.controller = mainViewPort_controller;
        this.activityDatabase = new ActivityDatabase();
        //创建新场景
        thisStage = new Stage();

        this.activityDatabase = new ActivityDatabase();

        this.managerAccount = new ManagerAccount(this.controller.getAccount());

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerAct.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 1000, 400));
            thisStage.setTitle("欢迎来到班级活动添加界面~");
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
        //点击删除按钮后的反应
        this.Delete.setOnAction(event -> DeleteClicked());

    }

    /**
     * 确定添加活动
     */
    private void AssureClicked() {
        LogFile.info("Manager" + this.managerAccount.getID(),"管理员构建活动");
        try {
            String Name = this.Name.getText();

            int Month = Integer.parseInt(this.Month.getText());
            int Date = Integer.parseInt(this.Date.getText());
            int STH = Integer.parseInt(this.STH.getText());
            int STM = Integer.parseInt(this.STM.getText());
            int ENH = Integer.parseInt(this.ENH.getText());
            int ENM = Integer.parseInt(this.ENM.getText());
            if (Month <= 0 || Month > 12 || Date <= 0 || Date > 31)
                throw new MyException(1);
            Time time = new Time(STH, STM, ENH, ENM, Date, Month);

            String ClassID = this.ClassNum.getText();

            String Building = this.Building.getText();
            Construction construction = new Construction(Building);
            int Floor = Integer.parseInt(this.Floor.getText());
            int Room = Integer.parseInt(this.Room.getText());

            int Num = 3000;
            ArrayList<Activity> activities = new ArrayList<>();
            this.activityDatabase.find(activities, ClassID, 1);
            if (!activities.isEmpty())
                Num = activities.get(activities.size() - 1).getM_iNum() + 1;
            Activity activity = new Activity(Name, time, Property.GROUP, construction, Floor, Room, Num);

            if (this.managerAccount.addClassActivity(activity, ClassID))
                //根据上面的参数构建活动并传入
                this.Info.setText(Name + "已添加至班级\b" + ClassID);
            else
                this.Info.setText("添加活动失败！");
        } catch (NumberFormatException e) {
            this.ErrorInfo.setText("输入错误！\n请按照合理的方式输入数据哦！");
        } catch (MyException e1) {
            this.ErrorInfo.setText(e1.getMessage());
        }
    }


    /**
     * 删除班级活动
     */
    private void DeleteClicked() {
        LogFile.info("Manager" + this.managerAccount.getID(),"管理员删除活动");
        if (this.ToBe.getText().isEmpty())
            this.ErrorInfo.setText("请输入要删除的活动信息！");
        else {
            Activity activity = new Activity();
            activity.setM_sName(this.ToBe.getText());
            if (!this.DClass.getText().isEmpty()) {
                String tool = this.DClass.getText();
                if (activity.getM_iNum() != 0) {
                    this.managerAccount.deleteClassActivity(activity, tool);
                    this.Info.setText("活动" + tool + "删除成功！");
                } else {
                    this.ErrorInfo.setText("未找到您所要删除的活动");
                }
            } else {
                this.ErrorInfo.setText("请输入要删除活动的班级！");
            }
        }
    }

    /**
     * 回到主界面
     */
    private void BackToMainClicked() {
        LogFile.info("Manager" + this.managerAccount.getID(),"管理员回到主界面");
        SystemTime.restartTime();
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }
}
