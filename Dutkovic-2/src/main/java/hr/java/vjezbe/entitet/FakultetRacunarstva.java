package hr.java.vjezbe.entitet;

import java.math.BigDecimal;

public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski{

    @Override
    public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, int diplomskiPismeni, int diplomskiObrana) {
        return (BigDecimal.valueOf(3).multiply(odrediProsjekOcjenaNaIspitima(ispiti)).add(BigDecimal.valueOf(diplomskiPismeni)).add(BigDecimal.valueOf(diplomskiObrana))).divide(BigDecimal.valueOf(5));
    }

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
            if (brojacOcjena > najviseIzvrsnihOcjena){
                najboljiStudent = temp;
            }
            brojacOcjena = 0;
        }
        return najboljiStudent;
    }

    @Override
    public Student odrediStudentaZaRektorovuNagradu() {
        Student[] studenti = getStudenti();
        Ispit[] ispiti = getIspiti();
        Student najboljiStudent = studenti[0];
        Ispit[] NajboljiIspiti = filtrirajIspitePoStudentu(ispiti, najboljiStudent);
        BigDecimal NajboljiProsjek = odrediProsjekOcjenaNaIspitima(NajboljiIspiti);
        for (int i = 1; i < studenti.length; i++) {
            Student temp = studenti[i];
            Ispit[] tempIspiti = filtrirajIspitePoStudentu(ispiti, temp);
            BigDecimal prosjekZaTemp = odrediProsjekOcjenaNaIspitima(tempIspiti);
            if (prosjekZaTemp.compareTo(NajboljiProsjek) > 0){
                    najboljiStudent = studenti[i];
                    NajboljiProsjek = prosjekZaTemp;
            }
            else if (prosjekZaTemp.compareTo(NajboljiProsjek) == 0 && temp.getDatumRodenja().isAfter(najboljiStudent.getDatumRodenja())) {
                najboljiStudent = studenti[i];
                NajboljiProsjek = prosjekZaTemp;
            }
        }
        return najboljiStudent;
    }

    public FakultetRacunarstva(String nazivUstanove, Predmet[] predmeti, Profesor[] profesori, Student[] studenti, Ispit[] ispiti) {
        super(nazivUstanove, predmeti, profesori, studenti, ispiti);
    }
}
