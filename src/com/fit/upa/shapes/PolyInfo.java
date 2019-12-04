package com.fit.upa.shapes;

//import com.fit.upa.shapes.Poly;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

import java.io.IOException;

public class PolyInfo {
    public Shapes.Poly owner;
    @FXML
    public Text name;

    @FXML
    public GridPane gpane;

    @FXML
    public AnchorPane pane;

    @FXML
    public Button imageButton;

    public static PolyInfo instance;
    public PolyInfo(){
        instance = this;
    }
    public static PolyInfo getInstance(){
        return instance;
    }

    public void setOwner(Shapes.Poly p){
        owner = p;
    }

    public void initialize(){
        imageButton.setVisible(false);
        name.setText("hello");
    }

    public void setName(String text) {
        // set text from another class
        name.setText(owner.name);
    }
    public GridPane getGPane(){
        return gpane;
    }

    @FXML
    public void onClick(ActionEvent event) throws IOException {
        ch1.setSelected(false);
        owner.setEnableEdit(false);
        owner.discardChanges();
        owner.orderObjects();
        //ElemSelect.getInstance().Apane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        pane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("../mainMenu.fxml")));
        //pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
    }

    @FXML
    public void onApply(ActionEvent event) {
        if(ch1.isSelected()){
            owner.updateCoordFromTextField();
            owner.applyChanges();
            ch1.setSelected(false);
            check();
        }

    }

    @FXML
    public CheckBox ch1;

    @FXML
    public void check(){//ActionEvent event) throws IOException {
        if(ch1.isSelected()){
            owner.setEnableEdit(true);
        }
        if(!ch1.isSelected()){
            owner.setEnableEdit(false);
            owner.discardChanges();
            owner.orderObjects();
        }
    }

    public void setSelect(){
        ch1.setSelected(true);
    }

    @FXML
    public void onBtn(ActionEvent event) {
        System.out.println("TODO button pre obrazok");
    }
}