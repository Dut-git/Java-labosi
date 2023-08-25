package hr.java.vjezbe;

import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfesorPretragaController {

    List<Profesor> profesori = new ArrayList<>();
    @FXML
    private TextField sifraProfesoraTextField;
    @FXML
    private TextField imeProfesoraTextField;
    @FXML
    private TextField prezimeProfesoraTextField;
    @FXML
    private TextField titulaProfesoraTextField;
    @FXML
    private TableView<Profesor> profesorTableView;
    @FXML
    private TableColumn<Profesor, String> sifraTableColumn;
    @FXML
    private TableColumn<Profesor, String> imeTableColumn;
    @FXML
    private TableColumn<Profesor, String> prezimeTableColumn;
    @FXML
    private TableColumn<Profesor, String> titulaTableColumn;
    @FXML
    public void initialize() {
        profesori = Datoteke.dohvatiProfesore();

        dohvatiProfesore();

        sifraTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSifra()));
        imeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIme()));
        prezimeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrezime()));
        titulaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitula()));
        profesorTableView.setItems(FXCollections.observableList(profesori));
    }

    public void dohvatiProfesore(){
        String sifraProfesora = sifraProfesoraTextField.getText();
        String imeProfesora = imeProfesoraTextField.getText();
        String prezimeProfesora = prezimeProfesoraTextField.getText();
        String titulaProfesora = titulaProfesoraTextField.getText();

        List<Profesor> filtriraniProfesori = profesori.stream()
                .filter(profesor -> profesor.getIme().toLowerCase().contains(imeProfesora.toLowerCase()))
                .filter(profesor -> profesor.getPrezime().toLowerCase().contains(prezimeProfesora.toLowerCase()))
                .filter(profesor -> profesor.getSifra().toLowerCase().contains(sifraProfesora.toLowerCase()))
                .filter(profesor -> profesor.getTitula().toLowerCase().contains(titulaProfesora.toLowerCase())).toList();

        profesorTableView.setItems(FXCollections.observableList(filtriraniProfesori));
    }

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
