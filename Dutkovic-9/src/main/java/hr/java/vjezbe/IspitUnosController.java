package hr.java.vjezbe;

import hr.java.vjezbe.baza.BazaPodataka;
import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import hr.java.vjezbe.util.Datoteke;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import tornadofx.control.DateTimePicker;

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
    private ChoiceBox<Ocjena> ocjenaIspita;
    @FXML
    private DateTimePicker datumIVrijemeIspita;

    public void initialize(){
        for (int i = 1; i < 6; i++){
            ocjenaIspita.getItems().add(BazaPodataka.intToOcjena(i));
        }
        List<Predmet> predmeti = null;
        try {
            predmeti = BazaPodataka.dohvatiPredmete();
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        List<Student> studenti = null;
        try {
            studenti = BazaPodataka.dohvatiStudente();
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

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

        Predmet predmet = predmetIspitaListView.getSelectionModel().getSelectedItem();
        if (predmet == null) {
            prazniTextFieldovi.add("Ispitu trebate odabrati predmet!");
        }
        Student student = studentIspitaListView.getSelectionModel().getSelectedItem();
        if (student == null) {
            prazniTextFieldovi.add("Ispitu trebate odabrati studenta!");
        }
        Ocjena ocjena = null;
        if (ocjenaIspita.getSelectionModel().isEmpty()) {
            prazniTextFieldovi.add("Ispitu trebate unesti ocjenu!");
        } else {
            ocjena = ocjenaIspita.getSelectionModel().getSelectedItem();
        }
        LocalDateTime datumIspita = null;
        if(datumIVrijemeIspita != null) {
            try {
                if(!datumIVrijemeIspita.getDateTimeValue().isAfter(LocalDateTime.now().minusSeconds(120))) {
                    datumIspita = datumIVrijemeIspita.getDateTimeValue();
                }
                else  {
                    prazniTextFieldovi.add("Ispitu trebate unesti datum i vrijeme!");
                }
            } catch (DateTimeParseException e) {
                prazniTextFieldovi.add("Ispitu trebate unesti datum i vrijeme!");
            }
        }
        else {
            prazniTextFieldovi.add("Ispitu trebate unesti datum i vrijeme!");
        }
        Dvorana dvorana = new Dvorana("nazivDvorane", "zgradaDvorane");

        if(prazniTextFieldovi.isEmpty()){
            try {
                BazaPodataka.spremiNoviIspit(new Ispit(CreateId(BazaPodataka.dohvatiIspite()), predmet, student, ocjena, datumIspita, dvorana));
            } catch (BazaPodatakaException e) {
                throw new RuntimeException(e);
            }
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
