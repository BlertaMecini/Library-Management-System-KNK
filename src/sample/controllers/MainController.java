package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainController implements Initializable {
    @FXML
    private TextField bookIdInput;
    @FXML
    private Text bookName;
    @FXML
    private Text authorName;
    @FXML
    private HBox book_info;
    @FXML
    private Text availability;
    @FXML
    private HBox member_info;
    @FXML
    private TextField memberIdInput;
    @FXML
    private Text contact;
    @FXML
    private Text memberName;
    @FXML
    private StackPane rootPane;

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
        loadWindow("/sample/views/addBook.fxml", "Add Book");
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
            stage.getIcons().add(new Image("https://static.thenounproject.com/png/3314579-200.png"));
            stage.setResizable(false);
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
        loadWindow("/sample/views/addBook.fxml", "Add Book");
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
        loadWindow("/sample/views/aboutUs.fxml", "About Us");
    }
    private static final String GitHub = "https://github.com/BlertaMecini/Library-Managment-System-KNK";

    private void loadWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
            handleWebpageLoadException(url);
        }
    }

    private void handleWebpageLoadException(String url) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        Stage stage = new Stage();
        Scene scene = new Scene(new StackPane(browser));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void loadGitHub(ActionEvent event) {
        loadWebpage(GitHub);
    }


    @FXML
    private  void issueHandler(ActionEvent actionEvent) {
        //to be implemented
    }

    @FXML
    private void handleMenuLogOut(ActionEvent actionEvent) {
        //LOGOUUT te manu bar nalt me implementu qety

    }

    @FXML
    private void logoutAction(ActionEvent actionEvent) {
        //LOGOUT te buttoni anash me implementu qety
    }
}