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
    public AnchorPane Apane;

    public static ElemSelect instance;
    public ElemSelect(){
        instance = this;
    }
    public static ElemSelect getInstance(){
        return instance;
    }

    public void initialize(){
        DbConnection dbConn = DbConnection.getInstance();
        System.out.println(dbConn.isConnected());

        ArrayList<ObjectsInDB> arrayList = dbConn.query("SELECT c.name, c.geometry FROM city c");
        System.out.println(arrayList);
        for(ObjectsInDB elem : arrayList) {
            System.out.println(">>" + elem.info());
        }
        Shapes shapes = new Shapes(arrayList, group);
    }
}