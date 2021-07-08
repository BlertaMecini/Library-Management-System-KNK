package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.repositories.DatabaseHandler;


import java.net.URL;
import java.util.ResourceBundle;

public class addMemberController implements Initializable {
    @FXML
    private  String mGender;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField memberID;

    @FXML
    private TextField name;

    @FXML
    private TextField email;

    @FXML
    private TextField phone;

    @FXML
    private RadioButton female;

    @FXML
    private RadioButton male;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        databaseHandler=new DatabaseHandler();
    }
    @FXML
    private void saveButton(ActionEvent actionEvent) {
        String mID =memberID.getText();
        String mName =name.getText();
        String mEmail =email.getText();
        String mPhone =phone.getText();
        if(male.isSelected()){
            mGender = "Male";
        }
        else if(female.isSelected()) {
            mGender = "Female";
        };


        if(mID.isEmpty()|| mName.isEmpty()|| mEmail.isEmpty()|| mPhone.isEmpty() || mGender.isEmpty()){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("All fields are required. Please fill them out!");
            alert.showAndWait();
            return;
        }


        String qu="INSERT INTO addMember VALUES (" +
                "'"+ mID +"',"+
                "'"+ mName +"',"+
                "'"+ mEmail +"',"+
                "'"+ mPhone +"',"+
                "'"+ mGender +" "+
                ")";
        if(databaseHandler.execAction(qu)){
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("New member succesfully added!");
            alert.showAndWait();
            clear();

        } else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Sorry, we couldn't add this member!");
            alert.showAndWait();
            clear();

        }
    }

    public void clear(){
        memberID.setText("");
        name.setText("");
        email.setText("");
        phone.setText("");
        male.setSelected(false);
        female.setSelected(false);
    }

    @FXML
    private  void cancelButton(ActionEvent actionEvent) {
        ((Stage)rootPane.getScene().getWindow()).close();
    }

}
