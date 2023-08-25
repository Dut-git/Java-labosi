package hr.java.vjezbe;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class MainController {
    @FXML
    private GridPane mainContainer;

    public void prikaziPretraguProfesora() {
        GridPane root;
        try {
            root = FXMLLoader.load(Main.class.getResource("profesor-pretraga.fxml"));
            Main.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prikaziPretraguPredmeta() {
        GridPane root;
        try {
            root = FXMLLoader.load(Main.class.getResource("predmet-pretraga.fxml"));
            Main.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prikaziPretraguStudenta() {
        GridPane root;
        try {
            root = FXMLLoader.load(Main.class.getResource("student-pretraga.fxml"));
            Main.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prikaziPretraguIspita() {
        GridPane root;
        try {
            root = FXMLLoader.load(Main.class.getResource("ispit-pretraga.fxml"));
            Main.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}