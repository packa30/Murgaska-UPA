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
        group = MainMenu.drawGroup;

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
        createElem("text","text2",2006);
        try {
            pane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("mainMenu.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createElem(String name, String type, int shapeType){
        final String[] sqlQuery = {"INSERT INTO map VALUES(" + name + "," + type + ", SDO_GEOMETRY("};

        if(shapeType == 2001 && listOfPoints.size() == 2){
            //Point
            sqlQuery[0] += "2001,NULL,SDO_POINT_TYPE(";
            listOfPoints.forEach((n)-> sqlQuery[0] += n.toString() +",");
            sqlQuery[0] += "NULL), NULL, NULL));";
        }else if (shapeType == 2002 && listOfPoints.size() == 4){
            //Line
            sqlQuery[0] += "2002,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,2,1), SDO_ORDINATE_ARRAY(";
            listOfPoints.forEach((n)-> sqlQuery[0] += n.toString() +",");
            sqlQuery[0]  = sqlQuery[0].substring(0,sqlQuery[0].length()-1);
            sqlQuery[0] += ")));";
        }else if(shapeType == 2003 && listOfPoints.size() == 6){
            //Circle
            sqlQuery[0] += "2003,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,1003,4), SDO_ORDINATE_ARRAY(";
            listOfPoints.forEach((n)-> sqlQuery[0] += n.toString() +",");
            sqlQuery[0]  = sqlQuery[0].substring(0,sqlQuery[0].length()-1);
            sqlQuery[0] += ")));";
        }else if(shapeType == 2003 && listOfPoints.size() > 6){
            //Polygon
            sqlQuery[0] += "2003,NULL,NULL,";
            if(type.equals("estate")){
                sqlQuery[0] += "SDO_ELEM_INFO_ARRAY(1,1003,1), SDO_ORDINATE_ARRAY(";
            }else{
                sqlQuery[0] += "SDO_ELEM_INFO_ARRAY(1,2003,1), SDO_ORDINATE_ARRAY(";
            }
            listOfPoints.forEach((n)-> sqlQuery[0] += n.toString() +",");
            sqlQuery[0]  = sqlQuery[0].substring(0,sqlQuery[0].length()-1);
            sqlQuery[0] += ")));";
        }else if(shapeType == 2006){
            int cnt = 1 + listOfPoints.size()/4;
            sqlQuery[0] += "2006,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,4," + cnt + ", ";

            for (int i = 0; i < cnt; i++){
                sqlQuery[0] += Integer.toString((1 + (i*2))) + ",2,1, ";
            }

            sqlQuery[0]  = sqlQuery[0].substring(0,sqlQuery[0].length()-1);
            sqlQuery[0] += "), SDO_ORDINATE_ARRAY(";

            for (int i = 0; i < listOfPoints.size()-2; i+=2){
                for (int j = 0; j < 4; j++){
                    sqlQuery[0] += Integer.toString(listOfPoints.get(i + j)) + " ,";
                }
            }

            sqlQuery[0]  = sqlQuery[0].substring(0,sqlQuery[0].length()-1);
            sqlQuery[0] += ")));";
        }

        System.out.println(sqlQuery[0]);
        return sqlQuery[0];
    }

}
