package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //实例化第一个Controller，它加载hello-view.fxml
        HelloController controller = new HelloController();

        //展示新窗口
        controller.showStage();
    }
    public static void main(String[] args) {
        launch(args);
    }
}