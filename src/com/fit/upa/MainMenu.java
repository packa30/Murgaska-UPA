package com.fit.upa;

import com.fit.upa.shapes.PolyInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

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

    public void onAddObj() throws IOException {
        try {
            MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("createMenu.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}