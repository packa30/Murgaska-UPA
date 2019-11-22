package com.fit.upa.shapes;

//import com.fit.upa.shapes.Rec;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

import java.io.IOException;

public class RecInfo {
    public Shapes.Rec owner;
    @FXML
    public Text name;

    @FXML
    public GridPane gpane;

    @FXML
    public AnchorPane pane;

    public static RecInfo instance;
    public RecInfo(){
        instance = this;
    }
    public static RecInfo getInstance(){
        return instance;
    }

    public void setOwner(Shapes.Rec r){
        owner = r;
    }

    public void initialize(){
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
        System.out.println("you clicked me");
        ch1.setSelected(false);
        owner.setEnableEdit(false);
        //ElemSelect.getInstance().Apane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        pane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("../mainMenu.fxml")));
        //pane = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
    }

    @FXML
    public CheckBox ch1;

    @FXML
    public void check(ActionEvent event) throws IOException {
        if(ch1.isSelected()){
            System.out.println("mozes menit");
            owner.setEnableEdit(true);
        }
        if(!ch1.isSelected()){
            System.out.println("nemozes menit");
            owner.setEnableEdit(false);
        }
    }

    public void setSelect(){
        ch1.setSelected(true);
    }
}