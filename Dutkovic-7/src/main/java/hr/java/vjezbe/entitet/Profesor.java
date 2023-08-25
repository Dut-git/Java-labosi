package hr.java.vjezbe.entitet;

/**
 * Predstavlja profesora u programu
 */

public class Profesor extends Osoba {


    private String ime;
    private String prezime;
    private String sifra;
    private String titula;

    /**
     * sluzi za sigurno kreiranje profesora
     */
    public static class ProfesorBuilder{
        private Long id;
        private String ime;
        private String prezime;
        private String sifra;
        private String titula;

        public ProfesorBuilder(Long id, String ime, String prezime) {
            this.id = id;
            this.ime = ime;
            this.prezime = prezime;
        }

        public ProfesorBuilder saSifrom(String sifra){
            this.sifra = sifra;
            return this;
        }

        public ProfesorBuilder saTitulom(String titula){
            this.titula = titula;
            return this;
        }

        public Profesor build(){
            return new Profesor(this);
        }

    }

    /**
     * sluzi za kreiranje novog profesora preko builder klase
     * @param builder objekt builder klase pomocu kojeg kreiramo novog profesora
     */
    private Profesor(ProfesorBuilder builder) {
        super(builder.id, builder.ime, builder.prezime);
        this.ime = builder.ime;
        this.prezime = builder.prezime;
        this.sifra = builder.sifra;
        this.titula = builder.titula;
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

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getTitula() {
        return titula;
    }

    public void setTitula(String titula) {
        this.titula = titula;
    }
}
