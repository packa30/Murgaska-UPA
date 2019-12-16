package com.fit.upa;

import com.fit.upa.shapes.Shapes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import oracle.jdbc.driver.DBConversion;

import java.io.IOException;
import java.util.ArrayList;

public class MainMenu {
    @FXML
    public AnchorPane pane;

    public static Group drawGroup;

    public static MainMenu instance;
    public MainMenu(){
        instance = this;
    }
    public static MainMenu getInstance(){
        return instance;
    }

    public AnchorPane getAnchor(){
        return pane;
    }

    private boolean isPressed;

    @FXML
    private Button findAction;

    @FXML
    private Button select;

    public boolean isAction;

    public void initialize(){
        isPressed = false;
        isAction = false;
        findAction = (Button) pane.lookup("#find");
        select = (Button) pane.lookup("#select");
        select.setVisible(true);
    }

    public void onAddObj() throws IOException {
        MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("createMenu.fxml")));
    }

    public void logout() throws Exception {
        DbConnection.getInstance().disconnect();
        MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("login.fxml")));
        Main.instance.start(Main.instance.stage);
    }

    public void findAction(){
        ArrayList<ObjectsInDB> arrayList = new ArrayList<>();
        ArrayList<ObjectsInDB> gasList = new ArrayList<>();
        ArrayList<ObjectsInDB> iList = new ArrayList<>();

        if(!isPressed){
            findAction.setText("Hide");
            isPressed = true;
            select.setVisible(true);
            // Objektz ktorsu v kolizii s gas area
            arrayList = DbConnection.getInstance().query("select a.name, a.type, a.geometry from map a, map b where (a.type = 'build') and (b.type = 'gas-area') and (sdo_relate(a.geometry, b.geometry, 'mask=ANYINTERACT') = 'TRUE')");
            String sql = "";
            for(ObjectsInDB elem : arrayList) {
                System.out.println(elem.name);

                if(elem.eleminfo[2] == 3){
                    //Rec
                    for (Shapes.Rec i : Shapes.instance.recs){
                        if(i.name.equals(elem.name)  && i.objType.equals("build")){
                            i.setStroke(Color.BLACK);
                            i.setFill(Color.BLACK);
                        }
                    }
                }
                else if(elem.eleminfo[2] == 1 && elem.type == 3){
                    // Poly
                    for (Shapes.Poly i : Shapes.instance.polys){
                        if(i.name.equals(elem.name) && i.objType.equals("build")){
                            i.setStroke(Color.BLACK);
                            i.setFill(Color.BLACK);
                        }
                    }
                }
            }
        }else{
            findAction.setText("Find");
            isPressed = false;
            select.setVisible(true);

            arrayList = DbConnection.getInstance().query("select a.name, a.type, a.geometry from map a, map b where (a.type = 'build') and (b.type = 'gas-area') and (sdo_relate(a.geometry, b.geometry, 'mask=ANYINTERACT') = 'TRUE')");

            for(ObjectsInDB elem : arrayList) {
                System.out.println(elem.name);
                if(elem.eleminfo[2] == 3){
                    //Rec
                    for (Shapes.Rec i : Shapes.instance.recs){
                        if(i.name.equals(elem.name)){
                            if(i.objType.equals("build")){
                                i.setFill(Color.STEELBLUE.deriveColor(1,1,1,1));
                                i.setStroke(Color.BLUE);
                            }else if (i.objType.equals("land")){
                                i.setFill(Color.DARKGOLDENROD.deriveColor(1,1,1,1));
                                i.setStroke(Color.BROWN);
                            }
                        }
                    }
                }
                else if(elem.eleminfo[2] == 1 && elem.type == 3){
                    // Poly
                    for (Shapes.Poly i : Shapes.instance.polys){
                        if(i.name.equals(elem.name)){
                            if(i.objType.equals("build")){
                                i.setFill(Color.STEELBLUE.deriveColor(1,1,1,1));
                                i.setStroke(Color.BLUE);
                            }else if (i.objType.equals("land")){
                                i.setFill(Color.DARKGOLDENROD.deriveColor(1,1,1,1));
                                i.setStroke(Color.BROWN);
                            }
                        }
                    }
                }
            }
        }

    }


    public void onAction(ActionEvent actionEvent) {
        ArrayList<ObjectsInDB> arrayList = DbConnection.getInstance().query("SELECT  a.name, a.type, a.geometry from map a, map b where (a.name <> b.name) and b.type = 'build' and a.type ='build' and (sdo_relate(a.geometry, b.geometry, 'mask=ANYINTERACT') = 'TRUE')");
        if(arrayList.size() > 1 && !isAction){
            select.setText("Disable");
            isAction = true;
            String sql = "SELECT a.name, a.type, SDO_GEOM.SDO_INTERSECTION(a.geometry, b.geometry, 0.005) geometry FROM map a, map b  WHERE a.name = '"+arrayList.get(1).name+"' AND b.name = '"+arrayList.get(0).name+"'";
            ArrayList<ObjectsInDB> in = DbConnection.getInstance().query(sql);
            if(in.size() > 0){
                in.get(0).name = "test42";
                in.get(0).objType = "test42";

                Shapes.instance.Shapes(in,MainMenu.drawGroup);
                toBack(arrayList);
                findTest();
                Shapes.instance.orderObject();
            }

        }else if(arrayList.size() > 1 && isAction){
            isAction = false;
            clearTest();
            select.setText("Action");
        }
    }

    public void findTest(){
        for (Shapes.Poly i : Shapes.instance.polys) {
            if (i.name.equals("test42")) {
                i.setFill(Color.MAROON.deriveColor(1,1,1,1));
                i.setStroke(Color.MAROON);
            }
        }

        for (Shapes.Rec i : Shapes.instance.recs){
            if(i.name.equals("test42")){
                i.setFill(Color.MAROON.deriveColor(1,1,1,1));
                i.setStroke(Color.MAROON);
            }
        }
    }

    public void toBack(ArrayList<ObjectsInDB> arrayList){
        for (ObjectsInDB obj : arrayList){
            for (Shapes.Poly i : Shapes.instance.polys) {
                if (i.name.equals(obj.name)) {
                    i.toBack();
                }
            }

            for (Shapes.Rec i : Shapes.instance.recs){
                if(i.name.equals(obj.name)){
                    System.out.println("Nasiel som");
                    i.toBack();
                }
            }
        }
    }

    public void clearTest(){
        for (Shapes.Poly i : Shapes.instance.polys) {
            if (i.name.equals("test42")) {
                i.delete();
                break;
            }
        }

        for (Shapes.Rec i : Shapes.instance.recs){
            if(i.name.equals("test42")){
                i.delete();
                break;
            }
        }
    }
}