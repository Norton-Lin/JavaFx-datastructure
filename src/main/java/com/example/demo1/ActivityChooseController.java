package com.example.demo1;

import com.example.demo1.Code.Mysql.ActivityDatabase;
import com.example.demo1.Code.Util.Authority;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.FuzzySearch;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.entity.account.TeacherAccount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityChooseController {
    private final Stage thisStage;
    private final MainViewPort_Controller controller;


    @FXML
    public Button Search1;
    @FXML
    public Button Assure1;
    @FXML
    public Button Search2;
    @FXML
    public Button Assure2;
    @FXML
    public Button backToMain;
    @FXML
    public Label SearchResult_Name;
    @FXML
    public Label SearchResult_Boolean;
    @FXML
    public TextField ToBeFuzzySearched;
    @FXML
    public TextField ToBeBinarySearched;
    @FXML
    public TextArea ResOfSearch;

    StudentAccount studentAccount;
    TeacherAccount teacherAccount;

    //存放本账号的课程表
    ArrayList<Activity> activities;

    public ActivityChooseController(MainViewPort_Controller mainViewPort_controller) {
        //得到新Controller
        this.controller = mainViewPort_controller;

        //创建新场景
        thisStage = new Stage();

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseChoose.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 450, 400));
            thisStage.setTitle("欢迎来到选择活动界面~");
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.activities = getActivity(this.controller.getAccount().getAuthority());

        if (this.controller.getAccount().getAuthority() == Authority.Student) {
            this.studentAccount = new StudentAccount(this.controller.getAccount());
            this.activities = this.studentAccount.getActivity();
        }
        else if (this.controller.getAccount().getAuthority() == Authority.Teacher) {
            this.teacherAccount = new TeacherAccount(this.controller.getAccount());
            this.activities = this.teacherAccount.getActivity();
        }
    }

    public void showStage() {
        //创建新场景
        this.thisStage.show();
    }

    private void initialize() {
        //点击查询所有课程的反应
        this.Search1.setOnAction(event -> Search1ButtonClicked());
        //点击添加未选课程后的反应
        this.Assure1.setOnAction(event -> Assure1ButtonClicked());
        //点击查询自己课程后的反应
        this.Search2.setOnAction(event -> Search2ButtonClicked());
        //点击删除已有课程之后的反应
        this.Assure2.setOnAction(event -> Assure2ButtonClicked());
        //点击回到主菜单后的反应
        this.backToMain.setOnAction(event -> backToMainClicked());
    }

    /**
     * 将在所有课程库中查找到的结果显示在文本框中
     */
    private ArrayList<Activity> Search1ButtonClicked() {
        //用来存储活动列表的空表
        ArrayList<Activity> tool = new ArrayList<>();
        //实例化数据库
        ActivityDatabase activityDatabase = new ActivityDatabase();
        //从数据库中得到活动列表
        activityDatabase.find(tool);
        //实例化一个模糊查找对象
        FuzzySearch fuzzySearch = new FuzzySearch();
        //建立一个空表用于存储模糊查找结果
        ArrayList<Activity> primaryResults;
        //将模糊查找结果存入 list
        primaryResults = fuzzySearch.get_FS_result(ToBeFuzzySearched.getText(), tool);
        //查询结果的list
        ArrayList<String> texts = new ArrayList<>();
        //将结果字符串化
        for (Activity activity : primaryResults)
            texts.add(activity.getM_sName() + " 编号为：" + activity.getM_iNum());
        //将查询得到的结果显示在文本框中
        ResOfSearch.setText(texts.toString());
        return primaryResults;
    }

    /**
     * 点击此按钮选定课程
     */
    private void Assure1ButtonClicked() {
        //用来存储活动列表的空表
        ArrayList<Activity> tool = new ArrayList<>();
        //实例化数据库
        ActivityDatabase activityDatabase = new ActivityDatabase();
        //从数据库中得到活动列表
        activityDatabase.find(tool);
        //实例化查找类型对象
        Search search = new Search();
        //标记查找是否成功
        boolean mark = false;
        try {
            //存储精确查询结果
            int fin = 0;

            if (Search1ButtonClicked() == null) {
                //模糊查找失败或未进行模糊查找，在所有活动列表中查询
                int Num1 = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), tool);
                if (Num1 != tool.size()) {
                    mark = true;
                    fin = Num1;
                }
            }
            else {
                //查找成功后在模糊查找得到的列表中精确查找
                int Num2 = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), Search1ButtonClicked());
                if (Num2 != Search1ButtonClicked().size()) {
                    mark = true;
                    fin = Num2;
                }
            }

            if (mark) {
                //展示查找到的活动名称
                SearchResult_Name.setText(tool.get(fin).getM_sName() + " 编号为：" + tool.get(fin).getM_iNum());
                //根据账号类型将课程添加到课表
                if (this.controller.getAccount().getAuthority() == Authority.Student)
                    this.studentAccount.addActivity(tool.get(fin));
                else if (this.controller.getAccount().getAuthority() == Authority.Teacher)
                    this.teacherAccount.addActivity(tool.get(fin));
                SearchResult_Boolean.setText("查找并添加成功！好耶！");
            }
            else
                SearchResult_Boolean.setText("查找失败……检查一下操作吧！");

        } catch (Exception e) {
            System.out.println("用户输入异常");
            SearchResult_Name.setText("右侧输入框中应该输入数字哦~");
        }
    }

    private ArrayList<Activity> Search2ButtonClicked() {
        //实例化模糊查找对象
        FuzzySearch fuzzySearch = new FuzzySearch();
        //建立一个空表用于存储模糊查找结果
        ArrayList<Activity> primaryResults;
        //将模糊查找结果存入 list
        primaryResults = fuzzySearch.get_FS_result(ToBeFuzzySearched.getText(), this.activities);
        //查询结果的list
        ArrayList<String> texts = new ArrayList<>();
        //将结果字符串化
        for (Activity activity : primaryResults)
            texts.add(activity.getM_sName() + " 编号为：" + activity.getM_iNum());
        //将查询得到的结果显示在文本框中
        ResOfSearch.setText(texts.toString());
        return primaryResults;
    }

    private void Assure2ButtonClicked() {
        //实例化查找类型对象
        Search search = new Search();
        boolean mark = false;
        try {
            //存储精确查询结果
            int fin = 0;

            if (Search1ButtonClicked() == null) {
                //模糊查找失败或未进行模糊查找，在所有课程列表中查询
                int Num1 = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), this.activities);
                if (Num1 != this.activities.size()) {
                    mark = true;
                    fin = Num1;
                }
            }
            else {
                //查找成功后在模糊查找得到的列表中精确查找
                int Num2 = search.BinaryCourseSearch(Integer.parseInt(ToBeBinarySearched.getText()), Search2ButtonClicked());
                if (Num2 != Search2ButtonClicked().size()) {
                    mark = true;
                    fin = Num2;
                }
            }

            if (mark) {
                //展示查找到的课程名称
                SearchResult_Name.setText(this.activities.get(fin).getM_sName() + " 编号为：" + this.activities.get(fin).getM_iNum());
                //根据账号类型将课程从课表中删除
                if (this.controller.getAccount().getAuthority() == Authority.Student)
                    this.studentAccount.decActivity(this.activities.get(fin));
                else if (this.controller.getAccount().getAuthority() == Authority.Teacher)
                    this.teacherAccount.decActivity(this.activities.get(fin));
                SearchResult_Boolean.setText("查找并删除成功！好耶！");
            }
            else
                SearchResult_Boolean.setText("查找失败……检查一下操作吧！");

        } catch (Exception e) {
            System.out.println("用户输入异常");
            SearchResult_Name.setText("右侧输入框中应该输入数字哦~");
        }
    }

    private void backToMainClicked() {
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    private ArrayList<Activity> getActivity(Authority authority) {
        //存放单人课程列表
        ArrayList<Activity> activities_fake = new ArrayList<>();
        //根据权限获取账户信息
        switch (authority) {
            case Student -> {
                StudentAccount studentAccount = new StudentAccount(this.controller.getAccount());
                activities_fake = studentAccount.getActivity();
            }
            case Teacher -> {
                TeacherAccount teacherAccount = new TeacherAccount(this.controller.getAccount());
                activities_fake = teacherAccount.getActivity();
            }
        }
        return activities_fake;
    }
}