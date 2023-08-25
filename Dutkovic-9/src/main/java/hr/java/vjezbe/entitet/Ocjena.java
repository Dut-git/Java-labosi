package hr.java.vjezbe.entitet;

/**
 * Enumeracija svih dozvoljenih vrijednosti ocjena.
 */
public enum Ocjena {
    NEDOVOLJAN(1, "nedovoljan"),
    DOVOLJAN(2, "dovoljan"),
    DOBAR(3, "dobar"),
    VRLO_DOBAR(4, "vrlo dobar"),
    IZVRSTAN(5, "izvrstan");

    private final Integer Numericki;
    private final String Pismeno;
    Ocjena(int Numericki, String Pismeno) {
        this.Numericki = Numericki;
        this.Pismeno = Pismeno;
    }

    public Integer getNumericki() {
        return Numericki;
    }

    public String getPismeno() {
        return Pismeno;
    }


    @Override
    public String toString() {
        return getNumericki().toString();
    }
}
