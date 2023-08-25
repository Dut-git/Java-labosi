package hr.java.vjezbe.entitet;

import hr.java.vjezbe.glavna.Glavna;
import hr.java.vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Predstavlja ustanovu veleuciliste jave u programu
 */
public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska{

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    /**
     * izracunava konacnu ocjenu studija
     * @param ispiti niz ispita od studenta
     * @param zavrsniPismeni ocjena pismenog dijela
     * @param zavrsniObrana ocjena obrane diplomskog
     * @return vraca izracun ocjene
     */
    @Override
    public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> ispiti, int zavrsniPismeni, int zavrsniObrana) {
        try {
            return (BigDecimal.valueOf(2).multiply(odrediProsjekOcjenaNaIspitima(ispiti)).add(BigDecimal.valueOf(zavrsniPismeni)).add(BigDecimal.valueOf(zavrsniObrana))).divide(BigDecimal.valueOf(4));
        } catch (NemoguceOdreditiProsjekStudentaException e) {
            logger.error("Pogresno unesen podatak: ", e);
            System.out.println(e.getMessage());
            return BigDecimal.valueOf(1);
        }
    }

    /**
     *
     * @param godina godina prema kojoj filtriramo studente
     * @return vraca najboljeg studenta
     */
    @Override
    public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
        List<Student> studenti = getStudenti();
        List<Ispit> ispiti = getIspiti();
        Student najboljiStudent = studenti.get(0);
        List<Ispit> NajboljiIspiti = filtrirajIspitePoStudentu(ispiti, najboljiStudent);
        BigDecimal NajboljiProsjek = null;
        try {
            NajboljiProsjek = odrediProsjekOcjenaNaIspitima(NajboljiIspiti);
        } catch (NemoguceOdreditiProsjekStudentaException e) {
            logger.error("Pogresno unesen podatak: ", e);
            System.out.println(e.getMessage());
        }
        for (int i = 1; i < studenti.size(); i++) {
            Student temp = studenti.get(i);
            List<Ispit> tempIspiti = filtrirajIspitePoStudentu(ispiti, temp);
            BigDecimal prosjekZaTemp = null;
            try {
                prosjekZaTemp = odrediProsjekOcjenaNaIspitima(tempIspiti);
            } catch (NemoguceOdreditiProsjekStudentaException e) {
                logger.error("Pogresno unesen podatak: ", e);
                System.out.println(e.getMessage());
            }
            if (prosjekZaTemp.compareTo(NajboljiProsjek) >= 0){
                najboljiStudent = studenti.get(i);
                NajboljiProsjek = prosjekZaTemp;
            }
        }
        if(najboljiStudent == null){
            logger.error("Null pointer zato sto nema najboljeg studenta.");
            throw new NullPointerException("Student godine se ne moze odrediti zato sto svi studenti nemaju niti jednu ocjenu odlican.");
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
    public VeleucilisteJave(String nazivUstanove, List<Predmet> predmeti, List<Profesor> profesori, List<Student> studenti, List<Ispit> ispiti) {
        super(nazivUstanove, predmeti, profesori, studenti, ispiti);
    }
}
