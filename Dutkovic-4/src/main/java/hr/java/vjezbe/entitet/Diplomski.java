package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

/**
 * Prosiruje interface Visokoskolska i nadodaje metodu odrediStudentaZaRektorovuNagradu
 */
public interface Diplomski extends Visokoskolska{

    /**
     * prolazi kroz niz studenata i odreduje studenta za rektorovu nagradu
     */
    Student odrediStudentaZaRektorovuNagradu() throws NemoguceOdreditiProsjekStudentaException;

}
