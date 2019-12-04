
package com.fit.upa;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.*;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.List;



public class MultiOBJ {
    private static final String SQL_INSERT_NEW = "INSERT INTO multiobj (spatname, spattype, image) VALUES (?, ?, ordsys.ordimage.init())";
    private static final String SQL_SELECT_IMAGE_FOR_UPDATE = "SELECT image FROM MultiOBJ WHERE id = ? FOR UPDATE";
    private static final String SQL_UPDATE_IMAGE = "UPDATE MultiOBJ SET image = ? WHERE id = ?";
    private static final String SQL_FIND_IMAGE = "SELECT image FROM MultiOBJ WHERE spatname = ?";
    private static final String SQL_FIND_DEL_IMAGE = "SELECT id FROM MultiOBJ WHERE spatname = ?";
    private static final String SQL_DELETE_IMAGE = "DELETE FROM MultiOBJ WHERE id = ?";

//    private static final String SQL_UPDATE_DATA = "UPDATE MultyOBJ SET title = ? WHERE id = ?";
//    private static final String SQL_SELECT_IMAGE_FOR_UPDATE = "SELECT image FROM MultyOBJ WHERE id = ? FOR UPDATE";
//
//    private static final String SQL_MAX_ID = "SELECT MAX(id) FROM MultyOBJ";
//
//
    private static final String SQL_UPDATE_STILLIMAGE = "UPDATE MultiOBJ p SET p.image_si = SI_StillImage(p.image.getContent()) WHERE p.id = ?"; // an SQL method call needs to be on table.column, not just column
    private static final String SQL_UPDATE_STILLIMAGE_META = "UPDATE MultiOBJ SET image_ac = SI_AverageColor(image_si), image_ch = SI_ColorHistogram(image_si), image_pc = SI_PositionalColor(image_si), image_tx = SI_Texture(image_si) WHERE id = ?";
    public Button prev;
    public Button next;
    public Button delImg;

    private String title;
    private DbConnection dbConn;
    private int index;
    @FXML
    public Text SpatObj;


    public Text getSpatObj(){
        return SpatObj;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setSpatObj(Text SpatObj) {
        this.SpatObj = SpatObj;
    }

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    @FXML
    public AnchorPane pane;

    @FXML
    public AnchorPane imageOut;

    public static MultiOBJ instance;
    public MultiOBJ() throws IOException, SQLException {
        dbConn = DbConnection.getInstance();
        System.out.println(dbConn);
        instance = this;
    }

    public static MultiOBJ getInstance(){
        return instance;
    }
    public AnchorPane getAnchor(){
        return pane;
    }

    @FXML
    public void backImg(ActionEvent event) throws IOException {
        pane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("mainMenu.fxml")));
    }

    @FXML
    public void addImg(ActionEvent event) throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
//        fileChooser.showOpenMultipleDialog(null);
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
        if (selectedFiles != null){
            for (File child : selectedFiles) {
                toDBFile(dbConn.getConn(), child.toString());
            }
        }
        showImg();
    }

    private void recreateStillImageData(Connection connection, int rs) throws SQLException {
        try (PreparedStatement preparedStatementSi = connection.prepareStatement(SQL_UPDATE_STILLIMAGE)) {
            preparedStatementSi.setInt(1, rs);
            preparedStatementSi.executeUpdate();
        }
        try (PreparedStatement preparedStatementSiMeta = connection.prepareStatement(SQL_UPDATE_STILLIMAGE_META)) {
            preparedStatementSiMeta.setInt(1, rs);
            preparedStatementSiMeta.executeUpdate();
        }
    }

    public void toDBFile(Connection connection, String filename) throws SQLException, IOException{
        final boolean previousAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        try (PreparedStatement Insert = connection.prepareStatement(SQL_INSERT_NEW, new String[] { "id", "spatname", "spattype" })){
            OrdImage ordImage;
            Insert.setString(1, String.valueOf(SpatObj.getText()));
            Insert.setString(2, "build");
            Insert.executeQuery();
            System.out.println(SpatObj);

            ResultSet rs = Insert.getGeneratedKeys();
            rs.next();
            int right = rs.getBigDecimal(1).intValue();

            try (PreparedStatement find = connection.prepareStatement(SQL_SELECT_IMAGE_FOR_UPDATE)) {
                find.setInt(1, right);
                try (ResultSet resultSet = find.executeQuery()) {
                    if (resultSet.next()) {
                        final OracleResultSet oracleResultSet = (OracleResultSet) resultSet;
                        ordImage = (OrdImage) oracleResultSet.getORAData(1, OrdImage.getORADataFactory());
                        ordImage.loadDataFromFile(filename);
                        ordImage.setProperties();
                        try (PreparedStatement preparedStatementUpdate = connection.prepareStatement(SQL_UPDATE_IMAGE)) {
                            final OraclePreparedStatement fin = (OraclePreparedStatement) preparedStatementUpdate;
                            fin.setORAData(1, ordImage);
                            preparedStatementUpdate.setInt(2, right);
                            preparedStatementUpdate.executeUpdate();
                        }
                        recreateStillImageData(connection, right);
                    }
                }
            }
        } finally {
            connection.setAutoCommit(previousAutoCommit);
        }
    }

    public OracleResultSet findImg() throws SQLException, IOException {
        OraclePreparedStatement pstmt = null;
        OracleResultSet rs = null;
        pstmt = (OraclePreparedStatement)dbConn.getConn().prepareStatement(SQL_FIND_IMAGE);
        pstmt.setString(1,String.valueOf(SpatObj.getText()));
        rs = (OracleResultSet)pstmt.executeQuery();
        return rs;
    }
    public void showImg() throws SQLException, IOException {
        OracleResultSet rs = findImg();
        rs=findCur(rs);
        if (rs.next()){
            imageOut.setVisible(true);
            delImg.setVisible(true);
            Image image = getImgFromDB(rs);
            this.imageOut.getChildren().clear();
            if(image != null)
            {
                ImageView imgView = new ImageView(image);
                imgView.fitWidthProperty().bind(this.imageOut.widthProperty());
                imgView.fitHeightProperty().bind(this.imageOut.heightProperty());
                this.imageOut.getChildren().add(imgView);
            }
        }
        else{
            imageOut.setVisible(false);
            delImg.setVisible(false);
        }
        nextVis(rs);
    }

    public OracleResultSet findCur(OracleResultSet rs) throws SQLException {
        int counter=0;
        while(counter<index){
            rs.next();
            counter++;
        }
        prevVis(counter);
        return rs;
    }

    public ResultSet findCur(ResultSet rs) throws SQLException {
        int counter=0;
        while(counter<index){
            rs.next();
            counter++;
        }
        prevVis(counter);
        return rs;
    }

    public void prevVis(int counter){
        if (counter != 0){
            prev.setVisible(true);
        }else{
            prev.setVisible(false);
        }
    }

    public void nextVis(OracleResultSet rs) throws SQLException {
        if (rs.next()){
            next.setVisible(true);
        }else{
            next.setVisible(false);
        }
    }

    public ResultSet findForDel() throws SQLException {
        PreparedStatement find = dbConn.getConn().prepareStatement(SQL_FIND_DEL_IMAGE);
        find.setString(1, String.valueOf(SpatObj.getText()));
        ResultSet rs = find.executeQuery();
        if (rs.next()){
            findCur(rs);
            return rs;
        }
        return null;
    }

    public void delImg() throws SQLException, IOException {
        ResultSet rs = findForDel();
        if(rs!=null){
            OraclePreparedStatement pstmt = null;
            pstmt = (OraclePreparedStatement)dbConn.getConn().prepareStatement(SQL_DELETE_IMAGE);
            pstmt.setInt(1,rs.getInt(1));
            pstmt.executeQuery();
            prevA();
        }
    }

    public Image getImgFromDB(OracleResultSet rs) throws SQLException, IOException
    {

        OrdImage oImage = (OrdImage)rs.getORAData("image",OrdImage.getORADataFactory());
        if (oImage != null){
            BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(oImage.getDataInByteArray()));
            Image image = SwingFXUtils.toFXImage(buffImg, null);
            return image;
        }
        return null;
    }

    public void prevA() throws IOException, SQLException {
        if(this.getIndex() != 0){
            this.setIndex(this.getIndex()-1);
        }
        this.showImg();
    }

    public void nextA() throws IOException, SQLException {
            this.setIndex(this.getIndex()+1);
            this.showImg();
    }

    @FXML
    public void prev(ActionEvent event) throws IOException, SQLException {
        prevA();
    }

    @FXML
    public void next(ActionEvent event) throws IOException, SQLException {
        nextA();
    }
}



