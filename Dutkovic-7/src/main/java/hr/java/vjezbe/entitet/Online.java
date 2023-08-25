package hr.java.vjezbe.entitet;

/**
 * Prosiruje interface Ispit i nadodaje metodu softwareZaOnlineIspit
 */

public sealed interface Online permits Ispit{

    /**
     * vraca naziv softvera za ispit
     * @param nazivSoftware naziv softvera koji se koristi
     * @return vraca naziv softvera
     */
    String softwareZaOnlineIspit(String nazivSoftware);

}
