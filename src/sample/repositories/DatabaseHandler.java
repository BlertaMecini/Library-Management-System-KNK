package sample.repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.sql.*;

// This class handles the database connection

public final  class DatabaseHandler {

    // Variables used for connecting to the database and creating statements
    // Change these values to your mySQL values
    private static DatabaseHandler handler=null;
    private static String databaseName="admin";
    private static String userName="root";
    private static String password="1111";
    private static final String DB_URL="jdbc:mysql://127.0.0.1/"+ databaseName;

    private static Connection conn=null;
    private static Statement stmt=null;


    // Constructor of the class has methods that are called when the class is instanciated
    public DatabaseHandler(){
        createConnection();
        setupBookTable();
        setupIssuedBooksTable();
        setupMemberTable();
    }

    // If there is no instance of the class we get the instance
    public static DatabaseHandler getInstance(){
        if(handler==null){
            handler=new DatabaseHandler();
        }
        return handler;
    }


    // Conneting to the database
    void createConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,userName, password);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    // Setting up the table to add books, or this can be done manyally in mySQL
    void setupBookTable(){
        String TABLE_NAME="addBook";
        try{
            stmt=conn.createStatement();
            DatabaseMetaData dbm=conn.getMetaData();
            ResultSet tables=dbm.getTables(null,null,TABLE_NAME,null);
            if(tables.next()){
                System.out.println("Table "+TABLE_NAME+ " already exists.");
            } else{
                stmt.execute("CREATE TABLE"+TABLE_NAME+"("
                        + " id varchar(200) not null ,"
                        + " title varchar(200) not null ,"
                        + " author varchar(200) not null ,"
                        + " publisher varchar(200) not null ,"
                        + " quantity int not nulll ,"
                        + " isAvail boolean default true,"
                        + " primary key(id)"
                        +")");
            }
        } catch (SQLException ex){
            System.err.println(ex.getMessage()+ " ...setupDatabase");
        }
    }


    // Setting up the table for issued books
    void setupIssuedBooksTable(){
        String TABLE_NAME="issuedBooks";
        try{
            stmt=conn.createStatement();
            DatabaseMetaData dbm=conn.getMetaData();
            ResultSet tables=dbm.getTables(null,null,TABLE_NAME,null);
            if(tables.next()){
                System.out.println("Table "+TABLE_NAME+ " already exists.");
            } else{
                stmt.execute("CREATE TABLE "+TABLE_NAME+"("
                        + " bookID varchar(200) not null ,"
                        + " memberID varchar(200) not null ,"
                        + " issueTime timestamp default CURRENT_TIMESTAMP,"
                        + " renew_count integer default 0 ,"
                        + " primary key(bookID,memberID),"
                        + " foreign key(bookID) references addBook(id),"
                        + " foreign key(memberID) references addMember(memberID)"
                        +")");
            }
        } catch (SQLException ex){
            System.err.println(ex.getMessage()+ " ...setupDatabase");
        }
    }
    void setupMemberTable(){
        String TABLE_NAME="addBook";
        try{
            stmt=conn.createStatement();
            DatabaseMetaData dbm=conn.getMetaData();
            ResultSet tables=dbm.getTables(null,null,TABLE_NAME,null);
            if(tables.next()){
                System.out.println("Table "+TABLE_NAME+ " already exists.");
            } else{
                stmt.execute("CREATE TABLE"+TABLE_NAME+"("
                        + " memberID varchar(200) not null ,"
                        + " name varchar(200) not null ,"
                        + " email varchar(200) not null ,"
                        + " phone varchar(200) not null ,"
                        + " gender enum('male','female'),"
                        + " primary key(memberID)"
                        +")");
            }
        } catch (SQLException ex){
            System.err.println(ex.getMessage()+ " ...setupDatabase");
        }
    }

    // A method that returns a ResultSet, this is used to execute queries
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }

        return result;
    }

    // A method that returns boolean values, used to inform if the action was succesfully executed
    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler " + ex.getLocalizedMessage());
            return false;
        }
    }

}
