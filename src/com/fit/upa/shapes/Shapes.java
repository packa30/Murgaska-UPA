package com.fit.upa.shapes;
import com.fit.upa.MainMenu;
import com.fit.upa.ObjectsInDB;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Shapes{

    public boolean shapeSelected = false;
    Group root;
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    public Shapes(ArrayList<ObjectsInDB> arrayList, Group g){
        root = g;
        for(ObjectsInDB elem : arrayList) {
            if(elem.type == 3){
                Group rG = new Group();
                g.getChildren().add(rG);
                shapes.add(new Rec(elem.ordinates,elem.name, rG));
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

    public class Poly extends Polyline {
        public boolean enableEdit = false;
        public Circle[] cs;
        public Double[] ordinates;
        public Double[] ordinatesOrigin;
        public TextField[] tfOrdinates;
        public String name;
        public Group poly = new Group();
        public Group root;
        public Poly(double[] ordinates, String name, Group g){
            this.name = name;
            //this.ordinates = ordinates;
            this.root = g;
            int pointsCount = ordinates.length/2-1;
            this.ordinates = new Double[pointsCount*2];
            this.tfOrdinates = new TextField[pointsCount*2];
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
            shapeSelected = state;
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

        private void enableDrag(Group g, com.fit.upa.shapes.Shapes.Poly owner) {
            final Delta dragDelta = new Delta();

            g.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println(name);
                }
            });
            g.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(!shapeSelected){
                    try {
                        MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("polyInfo.fxml")));
                        PolyInfo.getInstance().setOwner(owner);
                        PolyInfo.getInstance().setName(name);
                        System.out.println("<<<<<>>>>>>");
                        if(owner.enableEdit){
                            PolyInfo.getInstance().setSelect();
                        }
                        for(int i = 0; i < ordinates.length/2; i++) {
                            tfOrdinates[2*i] = new TextField(String.valueOf(ordinates[2*i]));
                            tfOrdinates[2*i+1] = new TextField(String.valueOf(ordinates[2*i+1]));
                            PolyInfo.getInstance().getGPane().addRow(i, new Label("point "+i), tfOrdinates[i*2], tfOrdinates[i*2+1]);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ;
                    }
                    else {
                        if(mouseEvent.getTarget() instanceof Circle){
                            dragDelta.x = ((Circle) mouseEvent.getTarget()).getCenterX()-mouseEvent.getX();
                            dragDelta.y = ((Circle) mouseEvent.getTarget()).getCenterY()-mouseEvent.getY();

                            ordinatesOrigin = ordinates;
                        }
                    }
                }
            });

            g.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getTarget() instanceof Circle){
                        double newX = mouseEvent.getX() + dragDelta.x;
                        double newY = mouseEvent.getY() + dragDelta.y;
                        ((Circle) mouseEvent.getTarget()).setCenterX(newX);
                        ((Circle) mouseEvent.getTarget()).setCenterY(newY);

                        for(int i=0; i<cs.length; i++){
                            if(mouseEvent.getTarget().equals(cs[i])){
                                ordinates[i*2] = newX;
                                tfOrdinates[i*2].setText(String.valueOf(newX));
                                ordinates[i*2+1] = newY;
                                tfOrdinates[i*2+1].setText(String.valueOf(newY));
                                Poly.super.getPoints().setAll(ordinates);
                            }
                        }
                    }
                }
            });
        }
    }

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
            shapeSelected = state;
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

        private void enableDrag(Group g, com.fit.upa.shapes.Shapes.Rec owner) {

            g.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(!shapeSelected){
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
                }}
            });
            g.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println(name);
                }
            });
        }
    }

    private class Delta{double x,y;}
}