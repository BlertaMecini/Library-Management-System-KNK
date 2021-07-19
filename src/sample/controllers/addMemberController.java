package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.repositories.DatabaseHandler;

import java.lang.reflect.Member;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


// This is a controller for adding members to the database
public class addMemberController implements Initializable {

    DatabaseHandler databaseHandler;
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
    private CheckBox check;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;

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



    @FXML
    // If the users click the save button , this is executed
    private void saveButton(ActionEvent actionEvent) {

        String mID = memberID.getText();
        String mName = name.getText();
        String mEmail = email.getText();
        String mPhone = phone.getText();
        String mGender = "";
        if (male.isSelected()) {
            mGender = "male";
        } else if (female.isSelected()) {
            mGender = "female";
        }
        ;

        if(isInEditMode){
            handleEditOperation();
            return;
        }

        if (mID.isEmpty() || mName.isEmpty() || mEmail.isEmpty() || mPhone.isEmpty() || mGender.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("All fields are required. Please fill them out!");
            alert.showAndWait();
            return;
        }
        else if (!mID.matches("[0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Member ID must contain only numbers!");
            alert.showAndWait();
            return;
        }
        else if (!mName.matches("^[a-zA-Z\\s]*$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Name must contain only letters and whitespaces!");
            alert.showAndWait();
            return;
        }
        else if (!mEmail.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("The email you typed is not valid!");
            alert.showAndWait();
            return;
        } else if (!mPhone.matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Invalid Phone Number");
            alert.showAndWait();
            return;
        }


        String qu = "INSERT INTO addMember VALUES (" +
                "'" + mID + "'," +
                "'" + mName + "'," +
                "'" + mEmail + "'," +
                "'" + mPhone + "'," +
                "'" + mGender + "' " +
                ")";

        if (databaseHandler.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("New member succesfully added!");
            alert.showAndWait();
            clear();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR");
            alert.setContentText("Sorry, we couldn't add this member!");
            alert.showAndWait();
            clear();

        }
    }

    private void handleEditOperation() {
        String gender = "female";
        viewMembersController.Member member = new viewMembersController.Member(memberID.getText(),name.getText(),email.getText(),phone.getText(), gender);
        if(databaseHandler.updateMember(member)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("SUCCESS");
            alert.setContentText("Success! Member updated");
            alert.showAndWait();
            clear();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Failed ! Member can not be updated");
            alert.showAndWait();
            clear();
        }
    }

    // Clearing the window after the save button is clicked
    public void clear() {
        memberID.setText("");
        name.setText("");
        email.setText("");
        phone.setText("");
        male.setSelected(false);
        female.setSelected(false);
        check.setSelected(false);
    }

    // A method to close the window if user clicks the close button
    @FXML
    private void cancelButton(ActionEvent actionEvent) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    public void inflatedUI (viewMembersController.Member member){
        String gender = member.getGender();
        memberID.setText(member.getMemberID());
        name.setText(member.getName());
        email.setText(member.getEmail());
        phone.setText(member.getPhone());
        memberID.setEditable(false);
        isInEditMode = Boolean.TRUE;
        female.setSelected(true);
        if(gender.toLowerCase() == "male"){
            male.setSelected(true);
        }else if (gender.toLowerCase() == "female"){
            female.setSelected(true);
        }


    }

}
