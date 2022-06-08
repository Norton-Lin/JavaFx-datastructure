package com.example.demo1;

import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.systemtime.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;

public class CourseTableController {
    //控制当前Controller的场景
    private final Stage thisStage;

    //存储本账号的课程们
    ArrayList<Course> courses;

    //允许我们访问HelloController的方法们
    private final MainViewPort_Controller controller;

    //在查询课程信息时会用到的Search类的实例化
    private final Search search;

    @FXML
    public TextArea CourseTable;
    @FXML
    public TextField ToBeSearched;
    @FXML
    public Button Search;
    @FXML
    public Button BackToMain;
    @FXML
    public TextArea Info;

    int[][] values = {{1,2},{3,4},{3,4,5},{6,7},{6,7,8},{8,9},{9,10,11},{12,13,14},{13,14}};

    public CourseTableController(MainViewPort_Controller controller) {

        this.controller = controller;

        //创建新场景
        this.thisStage = new Stage();

        //学生账号
        StudentAccount studentAccount = new StudentAccount(this.controller.getAccount());

        //获取课程
        this.courses = studentAccount.getCourse();

        //实例化查找对象
        search = new Search();

        BuildTheTable();

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseTable.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 900, 200));
            thisStage.setTitle("欢迎来到课程表~");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        //点击回到主菜单后的反应
        this.BackToMain.setOnAction(event -> BackToMainMenu());
        //点击查找按钮后的反应
        this.Search.setOnAction(event -> handleSearch());
    }

    public void showStage() {
        this.thisStage.show();
    }


    /**
     * 根据上课时间得到节次
     * @return 某一门课的节次
     */
    protected int[] getSex(String times) {
        int[] results = new int[3];
        switch (times) {
            case ("80935") -> results = this.values[0];
            case ("9501125") -> results = this.values[1];
            case ("9501215") -> results = this.values[2];
            case ("1301435")-> results = this.values[3];
            case ("1301530") -> results = this.values[4];
            case ("14451625") -> results = this.values[5];
            case ("15401810") -> results = this.values[6];
            case ("18302055") -> results = this.values[7];
            case ("19202055") -> results = this.values[8];
        }
        return results;
    }

    protected void BackToMainMenu() {
        SystemTime.restartTime();
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    protected void handleSearch() {
        int Num = search.BinaryCourseSearch(
                Integer.parseInt(ToBeSearched.getText()), this.courses);
        String Info;
        if (Num != this.courses.size()) {
            Course temp = this.courses.get(Num);
            Info = "课程名：" + temp.getM_sName()
                    + "\n" + "课程编号：" + temp.getM_iNum()
                    + "\n" + "任课教师：" + temp.getM_sTeacher()
                    + "\n" + "上课时间：" + temp.getM_tTime().getStartHour() + ":"
                    + temp.getM_tTime().getStartMinute() + "~"
                    + temp.getM_tTime().getEndHour() + ":"
                    + temp.getM_tTime().getEndMinute()
                    + "\n" + "上课地点：" + temp.getM_sConstruction()
                    + temp.getM_iFloor() + "层" + temp.getM_iRoom() + "室"
                    + "\n" + "考试时间：" + temp.getM_cExamTime().getStartMonth()
                    + "月" + temp.getM_cExamTime().getStartDate() + "日"
                    + "星期" + temp.getM_cExamTime().getWeek() + "\t"
                    + temp.getM_cExamTime().getStartHour() + ":"
                    + temp.getM_cExamTime().getStartMinute() + "~"
                    + temp.getM_cExamTime().getEndHour() + ":"
                    + temp.getM_cExamTime().getEndMinute()
                    + "\n" + "考试地点：" + temp.getM_cExamConstruction();
        } else {
            Info = "课表中无此课程，查找失败！";
        }
        this.Info.setText(Info);
    }

    protected void BuildTheTable() {
        //存储一周的课表
        String[][] AWeek = new String[7][14];
        for (Course course : this.courses) {

        }
    }
}
