package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.repositories.DatabaseHandler;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Library Managment System");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("https://static.thenounproject.com/png/3314579-200.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        new Thread(() -> DatabaseHandler.getInstance()).start();
    }
}