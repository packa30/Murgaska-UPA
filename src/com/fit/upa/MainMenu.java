package com.fit.upa;

import com.fit.upa.shapes.Shapes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

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
        MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("createMenu.fxml")));
    }

    public void logout() throws Exception {
        DbConnection.getInstance().disconnect();
        MainMenu.getInstance().getAnchor().getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("login.fxml")));
        Main.instance.start(Main.instance.stage);
    }

    public void findAction(){
        for (Shapes.Circ i : Shapes.instance.circs){
            if(i.name.equals("test22-area")){
                drawGroup.getChildren().remove(i);
                System.out.println("HHHHHHHHHHHHHHHHHHH");
//                i.setFill(Color.GREEN.deriveColor(1,1,1,0.5));
//                i.setStroke(Color.GREEN);
            }
        }

        for (Shapes.Poly i : Shapes.instance.polys){
            if(i.name.equals("e22-area")){
                System.out.println("OOOOOOOOOOOOOOOOOO");

                //                i.setFill(Color.GREEN.deriveColor(1,1,1,0.5));
//                i.setStroke(Color.GREEN);
            }
        }
    }
}