package com.fit.upa;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;

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
        }
    }

    class Rec extends Rectangle {
        String name;
        double[] ordinates;
        Group rec = new Group();
        public Rec(double[] ordinates, String name, Group g){
            this.name = name;
            this.ordinates = ordinates;
            Circle[] recCorners= {new Circle(ordinates[0], ordinates[1], 5),
                    new Circle(ordinates[2], ordinates[3], 5)};
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
