package com.fit.upa.shapes;

import com.fit.upa.DbConnection;
import com.fit.upa.MultiOBJ;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class CircInfo {
    public Shapes.Circ owner;
    @FXML
    public Text name;

    @FXML
    public GridPane gpane;

    @FXML
    public AnchorPane pane;

    @FXML
    public Button imageButton;

    @FXML
    public Text size_val;
    @FXML
    public Text free_val;
    @FXML
    public Text size;
    @FXML
    public Text free;
    @FXML
    public Text gas;
    @FXML
    public Text gas_val;

    public static CircInfo instance;
    public CircInfo(){
        instance = this;
    }
    public static CircInfo getInstance(){
        return instance;
    }

    public void setOwner(Shapes.Circ r){
        owner = r;
    }

    public void initialize(){
        imageButton.setVisible(false); //nezobrazi button pre obrazky
        name.setText("hello");

        size     = (Text) pane.lookup("#velkost");
        free     = (Text) pane.lookup("#plocha");
        size_val = (Text) pane.lookup("#velkost_val");
        free_val = (Text) pane.lookup("#plocha_val");
        gas      = (Text) pane.lookup("#plynovod");
        gas_val  = (Text) pane.lookup("#plynovod_val");
        gas.setVisible(false);
        gas_val.setVisible(false);
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

    public void getLandSize(){
        DbConnection connection = DbConnection.getInstance();
        if(owner.objType.equals("land")){
            String result0 = connection.selectVal("SELECT SUM(SDO_GEOM.SDO_AREA(geometry, 1)) val FROM map where name = \'" + owner.name +"\'");
            size_val.setText( result0 + " m2");

            String result1 = connection.selectVal("SELECT SUM(SDO_GEOM.SDO_AREA(b.geometry, 1)) val FROM map a, map b where( a.name = \'" + owner.name + "\')  and (b.type = 'build') and (sdo_relate(a.geometry, b.geometry, 'mask=ANYINTERACT') = 'TRUE')");
            if(result1 != null){
                double val = Float.parseFloat(result0) - Float.parseFloat(result1) ;
                val = (Math.round(val * 100)/100.0);
                free_val.setText(val + " m2");
            }else{
                free_val.setText( result0 + " m2");
            }

            String result2 = connection.selectVal("SELECT count(b.name) val FROM map a, map b where(a.name = \'" + owner.name + "\')  and (b.type = 'build') and (sdo_relate(a.geometry, b.geometry, 'mask=ANYINTERACT') = 'TRUE')");
            gas.setText("Pocet budov");

            gas_val.setText(result2);
            gas.setVisible(true);
            gas_val.setVisible(true);

        }else if( owner.objType.equals("build")){
            size.setText("Obytna plocha");
            String result0 = connection.selectVal("SELECT SUM(SDO_GEOM.SDO_AREA(geometry, 1)) val FROM map where name = \'" + owner.name +"\'");
            size_val.setText( result0 + " m2");
            free_val.setVisible(false);
            free.setVisible(false);
        }else{
            size_val.setVisible(false);
            size.setVisible(false);
            free_val.setVisible(false);
            free.setVisible(false);
        }
    }
}
