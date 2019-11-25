package com.fit.upa;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import oracle.spatial.geometry.JGeometry;

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

    public ArrayList<ObjectsInDB> query(String query){
        ArrayList<ObjectsInDB> arrayList = new ArrayList<ObjectsInDB>();
        if(!connected){
            System.out.println("Already disconnected");
        }
        else {
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String name = rs.getString(1);
                    System.out.println(name);
                    byte[] result = rs.getBytes(2);
                    JGeometry jGeometry;
                    jGeometry = JGeometry.load(result);
                    //System.out.println(jGeometry.toStringFull());
                    double[] ordinates = jGeometry.getOrdinatesArray();
                    int[] eleminfo = jGeometry.getElemInfo();
                    System.out.println(Arrays.toString(eleminfo));
                    System.out.println(Arrays.toString(ordinates));

                    double[] viewOrdinates = changeToAppOrdinates(ordinates);

                    //TODO pre dalsie objekty
                    if (eleminfo[2] == 3) {
                        arrayList.add(new ObjectsInDB(3,name, ordinates));
                    } else if (eleminfo[2] == 1) {
                        arrayList.add(new ObjectsInDB(1,name, viewOrdinates));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public double[] changeToAppOrdinates(double[] ordinates){
        double[] viewOrdinates = new double[ordinates.length];
        for(int i=0;i<ordinates.length;i++){
            if(i%2==0){
                viewOrdinates[i] = ordinates[i]*3.8;
            }
            else{
                viewOrdinates[i] = 760-(ordinates[i]*3.8);
            }
        }
        return viewOrdinates;
    }

    public Connection getConn(){
        return conn;
    }
    public boolean isConnected(){
        return connected;
    }
}