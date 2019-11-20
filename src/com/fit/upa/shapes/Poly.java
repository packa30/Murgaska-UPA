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
import javafx.scene.shape.Polyline;

import java.io.IOException;
import java.util.Arrays;

public class Poly extends Polyline {
    public boolean enableEdit = false;
    public Circle[] cs;
    public String name;
    public Double[] ordinates;
    public Group poly = new Group();
    public Group root;
    public Poly(double[] ordinates, String name, Group g){
        this.name = name;
        //this.ordinates = ordinates;
        this.root = g;
        int pointsCount = ordinates.length/2-1;
        this.ordinates = new Double[pointsCount*2];
        Circle[] polyCorners = new Circle[pointsCount];
        for(int i=0; i<pointsCount; i++){
            this.ordinates[2*i] = ordinates[2*i];
            this.ordinates[2*i+1] = ordinates[2*i+1];
            polyCorners[i] = new Circle(ordinates[2*i], ordinates[2*i+1], 0);
        }
        this.cs = polyCorners;
        for(Circle c: polyCorners){
            c.setFill(Color.TOMATO.deriveColor(1,1,1,0.5));
            c.setStroke(Color.TOMATO);
            poly.getChildren().add(c);
        }
        System.out.println(Arrays.toString(this.ordinates));
        super.getPoints().addAll(this.ordinates);
        root.getChildren().add(poly);
        setFill(Color.DARKGRAY.deriveColor(1,1,1,1));
        setStroke(Color.GRAY);
        enableDrag(root, this);
    }

    public void setEnableEdit(boolean state){
        enableEdit = state;
        if(state){
            root.toFront();
            poly.toFront();
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

    private void enableDrag(Group g, Poly owner) {
        g.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println(name);
            }
        });
        g.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("polyInfo.fxml")));
                    PolyInfo.getInstance().setOwner(owner);
                    PolyInfo.getInstance().setName(name);
                    if(owner.enableEdit){
                        PolyInfo.getInstance().setSelect();
                    }
                    for(int i = 0; i < ordinates.length/2; i++) {
                        PolyInfo.getInstance().getGPane().addRow(i, new Label("point"+i), new TextField(String.valueOf(ordinates[2*i])), new TextField(String.valueOf(ordinates[2*i+1])));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ;

            }
        });
    }
}
