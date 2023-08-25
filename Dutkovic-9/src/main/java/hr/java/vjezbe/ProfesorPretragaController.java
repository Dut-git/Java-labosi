package hr.java.vjezbe;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
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
        try {
            profesori = BazaPodataka.dohvatiProfesore();
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

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

        List<Profesor> filtriraniProfesori = null;
        try {
            filtriraniProfesori = BazaPodataka.dohvatiProfesorePremaKriterijima(new Profesor.ProfesorBuilder(null, imeProfesora, prezimeProfesora).saSifrom(sifraProfesora).saTitulom(titulaProfesora).build());
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

        profesorTableView.setItems(FXCollections.observableList(filtriraniProfesori));
    }

    public void prikaziPretraguProfesora() {
        prikazi("profesor-pretraga.fxml");
    }

    public void prikaziPretraguPredmeta() {
        prikazi("predmet-pretraga.fxml");
    }

    public void prikaziPretraguStudenta() {
        prikazi("student-pretraga.fxml");
    }

    public void prikaziPretraguIspita() {
        prikazi("ispit-pretraga.fxml");
    }

    public void prikaziUnosProfesora() {
        prikazi("profesor-unos.fxml");
    }

    public void prikaziUnosPredmeta() {
        prikazi("predmet-unos.fxml");
    }

    public void prikaziUnosStudenta() {
        prikazi("student-unos.fxml");
    }

    public void prikaziUnosIspita() {
        prikazi("ispit-unos.fxml");
    }

    public void prikazi(String url){
        GridPane root;
        try {
            root = FXMLLoader.load(Main.class.getResource(url));
            Main.setMainPage(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
