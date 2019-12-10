package com.fit.upa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;

import static javafx.application.Application.launch;

public class login {
    @FXML
    public TextField Login;
    public TextField Password;

    @FXML
    public AnchorPane pane;

    public static login instance;

    private static Stage primaryStage;

    public CheckBox checked;

    private boolean dbInit=false;

    public login() throws IOException, SQLException {
        //System.out.println(dbConn);
        instance = this;
    }
    public static login getInstance(){
        return instance;
    }

    public void setStage(Stage primaryStage){
        login.primaryStage = primaryStage;
    }

    public void Submit(ActionEvent event) throws IOException, SQLException {
        DbConnection dbConnect = DbConnection.getInstance();
        dbConnect.setLogin(Login.getText());
        dbConnect.setPassword(Password.getText());
        doConnect(dbConnect);
    }

    public void xgrofc00(ActionEvent event) throws IOException, SQLException {
        DbConnection dbConnect = DbConnection.getInstance();
        dbConnect.setLogin("xgrofc00");
        dbConnect.setPassword("m2jCm39y");
        doConnect(dbConnect);
    }

    public void xkrajc17(ActionEvent event) throws IOException, SQLException {
        DbConnection dbConnect = DbConnection.getInstance();
        dbConnect.setLogin("xkrajc17");
        dbConnect.setPassword("aYh3FOPk");
        doConnect(dbConnect);
    }

    public void xnocia00(ActionEvent event) throws IOException, SQLException {
        DbConnection dbConnect = DbConnection.getInstance();
        dbConnect.setLogin("xnocia00");
        dbConnect.setPassword("eWHhifOx");
        doConnect(dbConnect);
    }

    private void doConnect(DbConnection dbConnection) throws IOException, SQLException {
        dbConnection.connect();
        if (dbConnection.isConnected()) {
            System.out.println("we are successfuly connected");
            if(dbInit){
                initDB();
            }
            Parent root = FXMLLoader.load(getClass().getResource("elemSelect.fxml"));
            Scene scene = new Scene(root ,1024, 768);
            primaryStage.setScene(scene);

        }else{
            System.out.println("wrong login or password");
        }
    }

    public void initDB() throws IOException, SQLException {
        String[] toBeExecuted={"map.sql","multidb_init.sql"};
        for (int i=0; i < toBeExecuted.length;i++){
            initDB(toBeExecuted[i]);
        }
    }

    public void initDB(String aSQLScriptFilePath) throws IOException,SQLException {
        Statement stmt = DbConnection.getInstance().getConn().createStatement();
        InputStream inputStream = getInstance().getClass().getResourceAsStream("initDB/"+aSQLScriptFilePath);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        StringBuffer sb = new StringBuffer();
        while ((str = in.readLine()) != null) {
            sb.append(str + "\n ");
        }
        String[] stmts = sb.toString().split(";");
        for (int i=0;i<stmts.length;i++){
//            System.out.println(stmts[i]);
            try {
            stmt.executeUpdate(stmts[i]);
            } catch (Exception e) {
//                System.err.println("Failed to Execute" + aSQLScriptFilePath +". The error is "+ e.getMessage());
            }
        }
        in.close();
    }

    public void initDB(ActionEvent event){
        if(!checked.isSelected()){
            dbInit=false;
        }else{
            dbInit=true;
        }
    }

}