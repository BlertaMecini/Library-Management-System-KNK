package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainController implements Initializable {
    @FXML
    private TextField bookIdInput;
    @FXML
    public Text bookName;
    @FXML
    public Text authorName;
    @FXML
    public HBox book_info;
    @FXML
    public Text availability;
    @FXML
    public HBox member_info;
    @FXML
    public TextField memberIdInput;
    @FXML
    public Text contact;
    @FXML
    public Text memberName;
    @FXML
    public StackPane rootPane;

    Connection conn;

    @Override
    public void initialize(URL rl, ResourceBundle rb) {
    }

    //need to change location to real ones
    @FXML
   private void loadAddMember(javafx.event.ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "Add Member");
    }

    @FXML
    private void loadAddBook(javafx.event.ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "Add Book");
    }

    @FXML
    private void loadViewMembers(javafx.event.ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "Member List");
    }

    @FXML
    private void loadViewBooks(javafx.event.ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "Book list");
    }

    @FXML
    private void loadViewIssuedBooks(javafx.event.ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "Issued Books");
    }


    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // not done yet
    @FXML
    private void loadBookInfo(ActionEvent actionEvent) throws SQLException {
        clearBookcache();

        String id=bookIdInput.getText();
        Statement stmt = conn.createStatement();
        String query="SELECT * FROM BOOK WHERE id='"+id+"'";
        ResultSet rs = stmt.executeQuery(query);
        Boolean flag=false;

        try{
        while(rs.next())
        {
            String bName=rs.getString("title");
            String bAuthor=rs.getString("author");
            Boolean bStatus=rs.getBoolean("isAvail");
            bookName.setText(bName);
            authorName.setText(bAuthor);
            String status=(bStatus)?"Available":"Not Available";
            availability.setText(status);
            flag=true;
        }
        if(!flag){
            bookName.setText("No such book found");
        }
        } catch (SQLException ex){
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void clearBookcache(){
        bookName.setText("");
        authorName.setText("");
        availability.setText("");
    }

    @FXML
    private void loadMemberInfo(ActionEvent actionEvent) throws SQLException {
        clearMembercache();
        String id=memberIdInput.getText();
        Statement stmt = conn.createStatement();
        String query="SELECT * FROM MEMBER WHERE id='"+id+"'";
        ResultSet rs= stmt.executeQuery(query);
        Boolean flag=false;
        try{
            while(rs.next())
            {
                String mName=rs.getString("name");
                String mMobile=rs.getString("mobile");

                memberName.setText(mName);
                contact.setText(mMobile);

                flag=true;
            }
            if(!flag){
                memberName.setText("No such member found");
            }
        } catch (SQLException ex){
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void clearMembercache(){
        memberName.setText("");
        contact.setText("");
    }


    @FXML
    private void handleMenuClose(ActionEvent actionEvent) {
        ((Stage)rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void handleAddMember(javafx.event.ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "Add Member");
    }

    @FXML
    private void handleAddBook(javafx.event.ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "Add Book");
    }

    @FXML
    private void handleViewMembers(javafx.event.ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "View Members");
    }

    @FXML
    private void handleViewBooks(javafx.event.ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "View Books");
    }

    @FXML
    private void aboutHandler(ActionEvent actionEvent) {
        //to be implemented
    }

    @FXML
    private  void issueHandler(ActionEvent actionEvent) {
        //to be implemented
    }
}