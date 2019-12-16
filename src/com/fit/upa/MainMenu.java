package com.fit.upa;

import com.fit.upa.shapes.Shapes;
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


    public void initialize(){
        isPressed = false;
        findAction = (Button) pane.lookup("#find");
        select = (Button) pane.lookup("#select");
        select.setVisible(false);
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
            select.setVisible(false);
            arrayList = DbConnection.getInstance().query("select a.name, a.type, a.geometry from map a, map b where (a.type = 'build') and (b.type = 'gas-area') and (sdo_relate(a.geometry, b.geometry, 'mask=ANYINTERACT') = 'TRUE')");

            for(ObjectsInDB elem : arrayList) {
                System.out.println(elem.name);
                if(elem.eleminfo[2] == 3){
                    //Rec
                    for (Shapes.Rec i : Shapes.instance.recs){
                        if(i.name.equals(elem.name)){
                            i.setStroke(Color.BLACK);
                            i.setFill(Color.BLACK);
                        }
                    }
                }
                else if(elem.eleminfo[2] == 1 && elem.type == 3){
                    // Poly
                    for (Shapes.Poly i : Shapes.instance.polys){
                        i.setStroke(Color.BLACK);
                        i.setFill(Color.BLACK);
                    }
                }
            }
        }else{
            findAction.setText("Find");
            isPressed = false;
            select.setVisible(false);

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


}