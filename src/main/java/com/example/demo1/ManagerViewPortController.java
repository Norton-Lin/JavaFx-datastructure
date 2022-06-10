package com.example.demo1;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Mysql.AccountDatabase;
import com.example.demo1.Code.Mysql.ConstructionDatabase;
import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.Util.MyException;
import com.example.demo1.Code.Util.Property;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.Construction;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.Navigate;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.Account;
import com.example.demo1.Code.entity.account.ManagerAccount;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.systemtime.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    //存储要操作的学生账户
    StudentAccount studentAccount;

    //存储待添加课程
    Course ToBeOperated;

    //存储所有建筑物的列表
    ArrayList<Construction> allCons;

    //存储管理员账号
    ManagerAccount managerAccount;

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
    public TextArea AllCourses;
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

        //用来存储新建的课程
        this.course_alter = new Course();

        this.managerAccount = new ManagerAccount(this.controller.getAccount());

        //实例化数据库
        CourseDatabase courseDatabase1 = new CourseDatabase();

        //获取所有课程的列表
        courseDatabase1.find(this.courses);

        //实例化建筑物数据库
        ConstructionDatabase database = new ConstructionDatabase();

        this.allCons = new ArrayList<>();
        //获取所有建筑物
        database.find(this.allCons);

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
        this.Delete2.setOnAction(event -> DeleteFromStu());
    }

    /**
     * 回到主界面
     */
    protected void BackToMainMenu() {
        LogFile.info("Manager" + this.managerAccount.getID(),"管理员构建活动");
        SystemTime.restartTime();
        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        this.thisStage.hide();
    }

    /**
     * 根据管理员输入的课程编号查找课程
     */
    protected void SearchForCourse() {
        LogFile.info("Manager" + this.managerAccount.getID(),"管理员查找课程");
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
                             "星期" + this.course.getM_cExamTime().getWeek() + "\t"
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

        try {
            //从用户输入中读取时间信息
            if (!mark) {
                month = Integer.parseInt(this.Month.getText());
                week = Integer.parseInt(this.Week.getText());
                date = Integer.parseInt(this.Day.getText());
            } else {
                week = Integer.parseInt(this.Week.getText());
            }
            sth = Integer.parseInt(this.StartHour.getText());
            stm = Integer.parseInt(this.StartMinute.getText());
            enh = Integer.parseInt(this.EndHour.getText());
            enm = Integer.parseInt(this.EndMinute.getText());

            if (mark) {
                //判断输入是否合法
                if (sth <= 0 || stm < 0 || enh < 0 || enm < 0 || stm > 60 || sth > 24 || enh > 24 || enm > 60 || week <=0 || week > 7)
                    throw new NumberFormatException();

                //构建时间类型对象，本对象为上课时间，仅需包含星期与起止时间
                time2 = new Time(sth, stm, enh, enm, week);

                LogFile.info("Manager" + this.managerAccount.getID(),"管理员设置" + this.course.toString() + "的上课时间");
                this.course.setM_tTime(time2);
            } else {
                //判断输入是否合法
                if (month <= 0 || month > 12 || week < 1 || week > 7 || date > 31 || date <= 0 || sth < 0 || stm < 0 || enh < 0 || enm < 0 || stm > 60 || sth > 24
                        || enh > 24 || enm > 60)
                    throw new NumberFormatException();

                //构建时间类型对象，本对象为考试时间，需要包含月日星期等信息
                time1 = new Time(sth, stm, enh, enm, month, date, week);

                LogFile.info("Manager" + this.managerAccount.getID(),"管理员设置" + this.course.toString() + "的考试时间");
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

    /**
     * 设置上课地点或考试地点
     * @param mark 上课信息还是考试信息的标记
     */
    protected void SetConstruction(boolean mark) {

        CourseDatabase database = new CourseDatabase();

        Construction temp = new Construction();
        try {
            try {
                int Con_name = Integer.parseInt(this.Building.getText());
                if (Con_name >=0 && Con_name < this.allCons.size())
                    for (Construction tool : allCons) {
                        if (tool.get_con_number() == Con_name) {
                            temp = tool;
                            break;
                        }
                    }
                else
                    throw new MyException(0);
            } catch (NumberFormatException e) {
                String ConName = this.Building_Alter.getText();
                boolean flag = false;
                for (Construction tool : this.allCons) {
                    if (tool.get_con_name().equals(ConName)) {
                        temp = tool;
                        mark = true;
                        break;
                    }
                }
                if (!flag)
                    throw new MyException(0);
            }
        } catch (MyException e1) {
            this.ErrorInfo.setText(e1.getMessage());
        }

        try {
            if (temp.get_con_name() != null) {
                //根据按钮不同设置上课地点或考试地点
                if (mark) {
                    this.course.setM_sConstruction(temp);
                    this.course.setM_iFloor(Integer.parseInt(this.Floor.getText()));
                    this.course.setM_iRoom(Integer.parseInt(this.Room.getText()));
                    LogFile.info("Manager" + this.managerAccount.getID(),"管理员设置" + this.course.toString() + "的上课地点");
                } else {
                    this.course.setM_cExamConstruction(temp);
                    this.course.setM_iExamFloor(Integer.parseInt(this.Floor.getText()));
                    this.course.setM_iExamRoom(Integer.parseInt(this.Room.getText()));
                    LogFile.info("Manager" + this.managerAccount.getID(),"管理员设置" + this.course.toString() + "的考试地点");
                }

                database.update(this.course);
                this.Info.setText("更新成功！");
            } else
                this.ErrorInfo.setText("地点不存在！");
        } catch (NumberFormatException e) {
            this.ErrorInfo.setText("楼层与房间号请输入数字");
        }
    }

    /**
     * 根据输入的各种信息，创建全新的课程
     */
    protected void Create() throws MyException{

        //获取课程名
        String Name = this.Name.getText();
        this.course_alter.setM_sName(Name);

        //获取课程教师
        String Teacher = this.Teacher.getText();
        this.course_alter.setM_sTeacher(Teacher);

        //获取课程属性
        Property property = null;
        if (this.Theory.isSelected()) {
            property = Property.Theory;
        } else if (this.Practice.isSelected()) {
            property = Property.Practice;
        } else if (this.PE.isSelected()) {
            property = Property.Sports;
        }
        this.course_alter.setM_eProperty(property);

        //获取课程群
        String group = this.Group.getText();
        this.course_alter.setM_sCurGroup(group);

        //获取课程时间
        int week  = Integer.parseInt(this.Week_Alter.getText());
        int sth = Integer.parseInt(this.StartHour_Alter.getText());
        int stm = Integer.parseInt(this.StartMinute_Alter.getText());
        int enh = Integer.parseInt(this.EndHour_Alter.getText());
        int enm = Integer.parseInt(this.EndMinute_Alter.getText());
        Time time = new Time(sth, stm, enh, enm, week);
        this.course_alter.setM_tTime(time);
        if (week <= 0 || week > 7 || sth < 0 || sth >= 24 || stm < 0 || stm >= 60 || enh < 0 || enh >= 24 || enm < 0 || enm >= 60)
            throw new MyException(2);

        //获取课程最大人数
        int max = Integer.parseInt(this.MaxPle.getText());
        this.course_alter.setM_iMaxPle(max);

        //获取课程建筑
        Construction construction = new Construction();
        try {
            int ConNum = Integer.parseInt(this.Building_Alter.getText());
            if (ConNum >=0 && ConNum < this.allCons.size())
                for (Construction temp : allCons) {
                    if (temp.get_con_number() == ConNum) {
                        construction = temp;
                        break;
                    }
                }
            else
                throw new MyException(0);
        } catch (NumberFormatException e) {
            String ConName = this.Building_Alter.getText();
            boolean mark = false;
            for (Construction tool : this.allCons) {
                if (tool.get_con_name().equals(ConName)) {
                    construction = tool;
                    mark = true;
                    break;
                }
            }
            if (!mark)
                throw new MyException(0);
        }
        this.course_alter.setM_sConstruction(construction);

        //获取上课楼层
        int floor = Integer.parseInt(this.Floor_Alter.getText());
        if (floor > 0 && floor <= construction.get_con_room().size())
            this.course_alter.setM_iFloor(floor);
        else
            throw new MyException(2);

        //获取上课房间号
        int room = Integer.parseInt(this.Room_Alter.getText());
        if (construction.get_con_room().get(floor - 1).contains(room))
            this.course_alter.setM_iRoom(room);
        else
            throw new MyException(2);

        //设置一个自增的编号
        int Num = this.courses.get(this.courses.size() - 1).getM_iNum() + 1;
        this.course_alter.setM_iNum(Num);

    }

    /**
     * 管理员预览自己构建的课程信息
     */
    protected void PreViewCour() {
        LogFile.info("Manager" + this.managerAccount.getID(),"管理员预览自己构建的课程信息");
        try {
            Create();
            String text = "课程信息：" + this.course_alter.getM_sName() + "\t" +
                    "教师为：" + this.course_alter.getM_sTeacher() + "\t" +
                    "编号为：" + this.course_alter.getM_iNum() + "\n" +
                    "上课时间为：" + "星期" + this.course_alter.getM_tTime().getWeek() + "\t" +
                    this.course_alter.getM_tTime().getStartHour() + "时" +
                    this.course_alter.getM_tTime().getStartMinute() + "分" + "~" +
                    this.course_alter.getM_tTime().getEndHour() + "时" +
                    this.course_alter.getM_tTime().getEndMinute() + "分" + "\n" +
                    "上课地点为：" + this.course_alter.getM_sConstruction() +
                    this.course_alter.getM_iFloor() + "层" +
                    this.course_alter.getM_iRoom() + "室" + "\t" +
                    "课程群号：" + this.course_alter.getM_sCurGroup() + "\n" +
                    "是否确认添加？";
            this.Info.setText(text);
        } catch (NumberFormatException e) {
            this.ErrorInfo.setText("输入异常，请重试");
        } catch (MyException e1) {
            this.ErrorInfo.setText(e1.getMessage());
        }
    }

    /**
     * 从数据库中删除课程
     */
    protected void DeleteFromDatabase() {
        CourseDatabase courseDatabase = new CourseDatabase();
        Search search = new Search();
        try {
            int Num = Integer.parseInt(this.Name.getText());
            ArrayList<Course> courses0 = new ArrayList<>();
            courseDatabase.find(courses0);
            int tool = search.BinaryCourseSearch(Num, courses0);
            if (tool != courses0.size()) {
                courseDatabase.delete(courses0.get(tool));
                this.Info.setText(courses0.get(tool).getM_sName() + "编号为" + courses0.get(tool).getM_iNum() + "删除成功");
                LogFile.info("Manager" + this.managerAccount.getID(),"管理员删除" + this.course.toString());
            } else {
                this.Info.setText("查找与删除失败");
            }
        } catch (NumberFormatException e) {
            this.ErrorInfo.setText("若要删除课程请在课程名一栏输入编号！");
        }
    }

    /**
     * 添加一门全新的课程，并更新数据库
     */
    protected void AddANewCourse() {
        try {
            Create();

            CourseDatabase courseDatabase = new CourseDatabase();

            if (this.courses.contains(this.course_alter))
                this.ErrorInfo.setText("课程已存在！");
            else {

                String dirName0 = "D://Homework" + "//" + this.course_alter.getM_iNum();
                String dirName1 = "D://Resources" + "//" + this.course_alter.getM_iNum();
                File file0 = new File(dirName0);
                File file1 = new File(dirName1);
                boolean mark = file0.mkdir();
                boolean mark1 = file1.mkdir();
                this.course_alter.setM_sData(dirName1);
                if (!mark || !mark1)
                    this.ErrorInfo.setText("创建课程文件管理区失败！");
                courseDatabase.insert(this.course_alter);
                this.Info.setText("成功添加" + this.course_alter.getM_sName());
                this.courses.add(this.course_alter);
                LogFile.info("Manager" + this.managerAccount.getID(),"管理员添加了" + this.course.toString());
            }
        } catch (NumberFormatException e) {
            this.ErrorInfo.setText("输入异常，请重试");
        } catch (MyException e1) {
            this.ErrorInfo.setText(e1.getMessage());
        }
    }

    /**
     * 根据输入获得待操作学生账户和课程
     */
    protected void WriteIn() {
        try {
            AccountDatabase accountDatabase = new AccountDatabase();
            Account account = new Account();
            account.setM_sID(this.Stu_ID.getText());
            Search search = new Search();
            int tool = Integer.parseInt(this.Course_ID.getText());
            if (accountDatabase.findByName(account)) {
                //获得待操作学生账号
                this.studentAccount = new StudentAccount(account);
                //修改待操作课程编号
                int Num = search.BinaryCourseSearch(tool, this.courses);
                if (Num != this.courses.size()) {
                    this.ToBeOperated = this.courses.get(Num);
                } else
                    this.AllCourses.setText("查找失败！课程不在库中！");
            } else {
                this.ErrorInfo.setText("该学生不存在");
            }
        } catch (Exception e) {
            this.ErrorInfo.setText("输入信息异常！");
        }
    }

    /**
     * 预览自己即将进行的向学生账户添加课程操作
     */
    protected void PreViewCourWithStu() {
        WriteIn();
        StringBuilder text = new StringBuilder();
        text.append("账号").append(this.studentAccount.getID()).append("的课程有：").append("\n");
        for (Course course : this.studentAccount.getCourse()) {
            text.append(course.getM_sName()).append("\t")
                    .append("教师为：").append(course.getM_sTeacher()).append("\t")
                    .append("编号为：").append(course.getM_iNum()).append("\n");
        }
        text.append("是否将").append(this.ToBeOperated.getM_sName()).append("\t")
                .append("编号为：").append(this.ToBeOperated.getM_iNum())
                .append("添加到学生账户").append(this.studentAccount.getID());
        this.AllCourses.setText(text.toString());
        LogFile.info("Manager" + this.managerAccount.getID(),"管理员预览" + this.course.toString() + "与待添加学生" + this.studentAccount.getID());
    }

    /**
     * 向学生账户中添加课程
     */
    protected void AddCourseToStu() {
        WriteIn();
        this.AllCourses.setText(this.studentAccount.registerCourse(this.ToBeOperated));
        LogFile.info("Manager" + this.managerAccount.getID(),"管理员添加" + this.course.toString() + "到" + this.studentAccount.getID());
    }

    /**
     * 从学生账户中删除课程
     */
    protected void DeleteFromStu() {
        WriteIn();
        this.AllCourses.setText(this.studentAccount.exitCourse(this.ToBeOperated));
        LogFile.info("Manager" + this.managerAccount.getID(),"管理员从" + this.studentAccount.getID() + "删除" + this.course.toString());
    }
}

