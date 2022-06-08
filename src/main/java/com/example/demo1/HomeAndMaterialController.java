package com.example.demo1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeAndMaterialController {
    private final Stage thisStage;
    private final MainViewPort_Controller controller;

    public HomeAndMaterialController(MainViewPort_Controller controller) {
        //得到新Controller
        this.controller = controller;

        //创建新场景
        thisStage = new Stage();

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
    }
}
