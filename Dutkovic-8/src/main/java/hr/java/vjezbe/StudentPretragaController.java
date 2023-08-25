package hr.java.vjezbe;

import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentPretragaController {


    List<Student> studenti = new ArrayList<>();
    @FXML
    private TextField imeStudentaTextField;
    @FXML
    private TextField prezimeStudentaTextField;
    @FXML
    private TextField jmbagStudentaTextField;
    @FXML
    private DatePicker datumRodenjaStudentaTextField;
    @FXML
    private TableView<Student> studentTableView;
    @FXML
    private TableColumn<Student, String> imeTableColumn;
    @FXML
    private TableColumn<Student, String> prezimeTableColumn;
    @FXML
    private TableColumn<Student, String> jmbagTableColumn;
    @FXML
    private TableColumn<Student, String> datumRodenjaTableColumn;
    @FXML
    public void initialize() {
        studenti = Datoteke.dohvatiStudente();

        dohvatiStudente();

        imeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIme()));
        prezimeTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrezime()));
        jmbagTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJmbag()));
        datumRodenjaTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatumRodenja().toString()));
        studentTableView.setItems(FXCollections.observableList(studenti));
    }

    public void dohvatiStudente(){
        String imeStudenta = imeStudentaTextField.getText();
        String prezimeStudenta = prezimeStudentaTextField.getText();
        String jmbagStudenta = jmbagStudentaTextField.getText();
        LocalDate datumRodenjaStudenta = datumRodenjaStudentaTextField.getValue();

        List<Student> filtriraniStudenti = studenti.stream()
                .filter(student -> student.getIme().toLowerCase().contains(imeStudenta.toLowerCase()))
                .filter(student -> student.getPrezime().toLowerCase().contains(prezimeStudenta.toLowerCase()))
                .filter(student -> student.getJmbag().toLowerCase().contains(jmbagStudenta.toLowerCase()))
                .filter(student -> datumRodenjaStudenta == null || student.getDatumRodenja().equals(datumRodenjaStudenta)).toList();

        studentTableView.setItems(FXCollections.observableList(filtriraniStudenti));
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
