package hr.java.vjezbe.entitet;

/**
 * Enumeracija svih dozvoljenih vrijednosti ocjena.
 */
public enum Ocjena {
    NEDOVOLJAN(1, "nedovoljan"),
    DOVOLJAN(2, "dovoljan"),
    DOBAR(3, "dobar"),
    VRLO_DOBAR(3, "vrlo dobar"),
    IZVRSTAN(5, "izvrstan");

    private final int Numericki;
    private final String Pismeno;
    Ocjena(int Numericki, String Pismeno) {
        this.Numericki = Numericki;
        this.Pismeno = Pismeno;
    }

    public int getNumericki() {
        return Numericki;
    }

    public String getPismeno() {
        return Pismeno;
    }
}
