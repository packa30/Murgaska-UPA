package com.fit.upa;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

public class Shapes{


    Group root;
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    public Shapes(ArrayList<ObjectsInDB> arrayList, Group g, AnchorPane anch2){
        root = g;
        for(ObjectsInDB elem : arrayList) {
            if(elem.type == 3){
                Group rG = new Group();
                g.getChildren().add(rG);
                shapes.add(new Rec(elem.ordinates,elem.name, rG, anch2));
                Rec r = (Rec)shapes.get(shapes.size() - 1);
                System.out.println(r.name);
                rG.getChildren().add(r);
            }
            else if(elem.type == 1){
                Group pG = new Group();
                g.getChildren().add(pG);
                shapes.add(new Poly(elem.ordinates, elem.name, pG));
                Poly p = (Poly) shapes.get(shapes.size() - 1);
                System.out.println(p.name);
                pG.getChildren().add(p);
            }
        }
    }

    class Poly extends Polyline{
        String name;
        Double[] ordinates;
        Group poly = new Group();
        public Poly(double[] ordinates, String name, Group g){
            this.name = name;
            //this.ordinates = ordinates;
            int pointsCount = ordinates.length/2-1;
            this.ordinates = new Double[pointsCount*2];
            Circle[] polyCorners = new Circle[pointsCount];
            for(int i=0; i<pointsCount; i++){
                this.ordinates[2*i] = ordinates[2*i];
                this.ordinates[2*i+1] = ordinates[2*i+1];
                polyCorners[i] = new Circle(ordinates[2*i], ordinates[2*i+1], 0);
            }
            for(Circle c: polyCorners){
                c.setFill(Color.TOMATO.deriveColor(1,1,1,0.5));
                c.setStroke(Color.TOMATO);
                poly.getChildren().add(c);
            }
            System.out.println(Arrays.toString(this.ordinates));
            super.getPoints().addAll(this.ordinates);
            g.getChildren().add(poly);
            setFill(Color.DARKGRAY.deriveColor(1,1,1,1));
            setStroke(Color.GRAY);
            enableDrag(g);
        }
        private void enableDrag(Group g) {
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
                        ElemSelect.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("objInfo.fxml")));
                        ObjInfo.getInstance().setName(name);
                        for(int i = 0; i < ordinates.length/2; i++) {
                            ObjInfo.getInstance().getGPane().addRow(i, new Label("point"+i), new TextField(String.valueOf(ordinates[2*i])), new TextField(String.valueOf(ordinates[2*i+1])));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ;

                }
            });
        }
    }

    class Rec extends Rectangle {

        String name;
        double[] ordinates;
        Group rec = new Group();
        public Rec(double[] ordinates, String name, Group g, AnchorPane a){
            this.name = name;
            this.ordinates = ordinates;
            Circle[] recCorners= {new Circle(ordinates[0], ordinates[1], 0),
                    new Circle(ordinates[2], ordinates[3], 0)};
            for(Circle c: recCorners){
                c.setFill(Color.TOMATO.deriveColor(1,1,1,0.5));
                c.setStroke(Color.TOMATO);
                rec.getChildren().add(c);
            }
            super.setX(ordinates[0]);
            super.setY(ordinates[1]);
            super.setWidth(ordinates[2]-ordinates[0]);
            super.setHeight(ordinates[3]-ordinates[1]);

            g.getChildren().add(rec);

            setFill(Color.DARKGRAY.deriveColor(1,1,1,1));
            setStroke(Color.GRAY);
            enableDrag(g, a);
        }

        private void enableDrag(Group g, AnchorPane a) {

            g.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        ElemSelect.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("objInfo.fxml")));
                        ObjInfo.getInstance().setName(name);
                        //for(int i = 0; i < 2; i++) {
                            ObjInfo.getInstance().getGPane().addRow(0, new Label("point0"), new TextField(String.valueOf(ordinates[0])), new TextField(String.valueOf(ordinates[1])));
                            ObjInfo.getInstance().getGPane().addRow(1, new Label("point1"), new TextField(String.valueOf(ordinates[2])), new TextField(String.valueOf(ordinates[3])));
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

}
