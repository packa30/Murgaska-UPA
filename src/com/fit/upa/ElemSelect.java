package com.fit.upa;

import com.fit.upa.shapes.Shapes;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;

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

        ArrayList<ObjectsInDB> arrayList = dbConn.query("SELECT m.name, m.type, m.geometry FROM map m");
        Shapes shapes = new Shapes(arrayList, group);
    }
}