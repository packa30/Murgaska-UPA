package com.fit.upa.shapes;
import com.fit.upa.CreateMenu;
import com.fit.upa.DbConnection;
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
    public static Shapes instance;

    public boolean shapeSelected = false;
    Group root;
    public ArrayList<Poly> polys = new ArrayList<Poly>(); //zoznam objektov typu polygono
    public ArrayList<Rec> recs = new ArrayList<Rec>(); //zoznam objektov typu obdlznik
    public ArrayList<Circ> circs = new ArrayList<Circ>();
    ArrayList<Polyl> polyls = new ArrayList<Polyl>();
    ArrayList<Point> points = new ArrayList<Point>();

    DbConnection dbConn = DbConnection.getInstance();
    public Shapes(ArrayList<ObjectsInDB> arrayList, Group g){
        instance = this;

        root = g;
        for(ObjectsInDB elem : arrayList) {
            //System.out.println(elem.eleminfo[2]+" , " +elem.type);
            if(elem.type == 6){
                Group plG = new Group();
                g.getChildren().add(plG);
                polyls.add(new Polyl(elem.type, elem.ordinates, elem.name, elem.objType,elem.eleminfo, plG));
                Polyl p = polyls.get(polyls.size()-1);
                plG.getChildren().add(p);
            }
            else if(elem.eleminfo[2] == 3){
                Group rG = new Group();
                g.getChildren().add(rG);
                recs.add(new Rec(elem.ordinates, elem.name, elem.objType,elem.eleminfo, rG));
                Rec r = recs.get(recs.size() - 1);
                rG.getChildren().add(r);
            }
            else if(elem.eleminfo[2] == 1 && elem.type == 3){
                Group pG = new Group();
                g.getChildren().add(pG);
                polys.add(new Poly(elem.ordinates, elem.name, elem.objType,elem.eleminfo, pG));
                Poly p = polys.get(polys.size() - 1);
                pG.getChildren().add(p);
            }
            else if(elem.eleminfo[2] == 4){
                Group cG = new Group();
                g.getChildren().add(cG);
                circs.add(new Circ(elem.ordinates, elem.name, elem.objType,elem.eleminfo, cG));
                Circ c = circs.get(circs.size()-1);
                cG.getChildren().add(c);
            }
            else if(elem.eleminfo[2] == 1 && elem.type == 2){
                //System.out.println("mame zlozenu priamku");
                Group plG = new Group();
                g.getChildren().add(plG);
                polyls.add(new Polyl(elem.type, elem.ordinates, elem.name, elem.objType,elem.eleminfo, plG));
                Polyl p = polyls.get(polyls.size()-1);
                plG.getChildren().add(p);
            }
            else if(elem.type == 1){
                Group pG = new Group();
                g.getChildren().add(pG);
                points.add(new Point(elem.ordinates, elem.name, elem.objType, elem.eleminfo, pG));
                Point po = points.get(points.size()-1);
                pG.getChildren().add(po);
            }
        }
        orderObject();
    }
    public void deleteObj(String name){
        int i = 0;
        for(Rec rec: recs){
            if(rec.name.equals(name)){
                break;
            }
            i++;
        }
        if(i < recs.size()){
            //System.out.println(recs.get(i).name);
            dbConn.delete(recs.get(i).name);
            recs.remove(i);
        }
        i = 0;
        for(Circ c: circs){
            if(c.name.equals(name)){
                break;
            }
            i++;
        }
        if(i < circs.size()){
            //System.out.println(circs.get(i).name);
            dbConn.delete(circs.get(i).name);
            circs.remove(i);
        }
        i = 0;
        for(Poly p: polys){
            if(p.name.equals(name)){
                break;
            }
            i++;
        }
        if(i < polys.size()){
            //System.out.println(polys.get(i).name);
            dbConn.delete(polys.get(i).name);
            polys.remove(i);
        }
        i = 0;
        for(Polyl p: polyls){
            if(p.name.equals(name)){
                break;
            }
            i++;
        }
        if(i < polyls.size()){
            //System.out.println(polyls.get(i).name);
            dbConn.delete(polyls.get(i).name);
            polyls.remove(i);
        }
        i = 0;
        for(Point p: points){
            if(p.name.equals(name)){
                break;
            }
            i++;
        }
        if(i < points.size()){
            //System.out.println(points.get(i).name);
            dbConn.delete(points.get(i).name);
            points.remove(i);
        }
    }

    public void orderObject(){
        String[] order = {"land","road","build","electric-area","gas-area","electric-net","gas-pipeline","electric-mast","gas-mast"};
        for(String obType: order){
            for(Poly p: polys){
                if(p.objType.equals(obType)){
                    p.root.toFront();
                }
            }
            for(Rec r: recs){
                if(r.objType.equals(obType)){
                    r.root.toFront();
                }
            }
            for(Circ c: circs){
                if(c.objType.equals(obType)){
                    c.root.toFront();
                }
            }
            for(Polyl pl: polyls){
                if(pl.objType.equals(obType)){
                    pl.root.toFront();
                }
            }
            for(Point ps: points){
                if(ps.objType.equals(obType)){
                    ps.root.toFront();
                }
            }
        }

    }

    public class Point extends Circle{
        public boolean enableEdit = false;
        public Circle cs;

        public Double[] ordinates;
        public ArrayList<Double[]> ordinatesHistory = new ArrayList<Double[]>();
        public TextField[] tfOrdinates;

        public String name;
        public String objType;
        public int[] elemInfo;
        public Group pointc = new Group();
        public Group root;

        public Point(double[] ordinates, String name, String objType,int[] elemInfo, Group g) {
            this.name = name;
            this.objType = objType;
            this.elemInfo = elemInfo;
            this.root = g;

            this.ordinates = new Double[2];
            for(int i = 0; i < 2; i++){
                this.ordinates[i] = ordinates[i];
            }
            //System.out.println(">>"+Arrays.toString(ordinates));
            this.tfOrdinates = new TextField[2];
            Circle circCenter = new Circle(ordinates[0], ordinates[1],0);
            this.cs = circCenter;
            cs.setFill(Color.TOMATO.deriveColor(1,1,1,0.5));
            cs.setStroke(Color.TOMATO);
            super.setCenterX(ordinates[0]);
            super.setCenterY(ordinates[1]);
            super.setRadius(10);
            if(this.objType.equals("electric-mast")){
                setFill(Color.RED.deriveColor(1,1,1,1));
                setStroke(Color.DARKRED);
            } else if(this.objType.equals("gas-mast")){
                setFill(Color.YELLOWGREEN.deriveColor(1,1,1,1));
                setStroke(Color.YELLOW);
            }
            pointc.getChildren().add(circCenter);


            root.getChildren().add(pointc);
            enableDrag(root, this);
        }

        public void updateCoords(){
                cs.setCenterX(ordinates[0]);
                cs.setCenterY(ordinates[1]);

            Point.super.setCenterX(ordinates[0]);
            Point.super.setCenterY(ordinates[1]);
        }

        public void updateCoordFromTextField(){
            for(int i=0; i<ordinates.length; i++){
                ordinates[i] = Double.parseDouble(tfOrdinates[i].getText())*3.8;
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
            applyUpdate();
        }
        public void applyUpdate(){
            Shapes.this.dbConn.update(1, ordinates,name, elemInfo);
        }

        public void setEnableEdit(boolean state){
            enableEdit = state;
            shapeSelected = state;
            if(state){
                root.toFront();
                pointc.toFront();
                    cs.setRadius(10);
                    cs.toFront();

            }else {
                    cs.setRadius(0);
                }

        }

        public void orderObjects(){
            Shapes.this.orderObject();
        }
        public void delete(){
            Shapes.this.deleteObj(this.name);
            Shapes.this.root.getChildren().remove(this.root);
        }

        private void enableDrag(Group g, com.fit.upa.shapes.Shapes.Point owner) {
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
                    boolean active = true;
                    if(CreateMenu.instance != null){
                        if (CreateMenu.instance.active){
                            active = false;
                        }
                    }

                    if (active){
                        if(!shapeSelected){
                            try {
                                MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("PointInfo.fxml")));
                                PointInfo.getInstance().setOwner(owner);
                                PointInfo.getInstance().setName(name);

                                if(objType.equals("build")){
                                    PointInfo.getInstance().imageButton.setVisible(true);
                                }

                                if(owner.enableEdit){
                                    PointInfo.getInstance().setSelect();
                                }


                                    tfOrdinates[0] = new TextField(String.valueOf(ordinates[0]/3.8));
                                    tfOrdinates[1] = new TextField(String.valueOf(ordinates[1]/3.8));
                                    PointInfo.getInstance().getGPane().addRow(0, new Label("point 0"), tfOrdinates[0], tfOrdinates[1]);


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            if(mouseEvent.getTarget() instanceof Circle){
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

                }
            });
            g.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(shapeSelected) {
                        if (mouseEvent.getTarget() instanceof Circle) {
                            double deltaX = mouseEvent.getX() - dragDelta.x;
                            double deltaY = mouseEvent.getY() - dragDelta.y;


                                double newX = ordinatesHistory.get(ordinatesHistory.size()-1)[0] +deltaX;
                                double newY = ordinatesHistory.get(ordinatesHistory.size()-1)[1] + deltaY;
                                cs.setCenterX(newX); //TODO nacitat len raz
                                ordinates[0] = newX;
                                tfOrdinates[0].setText(String.valueOf(Math.round(newX/3.8 * 10)/10.0));
                                cs.setCenterY(newY);
                                ordinates[1] = newY;
                                tfOrdinates[1].setText(String.valueOf(Math.round(newY/3.8 * 10)/10.0));

                            Point.super.setCenterX(ordinates[0]);
                            Point.super.setCenterY(ordinates[1]);
                        }

                    }
                }
            });
        }
    }

    public class Polyl extends Polyline{
        public boolean enableEdit = false;
        public Circle[] cs;

        public Double[] ordinates;
        public ArrayList<Double[]> ordinatesHistory = new ArrayList<Double[]>();
        public TextField[] tfOrdinates;

        public String name;
        public String objType;
        public int type;
        public int[] elemInfo;
        public Group polylc = new Group();
        public Group root;

        public Polyl(int type, double[] ordinates, String name, String objType,int[] elemInfo, Group g){
            this.name = name;
            this.objType = objType;
            this.type = type;
            this.elemInfo = elemInfo;
            this.root = g;
/*
            if(type == 6){
                int pointsCount = ordinates.length/2;
                this.ordinates = new Double[(pointsCount-1)*2];
                this.tfOrdinates = new TextField[(pointsCount-1)*2];
                Circle[] circCorners = new Circle[pointsCount-1];
                for(int i=0; i<(pointsCount-1); i++){
                    this.ordinates[2*i] = ordinates[2*i];
                    this.ordinates[2*i+1] = ordinates[2*i+1];
                    circCorners[i] = new Circle(ordinates[2*i], ordinates[2*i+1], 0);
                }
                this.cs = circCorners;
            }
*/
            int pointsCount = ordinates.length/2;
            this.ordinates = new Double[pointsCount*2];
            //System.out.println(">>"+Arrays.toString(ordinates));
            this.tfOrdinates = new TextField[pointsCount*2];
            Circle[] circCorners = new Circle[pointsCount];
            for(int i=0; i<pointsCount; i++){
                this.ordinates[2*i] = ordinates[2*i];
                this.ordinates[2*i+1] = ordinates[2*i+1];
                circCorners[i] = new Circle(ordinates[2*i], ordinates[2*i+1], 0);
            }
            this.cs = circCorners;
            for(Circle c: circCorners){
                c.setFill(Color.TOMATO.deriveColor(1,1,1,0.5));
                c.setStroke(Color.TOMATO);
                polylc.getChildren().add(c);
            }

            super.getPoints().setAll(this.ordinates);

            if(this.objType.equals("electric-net")){
                setStroke(Color.DARKRED);

            } else if(this.objType.equals("gas-pipeline")){
                setStroke(Color.YELLOW);

            }
            setStrokeWidth(3);

            root.getChildren().add(polylc);
            enableDrag(root, this);
        }


        public void updateCoords(){
            for(int i=0; i<cs.length; i++){
                cs[i].setCenterX(ordinates[2*i]);
                cs[i].setCenterY(ordinates[2*i+1]);
            }
            Polyl.super.getPoints().setAll(this.ordinates);
        }

        public void updateCoordFromTextField(){
            for(int i=0; i<ordinates.length; i++){
                ordinates[i] = Double.parseDouble(tfOrdinates[i].getText())*3.8;
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
            applyUpdate();
        }
        public void applyUpdate(){
            Shapes.this.dbConn.update(type, ordinates,name, elemInfo);
        }

        public void setEnableEdit(boolean state){
            enableEdit = state;
            shapeSelected = state;
            if(state){
                root.toFront();
                polylc.toFront();
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

        public void orderObjects(){
            Shapes.this.orderObject();
        }
        public void delete(){
            Shapes.this.deleteObj(this.name);
            Shapes.this.root.getChildren().remove(this.root);
        }

        private void enableDrag(Group g, com.fit.upa.shapes.Shapes.Polyl owner) {
            final Delta dragDelta = new Delta();
            g.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    //System.out.println(name);
                }
            });

            g.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    boolean active = true;
                    if(CreateMenu.instance != null){
                        if (CreateMenu.instance.active){
                            active = false;
                        }
                    }

                    if (active){
                        if(!shapeSelected){
                            try {
                                MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("PolyLInfo.fxml")));
                                PolyLInfo.getInstance().setOwner(owner);
                                PolyLInfo.getInstance().setName(name);
                                PolyLInfo.getInstance().getLandSize();

                                if(objType.equals("build")){
                                    PolyLInfo.getInstance().imageButton.setVisible(true);
                                }

                                if(owner.enableEdit){
                                    PolyLInfo.getInstance().setSelect();
                                }

                                for(int i = 0; i < ordinates.length/2; i++) {
                                    tfOrdinates[2*i] = new TextField(String.valueOf(ordinates[2*i]/3.8));
                                    tfOrdinates[2*i+1] = new TextField(String.valueOf(ordinates[2*i+1]/3.8));
                                    PolyLInfo.getInstance().getGPane().addRow(i, new Label("point "+i), tfOrdinates[i*2], tfOrdinates[i*2+1]);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            if(mouseEvent.getTarget() instanceof Polyl){
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
                            else{
                                dragDelta.x = ((Circle) mouseEvent.getTarget()).getCenterX()-mouseEvent.getX();
                                dragDelta.y = ((Circle) mouseEvent.getTarget()).getCenterY()-mouseEvent.getY();
                                ordinatesHistory.add(ordinates.clone());
                            }
                        }
                    }

                }
            });

            g.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(shapeSelected) {
                        if (mouseEvent.getTarget() instanceof Polyl) {
                            double deltaX = mouseEvent.getX() - dragDelta.x;
                            double deltaY = mouseEvent.getY() - dragDelta.y;

                            for (int i = 0; i < cs.length; i++) {
                                double newX = ordinatesHistory.get(ordinatesHistory.size()-1)[i*2] +deltaX;
                                double newY = ordinatesHistory.get(ordinatesHistory.size()-1)[i*2+1] + deltaY;
                                cs[i].setCenterX(newX); //TODO nacitat len raz
                                ordinates[i * 2] = newX;
                                tfOrdinates[i * 2].setText(String.valueOf(Math.round(newX/3.8 * 10)/10.0));
                                cs[i].setCenterY(newY);
                                ordinates[i * 2 + 1] = newY;
                                tfOrdinates[i * 2 + 1].setText(String.valueOf(Math.round(newY/3.8 * 10)/10.0));
                            }
                            Polyl.super.getPoints().setAll(ordinates);
                        }
                        else {
                            double newX = mouseEvent.getX() + dragDelta.x;
                            double newY = mouseEvent.getY() + dragDelta.y;
                            ((Circle) mouseEvent.getTarget()).setCenterX(newX);
                            ((Circle) mouseEvent.getTarget()).setCenterY(newY);

                            for (int i = 0; i < cs.length; i++) {
                                if (mouseEvent.getTarget().equals(cs[i])) {
                                    ordinates[i * 2] = newX;
                                    tfOrdinates[i * 2].setText(String.valueOf(Math.round(newX/3.8 * 10)/10.0));
                                    ordinates[i * 2 + 1] = newY;
                                    tfOrdinates[i * 2 + 1].setText(String.valueOf(Math.round(newY/3.8 * 10)/10.0));
                                    Polyl.super.getPoints().setAll(ordinates);
                                }
                            }
                        }
                    }
                }
            });
        }

    }

    public class Circ extends Circle {
        public boolean enableEdit = false;
        public Circle[] cs;

        public Double[] ordinates;
        public ArrayList<Double[]> ordinatesHistory = new ArrayList<Double[]>();
        public TextField[] tfOrdinates;

        public String name;
        public String objType;
        public int[] elemInfo;
        public Group circ = new Group();
        public Group root;

        public Circ(double[] ordinates, String name, String objType,int[] elemInfo, Group g){
            this.name = name;
            this.objType = objType;
            this.elemInfo = elemInfo;
            this.root = g;

            //System.out.println(Arrays.toString(ordinates));
            int pointsCount = ordinates.length/2;
            this.ordinates = new Double[pointsCount*2];
            this.tfOrdinates = new TextField[pointsCount*2];
            Circle[] circCorners = new Circle[pointsCount];
            for(int i=0; i<pointsCount; i++){
                this.ordinates[2*i] = ordinates[2*i];
                this.ordinates[2*i+1] = ordinates[2*i+1];
                circCorners[i] = new Circle(ordinates[2*i], ordinates[2*i+1], 0);
            }
            this.cs = circCorners;
            for(Circle c: circCorners){
                c.setFill(Color.TOMATO.deriveColor(1,1,1,0.5));
                c.setStroke(Color.TOMATO);
                circ.getChildren().add(c);
            }
            double[] resultCircle = findCircle(cs);
            super.setCenterX(resultCircle[0]);
            super.setCenterY(resultCircle[1]);
            super.setRadius(resultCircle[2]);

            if(this.objType.equals("electric-area")){
                setFill(Color.INDIANRED.deriveColor(1,1,1,1));
                setStroke(Color.DARKRED);
            }
            else if(this.objType.equals("gas-area")){
                setFill(Color.LIGHTGOLDENRODYELLOW.deriveColor(1,1,1,1));
                setStroke(Color.YELLOW);
            }
            //System.out.println(Arrays.toString(this.ordinates));
            root.getChildren().add(circ);
            enableDrag(root, this);
        }


        public void updateCoords(){
            for(int i=0; i<cs.length; i++){
                cs[i].setCenterX(ordinates[2*i]);
                cs[i].setCenterY(ordinates[2*i+1]);
            }
            double[] resultCircle = findCircle(cs);
            Circ.super.setCenterX(resultCircle[0]);
            Circ.super.setCenterY(resultCircle[1]);
            Circ.super.setRadius(resultCircle[2]);
        }

        public void updateCoordFromTextField(){
            for(int i=0; i<ordinates.length; i++){
                ordinates[i] = Double.parseDouble(tfOrdinates[i].getText())*3.8;
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
            applyUpdate();
        }
        public void applyUpdate(){
            Shapes.this.dbConn.update(3, ordinates,name, elemInfo);
        }

        public void setEnableEdit(boolean state){
            enableEdit = state;
            shapeSelected = state;
            if(state){
                root.toFront();
                circ.toFront();
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

        public void orderObjects(){
            Shapes.this.orderObject();
        }
        public void delete(){
            Shapes.this.deleteObj(this.name);
            Shapes.this.root.getChildren().remove(this.root);
        }

        private void enableDrag(Group g, com.fit.upa.shapes.Shapes.Circ owner) {
            final Delta dragDelta = new Delta();
            g.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    //System.out.println(name);
                }
            });

            g.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    boolean active = true;
                    if(CreateMenu.instance != null){
                        if (CreateMenu.instance.active){
                            active = false;
                        }
                    }

                    if (active){
                        if(!shapeSelected){
                            try {
                                MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("circInfo.fxml")));
                                CircInfo.getInstance().setOwner(owner);
                                CircInfo.getInstance().setName(name);
                                CircInfo.getInstance().getLandSize();

                                if(objType.equals("build")){
                                    CircInfo.getInstance().imageButton.setVisible(true);
                                }

                                if(owner.enableEdit){
                                    CircInfo.getInstance().setSelect();
                                }

                                for(int i = 0; i < ordinates.length/2; i++) {
                                    tfOrdinates[2*i] = new TextField(String.valueOf(ordinates[2*i]/3.8));
                                    tfOrdinates[2*i+1] = new TextField(String.valueOf(ordinates[2*i+1]/3.8));
                                    CircInfo.getInstance().getGPane().addRow(i, new Label("point "+i), tfOrdinates[i*2], tfOrdinates[i*2+1]);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            if(mouseEvent.getTarget() instanceof Circ){
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
                            else{
                                dragDelta.x = ((Circle) mouseEvent.getTarget()).getCenterX()-mouseEvent.getX();
                                dragDelta.y = ((Circle) mouseEvent.getTarget()).getCenterY()-mouseEvent.getY();
                                ordinatesHistory.add(ordinates.clone());
                            }
                        }
                    }

                }
            });

            g.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(shapeSelected) {
                         if (mouseEvent.getTarget() instanceof Circ) {
                            double deltaX = mouseEvent.getX() - dragDelta.x;
                            double deltaY = mouseEvent.getY() - dragDelta.y;

                            for (int i = 0; i < cs.length; i++) {
                                double newX = ordinatesHistory.get(ordinatesHistory.size()-1)[i*2] +deltaX;
                                double newY = ordinatesHistory.get(ordinatesHistory.size()-1)[i*2+1] + deltaY;
                                cs[i].setCenterX(newX); //TODO nacitat len raz
                                ordinates[i * 2] = newX;
                                tfOrdinates[i * 2].setText(String.valueOf(Math.round(newX/3.8 * 10)/10.0));
                                cs[i].setCenterY(newY);
                                ordinates[i * 2 + 1] = newY;
                                tfOrdinates[i * 2 + 1].setText(String.valueOf(Math.round(newY/3.8 * 10)/10.0));
                            }
                             double[] resultCircle = findCircle(cs);
                             Circ.super.setCenterX(resultCircle[0]);
                             Circ.super.setCenterY(resultCircle[1]);
                             Circ.super.setRadius(resultCircle[2]);
                        }
                        else {
                            double newX = mouseEvent.getX() + dragDelta.x;
                            double newY = mouseEvent.getY() + dragDelta.y;
                            ((Circle) mouseEvent.getTarget()).setCenterX(newX);
                            ((Circle) mouseEvent.getTarget()).setCenterY(newY);

                            for (int i = 0; i < cs.length; i++) {
                                if (mouseEvent.getTarget().equals(cs[i])) {
                                    ordinates[i * 2] = newX;
                                    tfOrdinates[i * 2].setText(String.valueOf(Math.round(newX/3.8 * 10)/10.0));
                                    ordinates[i * 2 + 1] = newY;
                                    tfOrdinates[i * 2 + 1].setText(String.valueOf(Math.round(newY/3.8 * 10)/10.0));
                                    double[] resultCircle = findCircle(cs);
                                    Circ.super.setCenterX(resultCircle[0]);
                                    Circ.super.setCenterY(resultCircle[1]);
                                    Circ.super.setRadius(resultCircle[2]);
                                }
                            }
                        }
                    }
                }
            });


        }

        private double[] findCircle(Circle[] c){
            double x1 = c[0].getCenterX();
            double x2 = c[1].getCenterX();
            double x3 = c[2].getCenterX();
            double y1 = c[0].getCenterY();
            double y2 = c[1].getCenterY();
            double y3 = c[2].getCenterY();

            double x12 = x1 - x2;
            double x13 = x1 - x3;

            double y12 = y1 - y2;
            double y13 = y1 - y3;

            double y31 = y3 - y1;
            double y21 = y2 - y1;

            double x31 = x3 - x1;
            double x21 = x2 - x1;

            // x1^2 - x3^2
            double sx13 = (Math.pow(x1, 2) -
                    Math.pow(x3, 2));

            // y1^2 - y3^2
            double sy13 = (Math.pow(y1, 2) -
                    Math.pow(y3, 2));

            double sx21 = (Math.pow(x2, 2) -
                    Math.pow(x1, 2));

            double sy21 = (Math.pow(y2, 2) -
                    Math.pow(y1, 2));

            double f = ((sx13) * (x12)
                    + (sy13) * (x12)
                    + (sx21) * (x13)
                    + (sy21) * (x13))
                    / (2 * ((y31) * (x12) - (y21) * (x13)));
            double g = ((sx13) * (y12)
                    + (sy13) * (y12)
                    + (sx21) * (y13)
                    + (sy21) * (y13))
                    / (2 * ((x31) * (y12) - (x21) * (y13)));

            double cc = -Math.pow(x1, 2) - Math.pow(y1, 2) -
                    2 * g * x1 - 2 * f * y1;

            // eqn of circle be x^2 + y^2 + 2*g*x + 2*f*y + c = 0
            // where centre is (h = -g, k = -f) and radius r
            // as r^2 = h^2 + k^2 - c
            double h = -g;
            double k = -f;
            double sqr_of_r = h * h + k * k - cc;

            // r is the radius
            double r = Math.sqrt(sqr_of_r);

            double[] res = new double[3];
            res[0] = h;
            res[1] = k;
            res[2] = r;
            return res;
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
        public int[] elemInfo;
        public Group poly = new Group();
        public Group root;

        public Poly(double[] ordinates, String name, String objType,int[] elemInfo, Group g){
            this.name = name;
            this.objType = objType;
            this.elemInfo = elemInfo;
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
            super.getPoints().addAll(this.ordinates);
            root.getChildren().add(poly);

            System.out.println(this.objType);

            if(this.objType.equals("land")){
                setFill(Color.DARKGOLDENROD.deriveColor(1,1,1,1));
                setStroke(Color.BROWN);
            }
            else if(this.objType.equals("build")){
                setFill(Color.STEELBLUE.deriveColor(1,1,1,1));
                setStroke(Color.BLUE);
            }
            else {
                setFill(Color.DARKGRAY.deriveColor(1,1,1,1));
                setStroke(Color.GRAY);
            }
            enableDrag(root, this);
        }

        public String getObjType(){
            return this.objType;
        }

        public void orderObjects(){
            Shapes.this.orderObject();
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
                ordinates[i] = Double.parseDouble(tfOrdinates[i].getText())*3.8;
            }
            updateCoords();
        }

        public void delete(){
            Shapes.this.deleteObj(this.name);
            Shapes.this.root.getChildren().remove(this.root);
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
            applyUpdate();
        }
        public void applyUpdate(){
            Shapes.this.dbConn.update(3, ordinates,name, elemInfo);
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
        public void updateInDb(){

        }

        private void enableDrag(Group g, com.fit.upa.shapes.Shapes.Poly owner) {
            final Delta dragDelta = new Delta();

            g.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    //System.out.println(name);
                }
            });



            g.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    boolean active = true;
                    if(CreateMenu.instance != null){
                        if (CreateMenu.instance.active){
                            active = false;
                        }
                    }

                    if (active){
                        if(!shapeSelected){
                            try {
                                MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("polyInfo.fxml")));
                                PolyInfo.getInstance().setOwner(owner);
                                PolyInfo.getInstance().setName(name);
                                PolyInfo.getInstance().getLandSize();

                                if(objType.equals("build")){
                                    PolyInfo.getInstance().imageButton.setVisible(true);
                                }

                                if(owner.enableEdit){
                                    PolyInfo.getInstance().setSelect();
                                }
                                for(int i = 0; i < ordinates.length/2; i++) {
                                    tfOrdinates[2*i] = new TextField(String.valueOf(ordinates[2*i]/3.8));
                                    tfOrdinates[2*i+1] = new TextField(String.valueOf(ordinates[2*i+1]/3.8));
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
                                    tfOrdinates[i * 2].setText(String.valueOf(Math.round(newX/3.8 * 10)/10.0));
                                    ordinates[i * 2 + 1] = newY;
                                    tfOrdinates[i * 2 + 1].setText(String.valueOf(Math.round(newY/3.8 * 10)/10.0));
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
                                tfOrdinates[i * 2].setText(String.valueOf(Math.round(newX/3.8 * 10)/10.0));
                                cs[i].setCenterY(newY);
                                ordinates[i * 2 + 1] = newY;
                                tfOrdinates[i * 2 + 1].setText(String.valueOf(Math.round(newY/3.8 * 10)/10.0));
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
        public int[] elemInfo;
        public Double[] ordinates;
        public ArrayList<Double[]> ordinatesHistory = new ArrayList<Double[]>();
        public TextField[] tfOrdinates;
        public Group rec = new Group();
        public Group root;
        public Circle[] cs;
        public Rec(double[] ordinates, String name, String objType, int[] elemInfo, Group g){
            this.root = g;
            this.name = name;
            this.objType = objType;
            this.elemInfo = elemInfo;
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

        public void orderObjects(){
            Shapes.this.orderObject();
        }
        public void delete(){
            Shapes.this.deleteObj(this.name);
            Shapes.this.root.getChildren().remove(this.root);
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
                ordinates[i] = Double.parseDouble(tfOrdinates[i].getText())*3.8;
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
            applyUpdate();
        }

        public void applyUpdate(){
            Shapes.this.dbConn.update(33, ordinates,name, elemInfo);
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
                    boolean active = true;
                    if(CreateMenu.instance != null){
                        if (CreateMenu.instance.active){
                            active = false;
                        }
                    }

                    if (active){
                        if(!shapeSelected){
                            try {
                                MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("recInfo.fxml")));
                                RecInfo.getInstance().setOwner(owner);
                                RecInfo.getInstance().setName(name);
                                RecInfo.getInstance().getLandSize();

                                if(objType.equals("build")){
                                    RecInfo.getInstance().imageButton.setVisible(true);
                                }

                                if(owner.enableEdit){
                                    RecInfo.getInstance().setSelect();
                                }
                                for(int i=0; i<4; i++){
                                    tfOrdinates[i] = new TextField(String.valueOf(ordinates[i]/3.8));
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
                                //tfOrdinates[0].setText(String.valueOf(Math.round((newX/3.8) * 100)/100));
                                tfOrdinates[0].setText(String.valueOf(Math.round((newX/3.8) * 10)/10.0));
                                ordinates[1] = newY;
                                tfOrdinates[1].setText(String.valueOf(Math.round((newY/3.8) * 10)/10.0));
                            }
                            else{
                                Rec.super.setWidth(cs[1].getCenterX() - cs[0].getCenterX());
                                Rec.super.setHeight(cs[1].getCenterY() - cs[0].getCenterY());

                                ordinates[2] = newX;
                                tfOrdinates[2].setText(String.valueOf(Math.round((newX/3.8) * 10)/10.0));
                                ordinates[3] = newY;
                                tfOrdinates[3].setText(String.valueOf(Math.round((newY/3.8) * 10)/10.0));
                            }
                        } else if (mouseEvent.getTarget() instanceof Rec) {
                            double deltaX = mouseEvent.getX() - dragDelta.x;
                            double deltaY = mouseEvent.getY() - dragDelta.y;

                            //cs[0].setCenterX(startPoints.x1+deltaX);
                            cs[0].setCenterX(ordinatesHistory.get(ordinatesHistory.size()-1)[0]+deltaX);
                            ordinates[0] = cs[0].getCenterX();
                            tfOrdinates[0].setText(String.valueOf(Math.round(ordinates[0]/3.8 * 10)/10.0));
                            //cs[1].setCenterX(startPoints.x2+deltaX);
                            cs[1].setCenterX(ordinatesHistory.get(ordinatesHistory.size()-1)[2]+deltaX);
                            ordinates[2] = cs[1].getCenterX();
                            tfOrdinates[2].setText(String.valueOf(Math.round(ordinates[2]/3.8 * 10)/10.0));
                            //cs[0].setCenterY(startPoints.y1+deltaY);
                            cs[0].setCenterY(ordinatesHistory.get(ordinatesHistory.size()-1)[1]+deltaY);
                            ordinates[1] = cs[0].getCenterY();
                            tfOrdinates[1].setText(String.valueOf(Math.round(ordinates[1]/3.8 * 10)/10.0));
                           // cs[1].setCenterY(startPoints.y2+deltaY);
                            cs[1].setCenterY(ordinatesHistory.get(ordinatesHistory.size()-1)[3]+deltaY);
                            ordinates[3] = cs[1].getCenterY();
                            tfOrdinates[3].setText(String.valueOf(Math.round(ordinates[3]/3.8 * 10)/10.0));

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
                    //System.out.println(name);
                }
            });
        }
    }

    private class Delta{double x,y;}

    private class Points {double x1, y1, x2, y2;}
}