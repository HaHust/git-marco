package com.example.gitmacro;

import com.example.gitmacro.component.StoreComponent;
import com.example.gitmacro.model.DataConfig;
import com.example.gitmacro.model.GlobalData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void init() throws Exception {
        super.init();
        GlobalData.getInstance().setDataConfig(DataConfig.loadConfig("config.properties"));
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        StoreComponent.getInstance().setScene(scene);
        StoreComponent.getInstance().startComponent();
        GlobalData.getInstance().render();

    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        GlobalData.getInstance().getDataConfig().saveConfig("config.properties");
        super.stop();
    }
}