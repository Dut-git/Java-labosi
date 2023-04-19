package hr.java.vjezbe.entitet;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface Visokoskolska {

    BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(Ispit[] ispiti, int zavrsniPismeni, int zavrsniObrana);
    default BigDecimal odrediProsjekOcjenaNaIspitima(Ispit[] ispiti){
        float brojac = 0;
        float prosjek = 0;
        for (int i = 0; i < ispiti.length; i++) {
            if (ispiti[i].getOcjena() > 1){
                brojac++;
                prosjek += ispiti[i].getOcjena();
            }
        }
        return new BigDecimal(prosjek/brojac);
    }
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
