package hr.java.vjezbe.entitet;

import java.time.LocalDateTime;

/**
 * Sluzi za predstavljanje ispita u aplikaciji
 */
public non-sealed class Ispit extends Entitet implements Online{

    private Predmet predmet;
    private Student student;
    private Ocjena ocjena;
    private LocalDateTime datumIVrijeme;
    Dvorana dvorana;

    public String softwareZaOnlineIspit(String nazivSoftware){
        return "Software koji ce se koristiti zove se " + nazivSoftware;
    }

    public Ispit(Long id, Predmet predmet, Student student, Ocjena ocjena, LocalDateTime datumIVrijeme, Dvorana dvorana) {
        super(id);
        this.predmet = predmet;
        this.student = student;
        this.ocjena = ocjena;
        this.datumIVrijeme = datumIVrijeme;
        this.dvorana = dvorana;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getOcjena() {
        return ocjena.getNumericki();
    }

    public void setOcjena(Ocjena ocjena) {
        this.ocjena = ocjena;
    }

    public LocalDateTime getDatumIVrijeme() {
        return datumIVrijeme;
    }

    public void setDatumIVrijeme(LocalDateTime datumIVrijeme) {
        this.datumIVrijeme = datumIVrijeme;
    }

    public Dvorana getDvorana() {
        return dvorana;
    }
}
