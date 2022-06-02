package com.example.demo1;

import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.StudentAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    public TableView<EverySection> courseTable;
    @FXML
    public TextField ToBeSearched;
    @FXML
    public Button Search;
    @FXML
    public Button BackToMain;
    @FXML
    public TextArea Info;

    //每天课程时间范围，所有的课程不会超出这个范围
    int[][] keys = {{8,0,9,35}, {9,50,11,25}, {9,50,12,15}, {13,0,14,35},
            {13,0,15,30}, {14,45,16,25}, {15,40,18,10}, {18,30,20,55}, {19,20,20,55}};

    int[][] values = {{1,2},{3,4},{3,4,5},{6,7},{6,7,8},{8,9},{9,10,11},{12,13,14},{13,14}};

    public CourseTableController(MainViewPort_Controller controller) {

        this.controller = controller;

        //创建新场景
        this.thisStage = new Stage();
        this.thisStage.setMaximized(true);

        search = new Search();

        //设置课程表不可编辑
        this.courseTable.setEditable(false);

        //学生账号
        StudentAccount studentAccount = new StudentAccount(this.controller.getAccount());

        //获取课程
        this.courses = studentAccount.getCourse();

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseTable.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
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
        //向TableView中添加元素
//        courseTable.setItems(getSections());
    }

    public void showStage() {
        this.thisStage.show();
    }

    /**
     * 根据上课时间和上课星期将课程一位数组扩容为二维，注意由于课程为传引用调用所以不能对course做更改
     * @return 每一行显示的课程组成的arraylist
     */
    protected ObservableList<EverySection> getSections() {
        ObservableList<EverySection> everySectionArrayList = FXCollections.observableArrayList();

        //共有14种课程位置可能性，循环14次
        for (int i = 0; i < 14; i++) {
            //存储每一行内容的对象，用于筛选符合条件的课程
            EverySection temp = new EverySection();

            //用于进行第一步筛选的临时数组，根据当前循环计数判断节次，进而获取当前节次的课程
            ArrayList<Course> tool = getLocation(i + 1);

            //第二步筛选，用来对每一行分配对应星期的课
            for (Course course : tool)
                switch (course.getM_tTime().getWeek()) {
                    case 1 -> temp.Mon_Cour = course.getM_sName() + course.getM_iNum();
                    case 2 -> temp.Tue_Cour = course.getM_sName() + course.getM_iNum();
                    case 3 -> temp.Wed_Cour = course.getM_sName() + course.getM_iNum();
                    case 4 -> temp.Thu_Cour = course.getM_sName() + course.getM_iNum();
                    case 5 -> temp.Fri_Cour = course.getM_sName() + course.getM_iNum();
                    case 6 -> temp.Sat_Cour = course.getM_sName() + course.getM_iNum();
                    case 7 -> temp.Sun_Cour = course.getM_sName() + course.getM_iNum();
                }

            //根据当前循环次数判断当前为第几行，并将其加入temp中
            switch (i + 1) {
                case 1 -> temp.Sec = "一";
                case 2 -> temp.Sec = "二";
                case 3 -> temp.Sec = "三";
                case 4 -> temp.Sec = "四";
                case 5 -> temp.Sec = "五";
                case 6 -> temp.Sec = "六";
                case 7 -> temp.Sec = "七";
                case 8 -> temp.Sec = "八";
                case 9 -> temp.Sec = "九";
                case 10 -> temp.Sec = "十";
                case 11 -> temp.Sec = "十一";
                case 12 -> temp.Sec = "十二";
                case 13 -> temp.Sec = "十三";
                case 14 -> temp.Sec = "十四";
            }
            everySectionArrayList.add(temp);
        }
        return everySectionArrayList;
    }

    protected ArrayList<Course> getLocation(int num) {
        //建立哈希表，根据上课时间判定节次
        HashMap<int[], int[]> hashMap = new HashMap<>();
        for (int i = 0; i < 9; i++)
            hashMap.put(keys[i], values[i]);
        //根据哈希表进行节次到时间的映射，并据此建立每人同一节次的列表
        ArrayList<Course> results = new ArrayList<>();
        for (Course course : this.courses) {
            int[] times = new int[4];
            times[0] = course.getM_tTime().getStartHour();
            times[1] = course.getM_tTime().getStartMinute();
            times[2] = course.getM_tTime().getEndHour();
            times[3] = course.getM_tTime().getEndMinute();
            if (Contains(num, hashMap.get(times)))
                results.add(course);
        }
        return results;
    }

    /**
     * 根据输入的节次和所含节次列表判断当前节次是否存在于列表中
     * @return 是否含当前节次课程
     */
    protected boolean Contains(int num, int[] values) {
        boolean mark = false;
        for (int i : values)
            if (num == i) {
                mark = true;
                break;
            }
        return mark;
    }

    protected void BackToMainMenu() {
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    protected void handleSearch() {
        int Num = search.BinaryCourseSearch(
                Integer.parseInt(ToBeSearched.getText()), this.courses);
        String Info;
        Course temp = this.courses.get(Num);
        Info = "课程名：" + temp.getM_sName()
             + "\n" + "课程编号：" + temp.getM_iNum()
             + "\n" + "任课教师：" + temp.getM_sTeacher()
             + "\n" + "上课时间：" + temp.getM_tTime().getStartHour() + ":"
                + temp.getM_tTime().getStartMinute() + "~"
                + temp.getM_tTime().getEndHour() + ":"
                + temp.getM_tTime().getEndMinute()
             + "\n" + "上课地点：" + temp.getM_sConstruction()
             + "\n" + "考试时间：" + temp.getM_cExamTime().getStartMonth()
                + "月" + temp.getM_cExamTime().getStartDate() + "日"
                + "星期" + temp.getM_cExamTime().getWeek() + "\t"
                + temp.getM_cExamTime().getStartHour() + ":"
                + temp.getM_cExamTime().getStartMinute() + "~"
                + temp.getM_cExamTime().getEndHour() + ":"
                + temp.getM_cExamTime().getEndMinute()
             + "\n" + "考试地点：" + temp.getM_cExamConstruction();
        this.Info.setText(Info);
    }
}

class EverySection {
    String Sec;
    String Mon_Cour;
    String Tue_Cour;
    String Wed_Cour;
    String Thu_Cour;
    String Fri_Cour;
    String Sat_Cour;
    String Sun_Cour;
}
