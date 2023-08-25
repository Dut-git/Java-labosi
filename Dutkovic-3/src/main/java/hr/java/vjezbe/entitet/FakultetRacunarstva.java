package hr.java.vjezbe.entitet;

import hr.java.vjezbe.glavna.Glavna;
import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


/**
 * Sluzi za predstavljanje fakulteta racunarstva u programu
 */
public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    /**
     * izracunava konacnu ocjenu studija
     * @param ispiti niz ispita od studenta
     * @param diplomskiPismeni ocjena pismenog dijela
     * @param diplomskiObrana ocjena obrane diplomskog
     * @return vraca izracun ocjene
     */
    @Override
    public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, int diplomskiPismeni, int diplomskiObrana) {
        try {
            return (BigDecimal.valueOf(3).multiply(odrediProsjekOcjenaNaIspitima(ispiti)).add(BigDecimal.valueOf(diplomskiPismeni)).add(BigDecimal.valueOf(diplomskiObrana))).divide(BigDecimal.valueOf(5));
        } catch (NemoguceOdreditiProsjekStudentaException e) {
            logger.error("Pogresno unesen podatak: ", e);
            System.out.println(e.getMessage());
            return BigDecimal.valueOf(1);
        }
    }

    /**
     * odreduje najuspjesnijeg studenta prema godini
     * @param godina godina prema kojoj filtriramo studente
     * @return vraca najboljeg studenta
     */
    @Override
    public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
        Student[] studenti = getStudenti();
        Ispit[] ispiti = getIspiti();
        Student najboljiStudent = null;
        int najviseIzvrsnihOcjena = 0;
        int brojacOcjena = 0;
        for (int i = 0; i < studenti.length; i++) {
            Student temp = studenti[i];
            Ispit[] tempIspiti = filtrirajIspitePoStudentu(ispiti, temp);
            for (int j = 0; j < tempIspiti.length; j++) {
                if (tempIspiti[j].getDatumIVrijeme().getYear() == godina && tempIspiti[j].getOcjena() == 5) {
                    brojacOcjena++;
                }
            }
            if (brojacOcjena > najviseIzvrsnihOcjena) {
                najboljiStudent = temp;
            }
            brojacOcjena = 0;
        }
        if(najboljiStudent == null){
            logger.error("Null pointer zato sto nema najboljeg studenta.");
            throw new NullPointerException("Student godine se ne moze odrediti zato sto svi studenti nemaju niti jednu ocjenu odlican.");
        }
        return najboljiStudent;
    }

    /**
     * odreduje rektor studenta
     * @return vraca studenta koji je rektor
     * @throws PostojiViseNajmladjihStudenataException baca se ako postoji vise studenata koji su najmladi i najbolji
     */
    @Override
    public Student odrediStudentaZaRektorovuNagradu() throws PostojiViseNajmladjihStudenataException {
        Student[] studenti = getStudenti();
        Ispit[] ispiti = getIspiti();
        Student najboljiStudent = studenti[0];
        Ispit[] NajboljiIspiti = filtrirajIspitePoStudentu(ispiti, najboljiStudent);
        BigDecimal NajboljiProsjek = BigDecimal.valueOf(0);
        try {
            NajboljiProsjek = odrediProsjekOcjenaNaIspitima(NajboljiIspiti);
        } catch (NemoguceOdreditiProsjekStudentaException e) {
            logger.error("Pogresno unesen podatak: ", e);
            System.out.println(e.getMessage());
        }
        for (int i = 1; i < studenti.length; i++) {
            Student temp = studenti[i];
            Ispit[] tempIspiti = filtrirajIspitePoStudentu(ispiti, temp);
            BigDecimal prosjekZaTemp = BigDecimal.valueOf(0);
            try {
                prosjekZaTemp = odrediProsjekOcjenaNaIspitima(tempIspiti);
            } catch (NemoguceOdreditiProsjekStudentaException e) {
                logger.error("Pogresno unesen podatak: ", e);
                System.out.println(e.getMessage());
            }
            if (prosjekZaTemp.compareTo(NajboljiProsjek) > 0) {
                najboljiStudent = studenti[i];
                NajboljiProsjek = prosjekZaTemp;
            } else if (prosjekZaTemp.compareTo(NajboljiProsjek) == 0 && temp.getDatumRodenja().isAfter(najboljiStudent.getDatumRodenja())) {
                najboljiStudent = studenti[i];
                NajboljiProsjek = prosjekZaTemp;
            }
            else if(prosjekZaTemp.compareTo(NajboljiProsjek) >= 0 && temp.getDatumRodenja().isEqual(najboljiStudent.getDatumRodenja())){
                String message = "Student " + temp.getIme() + " " + temp.getPrezime() + " i " + najboljiStudent.getIme() + " " + najboljiStudent.getPrezime() + " imaju isti dan rodenja, najmladi su i imaju najbolji prosjek te nije dodijeliti rektorovu nagradu.";
                logger.error("Studenti {} i {} su bili najmladi i imali su iste prosjeke", temp, najboljiStudent);
                throw new PostojiViseNajmladjihStudenataException(message);
            }
        }
        return najboljiStudent;
    }

    /**
     * kreira novi fakultet racunarstva
     * @param nazivUstanove naziv unesene ustanove
     * @param predmeti niz predmeta koje fakultet sadrzi
     * @param profesori profesori koji predaju na fakultetu
     * @param studenti studenti koji studiraju na fakultetu
     * @param ispiti popis pisanih ispita za studente
     */
    public FakultetRacunarstva(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenti, Ispit[] ispiti) {
        super(nazivUstanove, predmeti, profesori, studenti, ispiti);
    }
}
