
package com.fit.upa;

import java.io.File;
import java.sql.*;

import com.fit.upa.shapes.Shapes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.pool.OracleDataSource;
import oracle.ord.im.OrdImage;

import java.io.IOException;
import java.util.List;


public class MultiOBJ {
    private static final String SQL_INSERT_NEW = "INSERT INTO multiobj (spatname, spattype, image) VALUES (?, ?, ordsys.ordimage.init())";
    private static final String SQL_SELECT_IMAGE_FOR_UPDATE = "SELECT image FROM MultiOBJ WHERE id = ? FOR UPDATE";
    private static final String SQL_UPDATE_IMAGE = "UPDATE MultiOBJ SET image = ? WHERE id = ?";

//    private static final String SQL_UPDATE_DATA = "UPDATE MultyOBJ SET title = ? WHERE id = ?";
//    private static final String SQL_SELECT_IMAGE_FOR_UPDATE = "SELECT image FROM MultyOBJ WHERE id = ? FOR UPDATE";
//
//    private static final String SQL_MAX_ID = "SELECT MAX(id) FROM MultyOBJ";
//
//
    private static final String SQL_UPDATE_STILLIMAGE = "UPDATE MultiOBJ p SET p.image_si = SI_StillImage(p.image.getContent()) WHERE p.id = ?"; // an SQL method call needs to be on table.column, not just column
    private static final String SQL_UPDATE_STILLIMAGE_META = "UPDATE MultiOBJ SET image_ac = SI_AverageColor(image_si), image_ch = SI_ColorHistogram(image_si), image_pc = SI_PositionalColor(image_si), image_tx = SI_Texture(image_si) WHERE id = ?";

    private String title;
    private DbConnection dbConn;
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

    @FXML
    public AnchorPane pane;

    public static MultiOBJ instance;
    public MultiOBJ() {
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

        for (File child : selectedFiles) {
            toDBFile(dbConn.getConn(), child.toString());
        }
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


    public void showImg(ActionEvent event) throws SQLException {

        OraclePreparedStatement pstmt = null;
        OracleResultSet rs = null;
        pstmt = (OraclePreparedStatement)dbConn.getConn().prepareStatement("SELECT image FROM MultiOBJ WHERE spatname = ?");
        pstmt.setString(1,String.valueOf(SpatObj.getText()));
        rs = (OracleResultSet)pstmt.executeQuery();
        if (rs.next()){
            OrdImage m_img = (OrdImage) rs.getORAData(1, OrdImage.getORADataFactory());

        }

    }
}



