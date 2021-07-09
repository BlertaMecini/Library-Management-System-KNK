package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
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

import javax.xml.transform.Result;
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
public class MainController implements Initializable {

    private static final String GitHub = "https://github.com/BlertaMecini/Library-Managment-System-KNK";

    @FXML
    private ListView<String> issueDataList;
    @FXML
    private TextField bookID;
    @FXML
    private TextField memberID;
    @FXML
    private  StackPane memberInfoContainer;
    @FXML
    private StackPane bookInfoContainer;
    Connection conn;
    DatabaseHandler databaseHandler;
    PieChart bookChart;
    PieChart memberChart;

    @FXML
    private TextField bookIdInput;
    @FXML
    private Text bookName;
    @FXML
    private Text authorName;
    @FXML
    private Text availability;
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
        initGraph();
   }

    // Showing the graphs
    private void initGraph() {

        bookChart = new PieChart(databaseHandler.getBookGraphicStatistics());
        memberChart = new PieChart(databaseHandler.getMemberGraphicStatistics());
        bookInfoContainer.getChildren().add(bookChart);
        memberInfoContainer.getChildren().add(memberChart); }


    //Refreshing the graphs
    public void refreshGraphs(){
        bookChart.setData(databaseHandler.getBookGraphicStatistics());
        memberChart.setData(databaseHandler.getMemberGraphicStatistics());
    }


    // Hidding the graphs
    private void hideShowGraph(Boolean status){
        if(status){
            bookChart.setOpacity(1);
            memberChart.setOpacity(1);
        }
        else{
            bookChart.setOpacity(0);
            memberChart.setOpacity(0);
        }
    }


    // Loading the corresponding windows when buttons are clicked
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
        loadWindow("/sample/views/viewMembers.fxml", "Members List");
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
                hideShowGraph(false);
                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Integer bStatus = rs.getInt("quantity");
                bookName.setVisible(true);
                bookName.setText(bName);
                authorName.setVisible(true);
                authorName.setText(bAuthor);
                String status = (bStatus) > 0 ? "Available" : "Not Available";
                availability.setVisible(true);
                availability.setText(status);
                flag = true;
            }
            if (flag==false) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book not found");
                alert.setHeaderText(null);
                alert.setContentText("No such book with this ID is found!");
                alert.showAndWait();
                bookIdInput.clear();

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
                hideShowGraph(false);
                String mName = rs.getString("name");
                String mMobile = rs.getString("email");

                memberName.setVisible(true);
                memberName.setText(mName);
                contact.setVisible(true);
                contact.setText(mMobile);

                flag = true;
            }
            if (flag==false) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Member not found");
                alert.setHeaderText(null);
                alert.setContentText("No such member with this ID is found!");
                alert.showAndWait();
                memberIdInput.clear();
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Clearing the member cache
    void clearMembercache() {
        memberName.setText("");
        contact.setText("");
    }

    // Handling the menu items at menu bar
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
        loadWindow("/sample/views/viewMembers.fxml", "View Members");
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

        if (memberID.isEmpty() || bookID.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("You must select Book ID and Member ID to issue a book!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Issue Operation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to issue the book titled '" + bookName.getText() + "' to '" + memberName.getText() + "' ?");

            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                String query="SELECT memberID,bookID from issuedBooks where memberID='"+memberID+"'" +
                        "and bookID ='"+bookID+"'";
                ResultSet result=databaseHandler.execQuery(query);

                try {
                    if(result.next()){
                        Alert bookIsAlreadyIssuedAlert = new Alert(Alert.AlertType.ERROR);
                        bookIsAlreadyIssuedAlert.setTitle("Duplicate Entry");
                        bookIsAlreadyIssuedAlert.setHeaderText(null);
                        bookIsAlreadyIssuedAlert.setContentText("This book was once issued to this member. You can renew it!");
                        bookIsAlreadyIssuedAlert.showAndWait();
                        clearIssueEntries();
                        hideShowGraph(true);
                        return;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (isAvailable == "Available") {
                    String query1 = "INSERT INTO issuedBooks(bookID,memberID) VALUES("
                            + "'" + bookID + "',"
                            + "'" + memberID + "')";

                    String query2 = "UPDATE addBook SET  quantity=quantity-1  where id='" + bookID + "'";


                    if (databaseHandler.execAction(query1) && databaseHandler.execAction(query2)) {
                        refreshGraphs();
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("SUCCESS");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Book was issued successfully!");
                        alert1.showAndWait();
                        clearIssueEntries();
                        hideShowGraph(true);
                    } else {
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("FAILED");
                        alert2.setHeaderText(null);
                        alert2.setContentText("Sorry, we couldn't issue the book!");
                        alert2.showAndWait();
                        clearIssueEntries();
                        hideShowGraph(true);
                    }
                }
             else {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("FAILED");
                alert3.setHeaderText(null);
                alert3.setContentText("This book isn't available!");
                alert3.showAndWait();
                clearIssueEntries();
                hideShowGraph(true);
            }
            }
         else if (response.get() == ButtonType.CANCEL) {
                Alert alert4 = new Alert(Alert.AlertType.INFORMATION);
                alert4.setTitle("CANCELED");
                alert4.setHeaderText(null);
                alert4.setContentText("Book issue was canceled!");
                alert4.showAndWait();
                clearIssueEntries();
                hideShowGraph(true);
            }
        }
    }



    // Clearing issue book information
    private void clearIssueEntries(){
        bookIdInput.clear();
        memberIdInput.clear();
        clearBookcache();
        clearMembercache();
    }

    // Clearing the info
    private void clearOnSubmissionIssueEntries(){
        bookID.clear();
        memberID.clear();
        issueDataList.getItems().clear();
    }


    // Submission tab

    boolean isReadyForSubmission=false;

    @FXML
    private void loadBookInfo2(ActionEvent event) {

        ObservableList<String> issueData = FXCollections.observableArrayList();

        String id = bookID.getText();
        String mid=memberID.getText();
        if( !id.isEmpty() && !mid.isEmpty()) {

            String qu = "SELECT * FROM issuedBooks WHERE bookID = '" + id + "' and memberID='" + mid + "'";

            ResultSet rs = databaseHandler.execQuery(qu);

            try {
                if(!rs.next()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText(null);
                    alert.setContentText("The book with ID: "+ id + " wasn't issued to member with  ID: " + mid +" !");
                    alert.showAndWait();
                    clearOnSubmissionIssueEntries();
                    return;
                }
                while (rs.next()) {
                    String mBookID = id;
                    String mMemberID = mid;
                    Timestamp mIssueTime = rs.getTimestamp("issueTime");
                    int mRenewCount = rs.getInt("renew_count");


                    issueData.add("Issue Information:- ");

                    issueData.add("Issue Date and Time: " + mIssueTime.toString());
                    issueData.add("Renew Count: " + mRenewCount);

                    issueData.add("Book Information:- ");

                    String query = "SELECT * FROM addBook WHERE id = '" + mBookID + "'";
                    ResultSet r1 = databaseHandler.execQuery(query);


                    while (r1.next()) {

                        issueData.add("Book Name: " + r1.getString("title"));
                        issueData.add("Book ID: " + r1.getString("id"));
                        issueData.add("Book Author: " + r1.getString("author"));
                        issueData.add("Book Publisher: " + r1.getString("publisher"));
                    }

                    String qu1 = "SELECT * FROM addMember WHERE memberID = '" + mMemberID + "'";
                    ResultSet r2 = databaseHandler.execQuery(qu1);
                    issueData.add("Member Information:- ");

                    while (r2.next()) {

                        issueData.add("Name: " + r2.getString("name"));
                        issueData.add("Mobile: " + r2.getString("mobile"));
                        issueData.add("Email: " + r2.getString("email"));
                    }

                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            isReadyForSubmission = true;
            issueDataList.getItems().setAll(issueData);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("You must select Book ID and Member ID to view the information!");
            alert.showAndWait();
            clearOnSubmissionIssueEntries();
        }

    }



// Submission and renewal

    @FXML
    private void loadOnSubmissionOp(ActionEvent event) {

        if (!isReadyForSubmission) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please Select A Book To Submit!");
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
            String mid=memberID.getText();
            String ac1 = " DELETE FROM issuedBooks WHERE bookID = '" + id + "' and memberID = '" + mid + "'";
            String ac2 = "UPDATE addBook SET quantity = quantity-1 WHERE id= '" + id + "'";

            if (databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2)) {
                Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION);
                confirmAlert.setTitle("Success");
                confirmAlert.setHeaderText(null);
                confirmAlert.setContentText("Book Has Been Submitted!");
                confirmAlert.showAndWait();
                clearOnSubmissionIssueEntries();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Submission Has Been Failed!");
                alert1.showAndWait();
                clearOnSubmissionIssueEntries();
            }

        }else{
            Alert  canceledAlert = new Alert(Alert.AlertType.INFORMATION);
            canceledAlert.setTitle("Canceled");
            canceledAlert.setHeaderText(null);
            canceledAlert.setContentText("Submission Operation canceled!");
            canceledAlert.showAndWait();
            clearOnSubmissionIssueEntries();
        }
    }


    @FXML
    private void loadRenewOp(ActionEvent event){

        if (!isReadyForSubmission) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please Select A Book To Renew!");
            alert.showAndWait();
            return;
        }

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Confirm Renew Operation");
        alert1.setHeaderText(null);
        alert1.setContentText("Are you sure you want to renew the book ?");
        Optional<ButtonType> response = alert1.showAndWait();
        if (response.get() == ButtonType.OK){
            String ac ="UPDATE issuedBooks SET issueTime = CURRENT_TIMESTAMP,renew_count = renew_count+1 WHERE bookID = '" +bookID.getText() + "'";
            System.out.println(ac);
            if(databaseHandler.execAction(ac)){

                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Success");
                alert2.setHeaderText(null);
                alert2.setContentText("Book has been renewed!");
                alert2.showAndWait();
                clearOnSubmissionIssueEntries();

            }else {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setTitle("Failed");
                alert3.setHeaderText(null);
                alert3.setContentText("Renew Has Been Failed!");
                alert3.showAndWait();
                clearOnSubmissionIssueEntries();
            } }else{
            Alert alert4 = new Alert(Alert.AlertType.INFORMATION);
            alert4.setTitle("Canceled");
            alert4.setHeaderText(null);
            alert4.setContentText("Renew Operation canceled!");
            alert4.showAndWait();
            clearOnSubmissionIssueEntries();
        }
    }


    // Logging the admin out if he clicks the log out menu item
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
            primaryStage.setTitle("Library Management System");
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(new Image("https://static.thenounproject.com/png/3314579-200.png"));
            ((Stage) rootPane.getScene().getWindow()).close();
            primaryStage.setScene(scene);
            primaryStage.show();
        } else{
            return;
        }
    }

        // Logging the admin out if he clicks the log out button
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