package com.example.demo1;

import com.example.demo1.Code.Util.Traffic;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.demo1.Code.entity.*;
import javafx.stage.Stage;

import java.io.IOException;

public class NavController {
    //从主界面继承而来的信息
    private final MainViewPort_Controller mainViewPort_controller;

    //本界面
    private final Stage thisStage;

    //单选组
    final ToggleGroup toggleGroup;

    //本页面中的各种元素
    @FXML
    public TextField StartPoint;
    @FXML
    public TextField EndPoint;
    @FXML
    public RadioButton Walk = new RadioButton();
    @FXML
    public RadioButton Bicycle = new RadioButton();
    @FXML
    public RadioButton Electric = new RadioButton();
    @FXML
    public RadioButton Car = new RadioButton();
    @FXML
    public Button submitButton;
    @FXML
    public TextArea ResOfNav;
    @FXML
    public Button backToMain;

    public NavController(MainViewPort_Controller mainController) {
        //将主界面的信息继承来
        this.mainViewPort_controller = mainController;

        //为本界面的单选按钮创建组
        this.toggleGroup = new ToggleGroup();

        //本界面的展开
        thisStage = new Stage();

        //加载FXML文件
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Nav.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 600, 400));
            thisStage.setTitle("欢迎使用导航系统~");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳出页面
     */
    public void showStage() {
        thisStage.show();
    }

    @FXML
    private void initialize() {
        submitButton.setOnAction(event -> handleSubmitButtonAction());
        backToMain.setOnAction(event -> handleBackAction());
    }

    protected void handleSubmitButtonAction() {
//        ArrayList<String> test = new ArrayList<>();
//        test.add("你好啊\n");
//        test.add("我是不烂尾石上超\n");
//        test.add("我是一个原批，舟批，π批\n");
//        test.add("希望你能喜欢我\n");
//        test.add("想找1\n");

        //默认交通方式为步行
        int traffic = 0;

        if (Bicycle.isSelected()) {
            traffic = 1;
        } else if (Electric.isSelected()) {
            traffic = 2;
        } else if (Car.isSelected()) {
            traffic = 3;
        }

        Navigate navigate = new Navigate();
        ResOfNav.setText(navigate.toNavigate(traffic, StartPoint.getText(), EndPoint.getText()).toString());
//        ResOfNav.setText(test.toString());
    }

    protected void handleBackAction() {

        //将第二个界面展示出来
        mainViewPort_controller.showStage();

        //本页面隐藏
        thisStage.hide();
    }
}
