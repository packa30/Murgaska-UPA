package com.fit.upa;


import com.fit.upa.shapes.Shapes;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.control.TextField;

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

    @FXML
    private ChoiceBox elementType;

    @FXML
    private TextField elementName;

    public static CreateMenu getInstance(){
        return instance;
    }

    private ArrayList<Integer> listOfPoints;
    private ArrayList<Circle>  listOfCircles;

    private DbConnection connect;

    public void initialize(){
        active = true;
        group = MainMenu.drawGroup;

        scene = (AnchorPane) group.getParent();
        listOfPoints  = new ArrayList<>();
        listOfCircles = new ArrayList<>();

        elementType = (ChoiceBox) pane.lookup("#typeBox");
        elementName = (TextField) pane.lookup("#name");

        connect = DbConnection.getInstance();

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
            }
        });
    }

    private void insertPoints(double x, double y){
        double yy = 760-y;
        if (!listOfPoints.contains((int)(x/3.8)) || !listOfPoints.contains((int)(yy/3.8))){
            listOfPoints.add((int)(x/3.8));
            listOfPoints.add((int)(yy/3.8));
        }
    }

    private void createPoint(int x, int y){
        int yy = 760 - y;
        Circle circle = new Circle(x,yy,10.f);
        circle.setFill(javafx.scene.paint.Color.TOMATO.deriveColor(1,1,1,0.5));
        circle.setStroke(Color.TOMATO);
        listOfCircles.add(circle);
        group.getChildren().add(circle);
    }

    private void removeElems(){
        listOfCircles.forEach((circle) -> group.getChildren().remove(circle));
        listOfCircles.clear();
        listOfPoints.clear();
    }

    private String createElem(String name, String type, int shapeType){
        final String[] sqlQuery = {"INSERT INTO map (type,name, geometry) VALUES(\'" + type + "\',\'" + name + "\', SDO_GEOMETRY("};

        if(shapeType == 2001 && listOfPoints.size() == 2){
            //Point
            sqlQuery[0] += "2001,NULL,SDO_POINT_TYPE(";
            listOfPoints.forEach((n)-> sqlQuery[0] += n.toString() +",");
            sqlQuery[0] += "NULL), NULL, NULL))";
        }else if (shapeType == 2002 && listOfPoints.size() == 4){
            //Line
            sqlQuery[0] += "2002,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,2,1), SDO_ORDINATE_ARRAY(";
            listOfPoints.forEach((n)-> sqlQuery[0] += n.toString() +",");
            sqlQuery[0]  = sqlQuery[0].substring(0,sqlQuery[0].length()-1);
            sqlQuery[0] += ")))";
        }else if(shapeType == 2003 && listOfPoints.size() == 6){
            //Circle
            sqlQuery[0] += "2003,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,1003,4), SDO_ORDINATE_ARRAY(";
            listOfPoints.forEach((n)-> sqlQuery[0] += n.toString() +",");
            sqlQuery[0]  = sqlQuery[0].substring(0,sqlQuery[0].length()-1);
            sqlQuery[0] += ")))";
        }else if(shapeType == 2003 && listOfPoints.size() > 6){
            //Polygon
            sqlQuery[0] += "2003,NULL,NULL,";
            if(true){
                sqlQuery[0] += "SDO_ELEM_INFO_ARRAY(1,1003,1), SDO_ORDINATE_ARRAY(";
            }else{
                sqlQuery[0] += "SDO_ELEM_INFO_ARRAY(1,2003,1), SDO_ORDINATE_ARRAY(";
            }
            listOfPoints.forEach((n)-> sqlQuery[0] += n.toString() +",");

            sqlQuery[0] += listOfPoints.get(0) + "," + listOfPoints.get(1);
            sqlQuery[0] += ")))";
        }else if(shapeType == 2002 && listOfPoints.size() > 4){
            int cnt = 1 + listOfPoints.size()/4;
            sqlQuery[0] += "2006,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,4," + cnt + ",";

            for (int i = 0; i < cnt; i++){
                sqlQuery[0] += Integer.toString((1 + (i*2))) + ",2,1,";
            }

            sqlQuery[0]  = sqlQuery[0].substring(0,sqlQuery[0].length()-1);
            sqlQuery[0] += "), SDO_ORDINATE_ARRAY(";

            for (int i = 0; i < listOfPoints.size()-2; i+=2){
                for (int j = 0; j < 4; j++){
                    sqlQuery[0] += Integer.toString(listOfPoints.get(i + j)) + " ,";
                }
            }

            sqlQuery[0]  = sqlQuery[0].substring(0,sqlQuery[0].length()-1);
            sqlQuery[0] += ")))";
        }else{
            sqlQuery[0] = "";
        }

        return sqlQuery[0];
    }


    private int elemTypeToShapeType(String type){
        if(type.equals("electric-mast") || type.equals("gas-mast")){
            return 2001;
        }else if (type.equals("electric-net") || type.equals("gas-pipeline")){
            return 2002;
        }else{
            return 2003;
        }
    }

    public String createArea(String name,String type){
        int x = listOfPoints.get(0);
        int y = listOfPoints.get(1);
        String data;

        if(type.toLowerCase().equals("electric-mast")){
            data = x + "," + (y-10) + "," + (x+10) + "," + y + "," + x + "," + (y+10);
        }else{
            data = x + "," + (y-15) + "," + (x+15) + "," + y + "," + x + "," + (y+15);
        }

        return "INSERT INTO map (type, name, geometry) VALUES(\'" + type.replace("mast","area").toLowerCase() + "\',\'" +  name.toLowerCase()+ "-area" +
                "\', SDO_GEOMETRY(2003,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,1003,4), SDO_ORDINATE_ARRAY(" + data + ")))";
    }

    public void onSave(ActionEvent actionEvent) {
        String name = elementName.getText().toLowerCase();
        String elemType = elementType.getValue().toString().toLowerCase();

        if(listOfPoints.isEmpty()){
            return;
        }

        ArrayList<ObjectsInDB> arrayList;
        String insert = createElem(name, elemType, elemTypeToShapeType(elemType));

        if(!insert.isEmpty()){
            connect.connect();
            connect.insert(insert);
            System.out.println(insert);

            // In object
            if(elemTypeToShapeType(elemType) != 2002){
                connect.checkCoverageElement(elemType,name,true);
            }else{
                //Is electric net
                System.out.println("Line insert");
                String areaType;
                String netType;
                String mastType;

                if(elemType.equals("electric-net")){
                    areaType = "electric-area";
                    netType  = "electric-net";
                    mastType = "electric-mast";
                }else{
                    areaType = "gas-area";
                    netType  = "gas-pipeline";
                    mastType = "gas-mast";
                }

                //Get area of net
                String getArea = "SELECT a.name, a.type, a.geometry FROM map a, map b where( a.type = \'" + areaType +"\') and ( (b.type = \'" + netType + "\' and b.name = \'" + name + "\')) and (sdo_relate(a.geometry, b.geometry, 'mask=ANYINTERACT') = 'TRUE')";
                arrayList = connect.query(getArea);
                System.out.println(getArea);
                if(arrayList == null)
                    return;

                if(arrayList.size() > 1){
                    Double[] coords = new Double[4];
                    ArrayList<ObjectsInDB> mastList;

                    int i = 0;
                    for (ObjectsInDB obj : arrayList) {
                        mastList = connect.query("SELECT b.name, b.type, b.geometry FROM map a, map b where( a.type = \'" + areaType + "\' and a.name= \'" + obj.name + "\') and ( b.type = \'" + mastType + "\' ) and (sdo_relate(a.geometry, b.geometry, 'mask=ANYINTERACT') = 'TRUE')");
                        coords[i] = mastList.get(0).ordinates[0];
                        coords[i+1] = mastList.get(0).ordinates[1];
                        i += 2;
                    }

                    for (Double coord : coords) {
                        System.out.println(coord);
                    }

                    int [] info = {1,2,1};
                    connect.update(2,coords,name,info);
                }
            }

            if(elemTypeToShapeType(elemType) == 2001){
                String area = createArea(name,elemType);
                connect.insert(area);
                arrayList = connect.query("SELECT m.name, m.type, m.geometry FROM map m where (m.name =\'" + name + "\') or (m.name =\'" + name + "-area\')");
            }else{
                arrayList = connect.query("SELECT m.name, m.type, m.geometry FROM map m where (m.name =\'" + name + "\')");
            }

            new Shapes(arrayList, group);
        }

        removeElems();
    }



    public void onRevert(ActionEvent actionEvent) {
        removeElems();
    }

    public void onExit(ActionEvent actionEvent) {
        removeElems();
        active = false;

        try {
            pane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("mainMenu.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
