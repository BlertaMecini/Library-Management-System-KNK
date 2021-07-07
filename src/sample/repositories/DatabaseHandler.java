package sample.repositories;

import javax.swing.*;
import java.sql.*;


public final  class DatabaseHandler {

    private static DatabaseHandler handler=null;
    private static String databaseName="admin";
    private static String userName="root";
    private static String password="error404";
    private static final String DB_URL="jdbc:mysql://127.0.0.1/"+ databaseName;

    private static Connection conn=null;
    private static Statement stmt=null;


    public DatabaseHandler(){
        createConnection();
    }

    public static DatabaseHandler getInstance(){
        if(handler==null){
            handler=new DatabaseHandler();
        }
        return handler;
    }

    void createConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL,userName, password);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    /*void setupBookTable(){
        String TABLE_NAME="addBook";
        try{
            stmt=conn.createStatement();
            DatabaseMetaData dbm=conn.getMetaData();
            ResultSet tables=dbm.getTables(null,null,TABLE_NAME,null);
            if(tables.next()){
                System.out.println("Table "+TABLE_NAME+ " already exists.");
            } else{
                stmt.execute("CREATE TABLE"+TABLE_NAME+"("
                        + " id varchar(200) not null \n,"
                        + " title varchar(200) not null ,\n"
                        + " author varchar(200) not null ,\n"
                        + " publisher varchar(200) not nulll ,\n"
                        + " isAvail boolean default true,\n"
                        + " primary key(id)"
                        +")");
            }
        } catch (SQLException ex){
            System.err.println(ex.getMessage()+ " ...setupDatabase");
        }
    }*/


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

    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
    }
}
