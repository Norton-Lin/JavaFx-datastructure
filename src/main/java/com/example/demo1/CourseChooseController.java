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

<<<<<<< HEAD
    StudentAccount studentAccount;

    //存放本账号的课程表
    ArrayList<Course> courses;

=======
>>>>>>> d4bc056e2650d41be6210bd3d2a1f4a3fce37229
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
<<<<<<< HEAD
    public TextField ToBeFuzzySearched;
    @FXML
    public TextField ToBeBinarySearched;
    @FXML
    public TextArea ResOfSearch;
=======
    private TextField ToBeSearched;
>>>>>>> d4bc056e2650d41be6210bd3d2a1f4a3fce37229

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
<<<<<<< HEAD
            //存储精确查询结果
            int fin = 0;

            try {
                if (Search1ButtonClicked() == null) {
                    //模糊查找失败或未进行模糊查找，在所有课程列表中查询
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
            } catch (NumberFormatException e) {
                mark = false;
            }


            if (mark) {
                //展示查找到的课程名称
                SearchResult_Name.setText(tool.get(fin).getM_sName() + " 编号为：" + tool.get(fin).getM_iNum());
                //根据账号类型将课程添加到课表
                this.studentAccount.addCourse(tool.get(fin));
                SearchResult_Boolean.setText("查找并添加成功！好耶！");
            }
            else
                SearchResult_Boolean.setText("查找失败……检查一下操作吧！");

=======
            Num = Integer.parseInt(this.ToBeSearched.getText());
            Search search = new Search();
//            tool = this.controller.getAccount();
//            search.BinaryCourseSearch();
>>>>>>> d4bc056e2650d41be6210bd3d2a1f4a3fce37229
        } catch (Exception e) {
            FuzzySearch fuzzySearch = new FuzzySearch();
            courseDatabase.find(tool);
            primaryResult =  fuzzySearch.get_FS_result(this.ToBeSearched.getText(), tool);
            for (Course course : primaryResult)
                listInComboBox.add(course.getM_sName());
            this.Results.getItems().addAll(listInComboBox);
        }
    }

<<<<<<< HEAD
    /**
     * 点此按钮从自身课程表中模糊查找课程
     * @return 模糊查找结果
     */
    private ArrayList<Course> Search2ButtonClicked() {
        //实例化模糊查找对象
        FuzzySearch fuzzySearch = new FuzzySearch();
        //建立一个空表用于存储模糊查找结果
        ArrayList<Course> primaryResults;
        //将模糊查找结果存入 list
        primaryResults = fuzzySearch.get_FS_result(ToBeFuzzySearched.getText(), this.courses);
        //查询结果的list
        ArrayList<String> texts = new ArrayList<>();
        //将结果字符串化
        for (Course course : primaryResults)
            texts.add(course.getM_sName() + " 编号为：" + course.getM_iNum());
        //将查询得到的结果显示在文本框中
        ResOfSearch.setText(texts.toString());
        return primaryResults;
=======
    private void Search1ButtonClicked() {

    }

    private void Search2ButtonClicked() {

>>>>>>> d4bc056e2650d41be6210bd3d2a1f4a3fce37229
    }

    /**
     * 在已有课表得到的模糊结果中精确查找
     */
    private void Assure2ButtonClicked() {

    }

    /**
     * 回到主界面
     */
    private void backToMainClicked() {
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }
}
