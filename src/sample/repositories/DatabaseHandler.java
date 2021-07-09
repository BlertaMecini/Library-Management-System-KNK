package sample.repositories;

import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import javax.swing.*;
import java.sql.*;

// This class handles the database connection

public final  class DatabaseHandler {

    // Variables used for connecting to the database and creating statements
    // Change these values to your mySQL values
    private static DatabaseHandler handler=null;
    private static final String databaseName="admin";
    private static final String DB_URL="jdbc:mysql://127.0.0.1/"+ databaseName;

    private static Connection conn=null;
    private static Statement stmt=null;


    // Constructor of the class has methods that are called when the class is instantiated
    public DatabaseHandler(){
        createConnection();
        setupBookTable();
        setupIssuedBooksTable();
    }

    // If there is no instance of the class we get the instance
    public static DatabaseHandler getInstance(){
        if(handler==null){
            handler=new DatabaseHandler();
        }
        return handler;
    }


    // Connecting to the database
    void createConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String password = "error404";
            String userName = "root";
            conn = DriverManager.getConnection(DB_URL, userName, password);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    // Setting up the table to add books, or this can be done manually in mySQL
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
                        + " quantity int not null ,"
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

    // A method that returns boolean values, used to inform if the action was successfully executed
    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler " + ex.getLocalizedMessage());
            return false;
        }
    }

    // Graphs
    public ObservableList<PieChart.Data> getBookGraphicStatistics(){
        ObservableList<PieChart.Data> data= FXCollections.observableArrayList();
        try {
            String query1="SELECT COUNT(*) FROM addBook";
            String query2="SELECT COUNT(*) FROM issuedBooks";

            ResultSet rs=execQuery(query1);
            if(rs.next()){
                int count=rs.getInt(1);
                data.add(new PieChart.Data("Total Books (" +  count + ")",count));
            }

            ResultSet rs2=execQuery(query2);
            if(rs2.next()){
                int count=rs2.getInt(1);
                data.add(new PieChart.Data("Issued Copies Of Books (" +  count + ")",count));
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return data;

    }


    public ObservableList<PieChart.Data> getMemberGraphicStatistics(){
        ObservableList<PieChart.Data> data= FXCollections.observableArrayList();
        try {
            String query1="SELECT COUNT(*) FROM addMember";
            String query2="SELECT COUNT(DISTINCT memberID) FROM issuedBooks";

            ResultSet rs=execQuery(query1);
            if(rs.next()){
                int count=rs.getInt(1);
                data.add(new PieChart.Data("Total Members (" +  count + ")",count));
            }

            ResultSet rs2=execQuery(query2);
            if(rs2.next()){
                int count=rs2.getInt(1);
                data.add(new PieChart.Data("Members With Books (" +  count + ")",count));
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return data;

    }
}
