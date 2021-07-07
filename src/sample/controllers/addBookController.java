package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.repositories.DatabaseHandler;
import java.net.URL;
import java.util.ResourceBundle;


public class addBookController implements Initializable {


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
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    DatabaseHandler  databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        databaseHandler=new DatabaseHandler();
    }



    public static boolean onlyDigits(String str, int n)
    {
        for (int i = 1; i < n; i++) {

            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }


    @FXML
    private void saveAction(ActionEvent actionEvent) {
        String bookId =id.getText();
        String bookName =title.getText();
        String bookAuthor =author.getText();
        String bookPublisher =publisher.getText();
        boolean onlyDigits=onlyDigits(bookId, bookId.length());

        if(bookId.isEmpty()|| bookAuthor.isEmpty()|| bookName.isEmpty()|| bookPublisher.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("All fields are required. Please fill them out!");
            alert.showAndWait();
            return;
        }

        //spo ban
        else if(onlyDigits==false && !bookId.startsWith("B") ){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("ID of book should start with letter 'B' followed by digits!");
            alert.showAndWait();
            return;
        }
        else if(!bookAuthor.matches("[a-zA-Z]+")){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Author name should contain only letters!");
            alert.showAndWait();
            return;
        }
        else if(bookPublisher.matches("[0-9]+")){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Publisher name can't contain only numbers!");
            alert.showAndWait();
            return;
        }

        String qu="INSERT INTO addBook VALUES (" +
                "'"+ bookId +"',"+
                "'"+ bookName +"',"+
                "'"+ bookAuthor +"',"+
                "'"+ bookPublisher +"',"+
                ""+ true +""+
                ")";
        if(databaseHandler.execAction(qu)){
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("New book succesfully added!");
            alert.showAndWait();
            clear();

        } else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Sorry, we couldn't add this book!");
            alert.showAndWait();
            clear();

        }
    }

    void clear(){
        id.setText("");
        title.setText("");
        author.setText("");
        publisher.setText("");
    }

    @FXML
    private  void cancelAction(ActionEvent actionEvent) {
            ((Stage)rootPane.getScene().getWindow()).close();
    }

}
