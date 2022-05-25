package com.example.demo1;

import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.FuzzySearch;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.StudentAccount;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class CourseChooseController {

    private final Stage thisStage;
    private final MainViewPort_Controller controller;

    @FXML
    private Button ExceptionInfo;
    @FXML
    private Button Search1;
    @FXML
    private Button Assure1;
    @FXML
    private Button Search2;
    @FXML
    private Button Assure2;
    @FXML
    private Button backToMain;
    @FXML
    private ComboBox<String> Results;
    @FXML
    private TextField ToBeSearched;

    public CourseChooseController(MainViewPort_Controller mainViewPort_controller) {
        //得到新Controller
        this.controller = mainViewPort_controller;

        //创建新场景
        thisStage = new Stage();

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseChoose.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 600, 300));
            thisStage.setTitle("欢迎来到选课界面~");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //根据账户的权限获取子类的实例化对象
//        switch (this.controller.getAccount().getAuthority()) {
//
//        }
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
     * 异常驱动型课程查询，若抛出异常证明输入汉字，执行模糊查询；否则输入为整形数，执行精确查询
     */
    private void Assure1ButtonClicked() {
        //用来存储课程列表的空表
        ArrayList<Course> tool = new ArrayList<>();
        //模糊查找的结果list
        ArrayList<Course> primaryResult;
        ArrayList<String> listInComboBox = new ArrayList<>();
        //课程编号
        int Num = 0;
        //实例化Course数据库
        CourseDatabase courseDatabase = new CourseDatabase();
        try {
            Num = Integer.parseInt(this.ToBeSearched.getText());
            Search search = new Search();
//            tool = this.controller.getAccount();
//            search.BinaryCourseSearch();
        } catch (Exception e) {
            FuzzySearch fuzzySearch = new FuzzySearch();
            courseDatabase.find(tool);
            primaryResult =  fuzzySearch.get_FS_result(this.ToBeSearched.getText(), tool);
            for (Course course : primaryResult)
                listInComboBox.add(course.getM_sName());
            this.Results.getItems().addAll(listInComboBox);
        }
    }

    private void Search1ButtonClicked() {

    }

    private void Search2ButtonClicked() {

    }

    private void Assure2ButtonClicked() {

    }

    private void backToMainClicked() {
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }
}
