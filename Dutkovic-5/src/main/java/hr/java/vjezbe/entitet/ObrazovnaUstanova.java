package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

import java.util.List;


/**
 * Sluzi za predstavljanje obrazovne ustanove u programu
 */
public abstract class ObrazovnaUstanova {

    private String nazivUstanove;
    private List<Predmet> predmeti;
    private List<Profesor> profesori;
    private List<Student> studenti;
    private List<Ispit> ispiti;

    /**
     * odreduje najuspjesnijeg studenta na godini
     * @param godina godina prema kojoj filtriramo studente
     * @return vraca najboljeg studenta
     * @throws NemoguceOdreditiProsjekStudentaException baca se kada student ima negativnu ocjenu pri odredivanju prosjeka
     */
    public abstract Student odrediNajuspjesnijegStudentaNaGodini(int godina) throws NemoguceOdreditiProsjekStudentaException;

    /**
     * kreira novi fakultet racunarstva
     * @param nazivUstanove naziv unesene ustanove
     * @param predmeti niz predmeta koje fakultet sadrzi
     * @param profesori profesori koji predaju na fakultetu
     * @param studenti studenti koji studiraju na fakultetu
     * @param ispiti popis pisanih ispita za studente
     */
    public ObrazovnaUstanova(String nazivUstanove, List<Predmet> predmeti, List<Profesor> profesori, List<Student> studenti, List<Ispit> ispiti) {
        this.nazivUstanove = nazivUstanove;
        this.predmeti = predmeti;
        this.profesori = profesori;
        this.studenti = studenti;
        this.ispiti = ispiti;
    }

    public Student getStudent(){
        return studenti.get(0);
    }

    public String getNazivUstanove() {
        return nazivUstanove;
    }

    public void setNazivUstanove(String nazivUstanove) {
        this.nazivUstanove = nazivUstanove;
    }

    public List<Predmet> getPredmeti() {
        return predmeti;
    }

    public void setPredmeti(List<Predmet> predmeti) {
        this.predmeti = predmeti;
    }

    public List<Profesor> getProfesori() {
        return profesori;
    }

    public void setProfesori(List<Profesor> profesori) {
        this.profesori = profesori;
    }

    public List<Student> getStudenti() {
        return studenti;
    }

    public void setStudenti(List<Student> studenti) {
        this.studenti = studenti;
    }

    public List<Ispit> getIspiti() {
        return ispiti;
    }

    public void setIspiti(List<Ispit> ispiti) {
        this.ispiti = ispiti;
    }
}
