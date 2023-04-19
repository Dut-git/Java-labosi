package hr.java.vjezbe.entitet;

import java.math.BigDecimal;

public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska{

    @Override
    public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, int zavrsniPismeni, int zavrsniObrana){
         return (BigDecimal.valueOf(2).multiply(odrediProsjekOcjenaNaIspitima(ispiti)).add(BigDecimal.valueOf(zavrsniPismeni)).add(BigDecimal.valueOf(zavrsniObrana))).divide(BigDecimal.valueOf(4));
    }

    @Override
    public Student odrediNajuspjesnijegStudentaNaGodini(int godina) {
        Student[] studenti = getStudenti();
        Ispit[] ispiti = getIspiti();
        Student najboljiStudent = studenti[0];
        Ispit[] NajboljiIspiti = filtrirajIspitePoStudentu(ispiti, najboljiStudent);
        BigDecimal NajboljiProsjek = odrediProsjekOcjenaNaIspitima(NajboljiIspiti);
        for (int i = 1; i < studenti.length; i++) {
            Student temp = studenti[i];
            Ispit[] tempIspiti = filtrirajIspitePoStudentu(ispiti, temp);
            BigDecimal prosjekZaTemp = odrediProsjekOcjenaNaIspitima(tempIspiti);
            if (prosjekZaTemp.compareTo(NajboljiProsjek) >= 0){
                najboljiStudent = studenti[i];
                NajboljiProsjek = prosjekZaTemp;
            }
        }
        return najboljiStudent;
    }

    public VeleucilisteJave(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenti, Ispit[] ispiti) {
        super(nazivUstanove, predmeti, profesori, studenti, ispiti);
    }
}
