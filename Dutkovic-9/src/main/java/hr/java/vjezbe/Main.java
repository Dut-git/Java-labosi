package hr.java.vjezbe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage newStage;

    @Override
    public void start(Stage stage) throws IOException {
        newStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setMainPage(GridPane root) {
        Scene scene = new Scene(root,600,600);
        newStage.setScene(scene);
        newStage.show();
    }

}