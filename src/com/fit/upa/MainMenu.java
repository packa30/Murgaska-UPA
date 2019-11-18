package com.fit.upa;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MainMenu {
    @FXML
    public AnchorPane anch2;

    public static MainMenu instance;
    public MainMenu(){
        instance = this;
    }
    public static MainMenu getInstance(){
        return instance;
    }
    public AnchorPane getAnchor(){
        return anch2;
    }
}
