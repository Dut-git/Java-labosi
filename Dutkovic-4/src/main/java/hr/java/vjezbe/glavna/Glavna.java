package hr.java.vjezbe.glavna;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.iznimke.NeispravanPodatakException;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import hr.java.vjezbe.sortiranje.StudentSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Predstavlja glavnu klasu u programu
 */
public class Glavna {

    private static final Logger logger = LoggerFactory.getLogger(Glavna.class);

    private static final int MIN_PROFESORA = 2;
    //private static final int MIN_PREDMETA = 3;
    private static final int MIN_STUDENTA = 2;
    private static final int MIN_ISPITA = 2;

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
    public static Predmet kreirajPredmet(Scanner sc, int i, List<Profesor> profesori, Map<Profesor, List<Predmet>> predmetiProfesora) {
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
                for (int j = 0; j < profesori.size(); j++) {
                    System.out.println((j + 1) + ". " + profesori.get(j).getIme() + " " + profesori.get(j).getPrezime());
                }
                System.out.print("Odabir >> ");
                odabir = sc.nextInt();
                sc.nextLine();
                if (odabir < 1 || odabir > profesori.size()) {
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

        Predmet predmet = new Predmet(sifra, naziv, brojEctsBodova, profesori.get(odabir - 1));
        if(predmetiProfesora.containsKey(profesori.get(odabir - 1))){
            predmetiProfesora.get(profesori.get(odabir - 1)).add(predmet);
        }
        else {
            predmetiProfesora.put(profesori.get(odabir - 1), new ArrayList<>() {{ add(predmet); }});
        }
        return predmet;
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
    public static Ispit kreirajIspit(Scanner sc, int i, List<Student> studenti, List<Predmet> predmeti) {
        System.out.println("Unesite " + (i + 1) + ". ispitni rok:");
        boolean ponoviPetlju;
        int odabirPredmeta = 0;
        do {
            try {
                ponoviPetlju = false;
                System.out.println("Odaberite predmet:");
                for (int j = 0; j < predmeti.size(); j++) {
                    System.out.println((j + 1) + ". " + predmeti.get(j).getNaziv());
                }
                System.out.print("Odabir >> ");
                odabirPredmeta = sc.nextInt();
                sc.nextLine();
                if (odabirPredmeta < 1 || odabirPredmeta > predmeti.size()) {
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
                for (int j = 0; j < studenti.size(); j++) {
                    System.out.println((j + 1) + ". " + studenti.get(j).getIme() + " " + studenti.get(j).getPrezime());
                }
                System.out.print("Odabir >> ");
                odabirStudenta = sc.nextInt();
                sc.nextLine();
                if (odabirStudenta < 1 || odabirStudenta > studenti.size()) {
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

        int intOcjena = 0;
        do {
            try {
                ponoviPetlju = false;
                System.out.print("Unesite ocjenu na ispitu (1-5): ");
                intOcjena = sc.nextInt();
                sc.nextLine();
                if (intOcjena > 5 || intOcjena < 1) {
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
        Ocjena ocjena = intToOcjena(intOcjena);

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

        predmeti.get(odabirPredmeta - 1).getStudenti().add(studenti.get(odabirStudenta - 1));
        Ispit ispit =  new Ispit(predmeti.get(odabirPredmeta - 1), studenti.get(odabirStudenta - 1), ocjena, datumIVrijeme, new Dvorana(nazivDvorane, zgradaDvorane));
        if(ocjena.getNumericki() == 1){
            ispit.getStudent().setNegativanStudent(true);
        }
        return ispit;
    }

    /**
     * prima integer ocjene i vraca pripadajuci enum objekt Ocjena
     * @param intOcjena ocjena koju pretvaramo
     * @return vraca Ocjenu koja pripada danom integeru
     */
    public static Ocjena intToOcjena(int intOcjena){
        return switch (intOcjena) {
            case 1 -> Ocjena.NEDOVOLJAN;
            case 2 -> Ocjena.DOVOLJAN;
            case 3 -> Ocjena.DOBAR;
            case 4 -> Ocjena.VRLO_DOBAR;
            case 5 -> Ocjena.IZVRSTAN;
            default -> throw new IllegalStateException("Unexpected value: " + intOcjena);
        };
    }

    /**
     * glavna metoda u kojoj pisemo tijelo programa
     * @param args niz argumenata
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

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
        List<ObrazovnaUstanova> ustanove = new ArrayList<>();

        for (int k = 0; k < brojObrazovnihUstanova; k++) {

            List<Profesor> profesori = new ArrayList<>();
            List<Predmet> predmeti = new ArrayList<>();
            List<Student> studenti = new ArrayList<>();
            List<Ispit> ispiti = new ArrayList<>();
            Map<Profesor, List<Predmet>> predmetiProfesora = new HashMap<>();

            System.out.println("Unesite podatke za " + (k + 1) + ". obrazovnu ustanovu:");

            System.out.println("Unesite koliko profesora zelite unijeti u obrazovnu ustanovu (minimalno " + MIN_PROFESORA + "): ");
            int brojProfesora = 0;
            do {
                try {
                    ponoviPetlju = false;
                    brojProfesora = sc.nextInt();
                    sc.nextLine();
                    if (brojProfesora < MIN_PROFESORA) {
                        throw new NeispravanPodatakException("Morate unijeti minimalno " + MIN_PROFESORA + " profesora!");
                    }
                } catch (NeispravanPodatakException exception) {
                    ponoviPetlju = true;
                    logger.error("Pogresno unesen podatak: ", exception);
                    System.out.println(exception.getMessage());
                } catch(InputMismatchException exception){
                System.out.println("Unos mora biti numericki, ponovite unos.");
                ponoviPetlju = true;
                sc.nextLine();
                logger.error("Pogresno unesen podatak!", exception);
            }
        } while (ponoviPetlju) ;
            for (int i = 0; i < brojProfesora; i++) {
                profesori.add(kreirajProfesora(sc, i));
            }
            for (Profesor profesor : profesori){
                predmetiProfesora.put(profesor, new ArrayList<>());
            }

            System.out.println("Unesite koliko predmeta zelite unijeti u obrazovnu ustanovu (minimalno " + (brojProfesora + 1) + "): ");
            int brojPredmeta = 0;
            do {
                try {
                    ponoviPetlju = false;
                    brojPredmeta = sc.nextInt();
                    sc.nextLine();
                    if (brojPredmeta < brojProfesora + 1) {
                        throw new NeispravanPodatakException("Morate unijeti minimalno " + (brojProfesora + 1) + " predmeta!");
                    }
                } catch (NeispravanPodatakException exception) {
                    ponoviPetlju = true;
                    logger.error("Pogresno unesen podatak: ", exception);
                    System.out.println(exception.getMessage());
                } catch(InputMismatchException exception){
                    System.out.println("Unos mora biti numericki, ponovite unos.");
                    ponoviPetlju = true;
                    sc.nextLine();
                    logger.error("Pogresno unesen podatak!", exception);
                }
            } while (ponoviPetlju) ;
            do {
                try {
                    ponoviPetlju = false;
                    for (int i = 0; i < brojPredmeta; i++) {
                        predmeti.add(kreirajPredmet(sc, i, profesori, predmetiProfesora));
                    }
                    if (predmetiProfesora.values().stream().anyMatch(List::isEmpty)) {
                        throw new NeispravanPodatakException("Svaki profesor mora imati minimalno jedan predmet, te minimalno jedan profesor mora imati dva predmeta!");
                    }
                } catch (NeispravanPodatakException exception) {
                    ponoviPetlju = true;
                    logger.error("Pogresno unesen podatak: ", exception);
                    System.out.println(exception.getMessage());
                }
            } while (ponoviPetlju);

            for (Profesor profesor : predmetiProfesora.keySet()) {
                if(!predmetiProfesora.get(profesor).isEmpty()) {
                    System.out.println("Profesor " + profesor.getIme() + " " + profesor.getPrezime() + " predaje sljedece predmete:");
                    for (int i = 0; i < predmetiProfesora.get(profesor).size(); i++) {
                        System.out.println((i + 1) + ") " + predmetiProfesora.get(profesor).get(i).getNaziv());
                    }
                }
            }

            System.out.println("Unesite koliko studenata zelite unijeti u obrazovnu ustanovu (minimalno " + (brojPredmeta - 1) + "): ");
            int brojStudenata = 0;
            do {
                try {
                    ponoviPetlju = false;
                    brojStudenata = sc.nextInt();
                    sc.nextLine();
                    if (brojStudenata < MIN_STUDENTA) {
                        throw new NeispravanPodatakException("Morate unijeti minimalno " + MIN_STUDENTA + " studenta!");
                    }
                } catch (NeispravanPodatakException exception) {
                    ponoviPetlju = true;
                    logger.error("Pogresno unesen podatak: ", exception);
                    System.out.println(exception.getMessage());
                } catch(InputMismatchException exception){
                    System.out.println("Unos mora biti numericki, ponovite unos.");
                    ponoviPetlju = true;
                    sc.nextLine();
                    logger.error("Pogresno unesen podatak!", exception);
                }
            } while (ponoviPetlju) ;
            for (int i = 0; i < brojStudenata; i++) {
                studenti.add(kreirajStudenta(sc, i));
            }

            System.out.println("Unesite koliko ispita zelite unijeti u obrazovnu ustanovu (minimalno " + MIN_ISPITA + "): ");
            int brojIspita = 0;
            do {
                try {
                    ponoviPetlju = false;
                    brojIspita = sc.nextInt();
                    sc.nextLine();
                    if (brojIspita < (brojStudenata + 1)) {
                        throw new NeispravanPodatakException("Morate unijeti minimalno " + (brojStudenata + 1) + " ispita!");
                    }
                } catch (NeispravanPodatakException exception) {
                    ponoviPetlju = true;
                    logger.error("Pogresno unesen podatak: ", exception);
                    System.out.println(exception.getMessage());
                } catch(InputMismatchException exception){
                    System.out.println("Unos mora biti numericki, ponovite unos.");
                    ponoviPetlju = true;
                    sc.nextLine();
                    logger.error("Pogresno unesen podatak!", exception);
                }
            } while (ponoviPetlju) ;
            do {
                try {
                    ponoviPetlju = false;
                    for (int i = 0; i < brojIspita; i++) {
                        ispiti.add(kreirajIspit(sc, i, studenti, predmeti));
                    }
                    int brojac = 0;
                    for (Predmet predmet : predmeti){
                        Set<Student> students = predmet.getStudenti();
                        if (students.size() >= 2){
                            brojac++;
                        }
                    }
                    if (brojac == 0){
                        throw new NeispravanPodatakException("Barem jedan predmet mora imati dva ili vise studenata!");
                    }
                } catch (NeispravanPodatakException exception) {
                    ponoviPetlju = true;
                    logger.error("Pogresno unesen podatak: ", exception);
                    System.out.println(exception.getMessage());
                }
            } while (ponoviPetlju);

            for (int i = 0; i < brojPredmeta; i++){
                if(predmeti.get(i).getStudenti().size() > 0){
                    System.out.println("Studenti upisani na predmet '" + predmeti.get(i).getNaziv() + "' su:");
                    for(Student s : predmeti.get(i).getStudenti().stream().sorted(new StudentSorter()).toList()){
                        System.out.println(s.getIme() + " " + s.getPrezime());
                    }
                }
                else {
                    System.out.println("Nema studenata upisanih na predmet '" + predmeti.get(i).getNaziv() + "'.");
                }
            }

            for (int i = 0; i < brojIspita; i++) {
                if (ispiti.get(i).getOcjena() == 5) {
                    System.out.println("Student " + ispiti.get(i).getStudent().getIme()
                            + " " + ispiti.get(i).getStudent().getPrezime() + " je ostvario ocjenu '" + Ocjena.IZVRSTAN.getPismeno() + "' na predmetu '"
                            + ispiti.get(i).getPredmet().getNaziv() + "'");
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
                ustanove.add(new VeleucilisteJave(nazivObrazovneUstanove, predmeti, profesori, studenti, ispiti));
            } else {
                ustanove.add(new FakultetRacunarstva(nazivObrazovneUstanove, predmeti, profesori, studenti, ispiti));
            }

            for (int i = 0; i < brojStudenata; i++) {
                int intOcjenaObraneZavrsnogRada = 0;
                int intOcjenaPismenogZavrsnogRada = 0;
                Ocjena ocjenaPismenogZavrsnogRada = null;
                Ocjena ocjenaObraneZavrsnogRada = null;
                if (!studenti.get(i).isNegativanStudent()) {
                    do {
                        try {
                            ponoviPetlju = false;
                            System.out.print("Unesite ocjenu pismenog dijela završnog rada za studenta: " + studenti.get(i).getIme() + " " + studenti.get(i).getPrezime() + ": ");
                            intOcjenaPismenogZavrsnogRada = sc.nextInt();
                            sc.nextLine();
                            if (intOcjenaPismenogZavrsnogRada > 5 || intOcjenaPismenogZavrsnogRada < 1) {
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
                    ocjenaPismenogZavrsnogRada = intToOcjena(intOcjenaPismenogZavrsnogRada);

                    do {
                        try {
                            ponoviPetlju = false;
                            System.out.print("Unesite ocjenu obrane završnog rada za studenta: " + studenti.get(i).getIme() + " " + studenti.get(i).getPrezime() + ": ");
                            intOcjenaObraneZavrsnogRada = sc.nextInt();
                            sc.nextLine();
                            if (intOcjenaObraneZavrsnogRada > 5 || intOcjenaObraneZavrsnogRada < 1) {
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
                    ocjenaObraneZavrsnogRada = intToOcjena(intOcjenaObraneZavrsnogRada);
                }
                else {
                    System.out.println("Studentu " + studenti.get(i).getIme() + " " + studenti.get(i).getPrezime() + " nije moguce unijeti ocjene iz zavrsnog rada zato sto je " + Ocjena.NEDOVOLJAN.getPismeno() + " is jednog ili vise ispita.");
                }

            List<Ispit> ispitiStudenta = new ArrayList<>();
            if (ustanove.get(k) instanceof FakultetRacunarstva racunarstvo) {
                ispitiStudenta = racunarstvo.filtrirajIspitePoStudentu(ispiti, studenti.get(i));
            }
            if (ustanove.get(k) instanceof VeleucilisteJave racunarstvo) {
                ispitiStudenta = racunarstvo.filtrirajIspitePoStudentu(ispiti, studenti.get(i));
            }
            if (ustanove.get(k) instanceof FakultetRacunarstva racunarstvo) {
                BigDecimal konacnaOcjena;
                if(!studenti.get(i).isNegativanStudent()) {
                    konacnaOcjena = racunarstvo.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, ocjenaPismenogZavrsnogRada.getNumericki(), ocjenaObraneZavrsnogRada.getNumericki());
                    konacnaOcjena = konacnaOcjena.setScale(0, RoundingMode.HALF_UP);
                }
                else {
                    konacnaOcjena = BigDecimal.valueOf(1);
                }

                System.out.println("Konačna ocjena studija studenta " + studenti.get(i).getIme() + " " + studenti.get(i).getPrezime() + " je " + konacnaOcjena);
                if (i == (brojStudenata - 1)) {
                    Student studentGodine;
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
            if (ustanove.get(k) instanceof VeleucilisteJave java) {
                BigDecimal konacnaOcjena;
                if(!studenti.get(i).isNegativanStudent()) {
                    konacnaOcjena = java.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, ocjenaPismenogZavrsnogRada.getNumericki(), ocjenaObraneZavrsnogRada.getNumericki());
                    konacnaOcjena = konacnaOcjena.setScale(0, RoundingMode.HALF_UP);
                }
                else {
                    konacnaOcjena = BigDecimal.valueOf(1);
                }
                System.out.println("Konačna ocjena studija studenta " + studenti.get(i).getIme() + " " + studenti.get(i).getPrezime() + " je " + konacnaOcjena);
                if (i == (brojStudenata - 1)) {
                    Student studentGodine;
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
        sc.close();
}
}
