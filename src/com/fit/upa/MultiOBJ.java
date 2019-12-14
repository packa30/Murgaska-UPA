
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
import javafx.scene.input.MouseEvent;
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
    private static final String SQL_SIMILAR_IMAGE = "SELECT dst.id, dst.spatname, SI_ScoreByFtrList(new SI_FeatureList(src.image_ac,?,src.image_ch,?,src.image_pc,?,src.image_tx,?),dst.image_si) AS similarity FROM multiobj src, multiobj dst WHERE (src.id = ?) AND (src.id <> dst.id) ORDER BY similarity ASC";



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
    public Button rotateImgLeft;
    public Button rotateImgRight;
    public Button findSim;

    private String title;
    private DbConnection dbConn;
    private int index;
    @FXML
    public Text SpatObj;
    @FXML
    public Text name;


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
        this.SpatObj = SpatObj; name.setText(SpatObj.getText());
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

    @FXML
    public AnchorPane imageSim;
    public AnchorPane imageSim1;
    public AnchorPane imageSim2;
    public AnchorPane imageSim3;
    public AnchorPane imageSim4;

    private int[] imageSimID = new int[4];
    private String[] imageSimID_fk = new String[4];
    private int imageSimIDShown;

    public static MultiOBJ instance;
    public MultiOBJ() throws IOException, SQLException {
        dbConn = DbConnection.getInstance();
        //System.out.println(dbConn);
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
                toDBFile(child.toString(),String.valueOf(SpatObj.getText()));
            }
        }
        showImg();
    }

    private void recreateStillImageData(int rs) throws SQLException {
        try (PreparedStatement preparedStatementSi = dbConn.getConn().prepareStatement(SQL_UPDATE_STILLIMAGE)) {
            preparedStatementSi.setInt(1, rs);
            preparedStatementSi.executeUpdate();
        }
        try (PreparedStatement preparedStatementSiMeta = dbConn.getConn().prepareStatement(SQL_UPDATE_STILLIMAGE_META)) {
            preparedStatementSiMeta.setInt(1, rs);
            preparedStatementSiMeta.executeUpdate();
        }
    }

    public void toDBFile(String filename, String spatName) throws SQLException, IOException{
        final boolean previousAutoCommit = dbConn.getConn().getAutoCommit();
        dbConn.getConn().setAutoCommit(false);
        try (PreparedStatement Insert = dbConn.getConn().prepareStatement(SQL_INSERT_NEW, new String[] { "id", "spatname", "spattype" })){

            Insert.setString(1, spatName);
            Insert.setString(2, "build");
            Insert.executeQuery();
            //System.out.println(SpatObj);

            ResultSet rs = Insert.getGeneratedKeys();
            rs.next();
            ResultSet resultSet = selectImg(rs.getBigDecimal(1).intValue());
            OrdImage ordImage = getSelectedImg(resultSet,filename);
            selectedUpdateImg(rs.getBigDecimal(1).intValue(),ordImage);

        } finally {
            dbConn.getConn().setAutoCommit(previousAutoCommit);
        }
    }

    public ResultSet selectImg(int id) throws SQLException {
        PreparedStatement find = dbConn.getConn().prepareStatement(SQL_SELECT_IMAGE_FOR_UPDATE);
        find.setInt(1, id);
        ResultSet resultSet = find.executeQuery();
        if (resultSet.next()) {
            return resultSet;
        }
        return null;
    }

    public OrdImage getSelectedImg(ResultSet resultSet, String filename) throws SQLException, IOException {
        OrdImage ordImage;
        final OracleResultSet oracleResultSet = (OracleResultSet) resultSet;
        ordImage = (OrdImage) oracleResultSet.getORAData(1, OrdImage.getORADataFactory());
        ordImage.loadDataFromFile(filename);
        ordImage.process("fixedScale=300 200");
        ordImage.setProperties();
        return ordImage;
    }

    public OrdImage getSelectedImg(ResultSet resultSet) throws SQLException, IOException {
        OrdImage ordImage;
        final OracleResultSet oracleResultSet = (OracleResultSet) resultSet;
        ordImage = (OrdImage) oracleResultSet.getORAData(1, OrdImage.getORADataFactory());
        return ordImage;
    }

    public void selectedUpdateImg(int id, OrdImage ordImage) throws SQLException, IOException {
        try (PreparedStatement preparedStatementUpdate = dbConn.getConn().prepareStatement(SQL_UPDATE_IMAGE)) {
            final OraclePreparedStatement fin = (OraclePreparedStatement) preparedStatementUpdate;
            fin.setORAData(1, ordImage);
            preparedStatementUpdate.setInt(2, id);
            preparedStatementUpdate.executeUpdate();
        }
        recreateStillImageData(id);
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
            setVisibility(true, false, 0);
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
            setVisibility(false, false, 0);
        }
        nextVis(rs);
    }

    public void setVisibility(boolean images, boolean sim_im, int simcount){
        imageOut.setVisible(images);
        delImg.setVisible(images);
        rotateImgLeft.setVisible(images);
        rotateImgRight.setVisible(images);
        findSim.setVisible(images);
        imageSim.setVisible(sim_im);
        imageSim1.setVisible(simcount >= 1);
        imageSim2.setVisible(simcount >= 2);
        imageSim3.setVisible(simcount >= 3);
        imageSim4.setVisible(simcount >= 4);
    };

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

    public ResultSet findWithID() throws SQLException {
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
        ResultSet rs = findWithID();
        if(rs!=null){
            OraclePreparedStatement pstmt = null;
            pstmt = (OraclePreparedStatement)dbConn.getConn().prepareStatement(SQL_DELETE_IMAGE);
            pstmt.setInt(1,rs.getInt(1));
            pstmt.executeQuery();
            prevA();
        }
    }

    public Image getImgFromDB(OracleResultSet rs) throws SQLException, IOException {

        OrdImage oImage = (OrdImage)rs.getORAData("image",OrdImage.getORADataFactory());
        if (oImage != null){
            BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(oImage.getDataInByteArray()));
            Image image = SwingFXUtils.toFXImage(buffImg, null);
            return image;
        }
        return null;
    }

    public void rotateImg(int angle) throws SQLException, IOException {
        final boolean previousAutoCommit = dbConn.getConn().getAutoCommit();
        dbConn.getConn().setAutoCommit(false);
        ResultSet rs = findWithID();
        ResultSet resultSet = selectImg(rs.getBigDecimal(1).intValue());
        OrdImage ordImage = getSelectedImg(resultSet);
        ordImage.process("rotate="+angle);
        selectedUpdateImg(rs.getBigDecimal(1).intValue(),ordImage);
        dbConn.getConn().setAutoCommit(previousAutoCommit);
        showImg();
    }

    public void findSim(ActionEvent event) throws IOException, SQLException {
        ResultSet rs = findWithID();
        try (PreparedStatement preparedStatement = dbConn.getConn().prepareStatement(SQL_SIMILAR_IMAGE)) {
            preparedStatement.setDouble(1, 0.3);
            preparedStatement.setDouble(2, 0.3);
            preparedStatement.setDouble(3, 0.1);
            preparedStatement.setDouble(4, 0.3);
            preparedStatement.setInt(5, rs.getBigDecimal(1).intValue());
            ResultSet resultSet2 = preparedStatement.executeQuery();
            int foursome=1;
            while(resultSet2.next()){
                this.imageSimID[foursome-1] = resultSet2.getInt(1);
                this.imageSimID_fk[foursome-1] = resultSet2.getString(2);
                this.imageSimIDShown=0;
                if (foursome == 1){
                    showSimImg(resultSet2.getInt(1));
                    showSimImg(resultSet2.getInt(1),foursome);
                }else{
                    showSimImg(resultSet2.getInt(1),foursome);
                }
                if (foursome==4){break;}
                foursome++;
            }
            setVisibility(true,true, foursome);
        }
    }

    public void showSimImg(int id) throws IOException, SQLException {
        ResultSet resultSet = selectImg(id);
        OrdImage oImage = getSelectedImg(resultSet);
        if (oImage != null){
            BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(oImage.getDataInByteArray()));
            Image image = SwingFXUtils.toFXImage(buffImg, null);
            if(image != null){
                ImageView imgView = new ImageView(image);
                imgView.fitWidthProperty().bind(this.imageSim.widthProperty());
                imgView.fitHeightProperty().bind(this.imageSim.heightProperty());
                this.imageSim.getChildren().add(imgView);
            }
        }
    }

    public void showSimImg(int id, int pos) throws IOException, SQLException {
        ResultSet resultSet = selectImg(id);
        OrdImage oImage = getSelectedImg(resultSet);
        if (oImage != null){
            BufferedImage buffImg = ImageIO.read(new ByteArrayInputStream(oImage.getDataInByteArray()));
            Image image = SwingFXUtils.toFXImage(buffImg, null);
            if(image != null){
                ImageView imgView = new ImageView(image);
                switch (pos){
                    case 1:
                        imgView.fitWidthProperty().bind(this.imageSim1.widthProperty());
                        imgView.fitHeightProperty().bind(this.imageSim1.heightProperty());
                        this.imageSim1.getChildren().add(imgView);
                        break;
                    case 2:
                        imgView.fitWidthProperty().bind(this.imageSim2.widthProperty());
                        imgView.fitHeightProperty().bind(this.imageSim2.heightProperty());
                        this.imageSim2.getChildren().add(imgView);
                        break;
                    case 3:
                        imgView.fitWidthProperty().bind(this.imageSim3.widthProperty());
                        imgView.fitHeightProperty().bind(this.imageSim3.heightProperty());
                        this.imageSim3.getChildren().add(imgView);
                        break;
                    case 4:
                        imgView.fitWidthProperty().bind(this.imageSim4.widthProperty());
                        imgView.fitHeightProperty().bind(this.imageSim4.heightProperty());
                        this.imageSim4.getChildren().add(imgView);
                        break;
                }
            }
        }
    }

    public void rotateImgRight(ActionEvent event) throws SQLException, IOException {
        rotateImg(90);
    }

    public void rotateImgLeft(ActionEvent event) throws SQLException, IOException {
        rotateImg(-90);
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

    public void imageSimHandler(MouseEvent mouseEvent) throws IOException, SQLException {
        if (mouseEvent.getSource() == imageSim1){
            showSimImg(this.imageSimID[0]);
            this.imageSimIDShown=0;
        }else if(mouseEvent.getSource() == imageSim2){
            showSimImg(this.imageSimID[1]);
            this.imageSimIDShown=1;
        }else if(mouseEvent.getSource() == imageSim3){
            showSimImg(this.imageSimID[2]);
            this.imageSimIDShown=2;
        }else if(mouseEvent.getSource() == imageSim4){
            showSimImg(this.imageSimID[3]);
            this.imageSimIDShown=3;
        }else{
            Text text = new Text();
            text.setText(this.imageSimID_fk[this.imageSimIDShown]);
            if (!text.getText().equals(SpatObj.getText())){
                pane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("multiOBJ.fxml")));
                MultiOBJ.getInstance().setSpatObj(text);
                MultiOBJ.getInstance().setIndex(0);
                MultiOBJ.getInstance().showImg();
            }

        }

    }
}



