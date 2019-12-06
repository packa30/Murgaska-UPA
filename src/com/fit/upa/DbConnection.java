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
                    String objType = rs.getString(2);
                    byte[] result = rs.getBytes(3);
                    JGeometry jGeometry;
                    jGeometry = JGeometry.load(result);
                    //System.out.println(jGeometry.toStringFull());
                    double[] ordinates = jGeometry.getOrdinatesArray();
                    int[] eleminfo = jGeometry.getElemInfo();
                    int type = jGeometry.getType();

                    double[] viewOrdinates = changeToAppOrdinates(ordinates);

                    System.out.println(eleminfo[2]+" , " +type);
                    //TODO pre dalsie objekty
                    if (eleminfo[2] == 3) {
                        arrayList.add(new ObjectsInDB(type ,objType,name, viewOrdinates, eleminfo));
                    } else if (eleminfo[2] == 1 && type == 3) {
                        arrayList.add(new ObjectsInDB(type ,objType,name, viewOrdinates, eleminfo));
                    } else if (eleminfo[2] == 4) {
                        arrayList.add(new ObjectsInDB(type, objType,name, viewOrdinates, eleminfo));
                    }else if (eleminfo[2] == 1 && type == 2) {
                        arrayList.add(new ObjectsInDB(type, objType, name, viewOrdinates, eleminfo));
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

    public void delete(String name){
        String sql;
        sql = "delete from map where name = '"+name+"'";
        System.out.println(sql);
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkCoverageElement(String type, String name, boolean isCreate ){
        String select = "select a.name, b.name from map a, map b where (a.type = '"+ type +"') and (b.type = '"+ type + "') and (a.name <> b.name) AND " +
                "(SDO_RELATE(a.geometry, b.geometry, 'mask=ANYINTERACT') = 'TRUE')";

        System.out.println(select);
        try (PreparedStatement find = conn.prepareStatement(select)) {
            try (ResultSet resultSet = find.executeQuery()) {
                if (resultSet.next()) {
                    if(isCreate)
                        delete(name);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void update(int type, Double[] ordinates, String name, int[] elemInfo ){
        double[] dbOrdinates = changeToDbOrdinates(ordinates);
        String sql = "";
        if(type == 3){ //jedna sa o obdlznik
            sql = "update map set geometry = SDO_GEOMETRY(2003, NULL, NULL,SDO_ELEM_INFO_ARRAY"+arrayToString(elemInfo)+",SDO_ORDINATE_ARRAY"+arrayToString(dbOrdinates)+") where name = '" + name + "'";
            System.out.println(sql);
        }
        else if(type == 2){
            sql = "update map set geometry = SDO_GEOMETRY(2002, NULL, NULL,SDO_ELEM_INFO_ARRAY"+arrayToString(elemInfo)+",SDO_ORDINATE_ARRAY"+arrayToString(dbOrdinates)+") where name = '" + name + "'";
            System.out.println(sql);
        }

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String sql){
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double[] changeToAppOrdinates(double[] ordinates){
        double[] viewOrdinates = new double[ordinates.length];
        for(int i=0;i<ordinates.length;i++){
            if(i%2==0){
                viewOrdinates[i] = ordinates[i]*3.8;
            }
            else{
                //viewOrdinates[i] = 760-(ordinates[i]*3.8);
                viewOrdinates[i] = ordinates[i]*3.8;
            }
        }
        return viewOrdinates;
    }

    public double[] changeToDbOrdinates(Double[] ordinates){
        double[] viewOrdinates = new double[ordinates.length];
        for(int i =0; i<ordinates.length; i++){
            viewOrdinates[i] = ordinates[i]/3.8;
        }
        return viewOrdinates;
    }

    public String arrayToString(int[] array){
        String str = "(";
        for(int i: array){
            str += i+",";
        }
        str = str.substring(0, str.length() - 1);
        str += ")";
        return str;
    }

    public String arrayToString(double[] array){
        String str = "(";
        for(double i: array){
            str += i+",";
        }
        str = str.substring(0, str.length() - 1);
        str += ")";
        return str;
    }

    public Connection getConn(){
        return conn;
    }
    public boolean isConnected(){
        return connected;
    }
}