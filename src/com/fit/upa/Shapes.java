package com.fit.upa;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Shapes {
    Group root;
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    public Shapes(ArrayList<ObjectsInDB> arrayList, Group g){
        root = g;
        for(ObjectsInDB elem : arrayList) {
            if(elem.type == 3){
                shapes.add(new Rec(elem.ordinates,elem.name, root));
                Rec r = (Rec)shapes.get(shapes.size() - 1);
                System.out.println(r.name);
                root.getChildren().add(r);
            }
            else if(elem.type == 1){
                shapes.add(new Poly(elem.ordinates, elem.name, root));
                Poly p = (Poly) shapes.get(shapes.size() - 1);
                System.out.println(p.name);
                root.getChildren().add(p);
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
        }
    }

    class Rec extends Rectangle {
        String name;
        double[] ordinates;
        Group rec = new Group();
        public Rec(double[] ordinates, String name, Group g){
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

        }
    }

}
