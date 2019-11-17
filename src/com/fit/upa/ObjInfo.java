package com.fit.upa;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ObjInfo {
    @FXML
    public Text name;

    @FXML
    public GridPane gpane;

    public static ObjInfo instance;
    public ObjInfo(){
        instance = this;
    }
    public static ObjInfo getInstance(){
        return instance;
    }

    public void initialize(){
        name.setText("hello");
    }

    public void setName(String text) {
        // set text from another class
        name.setText(text);
    }
    public GridPane getGPane(){
        return gpane;
    }
}
