package com.fit.upa.shapes;

import com.fit.upa.MainMenu;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class Rec extends Rectangle {
    public boolean enableEdit = false;
    public String name;
    public double[] ordinates;
    public Group rec = new Group();
    public Group root;
    public Circle[] cs;
    public Rec(double[] ordinates, String name, Group g){
        this.root = g;
        this.name = name;
        this.ordinates = ordinates;
        Circle[] recCorners= {new Circle(ordinates[0], ordinates[1], 0),
                new Circle(ordinates[2], ordinates[3], 0)};
        for(Circle c: recCorners){
            c.setFill(Color.TOMATO.deriveColor(1,1,1,0.5));
            c.setStroke(Color.TOMATO);
            rec.getChildren().add(c);
        }
        this.cs =recCorners;
        super.setX(ordinates[0]);
        super.setY(ordinates[1]);
        super.setWidth(ordinates[2]-ordinates[0]);
        super.setHeight(ordinates[3]-ordinates[1]);

        g.getChildren().add(rec);

        setFill(Color.DARKGRAY.deriveColor(1,1,1,1));
        setStroke(Color.GRAY);
        enableDrag(g,this);
    }

    public void setEnableEdit(boolean state){
        enableEdit = state;
        if(state){
            root.toFront();
            rec.toFront();
            for(Circle c: cs){
                c.setRadius(10);
                c.toFront();
            }
        }else {
            for(Circle c: cs){
                c.setRadius(0);
            }
        }
    }

    private void enableDrag(Group g, Rec owner) {

        g.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("recInfo.fxml")));
                    RecInfo.getInstance().setOwner(owner);
                    RecInfo.getInstance().setName(name);

                    if(owner.enableEdit){
                        RecInfo.getInstance().setSelect();
                    }

                    //for(int i = 0; i < 2; i++) {
                    RecInfo.getInstance().getGPane().addRow(0, new Label("point0"), new TextField(String.valueOf(ordinates[0])), new TextField(String.valueOf(ordinates[1])));
                    RecInfo.getInstance().getGPane().addRow(1, new Label("point1"), new TextField(String.valueOf(ordinates[2])), new TextField(String.valueOf(ordinates[3])));
                    //}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        g.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(name);
            }
        });
    }



}
