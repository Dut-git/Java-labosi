package hr.java.vjezbe;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.iznimke.NeispravanPodatakException;
import hr.java.vjezbe.util.Datoteke;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PredmetUnosController {

    @FXML
    private ListView<Student> studentiPredmetaListView;
    @FXML
    private TextField sifraPredmetaTextField;
    @FXML
    private TextField nazivPredmetaTextField;
    @FXML
    private TextField brojEctsaPredmetaTextField;
    @FXML
    private ListView<Profesor> nositeljPredmetaListView;

    public void initialize(){
        List<Student> studenti = Datoteke.dohvatiStudente();
        List<Profesor> profesori = Datoteke.dohvatiProfesore();

        for (Student student : studenti) {
            studentiPredmetaListView.getItems().add(student);
        }
        studentiPredmetaListView.setCellFactory(new Callback<ListView<Student>, ListCell<Student>>() {
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
        studentiPredmetaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        for (Profesor profesor : profesori) {
            nositeljPredmetaListView.getItems().add(profesor);
        }
        nositeljPredmetaListView.setCellFactory(new Callback<ListView<Profesor>, ListCell<Profesor>>() {
            @Override
            public ListCell<Profesor> call(ListView<Profesor> lv) {
                return new ListCell<Profesor>() {
                    @Override
                    public void updateItem(Profesor item, boolean empty) {
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

    public void dodajPredmet() {
        List<String> prazniTextFieldovi = new ArrayList<>();

        List<Student> studentiPredmeta = studentiPredmetaListView.getSelectionModel().getSelectedItems();
        if (studentiPredmeta.isEmpty()) {
            prazniTextFieldovi.add("Predmetu trebate odabrati studenta/e!");
        }
        Profesor nositeljPredmeta = nositeljPredmetaListView.getSelectionModel().getSelectedItem();
        if (nositeljPredmeta == null) {
            prazniTextFieldovi.add("Predmetu trebate odabrati nositelja!");
        }
        String sifraPredmeta = sifraPredmetaTextField.getText();
        if (sifraPredmeta.isBlank()) {
            prazniTextFieldovi.add("Predmetu trebate unesti sifru!");
        }
        List<Predmet> predmeti = Datoteke.dohvatiPredmete();
        if(predmeti.stream().anyMatch(predmet -> predmet.getSifra().equals(sifraPredmeta))){
            prazniTextFieldovi.add("Jedan od predmeta vec ima upisanu sifru, upisite drugu sifru!");
        }
        String nazivPredmeta = nazivPredmetaTextField.getText();
        if(nazivPredmeta.isBlank()){
            prazniTextFieldovi.add("Predmetu trebate unesti naziv!");
        }
        Integer brojEctsaPredmeta = null;
        if (brojEctsaPredmetaTextField.getText().isBlank()) {
            prazniTextFieldovi.add("Predmetu trebate unesti broj ECTS bodova!");
        } else {
            try {
                brojEctsaPredmeta = Integer.parseInt(brojEctsaPredmetaTextField.getText());
                if(brojEctsaPredmeta < 1){
                    throw new NeispravanPodatakException();
                }
            } catch (NumberFormatException e){
                prazniTextFieldovi.add("Broj ECTS bodova treba biti numericka vrijednost!");
            } catch (NeispravanPodatakException e){
                prazniTextFieldovi.add("Broj ECTS bodova treba biti vrijednost veca od 0!");
            }
        }

        if(prazniTextFieldovi.isEmpty()){
            Predmet predmet = new Predmet(CreateId(Datoteke.dohvatiPredmete()), sifraPredmeta, nazivPredmeta, brojEctsaPredmeta, nositeljPredmeta);
            Set<Student> students = new HashSet<>(studentiPredmeta);
            predmet.setStudenti(students);
            Datoteke.dodajPredmet(predmet);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Podaci o novom predmetu su uspjesno dodani!");
            alert.setTitle("Uspjesno spremanje podataka");
            alert.setHeaderText("Spremanje podataka o novom predmetu");
            alert.show();
        } else {
            String poruke = String.join("\n", prazniTextFieldovi);

            Alert alert = new Alert(Alert.AlertType.ERROR, poruke);
            alert.setTitle("Greška pri kreiranju novog predmeta.");
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
