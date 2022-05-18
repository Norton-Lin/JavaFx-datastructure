package com.example.demo1;

import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Construction;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.FuzzySearch;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.ManagerAccount;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.entity.account.TeacherAccount;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;

public class CourseChooseController {

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
    public ComboBox<String> Results;
    @FXML
    public TextField ToBeFuzzySearched;
    @FXML
    public TextField ToBeBinarySearched;

    //定义子类账户
    StudentAccount studentAccount;
    TeacherAccount teacherAccount;
    ManagerAccount managerAccount;

    public CourseChooseController(MainViewPort_Controller mainViewPort_controller) {
        //得到新Controller
        this.controller = mainViewPort_controller;

        //创建新场景
        thisStage = new Stage();

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseChoose.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 450, 400));
            thisStage.setTitle("欢迎来到选课界面~");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        根据账户的权限获取子类的实例化对象
//        switch (this.controller.getAccount().getAuthority()) {
//            case Student -> this.studentAccount = new StudentAccount(this.controller.getAccount());
//            case Teacher -> this.teacherAccount = new TeacherAccount(this.controller.getAccount());
//            case Manager -> this.managerAccount = new ManagerAccount(this.controller.getAccount());
//        }

        //重写课程类的toString方法
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
    private void Search1ButtonClicked() {
        //用来存储课程列表的空表
        ArrayList<Course> tool = new ArrayList<>();
        //模糊查找的结果list
        ArrayList<Course> primaryResult;
        //课程编号
        int Num;
        //实例化Course数据库
//        CourseDatabase courseDatabase = new CourseDatabase();
        try {
            //若抛出异常证明输入不是数字，执行模糊查找；否则精确查找
//            Num = Integer.parseInt(this.ToBeSearched.getText());
//            courseDatabase.find(tool);
//            Search search = new Search();
//            int i = search.BinaryCourseSearch(Num, tool);
//            this.Results.getItems().add(tool.get(i));
//            ArrayList<Course> test = new ArrayList<>();
//            test.add(new Course("000", new Time(0,0,0,0,0,0,0),new Construction()));
//            test.add(new Course("001", new Time(1,2,3,4,5,6,7),new Construction()));
            ArrayList<String> strings = new ArrayList<>();
            strings.add("1234");
            strings.add("4566");
//            this.Results.setItems(FXCollections.observableArrayList(strings));
            this.Results.getItems().addAll(strings);
        } catch (Exception e) {
            FuzzySearch fuzzySearch = new FuzzySearch();
//            courseDatabase.find(tool);
//            primaryResult =  fuzzySearch.get_FS_result(this.ToBeSearched.getText(), tool);
//            this.Results.getItems().addAll(primaryResult);
        }

//        //设置转换器，希望这个转换器是对的
//        Results.setConverter(new StringConverter<>() {
//            @Override
//            public String toString(Course course) {
//                if (course == null)
//                    return "";
//                return course.getM_sName() + " " + course.getM_iNum();
//            }
//
//            @Override
//            public Course fromString(String s) {
//                return null;
//            }
//        });
    }

    /**
     * 点击此按钮选定课程
     */
    private void Assure1ButtonClicked() {
        Results.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, course, t1) -> {

                }
        );
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
