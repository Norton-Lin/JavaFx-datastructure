package com.example.demo1;

import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.StudentAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.*;
import com.example.demo1.Code.Util.Section;

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
    public TableView<Section> courseTable = new TableView<Section>();
    @FXML
    public TextField ToBeSearched;
    @FXML
    public Button Search;
    @FXML
    public Button BackToMain;
    @FXML
    public TextArea Info;
    @FXML
    public TableColumn<Section, String> Numbers = new TableColumn<>("Sec");
    @FXML
    public TableColumn<Section, String> Mon = new TableColumn<>("Mon");
    @FXML
    public TableColumn<Section, String> Tue = new TableColumn<>("Tue");
    @FXML
    public TableColumn<Section, String> Wed = new TableColumn<>("Wed");
    @FXML
    public TableColumn<Section, String> Thu = new TableColumn<>("Thu");
    @FXML
    public TableColumn<Section, String> Fri = new TableColumn<>("Fri");
    @FXML
    public TableColumn<Section, String> Sat = new TableColumn<>("Sat");
    @FXML
    public TableColumn<Section, String> Sun = new TableColumn<>("Sun");

    ObservableList<Section> sectionArrayList;

    int[][] values = {{1,2},{3,4},{3,4,5},{6,7},{6,7,8},{8,9},{9,10,11},{12,13,14},{13,14}};

    public CourseTableController(MainViewPort_Controller controller) {

        this.controller = controller;

        //创建新场景
        this.thisStage = new Stage();
        this.thisStage.setMaximized(true);


        //学生账号
        StudentAccount studentAccount = new StudentAccount(this.controller.getAccount());

        //获取课程
        this.courses = studentAccount.getCourse();

        //实例化查找对象
        search = new Search();

        ArrayList<Section> EXAMPLE = getSections();

        sectionArrayList = FXCollections.observableArrayList(EXAMPLE);

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
    }

    public void showStage() {
        this.thisStage.show();
    }

    /**
     * 根据上课时间和上课星期将课程一位数组扩容为二维，注意由于课程为传引用调用所以不能对course做更改
     * @return 每一行显示的课程组成的arraylist
     */
    protected ArrayList<Section> getSections() {

        ArrayList<Section> haha = new ArrayList<>();

        //共有14种课程位置可能性，循环14次
        for (int i = 0; i < 14; i++) {
            //存储每一行内容的对象，用于筛选符合条件的课程
            Section temp = new Section();

            //用于进行第一步筛选的临时数组，根据当前循环计数判断节次，进而获取当前节次的课程
            ArrayList<Course> tool = getLocation(i + 1);

            //第二步筛选，用来对每一行分配对应星期的课
            for (Course course : tool)
                switch (course.getM_tTime().getWeek()) {
                    case 1 -> temp.setMon_Cour(course.getM_sName() + course.getM_iNum());
                    case 2 -> temp.setTue_Cour(course.getM_sName() + course.getM_iNum());
                    case 3 -> temp.setWed_Cour(course.getM_sName() + course.getM_iNum());
                    case 4 -> temp.setThu_Cour(course.getM_sName() + course.getM_iNum());
                    case 5 -> temp.setFri_Cour(course.getM_sName() + course.getM_iNum());
                    case 6 -> temp.setSat_Cour(course.getM_sName() + course.getM_iNum());
                    case 7 -> temp.setSun_Cour(course.getM_sName() + course.getM_iNum());
                }

            //根据当前循环次数判断当前为第几行，并将其加入temp中
            switch (i + 1) {
                case 1 -> temp.setSec("一");
                case 2 -> temp.setSec("二");
                case 3 -> temp.setSec("三");
                case 4 -> temp.setSec("四");
                case 5 -> temp.setSec("五");
                case 6 -> temp.setSec("六");
                case 7 -> temp.setSec("七");
                case 8 -> temp.setSec("八");
                case 9 -> temp.setSec("九");
                case 10 -> temp.setSec("十");
                case 11 -> temp.setSec("十一");
                case 12 -> temp.setSec("十二");
                case 13 -> temp.setSec("十三");
                case 14 -> temp.setSec("十四");
            }
            haha.add(temp);
        }
        return haha;
    }

    /**
     * 获得某个节次的课程列表（行
     * @return 一行中的所有课程
     */
    protected ArrayList<Course> getLocation(int num) {
        ArrayList<Course> results = new ArrayList<>();
        //存储节次的表
        int[] values;
        for (Course course : this.courses) {
            String times = String.valueOf(course.getM_tTime().getStartHour()) +
                    course.getM_tTime().getStartMinute() +
                    course.getM_tTime().getEndHour() +
                    course.getM_tTime().getEndMinute();
            values = getSex(times);
            if (Contains(num, values))
                results.add(course);
        }
        return results;
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
}
