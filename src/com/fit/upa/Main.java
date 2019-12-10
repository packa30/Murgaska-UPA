package com.fit.upa;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import oracle.jdbc.driver.DBConversion;

import java.util.ArrayList;

public class Main extends Application {
    public static Main instance;
    public Parent root;
    public Stage stage;

    public Main(){
        instance = this;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        login.getInstance().setStage(stage);

        Scene scene = new Scene(root ,1024, 768);

        //setting color to the scene
        scene.setFill(Paint.valueOf("f5f5f5"));

        //Setting the title to Stage.
        stage.setTitle("Login");

        //Adding the scene to Stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();

        //TODO

//        Creating a Scene by passing the group object, height and width

//        if(DbConnection.getInstance().isConnected()){
//            root = FXMLLoader.load(getClass().getResource("elemSelect.fxml"));
//            scene = new Scene(root ,1024, 768);
//
//            //setting color to the scene
//            scene.setFill(Paint.valueOf("f5f5f5"));
//
//            //Setting the title to Stage.
//            primaryStage.setTitle("Sample Application");
//
//            //Adding the scene to Stage
//            primaryStage.setScene(scene);
//
////        Displaying the contents of the stage
//            primaryStage.show();
//        }
    }
}