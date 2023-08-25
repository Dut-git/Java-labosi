package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;


/**
 * Sluzi za predstavljanje obrazovne ustanove u programu
 */
public abstract class ObrazovnaUstanova {

    private String nazivUstanove;
    private Predmet[] predmeti;
    private Profesor[] profesori;
    private Student[] studenti;
    private Ispit[] ispiti;

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
    public ObrazovnaUstanova(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenti, Ispit[] ispiti) {
        this.nazivUstanove = nazivUstanove;
        this.predmeti = predmeti;
        this.profesori = profesori;
        this.studenti = studenti;
        this.ispiti = ispiti;
    }

    public Student getStudent(){
        return studenti[0];
    }

    public String getNazivUstanove() {
        return nazivUstanove;
    }

    public void setNazivUstanove(String nazivUstanove) {
        this.nazivUstanove = nazivUstanove;
    }

    public Predmet[] getPredmeti() {
        return predmeti;
    }

    public void setPredmeti(Predmet[] predmeti) {
        this.predmeti = predmeti;
    }

    public Profesor[] getProfesori() {
        return profesori;
    }

    public void setProfesori(Profesor[] profesori) {
        this.profesori = profesori;
    }

    public Student[] getStudenti() {
        return studenti;
    }

    public void setStudenti(Student[] studenti) {
        this.studenti = studenti;
    }

    public Ispit[] getIspiti() {
        return ispiti;
    }

    public void setIspiti(Ispit[] ispiti) {
        this.ispiti = ispiti;
    }
}
