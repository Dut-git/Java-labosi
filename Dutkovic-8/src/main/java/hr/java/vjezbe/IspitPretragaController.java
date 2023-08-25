package hr.java.vjezbe;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.SimpleIntegerProperty;
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

public class IspitPretragaController {

    List<Ispit> ispiti = new ArrayList<>();
    @FXML
    private TextField nazivIspitaTextField;
    @FXML
    private TextField imeStudentaIspitaTextField;
    @FXML
    private TextField prezimeStudentaIspitaTextField;
    @FXML
    private TextField ocjenaIspitaTextField;
    @FXML
    private TextField datumIspitaTextField;
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
        ispiti = Datoteke.dohvatiIspite();

        dohvatiIspite();

        nazivTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPredmet().getNaziv()));
        imeStudentaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getIme()));
        prezimeStudentaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getPrezime()));
        ocjenaTableColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOcjena()).asObject());
        datumTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatumIVrijeme().toString()));
        ispitTableView.setItems(FXCollections.observableList(ispiti));
    }

    public void dohvatiIspite(){
        String nazivIspita = nazivIspitaTextField.getText();
        String imeStudentaIspita = imeStudentaIspitaTextField.getText();
        String prezimeStudentaIspita = prezimeStudentaIspitaTextField.getText();
        String ocjenaIspita = ocjenaIspitaTextField.getText();
        String datumIspita = datumIspitaTextField.getText();

        List<Ispit> filtriraniIspiti = ispiti.stream()
                .filter(ispit -> ispit.getPredmet().getNaziv().toLowerCase().contains(nazivIspita.toLowerCase()))
                .filter(ispit -> ispit.getStudent().getIme().toLowerCase().contains(imeStudentaIspita.toLowerCase()))
                .filter(ispit -> ispit.getStudent().getPrezime().toLowerCase().contains(prezimeStudentaIspita.toLowerCase()))
                .filter(ispit -> Integer.toString(ispit.getOcjena()).contains(ocjenaIspita))
                .filter(ispit -> ispit.getDatumIVrijeme().toString().contains(datumIspita)).toList();

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
