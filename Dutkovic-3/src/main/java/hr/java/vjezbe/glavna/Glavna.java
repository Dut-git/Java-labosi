package hr.java.vjezbe.glavna;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.iznimke.NeispravanPodatakException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Predstavlja glavnu klasu u programu
 */
public class Glavna {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    private static final int BROJ_PROFESORA = 2;
    private static final int BROJ_PREDMETA = 2;
    private static final int BROJ_STUDENTA = 2;
    private static final int BROJ_ISPITA = 2;

    /**
     * kreira novog prfesora
     * @param sc objekt za skeniranje podataka
     * @param i  broj pomocu kojeg znamo na kojem se predmetu nalazimo
     * @return vraca kreiranog profesora
     */
    public static Profesor kreirajProfesora(Scanner sc, int i) {
        System.out.println("Unesite " + (i + 1) + ". profesora:");
        System.out.print("Unesite sifru profesora: ");
        String sifra = sc.nextLine();
        System.out.print("Unesite ime profesora: ");
        String ime = sc.nextLine();
        System.out.print("Unesite prezime profesora: ");
        String prezime = sc.nextLine();
        System.out.print("Unesite titulu profesora: ");
        String titula = sc.nextLine();
        return new Profesor.ProfesorBuilder(ime, prezime).saSifrom(sifra).saTitulom(titula).build();
    }

    /**
     * kreira novi predmet
     * @param sc objekt za skeniranje podataka
     * @param i  broj pomocu kojeg znamo na kojem se predmetu nalazimo
     * @param profesori niz profesora
     * @return vraca kreirani predmet
     */
    public static Predmet kreirajPredmet(Scanner sc, int i, Profesor[] profesori) {
        boolean ponoviPetlju;
        System.out.println("Unesite " + (i + 1) + ". predmet:");

        System.out.print("Unesite sifru predmeta: ");
        String sifra = sc.nextLine();

        System.out.print("Unesite naziv predmeta: ");
        String naziv = sc.nextLine();

        int brojEctsBodova = 0;
        do {
            try {
                ponoviPetlju = false;
                System.out.print("Unesite broj ECTS bodova za predmet '" + naziv + "': ");
                brojEctsBodova = sc.nextInt();
                sc.nextLine();
                if (brojEctsBodova <= 0) {
                    throw new NeispravanPodatakException();
                }
            } catch (InputMismatchException exception) {
                System.out.println("Broj ECTS bodova mora biti numericka vrijednost, ponovno unesite broj ECTS bodova.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
                sc.nextLine();
            } catch (NeispravanPodatakException exception) {
                System.out.println("Broj ECTS bodova mora biti veci od 0, ponovno unesite broj ECTS bodova.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
            }
        } while (ponoviPetlju);

        int odabir = 0;
        do {
            try {
                ponoviPetlju = false;
                System.out.println("Odaberite profesora:");
                for (int j = 0; j < BROJ_PROFESORA; j++) {
                    System.out.println((j + 1) + ". " + profesori[j].getIme() + " " + profesori[j].getPrezime());
                }
                System.out.print("Odabir >> ");
                odabir = sc.nextInt();
                sc.nextLine();
                if (odabir < 1 || odabir > BROJ_PROFESORA) {
                    throw new NeispravanPodatakException();
                }
            } catch (InputMismatchException exception) {
                System.out.println("Odabir profesora mora biti numericka vrijednost, ponovno unesite odabir profesora.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
                sc.nextLine();
            } catch (NeispravanPodatakException exception) {
                System.out.println("Odabrali ste nepostojeceg profesora, ponovno unesite odabir profesora.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
            }
        } while (ponoviPetlju);

        do {
            try {
                ponoviPetlju = false;
                System.out.print("Unesite broj studenata za predmet '" + naziv + "': ");
                int brojStudenata = sc.nextInt();
                sc.nextLine();
                if (brojStudenata <= 0) {
                    throw new NeispravanPodatakException();
                }
            } catch (InputMismatchException exception) {
                System.out.println("Broj studenata mora biti numericka vrijednost, ponovno unesite broj studenata.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
                sc.nextLine();
            } catch (NeispravanPodatakException exception) {
                System.out.println("Broj studenata mora biti veci od 0, ponovno unesite broj studenata.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
            }
        } while (ponoviPetlju);
        return new Predmet(sifra, naziv, brojEctsBodova, profesori[odabir - 1]);
    }

    /**
     * kreira novog studenta
     * @param sc objekt za skeniranje podataka
     * @param i  broj pomocu kojeg znamo na kojem se predmetu nalazimo
     * @return vraca kreiranog studenta
     */
    public static Student kreirajStudenta(Scanner sc, int i) {
        System.out.println("Unesite " + (i + 1) + ". studenta:");
        System.out.print("Unesite ime studenta: ");
        String ime = sc.nextLine();
        System.out.print("Unesite prezime studenta: ");
        String prezime = sc.nextLine();
        boolean ponoviPetlju;
        LocalDate datumRodenja = null;
        do {
            try {
                ponoviPetlju = false;
                System.out.print("Unesite datum rodenja studenta " + ime + " " + prezime + " u formatu (dd.MM.yyyy.): ");
                String vrijeme = sc.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                datumRodenja = LocalDate.parse(vrijeme, formatter);
            } catch (Exception exception) {
                System.out.println("Unijeli ste pogresno napisan datum rodenja, ponovno unesite datum rodenja.");
                ponoviPetlju = true;
            }
        } while (ponoviPetlju);
        System.out.print("Unesite JMBAG studenta: ");
        String jmbag = sc.nextLine();
        return new Student(ime, prezime, jmbag, datumRodenja);
    }

    /**
     * kreira novi ispit
     * @param sc objekt za skeniranje podataka
     * @param i  broj pomocu kojeg znamo na kojem se predmetu nalazimo
     * @param studenti niz studenata
     * @param predmeti niz predmeta
     * @return vraca kreirani ispit
     */
    public static Ispit kreirajIspit(Scanner sc, int i, Student[] studenti, Predmet[] predmeti) {
        System.out.println("Unesite " + (i + 1) + ". ispitni rok:");
        boolean ponoviPetlju;
        int odabirPredmeta = 0;
        do {
            try {
                ponoviPetlju = false;
                System.out.println("Odaberite predmet:");
                for (int j = 0; j < BROJ_PREDMETA; j++) {
                    System.out.println((j + 1) + ". " + predmeti[j].getNaziv());
                }
                System.out.print("Odabir >> ");
                odabirPredmeta = sc.nextInt();
                sc.nextLine();
                if (odabirPredmeta < 1 || odabirPredmeta > BROJ_PREDMETA) {
                    throw new NeispravanPodatakException();
                }
            } catch (InputMismatchException exception) {
                System.out.println("Odabir predmeta mora biti numericka vrijednost, ponovno unesite odabir predmeta.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
                sc.nextLine();
            } catch (NeispravanPodatakException exception) {
                System.out.println("Odabrali ste nepostojeci predmet, ponovno unesite odabir predmeta.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
            }
        } while (ponoviPetlju);

        System.out.print("Unesite naziv dvorane: ");
        String nazivDvorane = sc.nextLine();

        System.out.print("Unesite zgradu dvorane: ");
        String zgradaDvorane = sc.nextLine();

        int odabirStudenta = 0;
        do {
            try {
                ponoviPetlju = false;
                System.out.println("Odaberite studenta:");
                for (int j = 0; j < BROJ_STUDENTA; j++) {
                    System.out.println((j + 1) + ". " + studenti[j].getIme() + " " + studenti[j].getPrezime());
                }
                System.out.print("Odabir >> ");
                odabirStudenta = sc.nextInt();
                sc.nextLine();
                if (odabirStudenta < 1 || odabirStudenta > BROJ_STUDENTA) {
                    throw new NeispravanPodatakException();
                }
            } catch (InputMismatchException exception) {
                System.out.println("Odabir studenta mora biti numericka vrijednost, ponovno unesite odabir studenta.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
                sc.nextLine();
            } catch (NeispravanPodatakException exception) {
                System.out.println("Odabrali ste nepostojeceg studenta, ponovno unesite odabir studenta.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
            }
        } while (ponoviPetlju);

        int ocjena = 0;
        do {
            try {
                ponoviPetlju = false;
                System.out.print("Unesite ocjenu na ispitu (1-5): ");
                ocjena = sc.nextInt();
                sc.nextLine();
                if (ocjena > 5 || ocjena < 1) {
                    throw new NeispravanPodatakException();
                }
            } catch (InputMismatchException exception) {
                System.out.println("Ocjena mora biti numericka vrijednost, ponovno unesite ocjenu.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
                sc.nextLine();
            } catch (NeispravanPodatakException exception) {
                System.out.println("Ocjena mora biti u rasponu od 1-5, ponovno unesite ocjenu.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
            }
        } while (ponoviPetlju);

        LocalDateTime datumIVrijeme = null;
        do {
            try {
                ponoviPetlju = false;
                System.out.print("Unesite datum i vrijeme ispita u formatu (dd.MM.yyyy.THH:mm): ");
                String datumIVrijemeIspita = sc.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm");
                datumIVrijeme = LocalDateTime.parse(datumIVrijemeIspita, formatter);
            } catch (Exception exception) {
                System.out.println("Unijeli ste pogresno napisan datum i vrijeme ispita, ponovno unesite datum i vrijeme ispita.");
                ponoviPetlju = true;
            }
        } while (ponoviPetlju);
        Ispit ispit =  new Ispit(predmeti[odabirPredmeta - 1], studenti[odabirStudenta - 1], ocjena, datumIVrijeme, new Dvorana(nazivDvorane, zgradaDvorane));
        if(ocjena == 1){
            ispit.getStudent().setNegativanStudent(true);
        }
        return ispit;
    }

    /**
     * glavna metoda u kojoj pisemo tijelo programa
     * @param args niz argumenata
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Profesor[] profesori = new Profesor[BROJ_PROFESORA];
        Predmet[] predmeti = new Predmet[BROJ_PREDMETA];
        Student[] studenti = new Student[BROJ_STUDENTA];
        Ispit[] ispiti = new Ispit[BROJ_ISPITA];

        boolean ponoviPetlju;
        int brojObrazovnihUstanova = 0;
        do {
            try {
                ponoviPetlju = false;
                System.out.print("Unesite broj obrazovnih ustanova: ");
                brojObrazovnihUstanova = sc.nextInt();
                sc.nextLine();
                if (brojObrazovnihUstanova <= 0) {
                    throw new NeispravanPodatakException();
                }
            } catch (InputMismatchException exception) {
                System.out.println("Broj obrazovnih ustanova mora biti numericka vrijednost, ponovno unesite broj obrazovnih ustanova.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
                sc.nextLine();
            } catch (NeispravanPodatakException exception) {
                System.out.println("Broj obrazovnih ustanova mora biti veci od 0, ponovno unesite broj obrazovnih ustanova.");
                ponoviPetlju = true;
                logger.error("Pogresno unesen podatak: ", exception);
            }
        } while (ponoviPetlju);
        ObrazovnaUstanova[] ustanove = new ObrazovnaUstanova[brojObrazovnihUstanova];

        for (int k = 0; k < brojObrazovnihUstanova; k++) {

            System.out.println("Unesite podatke za " + (k + 1) + ". obrazovnu ustanovu:");

            for (int i = 0; i < BROJ_PROFESORA; i++) {
                profesori[i] = kreirajProfesora(sc, i);
            }
            for (int i = 0; i < BROJ_PREDMETA; i++) {
                predmeti[i] = kreirajPredmet(sc, i, profesori);
            }
            for (int i = 0; i < BROJ_STUDENTA; i++) {
                studenti[i] = kreirajStudenta(sc, i);
            }
            for (int i = 0; i < BROJ_ISPITA; i++) {
                ispiti[i] = kreirajIspit(sc, i, studenti, predmeti);
            }

            for (int i = 0; i < BROJ_ISPITA; i++) {
                if (ispiti[i].getOcjena() == 5) {
                    System.out.println("Student " + ispiti[i].getStudent().getIme()
                            + " " + ispiti[i].getStudent().getPrezime() + " je ostvario ocjenu 'izvrstan' na predmetu '"
                            + ispiti[i].getPredmet().getNaziv() + "'");
                }
            }

            int odabirUstanove = 0;
            do {
                try {
                    ponoviPetlju = false;
                    System.out.print("Odaberite obrazovnu ustanovu za navedene podatke koju želite unijeti (1 - Veleučilište Jave, 2 - Fakultet računarstva): ");
                    odabirUstanove = sc.nextInt();
                    sc.nextLine();
                    if (odabirUstanove < 0 || odabirUstanove > 2) {
                        throw new NeispravanPodatakException();
                    }
                } catch (InputMismatchException exception) {
                    System.out.println("Odabir ustanove mora biti numericka vrijednost, ponovno unesite odabir ustanove.");
                    ponoviPetlju = true;
                    logger.error("Pogresno unesen podatak: ", exception);
                    sc.nextLine();
                } catch (NeispravanPodatakException exception) {
                    System.out.println("Odabrali ste nepostojecu ustanovu, ponovno unesite odabir ustanove.");
                    ponoviPetlju = true;
                    logger.error("Pogresno unesen podatak: ", exception);
                }
            } while (ponoviPetlju);

            System.out.print("Unesite naziv obrazovne ustanove: ");
            String nazivObrazovneUstanove = sc.nextLine();

            if (odabirUstanove == 1) {
                ustanove[k] = new VeleucilisteJave(nazivObrazovneUstanove, predmeti, profesori, studenti, ispiti);
            } else {
                ustanove[k] = new FakultetRacunarstva(nazivObrazovneUstanove, predmeti, profesori, studenti, ispiti);
            }

            for (int i = 0; i < BROJ_STUDENTA; i++) {
                int ocjenaObraneZavrsnogRada = 0;
                int ocjenaPismenogZavrsnogRada = 0;
                if (!studenti[i].isNegativanStudent()) {
                    do {
                        try {
                            ponoviPetlju = false;
                            System.out.print("Unesite ocjenu završnog rada za studenta: " + studenti[i].getIme() + " " + studenti[i].getPrezime() + ": ");
                            ocjenaPismenogZavrsnogRada = sc.nextInt();
                            sc.nextLine();
                            if (ocjenaPismenogZavrsnogRada > 5 || ocjenaPismenogZavrsnogRada < 1) {
                                throw new NeispravanPodatakException();
                            }
                        } catch (InputMismatchException exception) {
                            System.out.println("Ocjena mora biti numericka vrijednost, ponovno unesite ocjenu.");
                            ponoviPetlju = true;
                            logger.error("Pogresno unesen podatak: ", exception);
                            sc.nextLine();
                        } catch (NeispravanPodatakException exception) {
                            System.out.println("Ocjena mora biti u rasponu od 1-5, ponovno unesite ocjenu.");
                            ponoviPetlju = true;
                            logger.error("Pogresno unesen podatak: ", exception);
                        }
                    } while (ponoviPetlju);

                    do {
                        try {
                            ponoviPetlju = false;
                            System.out.print("Unesite ocjenu obrane završnog rada za studenta: " + studenti[i].getIme() + " " + studenti[i].getPrezime() + ": ");
                            ocjenaObraneZavrsnogRada = sc.nextInt();
                            sc.nextLine();
                            if (ocjenaObraneZavrsnogRada > 5 || ocjenaObraneZavrsnogRada < 1) {
                                throw new NeispravanPodatakException();
                            }
                        } catch (InputMismatchException exception) {
                            System.out.println("Ocjena mora biti numericka vrijednost, ponovno unesite ocjenu.");
                            ponoviPetlju = true;
                            logger.error("Pogresno unesen podatak: ", exception);
                            sc.nextLine();
                        } catch (NeispravanPodatakException exception) {
                            System.out.println("Ocjena mora biti u rasponu od 1-5, ponovno unesite ocjenu.");
                            ponoviPetlju = true;
                            logger.error("Pogresno unesen podatak: ", exception);
                        }
                    } while (ponoviPetlju);
                }
                else {
                    System.out.println("Studentu " + studenti[i].getIme() + " " + studenti[i].getPrezime() + " nije moguce unijeti ocjene iz zavrsnog rada zato sto je negativan is jednog ili vise ispita.");
                }

            Ispit[] ispitiStudenta = null;
            if (ustanove[k] instanceof FakultetRacunarstva racunarstvo) {
                ispitiStudenta = racunarstvo.filtrirajIspitePoStudentu(ispiti, studenti[i]);
            }
            if (ustanove[k] instanceof VeleucilisteJave racunarstvo) {
                ispitiStudenta = racunarstvo.filtrirajIspitePoStudentu(ispiti, studenti[i]);
            }
            if (ustanove[k] instanceof FakultetRacunarstva racunarstvo) {
                if(!studenti[i].isNegativanStudent()) {
                    BigDecimal konacnaOcjena = racunarstvo.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, ocjenaPismenogZavrsnogRada, ocjenaObraneZavrsnogRada);
                    konacnaOcjena = konacnaOcjena.setScale(0, RoundingMode.HALF_UP);
                    System.out.println("Konačna ocjena studija studenta " + studenti[i].getIme() + " " + studenti[i].getPrezime() + " je " + konacnaOcjena);
                }
                else {
                    BigDecimal konacnaOcjena = BigDecimal.valueOf(1);
                    System.out.println("Konačna ocjena studija studenta " + studenti[i].getIme() + " " + studenti[i].getPrezime() + " je " + konacnaOcjena);
                }
                if (i == (BROJ_STUDENTA - 1)) {
                    Student studentGodine = null;
                    try {
                        studentGodine = racunarstvo.odrediNajuspjesnijegStudentaNaGodini(LocalDate.now().getYear());
                        System.out.println("Najbolji student " + LocalDate.now().getYear() + ". godine je " + studentGodine.getIme() + " " + studentGodine.getPrezime() + " JMBAG: " + studentGodine.getJmbag());
                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                    try {
                        Student studentRektor = racunarstvo.odrediStudentaZaRektorovuNagradu();
                        System.out.println("Student koji je osvojio rektorovu nagradu je: " + studentRektor.getIme() + " " + studentRektor.getPrezime() + " JMBAG: " + studentRektor.getJmbag());
                    } catch (PostojiViseNajmladjihStudenataException e) {
                        System.out.println(e.getMessage());
                    }

                }
            }
            if (ustanove[k] instanceof VeleucilisteJave java) {
                if(!studenti[i].isNegativanStudent()) {
                    BigDecimal konacnaOcjena = java.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, ocjenaPismenogZavrsnogRada, ocjenaObraneZavrsnogRada);
                    konacnaOcjena = konacnaOcjena.setScale(0, RoundingMode.HALF_UP);
                    System.out.println("Konačna ocjena studija studenta " + studenti[i].getIme() + " " + studenti[i].getPrezime() + " je " + konacnaOcjena);
                }
                else {
                    BigDecimal konacnaOcjena = BigDecimal.valueOf(1);
                    System.out.println("Konačna ocjena studija studenta " + studenti[i].getIme() + " " + studenti[i].getPrezime() + " je " + konacnaOcjena);
                }
                if (i == (BROJ_STUDENTA - 1)) {
                    Student studentGodine = null;
                    try {
                        studentGodine = java.odrediNajuspjesnijegStudentaNaGodini(LocalDate.now().getYear());
                        System.out.println("Najbolji student " + LocalDate.now().getYear() + ". godine je " + studentGodine.getIme() + " " + studentGodine.getPrezime() + " JMBAG: " + studentGodine.getJmbag());
                    } catch (NullPointerException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
}
