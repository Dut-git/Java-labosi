package hr.java.vjezbe;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.Entitet;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentUnosController {

    @FXML
    private TextField imeStudentaTextField;
    @FXML
    private TextField prezimeStudentaTextField;
    @FXML
    private TextField jmbagStudentaTextField;
    @FXML
    private DatePicker datumRodenjaStudentaTextField;

    public void dodajStudenta(){
        try {
            List<Student> studenti = BazaPodataka.dohvatiStudente();
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        List<String> prazniTextFieldovi = new ArrayList<>();

        String imeStudenta = imeStudentaTextField.getText();
        if(imeStudenta.isBlank()){
            prazniTextFieldovi.add("Studentu trebate unesti ime!");
        }
        String prezimeStudenta = prezimeStudentaTextField.getText();
        if(prezimeStudenta.isBlank()){
            prazniTextFieldovi.add("Studentu trebate unesti prezime!");
        }
        String jmbagStudenta = jmbagStudentaTextField.getText();
        if(jmbagStudenta.isBlank()){
            prazniTextFieldovi.add("Studentu trebate unesti jmbag!");
        }
        LocalDate datumRodenjaStudenta = datumRodenjaStudentaTextField.getValue();
        if(datumRodenjaStudenta == null){
            prazniTextFieldovi.add("Studentu trebate unesti datum rodenja!");
        }

        if(prazniTextFieldovi.isEmpty()){
            try {
                BazaPodataka.spremiNovogStudenta(new Student(CreateId(BazaPodataka.dohvatiStudente()), imeStudenta, prezimeStudenta, jmbagStudenta, datumRodenjaStudenta ));
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Podaci o novom studentu su uspjesno dodani!");
            alert.setTitle("Uspjesno spremanje podataka");
            alert.setHeaderText("Spremanje podataka o novom studentu");
            alert.show();
        } else {
            String poruke = String.join("\n", prazniTextFieldovi);

            Alert alert = new Alert(Alert.AlertType.ERROR, poruke);
            alert.setTitle("Greška pri kreiranju novog studenta.");
            alert.setHeaderText("Ispravite sljedece pogreske:");
            alert.show();
        }
    }

    public <T extends Entitet> Long CreateId(List<T> objekti){
        return objekti.stream().mapToLong(Entitet::getId).max().orElse(0) + 1;
    }

    public void prikaziPretraguStudenta() {
        prikazi("student-pretraga.fxml");
    }

    public void prikaziPretraguPredmeta() {
        prikazi("predmet-pretraga.fxml");
    }

    public void prikaziPretraguProfesora() {
        prikazi("profesor-pretraga.fxml");
    }

    public void prikaziPretraguIspita() {
        prikazi("ispit-pretraga.fxml");
    }

    public void prikaziUnosStudenta() {
        prikazi("student-unos.fxml");
    }

    public void prikaziUnosPredmeta() {
        prikazi("predmet-unos.fxml");
    }

    public void prikaziUnosProfesora() {
        prikazi("profesor-unos.fxml");
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
