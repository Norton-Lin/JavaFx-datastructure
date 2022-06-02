package com.example.demo1;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        //实例化第一个Controller，它加载hello-view.fxml
        HelloController controller = new HelloController();

        //展示新窗口
        controller.showStage();
    }
    public static void main(String[] args) {
        launch(args);
    }
}