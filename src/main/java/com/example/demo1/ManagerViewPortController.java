package com.example.demo1;

import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Construction;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.Navigate;
import com.example.demo1.Code.entity.Search;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;

public class ManagerViewPortController {
    //控制当前Controller的场景
    private final Stage thisStage;

    //允许我们访问HelloController的方法们
    private final MainViewPort_Controller controller;

    //存储所有课程的ArrayList
    private final ArrayList<Course> courses = new ArrayList<>();

    //存储查找到的课程
    Course course;

    //管理员新建的课程
    Course course_alter;

    @FXML
    public TextField CourseNum;
    @FXML
    public Button SearchAll;
    @FXML
    public TextField Month;
    @FXML
    public TextField Week;
    @FXML
    public TextField Day;
    @FXML
    public TextField StartHour;
    @FXML
    public TextField StartMinute;
    @FXML
    public TextField EndHour;
    @FXML
    public TextField EndMinute;
    @FXML
    public Button CourTime;
    @FXML
    public Button ExamTime;
    @FXML
    public RadioButton ShaHe0 = new RadioButton();
    @FXML
    public RadioButton XiTuCheng0 = new RadioButton();
    @FXML
    public TextField Building;
    @FXML
    public TextField Floor;
    @FXML
    public TextField Room;
    @FXML
    public Button CourCon;
    @FXML
    public Button ExamCon;
    @FXML
    public Button BackToMain;
    @FXML
    public Label Info;
    @FXML
    public Label ErrorInfo;
    @FXML
    public TextField Name;
    @FXML
    public TextField Teacher;
    @FXML
    public RadioButton Theory = new RadioButton();
    @FXML
    public RadioButton Practice = new RadioButton();
    @FXML
    public RadioButton PE = new RadioButton();
    @FXML
    public TextField Week_Alter;
    @FXML
    public TextField StartHour_Alter;
    @FXML
    public TextField StartMinute_Alter;
    @FXML
    public TextField EndHour_Alter;
    @FXML
    public TextField EndMinute_Alter;
    @FXML
    public RadioButton ShaHe = new RadioButton();
    @FXML
    public RadioButton XiTuCheng = new RadioButton();
    @FXML
    public TextField Building_Alter;
    @FXML
    public TextField Floor_Alter;
    @FXML
    public TextField Room_Alter;
    @FXML
    public TextField Group;
    @FXML
    public TextField MaxPle;
    @FXML
    public Button PreView1;
    @FXML
    public Button Assure1;
    @FXML
    public Button Delete1;
    @FXML
    public TextField Stu_ID;
    @FXML
    public TextField Course_ID;
    @FXML
    public Button PreView2;
    @FXML
    public Button Assure2;
    @FXML
    public Button Delete2;

    public ManagerViewPortController(MainViewPort_Controller controller) {

        //收到了hello-view.fxml的Controller
        this.controller = controller;

        //创建新场景
        thisStage = new Stage();

        //实例化数据库
        CourseDatabase courseDatabase1 = new CourseDatabase();

        //获取所有课程的列表
        courseDatabase1.find(this.courses);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ManagerViewPort.fxml"));

            //将本文件设置为Controller
            loader.setController(this);

            //加载场景
            thisStage.setScene(new Scene(loader.load(), 1800, 400));

            //搭建窗口
            thisStage.setTitle("欢迎来到管理员界面!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        thisStage.show();
    }

    @FXML
    private void initialize() {
        this.BackToMain.setOnAction(event -> BackToMainMenu());
        this.SearchAll.setOnAction(event -> SearchForCourse());
        this.CourTime.setOnAction(event -> SetTime(true));
        this.ExamTime.setOnAction(event -> SetTime(false));
        this.CourCon.setOnAction(event -> SetConstruction(true));
        this.ExamCon.setOnAction(event -> SetConstruction(false));
        this.PreView1.setOnAction(event -> PreViewCour());
        this.Assure1.setOnAction(event -> AddANewCourse());
        this.PreView2.setOnAction(event -> PreViewCourWithStu());
        this.Assure2.setOnAction(event -> AddCourseToStu());
        this.Delete1.setOnAction(event -> DeleteFromDatabase());
    }

    /**
     * 回到主界面
     */
    protected void BackToMainMenu() {
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    /**
     * 根据管理员输入的课程编号查找课程
     */
    protected void SearchForCourse() {
        try {
            //获取用户输入的课程编号
            int Num1 = Integer.parseInt(this.CourseNum.getText());

            //实例化查找对象
            Search search = new Search();

            //获取要查找的课程编号
            int Num = search.BinaryCourseSearch(Num1, this.courses);

            //建立输出信息的字符串
            String Info;

            //当查找成功时输出课程信息
            if (Num != this.courses.size()) {
                //设置本次操作面向的课程
                this.course = this.courses.get(Num);

                Info = "您要查找的是否是" +
                        this.course.getM_sName()
                        + "\n" + "课程编号：" + this.course.getM_iNum()
                        + "\n" + "任课教师：" + this.course.getM_sTeacher()
                        + "\n" + "上课时间：" + "星期" + this.course.getM_tTime().getWeek() + this.course.getM_tTime().getStartHour() + ":"
                        + this.course.getM_tTime().getStartMinute() + "~"
                        + this.course.getM_tTime().getEndHour() + ":"
                        + this.course.getM_tTime().getEndMinute()
                        + "\n" + "上课地点：" + this.course.getM_sConstruction()
                        + this.course.getM_iFloor() + "层" + this.course.getM_iRoom() + "室" + "\n";
                if (this.course.getM_cExamTime().getWeek() > 0) {
                    Info = Info  + "考试时间：" + this.course.getM_cExamTime().getStartMonth() + "月" + this.course.getM_cExamTime().getStartDate() + "日" +
                             "星期" + this.course.getM_cExamTime().getWeek()
                             + this.course.getM_cExamTime().getStartHour() + "时"
                             + this.course.getM_cExamTime().getStartMinute() + "分" + "~"
                             + this.course.getM_cExamTime().getEndHour() + "时"
                             + this.course.getM_cExamTime().getEndMinute() + "分" + "\n";
                } else {
                    Info = Info + "考试时间未公布\n";
                }
                if (!this.course.getM_cExamConstruction().toString().equals("")) {
                    Info = Info + "考试地点：" + this.course.getM_cExamConstruction()
                             + this.course.getM_iExamFloor() + "层"
                             + this.course.getM_iExamRoom() + "室";
                } else {
                    Info = Info + "考试地点未公布";
                }
            } else {
                Info = "课程库中无此课程，查找失败！";
            }
            this.Info.setText(Info);
        } catch (NumberFormatException e) {
            this.Info.setText("请在搜索框中输入课程编号");
        } catch (Exception e) {
            this.ErrorInfo.setText("搜索出错！");
        }
    }

    /**
     * 设置/修改课程上课时间
     */
    protected void SetTime(boolean mark) {

        int month = 0, date = 0;
        int sth, stm, enh, enm, week;

        Time time1, time2;

        CourseDatabase database = new CourseDatabase();

        //从用户输入中读取时间信息
        if (!mark) {
            month = Integer.parseInt(this.Month.getText());
            week = Integer.parseInt(this.Week.getText());
            date = Integer.parseInt(this.Day.getText());
            sth = Integer.parseInt(this.StartHour.getText());
            stm = Integer.parseInt(this.StartMinute.getText());
            enh = Integer.parseInt(this.EndHour.getText());
            enm = Integer.parseInt(this.EndMinute.getText());
        } else {
            week = Integer.parseInt(this.Week.getText());
            sth = Integer.parseInt(this.StartHour.getText());
            stm = Integer.parseInt(this.StartMinute.getText());
            enh = Integer.parseInt(this.EndHour.getText());
            enm = Integer.parseInt(this.EndMinute.getText());
        }

        try {
            if (mark) {
                //判断输入是否合法
                if (sth <= 0 || stm < 0 || enh < 0 || enm < 0 || stm > 60 || sth > 24 || enh > 24 || enm > 60 || week <=0 || week > 7)
                    throw new NumberFormatException();

                //构建时间类型对象，本对象为上课时间，仅需包含起止时间
                time2 = new Time(sth, stm, enh, enm, week);

                this.course.setM_tTime(time2);
            } else {
                //判断输入是否合法
                if (month <= 0 || month > 12 || week < 1 || week > 7 || date > 31 || date <= 0 || sth < 0 || stm < 0 || enh < 0 || enm < 0 || stm > 60 || sth > 24
                        || enh > 24 || enm > 60)
                    throw new NumberFormatException();

                //构建时间类型对象，本对象为考试时间，需要包含月日星期等信息
                time1 = new Time(sth, stm, enh, enm, month, date, week);

                this.course.setM_cExamTime(time1);
            }

            database.update(this.course);
            Info.setText("发布/更新成功！");
        } catch (NullPointerException e) {
            this.ErrorInfo.setText("请先选中您要操作的课程！");
        } catch (NumberFormatException e1) {
            this.ErrorInfo.setText("请按正确的格式输入时间数字！");
        } catch (Exception e) {
            this.ErrorInfo.setText("输入错误！");
        }

    }

    protected void SetConstruction(boolean mark) {
        //建筑物查找的功能内置在Navigate中
        Navigate navigate = new Navigate();

        CourseDatabase database = new CourseDatabase();

        //根据建筑物名称找出建筑物
        Construction temp =navigate.getConstruction(this.Building.getText());

        if (this.ShaHe0.isSelected())  {
            temp.setCampus(0);
        } else  if (this.XiTuCheng0.isSelected()) {
            temp.setCampus(1);
        }

        try {
            if (temp != null) {
                //根据按钮不同设置上课地点或考试地点
                if (mark) {
                    this.course.setM_sConstruction(temp);
                    this.course.setM_iFloor(Integer.parseInt(this.Floor.getText()));
                    this.course.setM_iRoom(Integer.parseInt(this.Room.getText()));
                } else {
                    this.course.setM_cExamConstruction(temp);
                    this.course.setM_iExamFloor(Integer.parseInt(this.Floor.getText()));
                    this.course.setM_iExamRoom(Integer.parseInt(this.Room.getText()));
                }

                database.update(this.course);
                this.Info.setText("更新成功！");
            } else
                this.ErrorInfo.setText("地点不存在！");
        } catch (NumberFormatException e) {
            this.ErrorInfo.setText("楼层与房间号请输入数字");
        }
    }

    protected Course Create() throws Exception{

        Course temp;

            String Name = this.Name.getText();

            String Teacher = this.Teacher.getText();

            Property property = null;
            if (this.Theory.isSelected()) {
                property = Property.Theory;
            } else if (this.Practice.isSelected()) {
                property = Property.Practice;
            } else if (this.PE.isSelected()) {
                property = Property.Sports;
            }

            String group = this.Group.getText();

            int week  = Integer.parseInt(this.Week_Alter.getText());
            int sth = Integer.parseInt(this.StartHour_Alter.getText());
            int stm = Integer.parseInt(this.StartMinute_Alter.getText());
            int enh = Integer.parseInt(this.EndHour_Alter.getText());
            int enm = Integer.parseInt(this.EndMinute_Alter.getText());
            Time time = new Time(sth, stm, enh, enm, week);

            int campus = 0;
            if (this.ShaHe.isSelected()) {
                campus = 0;
            } else if (this.XiTuCheng.isSelected()) {
                campus = 1;
            }

            int max = Integer.parseInt(this.MaxPle.getText());

            String Building = this.Building_Alter.getText();

            Construction construction = new Construction(Building, campus);

            int floor = Integer.parseInt(this.Floor_Alter.getText());

            int room = Integer.parseInt(this.Room_Alter.getText());

            /*
             * 编号有问题！！！！！！！！
             */
            temp = new Course(Name, time, property, construction, floor, room, 0, max, 0);
            temp.setM_sTeacher(Teacher);
            temp.setM_sCurGroup(group);
        return temp;
    }

    protected void PreViewCour() {
        try {
            this.course_alter = Create();
            StringBuilder text = new StringBuilder();
            text.append("课程信息：").append(this.course_alter.getM_sName()).append("\t")
                    .append("教师为：").append(this.course_alter.getM_sTeacher()).append("\t")
                    .append("编号为：").append(this.course_alter.getM_iNum()).append("\n")
                    .append("上课时间为：").append("星期").append(this.course_alter.getM_tTime().getWeek())
                    .append(this.course_alter.getM_tTime().getStartHour()).append("时")
                    .append(this.course_alter.getM_tTime().getStartMinute()).append("分").append("~")
                    .append(this.course_alter.getM_tTime().getEndHour()).append("时")
                    .append(this.course_alter.getM_tTime().getEndMinute()).append("分").append("\n")
                    .append("上课地点为：").append(this.course_alter.getM_sConstruction())
                    .append(this.course_alter.getM_iFloor()).append("层")
                    .append(this.course_alter.getM_iRoom()).append("室").append("\t")
                    .append("课程群号：").append(this.course_alter.getM_sCurGroup()).append("\n")
                    .append("是否确认添加？");
            this.Info.setText(text.toString());
        } catch (Exception e) {
            this.ErrorInfo.setText("输入异常，请重试");
        }
    }

    protected void DeleteFromDatabase() {
        CourseDatabase courseDatabase = new CourseDatabase();
        Search search = new Search();
        int Num = Integer.parseInt(this.CourseNum.getText());
        ArrayList<Course> courses0 = new ArrayList<>();
        courseDatabase.find(courses0);
        int tool = search.BinaryCourseSearch(Num, courses0);
        if (tool != courses0.size()) {
            courseDatabase.delete(courses0.get(tool));
            this.Info.setText(courses0.get(tool).getM_sName() + "编号为" + courses0.get(tool).getM_iNum() + "删除成功");
        } else {
            this.Info.setText("查找与删除失败");
        }
    }

    protected void AddANewCourse() {
        try {
            this.course_alter = Create();
            //调用暂时有问题，先搁置
        } catch (Exception e) {
            this.ErrorInfo.setText("输入异常，请重试");
        }
    }

    protected void PreViewCourWithStu() {

    }
    protected void AddCourseToStu() {

    }
}
