package hr.java.vjezbe.entitet;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Predstavlja studenta u programu
 */
public class Student extends Osoba {

    private String ime;
    private String prezime;
    private String jmbag;
    private LocalDate datumRodenja;

    boolean negativanStudent = false;

    /**
     * kreira novog studenta
     * @param ime ime studenta
     * @param prezime prezime studenta
     * @param jmbag JMBAG studenta
     * @param datumRodenja datum rodenja studenta
     */
    public Student(String ime, String prezime, String jmbag, LocalDate datumRodenja) {
        super(ime, prezime);
        this.ime = ime;
        this.prezime = prezime;
        this.jmbag = jmbag;
        this.datumRodenja = datumRodenja;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return negativanStudent == student.negativanStudent && Objects.equals(ime, student.ime) && Objects.equals(prezime, student.prezime) && Objects.equals(jmbag, student.jmbag) && Objects.equals(datumRodenja, student.datumRodenja);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ime, prezime, jmbag, datumRodenja, negativanStudent);
    }

    public boolean isNegativanStudent() {
        return negativanStudent;
    }

    public void setNegativanStudent(boolean negativanStudent) {
        this.negativanStudent = negativanStudent;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getJmbag() {
        return jmbag;
    }

    public void setJmbag(String jmbag) {
        this.jmbag = jmbag;
    }

    public LocalDate getDatumRodenja() {
        return datumRodenja;
    }

    public void setDatumRodenja(LocalDate datumRodenja) {
        this.datumRodenja = datumRodenja;
    }
}
