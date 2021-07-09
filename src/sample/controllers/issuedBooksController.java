
        package sample.controllers;

        import javafx.beans.property.SimpleStringProperty;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.fxml.Initializable;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.TableColumn;
        import javafx.scene.control.TableView;
        import javafx.scene.control.TextField;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.scene.image.Image;
        import javafx.stage.Stage;
        import javafx.stage.StageStyle;
        import sample.repositories.DatabaseConnection;
        import sample.repositories.DatabaseHandler;

        import java.io.IOException;
        import java.net.URL;
        import java.sql.*;
        import java.util.ResourceBundle;
        import java.util.logging.Level;
        import java.util.logging.Logger;

public class issuedBooksController implements Initializable {

    DatabaseHandler databaseHandler;

    @FXML
    private TableView<IssuedBook> tableView;
    @FXML
    private TableColumn<IssuedBook, String> BookIDCol;
    @FXML
    private TableColumn<IssuedBook, String> MemberCol;
    @FXML
    private TableColumn<IssuedBook, String> TimeCol;
    @FXML
    private TableColumn<IssuedBook, String> RenewCol;

    public static class IssuedBook {
        private final SimpleStringProperty bookId;
        private final SimpleStringProperty name;
        private final SimpleStringProperty issueTime;
        private final SimpleStringProperty renew;


        public IssuedBook(String bookId, String name, String issueTime, String renew) {
            this.bookId = new SimpleStringProperty(bookId);
            this.name = new SimpleStringProperty(name);
            this.issueTime = new SimpleStringProperty(issueTime);
            this.renew = new SimpleStringProperty(renew);


        }

        public String getBookId() {
            return bookId.get();
        }

        public String getName() {
            return name.get();
        }

        public String getrenew() {
            return renew.get();
        }

        public String getissueTime() {
            return issueTime.get();
        }



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void initCol() {
        BookIDCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        MemberCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TimeCol.setCellValueFactory(new PropertyValueFactory<>("issueTime"));
        RenewCol.setCellValueFactory(new PropertyValueFactory<>("renew"));

    }

    private void loadData() {
        ObservableList<IssuedBook> list = FXCollections.observableArrayList();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM issuedBooks";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String bookId = rs.getString("bookId");
                String name = rs.getString("name");
                String issueTime = rs.getString("issueTime");
                String renew = rs.getString("renew");


                list.add(new IssuedBook(bookId, name, issueTime, renew));

            }
        } catch (SQLException ex) {
            Logger.getLogger(addBookController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.setItems(list);
    }



    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}