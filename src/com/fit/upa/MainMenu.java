package com.fit.upa;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainMenu {
    @FXML
    public AnchorPane pane;

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
}