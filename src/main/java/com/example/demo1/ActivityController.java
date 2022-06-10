package com.example.demo1;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.Construction;
import com.example.demo1.Code.entity.FuzzySearch;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.systemtime.SystemTime;
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
            thisStage.setScene(new Scene(loader.load(), 1000, 400));
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

    /**
     * 处理回到主界面的请求
     */
    private void BackToMainClicked() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生返回主界面");
        SystemTime.restartTime();
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    /**
     * 构建一个活动
     */
    private void AssureClicked() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生构建活动");
        try {
            //获得活动名
            String Name = this.Name.getText();

            //获得活动时间
            int Month = Integer.parseInt(this.Month.getText());
            int Date = Integer.parseInt(this.Date.getText());
            int STH = Integer.parseInt(this.STH.getText());
            int STM = Integer.parseInt(this.STM.getText());
            int ENH = Integer.parseInt(this.ENH.getText());
            int ENM = Integer.parseInt(this.ENM.getText());
            if (Month <= 0 || Month > 12 || Date <= 0 || Date > 31)
                throw new Exception();
            Time time = new Time(STH, STM, ENH, ENM, Date, Month);

            //根据选项判断活动类型（个人/集体）
            Property type = Property.SELF;
            ArrayList<Activity> activities = this.studentAccount.getActivity();
            int Num = 2000;
            if (!activities.isEmpty())
                Num = activities.get(activities.size() - 1).getM_iNum() + 1;
            if (this.Solo.isSelected()) {
                type = Property.SELF;
            } else if (this.Group.isSelected()) {
                type = Property.GROUP;
            }

            //获取活动地点
            String Building = this.Building.getText();
            Construction construction = new Construction(Building);
            int Floor = Integer.parseInt(this.Floor.getText());
            int Room = Integer.parseInt(this.Room.getText());

            Activity activity = new Activity(Name, time, type, construction, Floor, Room, Num);

            //根据上面的参数构建活动并传入
            this.ErrorInfo.setText(this.studentAccount.registerActivity(activity));
        } catch (Exception e) {
            this.ErrorInfo.setText("输入错误！\n请按照合理的方式输入数据哦！");
        }
    }

    /**
     * 查找功能
     */
    private void SearchClicked() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生查找活动");
        //从学生账户中获得所有活动信息
        ArrayList<Activity> activities = this.studentAccount.getActivity();
        //存储要输出在文本框中的信息的字符串
        StringBuilder text = new StringBuilder();

        //若搜索框为空，则输出该账号下所有活动
        if (this.ToBe.getText().isEmpty()) {
            if (!activities.isEmpty()) {
                for (Activity activity : activities) {
                    Time time = activity.getM_tTime();
                    text.append(activity.getM_sName()).append("\t")
                            .append("时间为：").append(time.getStartMonth()).append("月")
                            .append(time.getStartDate()).append("日").append("\t");

                    if (activity.getM_eProperty() == Property.SELF) {
                        text.append("活动属性：").append("个人活动").append("\t");
                    } else if (activity.getM_eProperty() == Property.GROUP) {
                        text.append("活动属性：").append("集体活动").append("\t");
                    }

                    text.append("活动地点：");

                    text.append(activity.getM_sConstruction().get_con_name())
                            .append(activity.getM_iFloor()).append("层")
                            .append(activity.getM_iRoom()).append("室");

                    text.append("\n");
                }
            //当某人活动列表为空时输出空表信息
            } else {
                text.append("活动列表为空~");
            }
        //当搜索框不为空时按照输入查找课程，具体实现使用模糊查找算法
        } else {
            try {
                int Num = Integer.parseInt(this.ToBe.getText());
                Search search = new Search();
                Num = search.BinaryCourseSearch(Num, activities);
                if (Num != activities.size()) {
                    Activity activity = activities.get(Num);
                    Time time = activity.getM_tTime();
                    text.append(activity.getM_sName()).append("\t")
                            .append("时间为：").append(time.getStartMonth()).append("月")
                            .append(time.getStartDate()).append("日").append("\t");

                    if (activity.getM_eProperty() == Property.SELF) {
                        text.append("活动属性：").append("个人活动").append("\t");
                    } else if (activity.getM_eProperty() == Property.GROUP) {
                        text.append("活动属性：").append("集体活动").append("\t");
                    }

                    text.append("活动地点：");

                    text.append(activity.getM_sConstruction().get_con_name())
                            .append(activity.getM_iFloor()).append("层")
                            .append(activity.getM_iRoom()).append("室");

                    text.append("\n");
                }
            } catch (NumberFormatException e) {
                //实例化模糊查找对象
                FuzzySearch fuzzySearch = new FuzzySearch();
                //根据输入框中内容查找获得结果
                ArrayList<Activity> results = fuzzySearch.get_FS_result(this.ToBe.getText(), activities);
                //若结果不为空则遍历输出
                if (results != null) {
                    for (Activity activity : activities) {
                        Time time = activity.getM_tTime();
                        text.append(activity.getM_sName()).append("\t")
                                .append("时间为：").append(time.getStartMonth()).append("月")
                                .append(time.getStartDate()).append("日").append("\t");

                        if (activity.getM_eProperty() == Property.SELF) {
                            text.append("活动属性：").append("个人活动").append("\t");
                        } else if (activity.getM_eProperty() == Property.GROUP) {
                            text.append("活动属性：").append("集体活动").append("\t");
                        }

                        text.append("活动地点：");

                        text.append(activity.getM_sConstruction().get_con_name())
                                .append(activity.getM_iFloor()).append("层")
                                .append(activity.getM_iRoom()).append("室");

                        text.append("\n");
                    }
                    //结果为空则输出报错信息
                } else {
                    text.append("查找失败，请重新输入");
                }
            }
            this.Info.setText(text.toString());
        }
    }

    /**
     * 删除活动
     */
    private void DeleteClicked() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生删除活动");
        Activity activity = new Activity();
        if (this.ToBe.getText().isEmpty())
            this.ErrorInfo.setText("请输入要删除的课程信息！");
        else {
            try {
                int Num = Integer.parseInt(this.ToBe.getText());
                ArrayList<Activity> activities = this.studentAccount.getActivity();
                Search search = new Search();
                int temp = search.BinaryCourseSearch(Num, activities);
                if (temp != activities.size())
                    activity = activities.get(Num);
                else
                    this.ErrorInfo.setText("活动列表中没有活动");
            } catch (NumberFormatException e) {
                FuzzySearch fuzzySearch = new FuzzySearch();
                ArrayList<Activity> results = fuzzySearch.get_FS_result(this.ToBe.getText(), this.studentAccount.getActivity());
                if (results != null)
                    activity = results.get(0);
                else
                    this.ErrorInfo.setText("活动列表中没有活动");
            }
            this.Info.setText(this.studentAccount.exitActivity(activity));
        }
    }
}
