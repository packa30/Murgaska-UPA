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
                shapes.add(new Rec(elem.ordinates,elem.name,elem.objType, rG));
                Rec r = (Rec)shapes.get(shapes.size() - 1);
                System.out.println(r.name);
                rG.getChildren().add(r);
            }
            else if(elem.type == 1){
                Group pG = new Group();
                g.getChildren().add(pG);
                shapes.add(new Poly(elem.ordinates, elem.name, elem.objType, pG));
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
        public ArrayList<Double[]> ordinatesHistory = new ArrayList<Double[]>();
        public TextField[] tfOrdinates;
        public String name;
        public String objType;
        public Group poly = new Group();
        public Group root;

        public Poly(double[] ordinates, String name, String objType, Group g){
            this.name = name;
            this.objType = objType;
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
            System.out.println("$$$$"+this.objType);
            if(this.objType.equals("land")){
                setFill(Color.DARKGOLDENROD.deriveColor(1,1,1,1));
                setStroke(Color.BROWN);
            }
            else{
                setFill(Color.DARKGRAY.deriveColor(1,1,1,1));
                setStroke(Color.GRAY);
            }
            enableDrag(root, this);
        }

        public String getObjType(){
            return this.objType;
        }

        public void updateCoords(){
            for(int i=0; i<cs.length; i++){
                cs[i].setCenterX(ordinates[2*i]);
                cs[i].setCenterY(ordinates[2*i+1]);
            }
            Poly.super.getPoints().setAll(ordinates);
        }

        public void updateCoordFromTextField(){
            for(int i=0; i<ordinates.length; i++){
                ordinates[i] = Double.parseDouble(tfOrdinates[i].getText());
            }
            updateCoords();
        }



        public void discardChanges(){
            if(!ordinatesHistory.isEmpty()) {
                ordinates = ordinatesHistory.get(0);
                ordinatesHistory.clear();
                updateCoords();
            }
        }

        public void applyChanges(){
            ordinatesHistory.clear();
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
                            ordinatesHistory.add(ordinates.clone());
                        }
                        else if(mouseEvent.getTarget() instanceof Poly){
                            dragDelta.x = mouseEvent.getX();
                            dragDelta.y = mouseEvent.getY();
                            ordinatesHistory.add(ordinates.clone());
                        }
                    }
                }
            });

            g.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(shapeSelected) {
                        if (mouseEvent.getTarget() instanceof Circle) {
                            double newX = mouseEvent.getX() + dragDelta.x;
                            double newY = mouseEvent.getY() + dragDelta.y;
                            ((Circle) mouseEvent.getTarget()).setCenterX(newX);
                            ((Circle) mouseEvent.getTarget()).setCenterY(newY);

                            for (int i = 0; i < cs.length; i++) {
                                if (mouseEvent.getTarget().equals(cs[i])) {
                                    ordinates[i * 2] = newX;
                                    tfOrdinates[i * 2].setText(String.valueOf(newX));
                                    ordinates[i * 2 + 1] = newY;
                                    tfOrdinates[i * 2 + 1].setText(String.valueOf(newY));
                                    Poly.super.getPoints().setAll(ordinates);
                                }
                            }
                        } else if (mouseEvent.getTarget() instanceof Poly) {
                            double deltaX = mouseEvent.getX() - dragDelta.x;
                            double deltaY = mouseEvent.getY() - dragDelta.y;

                            for (int i = 0; i < cs.length; i++) {
                                double newX = ordinatesHistory.get(ordinatesHistory.size()-1)[i*2] +deltaX;
                                double newY = ordinatesHistory.get(ordinatesHistory.size()-1)[i*2+1] + deltaY;
                                cs[i].setCenterX(newX); //TODO nacitat len raz
                                ordinates[i * 2] = newX;
                                tfOrdinates[i * 2].setText(String.valueOf(newX));
                                cs[i].setCenterY(newY);
                                ordinates[i * 2 + 1] = newY;
                                tfOrdinates[i * 2 + 1].setText(String.valueOf(newY));
                            }
                            Poly.super.getPoints().setAll(ordinates);
                        }
                    }
                }
            });
        }
    }

    public class Rec extends Rectangle {
        public boolean enableEdit = false;
        public String name;
        public String objType;
        public Double[] ordinates;
        public ArrayList<Double[]> ordinatesHistory = new ArrayList<Double[]>();
        public TextField[] tfOrdinates;
        public Group rec = new Group();
        public Group root;
        public Circle[] cs;
        public Rec(double[] ordinates, String name, String objType, Group g){
            this.root = g;
            this.name = name;
            this.objType = objType;
            this.ordinates = new Double[4];
            //System.out.println(">>>>>"+name+" "+Arrays.toString(ordinates));
            for(int i=0; i<ordinates.length; i++){
                this.ordinates[i] = ordinates[i];
            }
            this.tfOrdinates = new TextField[4];

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

            /*
            double width = Math.abs(ordinates[2]-ordinates[0]);
            double height = Math.abs(ordinates[3]-ordinates[1]);
            super.setX(ordinates[0]);
            super.setY(ordinates[1]-height);
            super.setWidth(width);
            super.setHeight(height);
             */

            g.getChildren().add(rec);
            if(this.objType.equals("build")){
                setFill(Color.STEELBLUE.deriveColor(1,1,1,1));
                setStroke(Color.BLUE);
            }
            else {
                setFill(Color.DARKGRAY.deriveColor(1, 1, 1, 1));
                setStroke(Color.GRAY);
            }
            enableDrag(g,this);
        }

        public void updateCoords(){
            for(int i=0; i<cs.length; i++){
                cs[i].setCenterX(ordinates[2*i]);
                cs[i].setCenterY(ordinates[2*i+1]);
            }
            //Rec.super.getPoints().setAll(ordinates);
            Rec.super.setX(ordinates[0]);
            Rec.super.setY(ordinates[1]);
            Rec.super.setWidth(ordinates[2]-ordinates[0]);
            Rec.super.setHeight(ordinates[3]-ordinates[1]);
        }

        public void updateCoordFromTextField(){
            for(int i=0; i<ordinates.length; i++){
                ordinates[i] = Double.parseDouble(tfOrdinates[i].getText());
            }
            updateCoords();
        }

        public void discardChanges(){
            if(!ordinatesHistory.isEmpty()) {
                ordinates = ordinatesHistory.get(0);
                ordinatesHistory.clear();
                updateCoords();
            }
        }

        public void applyChanges(){
            ordinatesHistory.clear();
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
            final Delta dragDelta = new Delta();
            final Points startPoints = new Points();
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
                            for(int i=0; i<4; i++){
                                tfOrdinates[i] = new TextField(String.valueOf(ordinates[i]));
                            }
                            RecInfo.getInstance().getGPane().addRow(0, new Label("point 0"), tfOrdinates[0], tfOrdinates[1]);
                            RecInfo.getInstance().getGPane().addRow(1, new Label("point 1"), tfOrdinates[2], tfOrdinates[3]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        if(mouseEvent.getTarget() instanceof Circle){
                            dragDelta.x = ((Circle) mouseEvent.getTarget()).getCenterX()-mouseEvent.getX();
                            dragDelta.y = ((Circle) mouseEvent.getTarget()).getCenterY()-mouseEvent.getY();
                            ordinatesHistory.add(ordinates.clone());
                        }
                        else if(mouseEvent.getTarget() instanceof Rec){
                            dragDelta.x = mouseEvent.getX();
                            dragDelta.y = mouseEvent.getY();/*
                            startPoints.x1 = cs[0].getCenterX();
                            startPoints.x2 = cs[1].getCenterX();
                            startPoints.y1 = cs[0].getCenterY();
                            startPoints.y2 = cs[1].getCenterY();*/
                            ordinatesHistory.add(ordinates.clone());/*
                            System.out.println("sp: "+ startPoints.x1 + startPoints.y1 + startPoints.x2 + startPoints.y2);
                            System.out.println("oh: "+Arrays.toString(ordinates));*/
                        }
                    }
                }
            });

            g.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(shapeSelected) {
                        if (mouseEvent.getTarget() instanceof Circle) {
                            double newX = mouseEvent.getX() + dragDelta.x;
                            double newY = mouseEvent.getY() + dragDelta.y;
                            ((Circle) mouseEvent.getTarget()).setCenterX(newX);
                            ((Circle) mouseEvent.getTarget()).setCenterY(newY);

                            if(mouseEvent.getTarget().equals(cs[0])){
                                Rec.super.setX(cs[0].getCenterX());
                                Rec.super.setY(cs[0].getCenterY());
                                Rec.super.setWidth(cs[1].getCenterX() - cs[0].getCenterX());
                                Rec.super.setHeight(cs[1].getCenterY() - cs[0].getCenterY());

                                ordinates[0] = newX;
                                tfOrdinates[0].setText(String.valueOf(newX));
                                ordinates[1] = newY;
                                tfOrdinates[1].setText(String.valueOf(newY));
                            }
                            else{
                                Rec.super.setWidth(cs[1].getCenterX() - cs[0].getCenterX());
                                Rec.super.setHeight(cs[1].getCenterY() - cs[0].getCenterY());

                                ordinates[2] = newX;
                                tfOrdinates[2].setText(String.valueOf(newX));
                                ordinates[3] = newY;
                                tfOrdinates[3].setText(String.valueOf(newY));
                            }
                        } else if (mouseEvent.getTarget() instanceof Rec) {
                            double deltaX = mouseEvent.getX() - dragDelta.x;
                            double deltaY = mouseEvent.getY() - dragDelta.y;

                            //cs[0].setCenterX(startPoints.x1+deltaX);
                            cs[0].setCenterX(ordinatesHistory.get(ordinatesHistory.size()-1)[0]+deltaX);
                            ordinates[0] = cs[0].getCenterX();
                            tfOrdinates[0].setText(String.valueOf(ordinates[0]));
                            //cs[1].setCenterX(startPoints.x2+deltaX);
                            cs[1].setCenterX(ordinatesHistory.get(ordinatesHistory.size()-1)[2]+deltaX);
                            ordinates[2] = cs[1].getCenterX();
                            tfOrdinates[2].setText(String.valueOf(ordinates[2]));
                            //cs[0].setCenterY(startPoints.y1+deltaY);
                            cs[0].setCenterY(ordinatesHistory.get(ordinatesHistory.size()-1)[1]+deltaY);
                            ordinates[1] = cs[0].getCenterY();
                            tfOrdinates[1].setText(String.valueOf(ordinates[1]));
                           // cs[1].setCenterY(startPoints.y2+deltaY);
                            cs[1].setCenterY(ordinatesHistory.get(ordinatesHistory.size()-1)[3]+deltaY);
                            ordinates[3] = cs[1].getCenterY();
                            tfOrdinates[3].setText(String.valueOf(ordinates[3]));

                            ordinates[0] = ordinatesHistory.get(ordinatesHistory.size()-1)[0]+deltaX;
                            ordinates[1] = ordinatesHistory.get(ordinatesHistory.size()-1)[1]+deltaY;
                            ordinates[2] = ordinatesHistory.get(ordinatesHistory.size()-1)[2]+deltaX;
                            ordinates[3] = ordinatesHistory.get(ordinatesHistory.size()-1)[3]+deltaY;

                            //TODO skusit prerobit
                            Rec.super.setX(cs[0].getCenterX());
                            Rec.super.setY(cs[0].getCenterY());
                            Rec.super.setWidth(cs[1].getCenterX() - cs[0].getCenterX());
                            Rec.super.setHeight(cs[1].getCenterY() - cs[0].getCenterY());
                        }
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

    private class Delta{double x,y;}

    private class Points {double x1, y1, x2, y2;}
}