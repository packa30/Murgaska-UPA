package com.fit.upa;

import com.fit.upa.shapes.Shapes;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class CreateMenu {
    public static CreateMenu instance;
    public boolean active;

    public Group group;

    @FXML
    private AnchorPane pane;

    private AnchorPane scene;

    public CreateMenu(){
        instance = this;
    }

    public static CreateMenu getInstance(){
        return instance;
    }

    private ArrayList<Integer> listOfPoints;
    private ArrayList<Circle>  listOfCircles;
    private ArrayList<Line>    listOfLines;

    public void initialize(){
        active = true;
        group = Main.drawGroup;

        scene = (AnchorPane) group.getParent();
        listOfPoints  = new ArrayList<>();
        listOfCircles = new ArrayList<>();
        listOfLines   = new ArrayList<>();

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(active){
                    insertPoints(event.getX(), event.getY());
                    createPoint((int)event.getX(),(int)event.getY());
                }
            }
        });

        group.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Hello");
            }
        });
        System.out.println(getClass());
    }

    private void insertPoints(double x, double y){
        listOfPoints.add((int)(x/3.8));
        listOfPoints.add((int)(y/3.8));
    }

    private void createPoint(int x, int y){
        Circle circle = new Circle(x,y,10.f);
        circle.setFill(javafx.scene.paint.Color.TOMATO.deriveColor(1,1,1,0.5));
        circle.setStroke(Color.TOMATO);
        listOfCircles.add(circle);
        group.getChildren().add(circle);
/*
        if(listOfPoints.size() > 3){
            Line line = new Line(
                    listOfPoints.get((int)(listOfPoints.size()-4))*3.8,
                    listOfPoints.get((int)(listOfPoints.size()-3))*3.8,
                    x,y);

            listOfLines.add(line);
            group.getChildren().add(line);
        }
*/
    }

    private void removeElems(){
        listOfCircles.forEach((circle) -> group.getChildren().remove(circle));
        listOfLines.forEach((line) -> group.getChildren().remove(line));
    }

    public void onClick(){
        active = false;
        removeElems();
        try {
            pane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("mainMenu.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
