package sample.controllers;
import com.sun.javafx.collections.ElementObservableListDecorator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
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

import java.util.Date;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


//Controller for the main window
public class renewController implements Initializable {

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
    @FXML
    private Text bookID;
    @FXML
    private ListView<String> issueDataList;
    Boolean isReadyForSubmission = false;
    private boolean isReadyForSubbmission;


    @Override
    public void initialize(URL rl, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
    }

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
        } else {
            return;
        }
    }

    // Logging the user out if he clicks the log out button
    @FXML
    private void logoutAction(ActionEvent actionEvent) throws IOException {
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


   @FXML
    private void loadBookInfo2(ActionEvent event) {

        ObservableList<String> issueData = FXCollections.observableArrayList();
    }

    boolean isReadyForSubmission = false;

    String id = bookID.getText();
    String qu = "SELECT * FROM ISSUE WHERE bookID = '" + id + "'";

    ResultSet rs = databaseHandler.execQuery(qu);
        try

    {
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String mBookID = id;
            String mMemberID = null;
            try {
                mMemberID = rs.getString("memberID");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Timestamp mIssueTime = null;
            try {
                mIssueTime = rs.getTimestamp("issueTime");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            int mRenewCount = 0;
            try {
                mRenewCount = rs.getInt("renew_count");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            ElementObservableListDecorator<Object> issueData = null;

            issueData.add("Issue Data and Time :" + mIssueTime.toGMTString());
            issueData.add("Issue Data and Time :" + mIssueTime.toGMTString());
            issueData.add("Renew Count :" + mRenewCount);

            issueData.add("Book Information:-");

            qu = "SELECT * FROM BOOK WHERE ID = '" + mBookID + "'";
            ResultSet r1 = databaseHandler.execQuery(qu);
            while (true) {
                try {
                    if (!r1.next()) break;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    issueData.add("Book Name :" + r1.getString("title"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    issueData.add("Book ID :" + r1.getString("id"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    issueData.add("Book Author :" + r1.getString("author"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    issueData.add("Book Publisher :" + r1.getString("publisher"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            qu = "SELECT * FROM MEMBER WHERE ID = '" + mMemberID + "'";
            r1 = databaseHandler.execQuery(qu);
            issueData.add("Member Information:-");
            while (true) {
                try {
                    if (!r1.next()) break;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    issueData.add("Name:" + r1.getString("name"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    issueData.add("Mobile:" + r1.getString("mobile"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    issueData.add("Email:" + r1.getString("email"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            boolean isReadyForSubmission = true;
        }
    }catch(
    SQLException ex)

    {
        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);

        String issueData = null;
        issueDataList.getItems().setAll(issueData);

    }

    @FXML
    private void loadOnSubmissionOp(ActionEvent event) {

        if (!isReadyForSubbmission) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please Select A Book To Submit");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Submission Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return the book ?");
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {

            String id = bookID.getText();
            String ac1 = " DELETE FROM ISSUE WHERE BOOKID = '" + id + "'";
            String ac2 = "UPDATE BOOK SET ISAVAIL = TRUE WHERE ID = '" + id + "'";

            if (databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Book Has Been Submitted");
                alert.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText(null);
                alert.setContentText("Submission Has Been Failed");
                alert.showAndWait();
            }

        }else{
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Canceled");
            alert.setHeaderText(null);
            alert.setContentText("Submission Operation canceled");
            alert1.showAndWait();

        }
    }
@FXML
    private void loadRenewOp(ActionEvent event){

    if (!isReadyForSubbmission) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Failed");
        alert.setHeaderText(null);
        alert.setContentText("Please Select A Book To Renew");
        alert.showAndWait();
        return;
    }

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirm Renew Operation");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to renew the book ?");
    Optional<ButtonType> response = alert.showAndWait();
    if (response.get() == ButtonType.OK){
        String ac ="UPDATE ISSUE SET isueTime = CURRENT TIMESTAMP,renew_count = renew_count+1 WHERE BOOKID = '" +bookID.getText() + "'";
        System.out.println(ac);
        if(databaseHandler.execAction(ac)){

            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Book has been renewed");
            alert1.showAndWait();

        }else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Renew Has Been Failed");
            alert.showAndWait();
        } }else{
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Canceled");
            alert.setHeaderText(null);
            alert.setContentText("Renew Operation canceled");
            alert1.showAndWait();

        }

    }
}




















