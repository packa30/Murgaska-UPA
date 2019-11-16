package com.fit.upa;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    public static void main(String[] args) {
        DbConnection dbConn = DbConnection.getInstance();
        dbConn.connect();
        if(dbConn.isConnected()){
            System.out.println("we are successfuly connected");
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DbConnection dbConn = DbConnection.getInstance();
        System.out.println(dbConn.isConnected());

        ArrayList<ObjectsInDB> arrayList = dbConn.query("SELECT c.name, c.geometry FROM city c");
        System.out.println(arrayList);
        for(ObjectsInDB elem : arrayList) {
            System.out.println(">>" + elem.info());
        }
        //creating a Group object
        Group group = new Group();

        Scale scale = new Scale(); //origin to bottom left corner
        scale.setY(-1);
        scale.pivotYProperty().bind(Bindings.createDoubleBinding(() -> group.getBoundsInLocal().getMaxY()+group.getBoundsInLocal().getHeight()/2,group.boundsInLocalProperty()));
        group.getTransforms().add(scale);


        Shapes shapes = new Shapes(arrayList, group);

        //Creating a Scene by passing the group object, height and width
        Scene scene = new Scene(group ,600, 300);

        //setting color to the scene
        scene.setFill(Paint.valueOf("f5f5f5"));

        //Setting the title to Stage.
        primaryStage.setTitle("Sample Application");

        //Adding the scene to Stage
        primaryStage.setScene(scene);

        //Displaying the contents of the stage
        primaryStage.show();
    }
}
