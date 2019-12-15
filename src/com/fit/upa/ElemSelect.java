package com.fit.upa;

import com.fit.upa.shapes.Shapes;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

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
        Scale scale =new Scale();
        scale.setX(1);
        scale.setY(-1);/*
        scale.pivotYProperty().bind(Bindings.createDoubleBinding(() ->
                        group.getBoundsInLocal().getMinY() + group.getBoundsInLocal().getHeight() /2,
                group.boundsInLocalProperty()));
        group.getTransforms().add(scale);*/
        Translate translate = new Translate();
        translate.setY(-760);
        group.getTransforms().addAll(scale, translate);
        MainMenu.drawGroup = group;
    }
}