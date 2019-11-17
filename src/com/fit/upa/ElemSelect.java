package com.fit.upa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class ElemSelect {
    @FXML
    private Group group;
    @FXML
    public AnchorPane anch2;

    public static ElemSelect instance;
    public ElemSelect(){
        instance = this;
    }
    public static ElemSelect getInstance(){
        return instance;
    }
    public AnchorPane getAnchor(){
        return anch2;
    }

/*
    @FXML
    public void onClick(ActionEvent event){
        System.out.println("you clicked me");
        //anch2 = FXMLLoader.load(getClass().getResource("objInfo.fxml"));
    }
*/
    public void initialize(){
        DbConnection dbConn = DbConnection.getInstance();
        System.out.println(dbConn.isConnected());

        ArrayList<ObjectsInDB> arrayList = dbConn.query("SELECT c.name, c.geometry FROM city c");
        System.out.println(arrayList);
        for(ObjectsInDB elem : arrayList) {
            System.out.println(">>" + elem.info());
        }
        Shapes shapes = new Shapes(arrayList, group, anch2);
    }

    @FXML
    public void ClickedClickMe(ActionEvent actionEvent) throws IOException {
        System.out.println(anch2.getId());
        anch2.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("objInfo.fxml")));// = FXMLLoader.load(getClass().getResource("objInfo.fxml"));
    }
}
