package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Nudi metode pomocu koji radimo sa podacima u ustanovi
 */
public interface Visokoskolska{

    /**
     * izracunava konacnu ocjenu studija
     * @param ispiti niz ispita od studenta
     * @param zavrsniPismeni ocjena pismenog dijela
     * @param zavrsniObrana ocjena obrane diplomskog
     * @return vraca izracun ocjene
     * @throws NemoguceOdreditiProsjekStudentaException baca se ako student ima negativnu ocjenu iz ispita
     */
    BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, int zavrsniPismeni, int zavrsniObrana) throws NemoguceOdreditiProsjekStudentaException;

    /**
     * odreduje prosjek ocjena na ispitima
     * @param ispiti niz ispita
     * @return vraca prosjek ocjena
     * @throws NemoguceOdreditiProsjekStudentaException baca se ako student ima negativnu ocjenu na ispitu
     */
    default BigDecimal odrediProsjekOcjenaNaIspitima(Ispit[] ispiti) throws NemoguceOdreditiProsjekStudentaException{
        float brojac = 0;
        float prosjek = 0;
        for (int i = 0; i < ispiti.length; i++) {
            if (ispiti[i].getOcjena() > 1){
                brojac++;
                prosjek += ispiti[i].getOcjena();
            }
            if(ispiti[i].getOcjena() == 1){
                String message = "Student " + ispiti[i].getStudent().getIme() + " " + ispiti[i].getStudent().getPrezime() + " ima negativnu ocjenu na ispitu za predmet " + ispiti[i].getPredmet().getNaziv();
                throw new NemoguceOdreditiProsjekStudentaException(message);
            }
        }
        return new BigDecimal(prosjek/brojac);
    }

    /**
     * flitrira polozene ispite
     * @param ispiti niz ispita
     * @return vraca polozene ispite
     */


    private Ispit[] filtrirajPolozeneIspite(Ispit[] ispiti){
        ArrayList<Ispit> ispiti2 = null;
        for (int i = 0; i < ispiti.length; i++) {
            if (ispiti[i].getOcjena() > 1) {
                ispiti2.add(ispiti[i]);
            }
        }
        Ispit[] ispiti3 = new Ispit[ispiti2.size()];
        for (int i = 0; i < ispiti2.size(); i++) {
            ispiti3[i] = ispiti2.get(i);
        }
        return ispiti3;
    }

    /**
     * vraca niz ispita po studentu
     * @param ispiti niz ispita
     * @param student student po kojem filtriramo
     * @return vraca niz filtriranih ispita
     */
    default Ispit[] filtrirajIspitePoStudentu(Ispit[] ispiti, Student student){
        ArrayList<Ispit> ispiti2 = new ArrayList<Ispit>();
        for (int i = 0; i < ispiti.length; i++) {
            if (ispiti[i].getStudent().getJmbag().equals(student.getJmbag())) {
                ispiti2.add(ispiti[i]);
            }
        }
        Ispit[] ispiti3 = new Ispit[ispiti2.size()];
        for (int i = 0; i < ispiti2.size(); i++) {
            ispiti3[i] = ispiti2.get(i);
        }
        return ispiti3;
    }
}
