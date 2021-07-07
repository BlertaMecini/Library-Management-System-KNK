package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class addBookController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb){
        //TODO
    }

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


    @FXML
    private void saveAction(ActionEvent actionEvent) {
    }

    @FXML
    private  void cancelAction(ActionEvent actionEvent) {
    }
}
