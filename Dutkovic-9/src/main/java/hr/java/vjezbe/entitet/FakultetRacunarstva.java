package hr.java.vjezbe.entitet;

import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;


/**
 * Sluzi za predstavljanje fakulteta racunarstva u programu
 */
public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski {

    private static final Logger logger = LoggerFactory.getLogger(FakultetRacunarstva.class);

    /**
     * izracunava konacnu ocjenu studija
     * @param ispiti niz ispita od studenta
     * @param diplomskiPismeni ocjena pismenog dijela
     * @param diplomskiObrana ocjena obrane diplomskog
     * @return vraca izracun ocjene
     */
    @Override
    public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, int diplomskiPismeni, int diplomskiObrana) {
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
        List<Student> studenti = getStudenti();
        List<Ispit> ispiti = getIspiti();
        Student najboljiStudent = null;
        int najviseIzvrsnihOcjena = 0;
        int brojacOcjena = 0;
        for (Student temp : studenti) {
            List<Ispit> tempIspiti = filtrirajIspitePoStudentu(ispiti, temp);
            for (Ispit ispit : tempIspiti) {
                if (ispit.getDatumIVrijeme().getYear() == godina && ispit.getOcjena().getNumericki() == 5) {
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
        List<Student> studenti = getStudenti();
        List<Ispit> ispiti = getIspiti();
        Student najboljiStudent = studenti.get(0);
        List<Ispit> NajboljiIspiti = filtrirajIspitePoStudentu(ispiti, najboljiStudent);
        BigDecimal NajboljiProsjek = BigDecimal.valueOf(0);
        try {
            NajboljiProsjek = odrediProsjekOcjenaNaIspitima(NajboljiIspiti);
        } catch (NemoguceOdreditiProsjekStudentaException e) {
            logger.error("Pogresno unesen podatak: ", e);
            System.out.println(e.getMessage());
        }
        for (int i = 1; i < studenti.size(); i++) {
            Student temp = studenti.get(i);
            List<Ispit> tempIspiti = filtrirajIspitePoStudentu(ispiti, temp);
            BigDecimal prosjekZaTemp = BigDecimal.valueOf(0);
            try {
                prosjekZaTemp = odrediProsjekOcjenaNaIspitima(tempIspiti);
            } catch (NemoguceOdreditiProsjekStudentaException e) {
                logger.error("Pogresno unesen podatak: ", e);
                System.out.println(e.getMessage());
            }
            if (prosjekZaTemp.compareTo(NajboljiProsjek) > 0) {
                najboljiStudent = studenti.get(i);
                NajboljiProsjek = prosjekZaTemp;
            } else if (prosjekZaTemp.compareTo(NajboljiProsjek) == 0 && temp.getDatumRodenja().isAfter(najboljiStudent.getDatumRodenja())) {
                najboljiStudent = studenti.get(i);
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
    public FakultetRacunarstva(Long id, String nazivUstanove, List<Predmet> predmeti, List<Profesor> profesori, List<Student> studenti, List<Ispit> ispiti) {
        super(id, nazivUstanove, predmeti, profesori, studenti, ispiti);
    }
}
