package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.repositories.DatabaseHandler;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


//Controller for the main window
public class MainController implements Initializable {

    private static final String GitHub = "https://github.com/BlertaMecini/Library-Managment-System-KNK";
    Connection conn;
    DatabaseHandler databaseHandler;

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

    @Override
    public void initialize(URL rl, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
    }


    // Loading the corrosponding windows when buttons are clicked
    // Need to change location to real ones for view members,books,issued books
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

    // A method for loading windows
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

    // Loading the book information for issuing books
    @FXML
    private void loadBookInfo(ActionEvent actionEvent) throws SQLException {

        clearBookcache();

        String id = bookIdInput.getText();
        String query = "SELECT * FROM addBook WHERE id='" + id + "'";
        ResultSet rs = databaseHandler.execQuery(query);
        Boolean flag = false;

        try {
            while (rs.next()) {
                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Integer bStatus = rs.getInt("quantity");
                bookName.setText(bName);
                authorName.setText(bAuthor);
                String status = (bStatus) > 0 ? "Available" : "Not Available";
                availability.setText(status);
                flag = true;
            }
            if (!flag) {
                bookName.setText("No such book is found!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // A method to clear the book cache when we search for a book
    void clearBookcache() {
        bookName.setText("");
        authorName.setText("");
        availability.setText("");
    }


    // A method for loading member info for issuing books
    @FXML
    private void loadMemberInfo(ActionEvent actionEvent) throws SQLException {

        clearMembercache();
        String id = memberIdInput.getText();
        String query = "SELECT * FROM addMember WHERE memberID='" + id + "'";
        ResultSet rs = databaseHandler.execQuery(query);
        Boolean flag = false;
        try {
            while (rs.next()) {
                String mName = rs.getString("name");
                String mMobile = rs.getString("email");


                memberName.setText(mName);
                contact.setText(mMobile);

                flag = true;
            }
            if (!flag) {
                memberName.setText("No such member is found!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Clearing the member cashe
    void clearMembercache() {
        memberName.setText("");
        contact.setText("");
    }

    // Handlind the menu items at menu bar
    @FXML
    private void handleMenuClose(ActionEvent actionEvent) {
        ((Stage) rootPane.getScene().getWindow()).close();
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
    private void handleViewIssuedBooks(ActionEvent actionEvent) {
        loadWindow("/sample/views/addMember.fxml", "View Issued Books");
    }

    @FXML
    private void aboutHandler(ActionEvent actionEvent) {
        loadWindow("/sample/views/aboutUs.fxml", "About Us");
    }

    // A method for loading web pages
    private void loadWebpage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
            handleWebpageLoadException(url);
        }
    }

    // If exceptions happen during the loading process of web pages
    private void handleWebpageLoadException(String url) {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        Stage stage = new Stage();
        Scene scene = new Scene(new StackPane(browser));
        stage.setScene(scene);
        stage.show();
    }


    // Loading GitHub
    @FXML
    private void loadGitHub(ActionEvent event) {
        loadWebpage(GitHub);
    }


    // Here we handle the issue book functionality
    @FXML
    private void issueHandler(ActionEvent actionEvent) {
        String memberID = memberIdInput.getText();
        String bookID = bookIdInput.getText();
        String isAvailable = availability.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to issue the book titled '" + bookName.getText() + "' to '" + memberName.getText() + "' ?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            if (isAvailable == "Available") {
                String query1 = "INSERT INTO issuedBooks(bookID,memberID) VALUES("
                        + "'" + bookID + "',"
                        + "'" + memberID + "')";

                String query2 = "UPDATE addBook SET  quantity=quantity-1  where id='" + bookID + "'";


                if (databaseHandler.execAction(query1) && databaseHandler.execAction(query2)) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("SUCCESS");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book was issued succesfully!");
                    alert1.showAndWait();

                    clearMembercache();
                    clearBookcache();
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("FAILED");
                    alert2.setHeaderText(null);
                    alert2.setContentText("Sorry, we couldn't issue the book!");
                    alert2.showAndWait();
                }
            } else {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("FAILED");
                alert3.setHeaderText(null);
                alert3.setContentText("This book isn't available!");
                alert3.showAndWait();
            }
        } else if (response.get() == ButtonType.CANCEL) {
            Alert alert4 = new Alert(Alert.AlertType.INFORMATION);
            alert4.setTitle("CANCELED");
            alert4.setHeaderText(null);
            alert4.setContentText("Book issue was canceled!");
            alert4.showAndWait();
        }

    }


    // Loging the admin out if he clicks the log out menu item
    @FXML
    private void handleMenuLogOut(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to Log Out?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            Parent parent = FXMLLoader.load(getClass().getResource("../views/login.fxml"));
            Scene scene = new Scene(parent);
            Stage primaryStage = new Stage();
            primaryStage.initStyle(StageStyle.DECORATED);
            primaryStage.setTitle("Library Managment System");
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image("https://static.thenounproject.com/png/3314579-200.png"));
            ((Stage) rootPane.getScene().getWindow()).close();
            primaryStage.setScene(scene);
            primaryStage.show();
        } else{
            return;
        }
    }

        // Logging the user out if he clicks the log out button
        @FXML
        private void logoutAction (ActionEvent actionEvent) throws IOException {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Log Out");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to Log Out?");

            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                Parent parent = FXMLLoader.load(getClass().getResource("../views/login.fxml"));
                Scene scene = new Scene(parent);
                Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        }

    }