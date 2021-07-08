package sample.repositories;
import java.sql.Connection;
import java.sql.DriverManager;

// A class to connect to database , used for  login
public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "admin";
        String databaseUser = "root";
        String databasePassword = "error404";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}
