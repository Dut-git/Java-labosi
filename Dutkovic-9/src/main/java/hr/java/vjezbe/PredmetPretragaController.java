package hr.java.vjezbe;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PredmetPretragaController {

    List<Predmet> predmeti = new ArrayList<>();
    @FXML
    private TextField sifraPredmetaTextField;
    @FXML
    private TextField nazivPredmetaTextField;
    @FXML
    private TextField brojECTSBodovaPredmetaTextField;
    @FXML
    private ChoiceBox<Profesor> nositeljPredmeta;
    @FXML
    private TableView<Predmet> predmetTableView;
    @FXML
    private TableColumn<Predmet, String> sifraTableColumn;
    @FXML
    private TableColumn<Predmet, String> nazivTableColumn;
    @FXML
    private TableColumn<Predmet, Integer> brojECTSBodovaTableColumn;
    @FXML
    private TableColumn<Predmet, String> nositeljTableColumn;
    @FXML
    public void initialize() {
        try {
            predmeti = BazaPodataka.dohvatiPredmete();
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        try {
            for (Profesor profesor : BazaPodataka.dohvatiProfesore()) {
                nositeljPredmeta.getItems().add(profesor);
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

        dohvatiPredmete();

        sifraTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSifra()));
        nazivTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNaziv()));
        brojECTSBodovaTableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBrojEctsBodova()).asObject());
        nositeljTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNositelj().getIme() + " " + cellData.getValue().getNositelj().getPrezime()));
        predmetTableView.setItems(FXCollections.observableList(predmeti));
    }

    public void dohvatiPredmete(){
        String sifraPredmeta = sifraPredmetaTextField.getText();
        String nazivPredmeta = nazivPredmetaTextField.getText();
        String brojECTSBodovaPredmeta = brojECTSBodovaPredmetaTextField.getText();
        Integer ects = null;
        try {
            ects = Integer.parseInt(brojECTSBodovaPredmeta);
        } catch (NumberFormatException e) {

        }
        Profesor nositelj = null;
        if(nositeljPredmeta != null){
            nositelj = nositeljPredmeta.getSelectionModel().getSelectedItem();
        }

        List<Predmet> filtriraniPredmeti = null;
        try {
            filtriraniPredmeti = BazaPodataka.dohvatiPredmetePremaKriterijima(new Predmet(null, sifraPredmeta, nazivPredmeta, ects, nositelj));
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

        predmetTableView.setItems(FXCollections.observableList(filtriraniPredmeti));
    }

    private String stringProfesora(Predmet predmet){
        String stringProfesora;
        stringProfesora = predmet.getNositelj().getIme() + " " + predmet.getNositelj().getPrezime();
        return stringProfesora;
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
