package hr.java.vjezbe;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import tornadofx.*;
import tornadofx.control.DateTimePicker;

public class IspitPretragaController {

    List<Ispit> ispiti = new ArrayList<>();
    @FXML
    private ChoiceBox<Predmet> predmetIspita;
    @FXML
    private ChoiceBox<Student> studentIspita;
    @FXML
    private ChoiceBox<Ocjena> ocjenaIspita;
    @FXML
    private DateTimePicker datumIVrijemeIspita;
    @FXML
    private TableView<Ispit> ispitTableView;
    @FXML
    private TableColumn<Ispit, String> nazivTableColumn;
    @FXML
    private TableColumn<Ispit, String> imeStudentaTableColumn;
    @FXML
    private TableColumn<Ispit, String> prezimeStudentaTableColumn;
    @FXML
    private TableColumn<Ispit, Integer> ocjenaTableColumn;
    @FXML
    private TableColumn<Ispit, String> datumTableColumn;
    @FXML
    public void initialize() {
        datumIVrijemeIspita = null;
        try {
            ispiti = BazaPodataka.dohvatiIspite();
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        try {
            for (Predmet predmet : BazaPodataka.dohvatiPredmete()) {
                predmetIspita.getItems().add(predmet);
            }
            for (Student student : BazaPodataka.dohvatiStudente()){
                studentIspita.getItems().add(student);
            }
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i < 6; i++){
            ocjenaIspita.getItems().add(BazaPodataka.intToOcjena(i));
        }

        dohvatiIspite();

        nazivTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPredmet().getNaziv()));
        imeStudentaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getIme()));
        prezimeStudentaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getPrezime()));
        ocjenaTableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOcjena().getNumericki()).asObject());
        datumTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatumIVrijeme().toString()));
        ispitTableView.setItems(FXCollections.observableList(ispiti));
    }

    public void dohvatiIspite(){
        Predmet predmet = predmetIspita.getSelectionModel().getSelectedItem();
        Student student = studentIspita.getSelectionModel().getSelectedItem();
        Ocjena ocjena = ocjenaIspita.getSelectionModel().getSelectedItem();
        LocalDateTime datumIspita = null;
        if(datumIVrijemeIspita != null) {
            datumIspita = datumIVrijemeIspita.getDateTimeValue();
        }

        List<Ispit> filtriraniIspiti = null;
        try {
            filtriraniIspiti = BazaPodataka.dohvatiIspitePremaKriterijima(new Ispit(null, predmet, student, ocjena, datumIspita, new Dvorana("Naziv", "Zgrada")));
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

        ispitTableView.setItems(FXCollections.observableList(filtriraniIspiti));
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
