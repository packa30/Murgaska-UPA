package com.fit.upa.shapes;

//import com.fit.upa.shapes.Rec;
import com.fit.upa.MultiOBJ;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

public class PolyLInfo {
    public Shapes.Polyl owner;
    @FXML
    public Text name;

    @FXML
    public GridPane gpane;

    @FXML
    public AnchorPane pane;

    @FXML
    public Button imageButton;

    public static PolyLInfo instance;
    public PolyLInfo(){
        instance = this;
    }
    public static PolyLInfo getInstance(){
        return instance;
    }

    public void setOwner(Shapes.Polyl r){
        owner = r;
    }

    public void initialize(){
        imageButton.setVisible(false); //nezobrazi button pre obrazky
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
    public CheckBox ch1;

    @FXML
    public void check(ActionEvent event) throws IOException {
        if(ch1.isSelected()){
            owner.setEnableEdit(true);
        }
        if(!ch1.isSelected()){
            owner.setEnableEdit(false);
            owner.discardChanges();
            owner.orderObjects();
        }
    }

    @FXML
    public void onApply(ActionEvent event) {
        if(ch1.isSelected()){
            owner.updateCoordFromTextField();
            owner.applyChanges();
        }

    }

    @FXML
    public void onDelete(ActionEvent event) throws IOException {
        if(ch1.isSelected()){
            ch1.setSelected(false);
            owner.setEnableEdit(false);
            owner.discardChanges();
            owner.orderObjects();
            owner.delete();
            pane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("../mainMenu.fxml")));
        }
    }

    public void setSelect(){
        ch1.setSelected(true);
    }

    @FXML
    public void onBtn(ActionEvent event) throws IOException, SQLException {
        pane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("../multiOBJ.fxml")));
        MultiOBJ.getInstance().setSpatObj(name);
        MultiOBJ.getInstance().setIndex(0);
        MultiOBJ.getInstance().showImg();
    }
}