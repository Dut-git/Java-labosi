package hr.java.vjezbe.entitet;

public class Predmet {

    private String sifra;
    private String naziv;
    private int brojEctsBodova;
    private Profesor nositelj;
    private Student[] studenti;

    public Predmet(String sifra, String naziv, int brojEctsBodova, Profesor nositelj) {
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
}
