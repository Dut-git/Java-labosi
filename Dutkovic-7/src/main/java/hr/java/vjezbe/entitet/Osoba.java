package hr.java.vjezbe.entitet;

/**
 * Predstavlja osobu u programu
 */

public class Osoba extends Entitet {

    private String ime;
    private String prezime;

    /**
     * kreira novu osobu
     * @param ime ime osobe
     * @param prezime prezime osobe
     */
    public Osoba(Long id, String ime, String prezime) {
        super(id);
        this.ime = ime;
        this.prezime = prezime;
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
}
