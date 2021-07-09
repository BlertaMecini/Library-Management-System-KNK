package sample.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

public class booklistController implements Initializable {

    DatabaseHandler databaseHandler ;

    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, String> BookIDColumn;
    @FXML
    private TableColumn<Book, String> TitleColumn;
    @FXML
    private TableColumn<Book, String> AuthorColumn;
    @FXML
    private TableColumn<Book, String> PublisherColumn;
    @FXML
    private TableColumn<Book, Integer> QuantityColumn;
    @FXML
    private TableColumn<Book, Boolean> AvailabilityColumn;

    public static class Book {
        private final SimpleStringProperty BookID;
        private final SimpleStringProperty Title;
        private final SimpleStringProperty Author;
        private final SimpleStringProperty Publisher ;
        private final SimpleIntegerProperty Quantity ;
        private final SimpleBooleanProperty Availability ;


        public Book ( String BookID ,  String Title , String Author , String Publisher , Integer Quantity, Boolean Availability){
            this.BookID = new SimpleStringProperty(BookID);
            this.Title = new SimpleStringProperty(Title);
            this.Author=new SimpleStringProperty(Author);
            this.Publisher =new SimpleStringProperty(Publisher);
            this.Quantity = new SimpleIntegerProperty(Quantity);
            this.Availability = new SimpleBooleanProperty(Availability);

        }

        public String getBookID() {
            return BookID.get();
        }

        public String getTitle() {
            return Title.get();
        }

        public String getAuthor() {
            return Author.get();
        }

        public String getPublisher() {
            return Publisher.get();
        }

        public Integer getQuantity() {
            return Quantity.get();
        }

        public Boolean getAvailability() {
            return Availability.get();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        loadData();
    }

    private void initCol() {
        BookIDColumn.setCellValueFactory(new PropertyValueFactory<>("BookID"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<>("Author"));
        PublisherColumn.setCellValueFactory(new PropertyValueFactory<>("Publisher"));
        QuantityColumn.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        AvailabilityColumn.setCellValueFactory(new PropertyValueFactory<>("Availability"));

    }
    private void loadData() {
        ObservableList<Book> list = FXCollections.observableArrayList();
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String  qu = "SELECT * FROM addBook";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String BookID = rs.getString("BookID");
                String Title = rs.getString("Title");
                String Author = rs.getString("Author");
                String Publisher= rs.getString("Publisher");
                Integer Quantity = rs.getInt("Quantity");
                Boolean Availability = rs.getBoolean("Availability");

                list.add(new Book(BookID, Title,Author, Publisher,Quantity,Availability));

            }
        } catch (SQLException ex) {
            Logger.getLogger(addBookController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.setItems(list);
    }
    @FXML
    private void addNewBook(javafx.event.ActionEvent actionEvent) {
        loadWindow("addBook.fxml", "Add Book");
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

}
