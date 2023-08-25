package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

import java.math.BigDecimal;
import java.util.List;

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
    BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, int zavrsniPismeni, int zavrsniObrana) throws NemoguceOdreditiProsjekStudentaException;

    /**
     * odreduje prosjek ocjena na ispitima
     * @param ispiti niz ispita
     * @return vraca prosjek ocjena
     * @throws NemoguceOdreditiProsjekStudentaException baca se ako student ima negativnu ocjenu na ispitu
     */
    default BigDecimal odrediProsjekOcjenaNaIspitima(List<Ispit> ispiti) throws NemoguceOdreditiProsjekStudentaException{
        float brojac = 0;
        float prosjek = 0;
        for (Ispit ispit : ispiti) {
            if (ispit.getOcjena().getNumericki() > 1) {
                brojac++;
                prosjek += ispit.getOcjena().getNumericki();
            }
            if (ispit.getOcjena().getNumericki() == 1) {
                String message = "Student " + ispit.getStudent().getIme() + " " + ispit.getStudent().getPrezime() + " ima negativnu ocjenu na ispitu za predmet " + ispit.getPredmet().getNaziv();
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


    private List<Ispit> filtrirajPolozeneIspite(List<Ispit> ispiti){
        return ispiti.stream().filter(ispit -> ispit.getOcjena().getNumericki() > 1).toList();
    }

    /**
     * vraca niz ispita po studentu
     * @param ispiti niz ispita
     * @param student student po kojem filtriramo
     * @return vraca niz filtriranih ispita
     */
    default List<Ispit> filtrirajIspitePoStudentu(List<Ispit> ispiti, Student student){
        /*List<Ispit> ispiti2 = new ArrayList<>();
        for(Ispit ispit : ispiti){
            ispiti2.add(ispit);
        }
        ispiti2.removeIf(ispit -> !ispit.getStudent().getJmbag().equals(student.getJmbag()));
        return ispiti2;*/
        return ispiti.stream().filter(ispit -> !ispit.getStudent().getJmbag().equals(student.getJmbag())).toList();
    }
}
