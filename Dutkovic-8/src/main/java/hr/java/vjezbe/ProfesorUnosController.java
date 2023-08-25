package hr.java.vjezbe;

import hr.java.vjezbe.entitet.Entitet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.util.Datoteke;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfesorUnosController {

    @FXML
    private TextField sifraProfesoraTextField;
    @FXML
    private TextField imeProfesoraTextField;
    @FXML
    private TextField prezimeProfesoraTextField;
    @FXML
    private TextField titulaProfesoraTextField;

    public void dodajProfesora(){
        List<Profesor> profesori = Datoteke.dohvatiProfesore();
        List<String> prazniTextFieldovi = new ArrayList<>();

        String sifraProfesora = sifraProfesoraTextField.getText();
        if(sifraProfesora.isBlank()){
            prazniTextFieldovi.add("Profesoru trebate unesti sifru!");
        }
        String imeProfesora = imeProfesoraTextField.getText();
        if(imeProfesora.isBlank()){
            prazniTextFieldovi.add("Profesoru trebate unesti ime!");
        }
        String prezimeProfesora = prezimeProfesoraTextField.getText();
        if(prezimeProfesora.isBlank()){
            prazniTextFieldovi.add("Profesoru trebate unesti prezime!");
        }
        String titulaProfesora = titulaProfesoraTextField.getText();
        if(titulaProfesora.isBlank()){
            prazniTextFieldovi.add("Profesoru trebate unesti titulu!");
        }

        if(profesori.stream().anyMatch(profesor -> profesor.getSifra().equals(sifraProfesora))){
            prazniTextFieldovi.add("Jedan od profesora vec ima upisanu sifru, upisite drugu sifru!");
        }

        if(prazniTextFieldovi.isEmpty()){
            Datoteke.dodajProfesora(new Profesor.ProfesorBuilder(CreateId(Datoteke.dohvatiProfesore()), imeProfesora, prezimeProfesora).saSifrom(sifraProfesora).saTitulom(titulaProfesora).build());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Podaci o novom profesoru su uspjesno dodani!");
            alert.setTitle("Uspjesno spremanje podataka");
            alert.setHeaderText("Spremanje podataka o novom profesoru");
            alert.show();
        } else {
            String poruke = String.join("\n", prazniTextFieldovi);

            Alert alert = new Alert(Alert.AlertType.ERROR, poruke);
            alert.setTitle("Greška pri kreiranju novog profesora.");
            alert.setHeaderText("Ispravite sljedece pogreske:");
            alert.show();
        }
    }

    public <T extends Entitet> Long CreateId(List<T> objekti){
        return objekti.stream().mapToLong(Entitet::getId).max().orElse(0) + 1;
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
