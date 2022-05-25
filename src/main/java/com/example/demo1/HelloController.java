package com.example.demo1;

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

    private Account account;

    public HelloController() {
        //创建新Stage
        thisStage = new Stage();

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
//        AccountDatabase database = new AccountDatabase();

        //将本类中的账户的账号密码设置为输入的账号密码
//        account.setM_sID(usernameField.getText());
//        account.setPassword(passwordField.getText());

        //登录成功的场景
//        if (accountDatabase.findByPassword(account)) {
//        buttonStatusText.setText("欢迎使用捏！");
//        MainViewPort_Controller mainViewPort_controller = new MainViewPort_Controller(this);
//        mainViewPort_controller.showStage();
//        }
//       else
//        //登陆失败的场景
//         buttonStatusText.setText("很遗憾，密码错误了捏……");

        account = new Account();
        this.account.setM_sID(usernameField.getText());
        this.account.setPassword(passwordField.getText());
        this.account.setM_eAuthority(Authority.Teacher);

        //创建主界面的Controller，它也将加载它自己的FXML文件
        //传入的this参数允许主界面Controller访问本类中的方法
        MainViewPort_Controller mainViewPort_controller = new MainViewPort_Controller(this);

        //将第二个界面展示出来
        mainViewPort_controller.showStage();

        //本页面隐藏
        thisStage.hide();
    }

    public Account getAccount() {
        return this.account;
    }


//    @FXML
//    protected void handleSubmitButtonAction() {
//        this.account.setM_sID(usernameField.getText());
//        this.account.setPassword(passwordField.getText());
//        this.account.setM_eAuthority(Authority.Teacher);
//        openMainViewController();
//    }
//        AccountDatabase accountDatabase = new AccountDatabase();
//        Account account = new Account();
//        account.setM_sID(usernameField.getText());
//        account.setPassword(passwordField.getText());
//        if (accountDatabase.findByPassword(account)) {
//            buttonStatusText.setText("欢迎使用捏！");
//            Stage stage = (Stage) submitButton.getScene().getWindow();
//            stage.hide();
//            try {
//                new MainViewPort_Controller(account);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//       else
//            buttonStatusText.setText("很遗憾，密码错误了捏……");
//        if (passwordField.getText().equals("123")) {
//            buttonStatusText.setText("Welcome!");
//            Account account = new Account();
//            account.setM_sID(usernameField.getText());
//            account.setPassword(passwordField.getText());
//            account.setM_eAuthority(Authority.Teacher);
//            try {
//                Stage stage = showMainViewPort(account);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public Stage showMainViewPort(Account account)  throws IOException{
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainViewPort.fxml"));
//        Parent root =
//        Stage stage = new Stage();
//        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainViewPort.fxml")),
//                ResourceBundle.getBundle("com.example.demo1.Total")), 350, 280));
//
//        MainViewPort_Controller controller = loader.getController();
//        controller.setAccount(account);
//
//        stage.show();
//
//        return stage;
//    }
}