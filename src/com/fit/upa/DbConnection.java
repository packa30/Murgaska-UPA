package com.fit.upa;

import java.sql.*;

public class DbConnection {

    public static void main(String[] args) {
        //Open a connection
        try (Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@//gort.fit.vutbr.cz:1521/orclpdb","xnocia00", "eWHhifOx")) {

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
            //Execute a query
            Statement stmt=conn.createStatement();

            ResultSet rs=stmt.executeQuery("select * from Students");
            //Extract data from result set
            while(rs.next()){
                int id = rs.getInt("id");
                String login = rs.getString("login");
                String fullname = rs.getString("fullName");
                System.out.print("ID: " + id + "   ");
                System.out.print("LOGIN: " + login + "   ");
                System.out.println("FULL NAME: " + fullname);
            }
            //Clean-up
            rs.close();
            stmt.close();
        //Handle errors
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
