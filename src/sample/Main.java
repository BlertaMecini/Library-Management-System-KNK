package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        //primaryStage.setTitle("Library Managment System");
        primaryStage.setScene(new Scene(root, 520, 400));
        primaryStage.show();
    }

}