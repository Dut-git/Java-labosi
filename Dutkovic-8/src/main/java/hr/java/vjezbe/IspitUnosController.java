package hr.java.vjezbe;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.util.Datoteke;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class IspitUnosController {

    @FXML
    private ListView<Predmet> predmetIspitaListView;
    @FXML
    private ListView<Student> studentIspitaListView;
    @FXML
    private TextField ocjenaIspitaTextField;
    @FXML
    private DatePicker datumIspitaDatePicker;
    @FXML
    private TextField vrijemeIspitaTextField;
    @FXML
    private TextField zgradaDvoraneIspitaTextField;
    @FXML
    private TextField nazivDvoraneIspitaTextField;

    public void initialize(){
        List<Predmet> predmeti = Datoteke.dohvatiPredmete();
        List<Student> studenti = Datoteke.dohvatiStudente();

        for (Predmet predmet : predmeti) {
            predmetIspitaListView.getItems().add(predmet);
        }
        predmetIspitaListView.setCellFactory(new Callback<ListView<Predmet>, ListCell<Predmet>>() {
            @Override
            public ListCell<Predmet> call(ListView<Predmet> lv) {
                return new ListCell<Predmet>() {
                    @Override
                    public void updateItem(Predmet item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                        } else {
                            setText(item.getNaziv());
                        }
                    }
                };
            }
        });
        for (Student student : studenti) {
            studentIspitaListView.getItems().add(student);
        }
        studentIspitaListView.setCellFactory(new Callback<ListView<Student>, ListCell<Student>>() {
            @Override
            public ListCell<Student> call(ListView<Student> lv) {
                return new ListCell<Student>() {
                    @Override
                    public void updateItem(Student item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                        } else {
                            setText(item.getIme() + " " + item.getPrezime());
                        }
                    }
                };
            }
        });
    }

    public void dodajIspit() {
        List<String> prazniTextFieldovi = new ArrayList<>();

        Predmet predmetIspita = predmetIspitaListView.getSelectionModel().getSelectedItem();
        if (predmetIspita == null) {
            prazniTextFieldovi.add("Ispitu trebate odabrati predmet!");
        }
        Student studentIspita = studentIspitaListView.getSelectionModel().getSelectedItem();
        if (studentIspita == null) {
            prazniTextFieldovi.add("Ispitu trebate odabrati studenta!");
        }
        Ocjena ocjenaIspita = null;
        if (ocjenaIspitaTextField.getText().isBlank()) {
            prazniTextFieldovi.add("Ispitu trebate unesti ocjenu!");
        } else {
            try {
                ocjenaIspita = Datoteke.intToOcjena(Integer.parseInt(ocjenaIspitaTextField.getText()));
            } catch (NumberFormatException e){
                prazniTextFieldovi.add("Ispitu trebate unesti numericku ocjenu!");
            } catch (IllegalStateException e){
                prazniTextFieldovi.add("Ispitu trebate unesti ocjenu izmedu 1-5!");
            }
        }
        LocalDate datumIspita = datumIspitaDatePicker.getValue();
        if(datumIspita == null){
            prazniTextFieldovi.add("Ispitu trebate unesti datum!");
        }
        String vrijemeIspita = vrijemeIspitaTextField.getText();
        if(vrijemeIspita.isBlank()){
            prazniTextFieldovi.add("Ispitu trebate unesti vrijeme!");
        }
        LocalDateTime datumIVrijemeIspita = null;
        if(!checkTime(vrijemeIspita)){
            prazniTextFieldovi.add("Ispitu trebate unesti pravilno vrijeme!");
        }
        else if (datumIspita != null) {
            datumIVrijemeIspita = spojiDatumIVrijeme(datumIspita, vrijemeIspita);
        }
        String zgradaDvorane = zgradaDvoraneIspitaTextField.getText();
        if(zgradaDvorane.isBlank()){
            prazniTextFieldovi.add("Ispitu trebate unesti zgradu dvorane!");
        }
        String nazivDvorane = nazivDvoraneIspitaTextField.getText();
        if(nazivDvorane.isBlank()){
            prazniTextFieldovi.add("Ispitu trebate unesti naziv dvorane!");
        }
        Dvorana dvorana = new Dvorana(nazivDvorane, zgradaDvorane);

        if(prazniTextFieldovi.isEmpty()){
            Datoteke.dodajIspit(new Ispit(CreateId(Datoteke.dohvatiIspite()), predmetIspita, studentIspita, ocjenaIspita, datumIVrijemeIspita, dvorana));
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Podaci o novom ispitu su uspjesno dodani!");
            alert.setTitle("Uspjesno spremanje podataka");
            alert.setHeaderText("Spremanje podataka o novom ispitu");
            alert.show();
        } else {
            String poruke = String.join("\n", prazniTextFieldovi);

            Alert alert = new Alert(Alert.AlertType.ERROR, poruke);
            alert.setTitle("Greška pri kreiranju novog ispita.");
            alert.setHeaderText("Ispravite sljedece pogreske:");
            alert.show();
        }
    }

    public LocalDateTime spojiDatumIVrijeme(LocalDate datum, String vrijeme){
        return LocalDateTime.of(datum, LocalTime.parse(vrijeme, DateTimeFormatter.ofPattern("HH:mm")));
    }

    public boolean checkTime(String stringVrijeme){
        try {
            LocalTime.parse(stringVrijeme, DateTimeFormatter.ofPattern("HH:mm"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
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
