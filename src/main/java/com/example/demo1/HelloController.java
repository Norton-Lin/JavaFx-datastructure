package com.example.demo1;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Mysql.AccountDatabase;
import com.example.demo1.Code.Util.Authority;
import com.example.demo1.Code.entity.account.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloController {
    private final Stage thisStage;

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button submitButton;
    @FXML
    public Label buttonStatusText;

    private final Account account;

    public HelloController() {
        //创建新Stage
        thisStage = new Stage();

        account = new Account();

        //加载FXML文件
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

            //将本类设置为Controller
            loader.setController(this);

            //加载本场景
            thisStage.setScene(new Scene(loader.load(), 350, 280));

            //建立窗口
            thisStage.setTitle("课程管理导航系统-肆贰贰型");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示在Constructor中加载的Stage
     */
    public void showStage() {
        thisStage.show();
    }

    /**
     * 这个函数允许你搭建你的场景，添加行为和配置结点等等操作
     */
    @FXML
    private void initialize() {
        submitButton.setOnAction(event -> GetSubmit());
    }

    private void GetSubmit() {
        //创建数据库
        AccountDatabase database = new AccountDatabase();

        //将本类中的账户的账号密码设置为输入的账号密码
        account.setM_sID(usernameField.getText());
        account.setPassword(passwordField.getText());

        //登录成功的场景
        if (database.findByPassword(account)) {
            LogFile.info("User" + this.account.getID(),"登录");
        buttonStatusText.setText("欢迎使用捏！");
        MainViewPort_Controller mainViewPort_controller = new MainViewPort_Controller(this);
        //本页面隐藏
        thisStage.hide();
        mainViewPort_controller.showStage();
        }
       else
        //登陆失败的场景
         buttonStatusText.setText("很遗憾，账号不存在或密码错误了捏……");
    }

    public Account getAccount() {
        return this.account;
    }
}