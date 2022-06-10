package com.example.demo1;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Mysql.HomeWorkDatabase;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.FuzzySearch;
import com.example.demo1.Code.entity.Homework;
import com.example.demo1.Code.entity.Search;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.submit.HomeworkOperation;
import com.example.demo1.Code.submit.MaterialOperation;
import com.example.demo1.Code.systemtime.SystemTime;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HomeAndMaterialController {
    private final Stage thisStage;
    private final MainViewPort_Controller controller;
    //当前页面学生账户
    StudentAccount studentAccount;
    //存储当前学生课表
    ArrayList<Course> courses;
    //存储查询结果
    Course result;
    //存储某个学生某一门课的所有作业
    ArrayList<Homework> homeworks;
    //存储查询作业的结果
    Homework homework;

    @FXML
    public TextField ToBe;
    @FXML
    public Button Search;
    @FXML
    public TextField HomeworkName;
    @FXML
    public Button AssureHomework;
    @FXML
    public TextArea Info;
    @FXML
    public Button LookForHomework;
    @FXML
    public Button Upload_Homework;
    @FXML
    public Button Upload_Resources;
    @FXML
    public Button BackToMain;

    public HomeAndMaterialController(MainViewPort_Controller controller) {
        //得到新Controller
        this.controller = controller;

        //创建新场景
        thisStage = new Stage();

        this.studentAccount = new StudentAccount(this.controller.getAccount());

        this.courses = this.studentAccount.getCourse();

        this.homeworks = new ArrayList<>();

        try {
            //加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomeWorkAndMaterial.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 1000, 400));
            thisStage.setTitle("欢迎来到上传作业界面~");
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
        this.BackToMain.setOnAction(event -> BackToMain());
        this.Search.setOnAction(event -> setSearch());
        this.LookForHomework.setOnAction(event -> handleLookForHomework());
        this.Upload_Homework.setOnAction(event -> setUpload_Homework());
        this.Upload_Resources.setOnAction(event -> setUpload_Resources());
        this.AssureHomework.setOnAction(event -> setAssureHomework());
    }

    /**
     * 回到主界面
     */
    protected void BackToMain() {
        SystemTime.restartTime();

        //将第二个界面展示出来
        this.controller.showStage();

        //本页面隐藏
        thisStage.hide();
    }

    /**
     * 查找课程
     */
    protected void setSearch() {
        HomeWorkDatabase database = new HomeWorkDatabase();
        if (!this.ToBe.getText().isEmpty()) {
            try {
                int Num0 = Integer.parseInt(this.ToBe.getText());
                Search search = new Search();
                int Num1 = search.BinaryCourseSearch(Num0, this.courses);
                if (Num1 != this.courses.size()) {
                    this.result = this.courses.get(Num1);
                    this.Info.setText("您要查看的课程是否是：" + this.result.toString() + "\n");
                    //查找并得到课程、账号对应的作业
                    database.find(this.result, this.homeworks, studentAccount);
                    LogFile.info("Student" + this.studentAccount.getID(),"学生在上传作业页面查找课程");
                } else {
                    this.Info.setText("查找失败！");
                }
            } catch (NumberFormatException e) {
                String name = this.ToBe.getText();
                FuzzySearch fuzzySearch = new FuzzySearch();
                ArrayList<Course> temp = fuzzySearch.get_FS_result(name, this.courses);
                if (temp != null) {
                    this.result = temp.get(0);
                    //查找并得到课程、账号对应的作业
                    database.find(this.result, this.homeworks, studentAccount);
                    this.Info.setText("您要操作的课程是否是：" + this.result.toString() + "\n");
                    LogFile.info("Student" + this.studentAccount.getID(),"学生在上传作业页面查找课程");
                } else {
                    this.Info.setText("查找失败");
                }
            }
        } else {
            LogFile.info("Student" + this.studentAccount.getID(),"学生在上传作业页面查找课程");
            StringBuilder text = new StringBuilder();
            if (this.courses != null) {
                text.append("您的课程有：\n");
                for (Course course : this.courses) {
                    text.append(course.toString()).append("\n");
                }
                text.append("请选择某一门精确的课程");
            } else {
                text.append("您名下没有课程！");
            }
            this.Info.setText(text.toString());
        }
    }

    /**
     * 查询并选中自己要交的作业
     */
    protected void setAssureHomework() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生查找并选中某一门课的作业");
        String temp = this.HomeworkName.getText();
        if (!temp.isEmpty()) {
            if (!this.homeworks.isEmpty()) {
                boolean mark = false;
                for (Homework tool : homeworks) {
                    if (tool.getM_iName().equals(temp)) {
                        this.homework = tool;
                        mark = true;
                        break;
                    }
                }
                if (mark)
                    this.Info.setText("您要找的是否是" + this.homework.getM_iName());
                else
                    this.Info.setText("查找失败！");
            } else {
                this.Info.setText("该课程没有作业");
            }
        } else {
            this.Info.setText("请输入作业名！");
        }
    }

    /**
     * 查询作业
     */
    protected void handleLookForHomework() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生查看某门课程的所有作业");
        StringBuilder results = new StringBuilder();
        if (this.result != null) {
            if (!homeworks.isEmpty()) {
                results.append(this.result.getM_sName()).append("编号为").append(this.result.getM_iNum()).append("的作业有：").append("\n");
                for (Homework tool : this.homeworks) {
                    results.append(tool.getM_iName()).append("\t");
                    if (tool.getM_iTag() == 0)
                        results.append("未交");
                    else
                        results.append("已交");
                    results.append("\n");
                }
                this.Info.setText(results.toString());
            } else
                this.Info.setText("当前课程无作业");
        } else {
            this.Info.setText("未选中课程！无法操作");
        }
    }

    /**
     * 上传作业
     */
    protected void setUpload_Homework() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生上传作业");
        if (this.homework != null) {
            String address = "D://Homework" + "//" + this.result.getM_iNum() + "//" + this.homework.getM_iName();
            HomeWorkDatabase database = new HomeWorkDatabase();
            HomeworkOperation.HomeworkPort(address);
            if (homework.getM_iTag() != 1) {
                homework.setM_iTag(1);
                database.insert(this.result, this.homework, studentAccount);
            }
        } else {
            this.Info.setText("未选中作业，请重试！");
        }
    }

    /**
     * 上传资料
     */
    protected void setUpload_Resources() {
        LogFile.info("Student" + this.studentAccount.getID(),"学生上传资料");
        if (this.result != null) {
            String resourcePath = this.result.getM_sData();
            MaterialOperation.MaterialPort(resourcePath);
        } else
            this.Info.setText("未选中课程，请重试！");
    }
}
