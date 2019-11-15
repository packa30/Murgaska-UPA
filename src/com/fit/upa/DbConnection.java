package com.fit.upa;
import java.sql.*;

public class DbConnection {
    private static DbConnection dbConn = null;
    private boolean connected = false;
    private Connection conn;

    private DbConnection(){}

    public static DbConnection getInstance(){
        if(dbConn == null) {
            dbConn = new DbConnection();
        }
        return dbConn;
    }

    public void connect() {
        if(!connected) {
            try {
                conn = DriverManager.getConnection("jdbc:oracle:thin:@//gort.fit.vutbr.cz:1521/orclpdb", "xnocia00", "eWHhifOx");
                connected = true;
            } catch (SQLException e) {
                connected = false;
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if(connected){
            try {
                conn.close();
                connected = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Already disconnected");
        }
    }
    public Connection getConn(){
        return conn;
    }
    public boolean isConnected(){
        return connected;
    }
}
