package hr.java.vjezbe.entitet;

import java.util.HashSet;
import java.util.Set;

/**
 * Predstavlja predmet u programu
 */

public class Predmet extends Entitet {

    private String sifra;
    private String naziv;
    private int brojEctsBodova;
    private Profesor nositelj;
    private Set<Student> studenti = new HashSet<>();

    /**
     * kreira novi predmet
     * @param sifra sifra predmeta
     * @param naziv naziv predmeta
     * @param brojEctsBodova broj ECTS bodova od predmeta
     * @param nositelj profesor koji drzi predmet
     */
    public Predmet(Long id, String sifra, String naziv, int brojEctsBodova, Profesor nositelj) {
        super(id);
        this.sifra = sifra;
        this.naziv = naziv;
        this.brojEctsBodova = brojEctsBodova;
        this.nositelj = nositelj;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrojEctsBodova() {
        return brojEctsBodova;
    }

    public void setBrojEctsBodova(int brojEctsBodova) {
        this.brojEctsBodova = brojEctsBodova;
    }

    public Profesor getNositelj() {
        return nositelj;
    }

    public void setNositelj(Profesor nositelj) {
        this.nositelj = nositelj;
    }

    public Set<Student> getStudenti() {
        return studenti;
    }

    public void setStudenti(Set<Student> studenti) {
        this.studenti = studenti;
    }
}
