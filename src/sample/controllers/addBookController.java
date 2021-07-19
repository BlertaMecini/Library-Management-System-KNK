package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.repositories.DatabaseHandler;

import java.net.URL;
import java.util.ResourceBundle;


// This is a controller to handle the add book functionality
public class addBookController implements Initializable {


    DatabaseHandler databaseHandler;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField title;
    @FXML
    private TextField id;
    @FXML
    private TextField author;
    @FXML
    private TextField publisher;
    @FXML
    private TextField quantity;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private CheckBox check;

    private Boolean isInEditMode = Boolean.FALSE;

    public static boolean onlyDigits(String str, int n) {
        for (int i = 1; i < n; i++) {

            if (Character.isDigit(str.charAt(i))) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = new DatabaseHandler();
    }

    // If the user clicks the save button
    @FXML
    private void saveAction(ActionEvent actionEvent) {

        String bookId = id.getText();
        String bookName = title.getText();
        String bookAuthor = author.getText();
        String bookPublisher = publisher.getText();
        String bookQuantity = quantity.getText();
        boolean onlyDigits = onlyDigits(bookId, bookId.length());

        if(isInEditMode){
            handleEditOperation();
            return;
        }
        // Validating the fields
        if (bookId.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookPublisher.isEmpty() || bookQuantity.isEmpty() || !check.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("All fields are required. Please fill them out!");
            alert.showAndWait();
            return;
        } else if (onlyDigits == false && !bookId.startsWith("B")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("ID of book should start with letter 'B' followed by digits!");
            alert.showAndWait();
            return;
        } else if (!bookAuthor.matches("[a-zA-Z ]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Author name should contain only letters!");
            alert.showAndWait();
            return;
        } else if (bookPublisher.matches("[0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Publisher name can't contain only numbers!");
            alert.showAndWait();
            return;
        } else if (!bookQuantity.matches("[0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Quantity should only contain numbers!");
            alert.showAndWait();
            return;
        }

        // If all fields are OK we add the values to the database
        String qu = "INSERT INTO addBook VALUES (" +
                "'" + bookId + "'," +
                "'" + bookName + "'," +
                "'" + bookAuthor + "'," +
                "'" + bookPublisher + "'," +
                "'" + bookQuantity + "'," +
                "" + true + "" +
                ")";

        if (databaseHandler.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("New book successfully added!");
            alert.showAndWait();
            clear();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Sorry, we couldn't add this book!");
            alert.showAndWait();
            clear();

        }
    }

    private void handleEditOperation() {
        booklistController.Book book = new booklistController.Book(id.getText(),title.getText(),author.getText(),publisher.getText(), Integer.parseInt(quantity.getText()), true);
        if(databaseHandler.updateBook(book)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("Success! Book updated");
            alert.showAndWait();
            clear();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Failed ! Book can not be updated");
            alert.showAndWait();
            clear();
        }
    }
    // Clearing the fields after save button is clicked
    void clear() {
        id.setText("");
        title.setText("");
        author.setText("");
        publisher.setText("");
        quantity.setText("");
        check.setSelected(false);
    }

    // Closing the window when the close button is clicked
    @FXML
    private void cancelAction(ActionEvent actionEvent) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    public void inflatedBUI (booklistController.Book book){

        id.setText(book.getBookID());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        title.setText(book.getTitle());
        quantity.setText(book.getQuantity().toString());

        isInEditMode = Boolean.TRUE;

        id.setEditable(false);


    }

}
