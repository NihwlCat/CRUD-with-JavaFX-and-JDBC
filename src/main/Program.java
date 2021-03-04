package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import java.io.IOException;

public class Program extends Application {

    private static Scene principal;

    public static Scene getPrincipal(){
        return principal;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
            ScrollPane root = loader.load();

            root.setFitToHeight(true);
            root.setFitToWidth(true);

            principal = new Scene(root);
            primaryStage.setScene(principal);
            primaryStage.setTitle("CRUD - JavaFX with JDBC");
            primaryStage.show();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
