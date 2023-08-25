package hr.java.vjezbe.glavna;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.iznimke.PostojiViseNajmladjihStudenataException;
import hr.java.vjezbe.sortiranje.StudentSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Predstavlja glavnu klasu u programu
 */
public class GlavnaDatoteka {
    private static final Logger logger = LoggerFactory.getLogger(GlavnaDatoteka.class);

    /**
     * Dohvaca podatke of profesorima iz datoteke i stavlja ih u listu
     * @return dohvaceni profesori
     */
    public static Map<Long, Profesor> dohvatiProfesore(Map<Profesor, List<Predmet>> predmetiProfesora) {
        Map<Long, Profesor> profesori = new HashMap<>();
        System.out.println("Ucitavanje profesora...");
        try (Scanner sc = new Scanner(new File("dat\\profesori.txt"))) {
            while (sc.hasNextLine()) {
                Long id = sc.nextLong();
                sc.nextLine();

                String sifra = sc.nextLine();
                String ime = sc.nextLine();
                String prezime = sc.nextLine();
                String titula = sc.nextLine();

                Profesor profesor = new Profesor.ProfesorBuilder(id, ime, prezime).saSifrom(sifra).saTitulom(titula).build();
                profesori.put(id, profesor);
            }
        } catch (FileNotFoundException e) {
            logger.error("File profesori.txt nije pronaden");
            throw new RuntimeException("File not found");
        }
        for (Profesor profesor : profesori.values()) {
            predmetiProfesora.put(profesor, new ArrayList<>());
        }
        return profesori;
    }

    /**
     * Dohvaca podatke of studentima iz datoteke i stavlja ih u listu
     *
     * @return dohvaceni studenti
     */
    public static Map<Long, Student> dohvatiStudente() {
        Map<Long, Student> studenti = new HashMap<>();
        System.out.println("Ucitavanje studenata...");
        try (Scanner scanner = new Scanner(new File("dat//studenti.txt"))) {
            while (scanner.hasNextLine()) {
                Long id = scanner.nextLong();
                scanner.nextLine();
                String ime = scanner.nextLine();
                String prezime = scanner.nextLine();
                String jmbag = scanner.nextLine();
                LocalDate datumRodjenja = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy."));
                Ocjena ocjenaPismenogZavrsnogRada = intToOcjena(scanner.nextInt());
                scanner.nextLine();
                Ocjena ocjenaObraneZavrsnogRada = intToOcjena(scanner.nextInt());

                Student student = new Student(id, ime, prezime, jmbag, datumRodjenja, ocjenaPismenogZavrsnogRada, ocjenaObraneZavrsnogRada);
                studenti.put(id, student);
            }
        } catch (FileNotFoundException e) {
            logger.error("File studenti.txt nije pronaden");
            throw new RuntimeException("File not found");
        }
        return studenti;
    }

    /**
     * Dohvaca podatke of predmetima iz datoteke i stavlja ih u listu
     *
     * @param profesori         lista profesora od kojih biramo nositelja
     * @param predmetiProfesora predmeti svakog profesora
     * @return dohvaceni predmeti
     */
    public static Map<Long, Predmet> dohvatiPredmete(Map<Long, Profesor> profesori, Map<Profesor, List<Predmet>> predmetiProfesora, Map<Long, Student> sviStudenti) {
        Map<Long, Predmet> predmeti = new HashMap<>();
        System.out.println("Ucitavanje predmeta...");
        try (Scanner scanner = new Scanner(new File("dat//predmeti.txt"))) {
            while (scanner.hasNextLine()) {
                Long id = scanner.nextLong();
                scanner.nextLine();

                String sifra = scanner.nextLine();
                String naziv = scanner.nextLine();
                int brojEctsBodova = scanner.nextInt();
                scanner.nextLine();
                Long odabir = scanner.nextLong();
                Profesor nositelj = profesori.get(odabir);
                scanner.nextLine();
                Set<Student> studenti = new HashSet<>(idsToList(scanner.nextLine(), sviStudenti));

                Predmet predmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj);
                predmet.setStudenti(studenti);
                predmeti.put(id, predmet);

                if (predmetiProfesora.containsKey(profesori.get(odabir))) {
                    predmetiProfesora.get(profesori.get(odabir)).add(predmet);
                } else {
                    predmetiProfesora.put(profesori.get(odabir), new ArrayList<>() {{
                        add(predmet);
                    }});
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("File predmeti.txt nije pronaden");
            throw new RuntimeException("File not found");
        }
        return predmeti;
    }

    /**
     * Dohvaca podatke of ispitima iz datoteke i stavlja ih u listu
     *
     * @param studenti lista studenata od kojih biramo studenta koji je pisao ispit
     * @param predmeti lista predmeta od kojih biramo predmet za koji se pisao ispit
     * @return dohvaceni ispiti
     */
    public static Map<Long, Ispit> dohvatiIspite(Map<Long, Student> studenti, Map<Long, Predmet> predmeti) {
        Map<Long, Ispit> ispiti = new HashMap<>();
        System.out.println("Ucitavanje ispita...");
        try (Scanner scanner = new Scanner(new File("dat//ispiti.txt"))) {
            while (scanner.hasNextLine()) {
                Long id = scanner.nextLong();
                scanner.nextLine();

                Long odabirPredmeta = scanner.nextLong();
                Predmet predmet = predmeti.get(odabirPredmeta);
                scanner.nextLine();

                Long odabirStudenta = scanner.nextLong();
                Student student = studenti.get(odabirStudenta);
                scanner.nextLine();

                Ocjena ocjena = intToOcjena(scanner.nextInt());
                scanner.nextLine();


                LocalDateTime datumIVrijeme = LocalDateTime.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm"));
                String zgrada = scanner.nextLine();
                String dvorana = scanner.nextLine();

                Ispit ispit = new Ispit(id, predmet, student, ocjena, datumIVrijeme, new Dvorana(zgrada, dvorana));
                if (ocjena.getNumericki() == 1) {
                    ispit.getStudent().setNegativanStudent(true);
                }
                ispiti.put(id, ispit);
            }
        } catch (FileNotFoundException e) {
            logger.error("File ispiti.txt nije pronaden");
            throw new RuntimeException("File not found");
        }
        return ispiti;
    }

    /**
     * Dohvaca podatke of ustanovama iz datoteke i stavlja ih u objekt sveuciliste
     * @param sviProfesori lista svih profesora iz kojih biramo one koji se nalaze u ustanovi
     * @param sviStudenti lista svih studenata iz kojih biramo one koji se nalaze u ustanovi
     * @param sviPredmeti lista svih predmeta iz kojih biramo one koji se nalaze u ustanovi
     * @param sviIspiti lista svih ispita iz kojih biramo one koji se nalaze u ustanovi
     * @return objekt sveucilista u koji smo spremili ucitane ustanove
     */
    public static Sveuciliste<ObrazovnaUstanova> dohvatiObrazovneUstanove(Map<Long, Profesor> sviProfesori, Map<Long, Student> sviStudenti, Map<Long, Predmet> sviPredmeti, Map<Long, Ispit> sviIspiti) {
        Sveuciliste<ObrazovnaUstanova> obrazovneUstanove = new Sveuciliste<>();
        System.out.println("Ucitavanje obrazovnih ustanova...\n");

        try (Scanner scanner = new Scanner(new File("dat//obrazovneUstanove.txt"))) {
            while (scanner.hasNextLine()) {
                Long id = scanner.nextLong();
                scanner.nextLine();

                String naziv = scanner.nextLine();
                int vrstaObrazovneUstanove = scanner.nextInt();
                scanner.nextLine();

                List<Profesor> profesori = new ArrayList<>(idsToList(scanner.nextLine(), sviProfesori));
                List<Student> studenti = new ArrayList<>(idsToList(scanner.nextLine(), sviStudenti));
                List<Predmet> predmeti = new ArrayList<>(idsToList(scanner.nextLine(), sviPredmeti));
                List<Ispit> ispiti = new ArrayList<>(idsToList(scanner.nextLine(), sviIspiti));

                if (vrstaObrazovneUstanove == 1) {
                    obrazovneUstanove.dodajObrazovnuUstanovu(new VeleucilisteJave(id, naziv, predmeti, profesori, studenti, ispiti));
                }
                else if(vrstaObrazovneUstanove == 2) {
                    obrazovneUstanove.dodajObrazovnuUstanovu(new FakultetRacunarstva(id, naziv, predmeti, profesori, studenti, ispiti));
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("File obrazovneUstanove.txt nije pronaden");
            throw new RuntimeException("File not found");
        }
        return obrazovneUstanove;
    }

    /**
     * prima integer ocjene i vraca pripadajuci enum objekt Ocjena
     *
     * @param intOcjena ocjena koju pretvaramo
     * @return vraca Ocjenu koja pripada danom integeru
     */
    public static Ocjena intToOcjena(int intOcjena) {
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
     * Pretvara string u kojem se nalazi niz id-ova u listu integera te dohvaca potrebne podatke iz liste u kojoj se nalaze svi podaci
     * @param stringPodataka string u kojem se nalaze odvojeni id-ovi
     * @param sviPodaci svi podaci nekog tipa od kojih uzimamo potrebne podatke
     * @return vraca listu u kojoj se nalaze izdvojeni potrebni podaci prema id-ovima iz stringa
     * @param <T> tip podataka koji se nalazi u listi
     */
    public static <T> List<T> idsToList(String stringPodataka, Map<Long, T> sviPodaci) {
        String[] stringArrayPodataka = stringPodataka.trim().split("\\s+");
        List<T> podaci = new ArrayList<>();
        for (String podatak : stringArrayPodataka) {
            podaci.add(sviPodaci.get(Long.parseLong(podatak)));
        }
        return podaci;
    }

    /**
     * glavna metoda u kojoj pisemo tijelo programa
     *
     * @param args niz argumenata
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Map<Profesor, List<Predmet>> predmetiProfesora = new HashMap<>();
        Map<Long, Profesor> profesori = dohvatiProfesore(predmetiProfesora);
        Map<Long, Student> studenti = dohvatiStudente();
        Map<Long, Predmet> predmeti = dohvatiPredmete(profesori, predmetiProfesora, studenti);
        Map<Long, Ispit> ispiti = dohvatiIspite(studenti, predmeti);
        Sveuciliste<ObrazovnaUstanova> obrazovneUstanove = dohvatiObrazovneUstanove(profesori, studenti, predmeti, ispiti);

        System.out.println("Ispis informacija ucitanih ustanova:");
        for (ObrazovnaUstanova obrazovnaUstanova : obrazovneUstanove.getObrazovneUstanove()) {
            System.out.println("Ustanova " + obrazovnaUstanova.getNazivUstanove() + ":");
            for (Profesor profesor : predmetiProfesora.keySet()) {
                if (!predmetiProfesora.get(profesor).isEmpty() && obrazovnaUstanova.getProfesori().contains(profesor)) {
                    System.out.println("Profesor " + profesor.getIme() + " " + profesor.getPrezime() + " predaje sljedece predmete:");
                    for (int i = 0; i < predmetiProfesora.get(profesor).size(); i++) {
                        System.out.println((i + 1) + ") " + predmetiProfesora.get(profesor).get(i).getNaziv());
                    }
                }
            }

            obrazovnaUstanova.getPredmeti().forEach(predmet -> {
                if (predmet.getStudenti().size() > 0) {
                    System.out.println("Studenti upisani na predmet '" + predmet.getNaziv() + "' su:");
                    predmet.getStudenti().stream().sorted(new StudentSorter()).forEach(student -> System.out.println(student.getIme() + " " + student.getPrezime()));
                } else {
                    System.out.println("Nema studenata upisanih na predmet '" + predmet.getNaziv() + "'.");
                }
            });

            obrazovnaUstanova.getIspiti().forEach(ispit -> {
                if (ispit.getOcjena() == 5) {
                    System.out.println("Student " + ispit.getStudent().getIme()
                            + " " + ispit.getStudent().getPrezime() + " je ostvario ocjenu '" + Ocjena.IZVRSTAN.getPismeno() + "' na predmetu '"
                            + ispit.getPredmet().getNaziv() + "'");
                }
            });

            if (obrazovnaUstanova instanceof FakultetRacunarstva racunarstvo) {
                for (Student student : obrazovnaUstanova.getStudenti()) {
                    List<Ispit> ispitiStudenta;
                    ispitiStudenta = racunarstvo.filtrirajIspitePoStudentu(racunarstvo.getIspiti(), student);
                    BigDecimal konacnaOcjena;
                    if (!student.isNegativanStudent()) {
                        konacnaOcjena = racunarstvo.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, student.getOcjenaPismenogZavrsnogRada().getNumericki(), student.getOcjenaObraneZavrsnogRada().getNumericki());
                        konacnaOcjena = konacnaOcjena.setScale(0, RoundingMode.HALF_UP);
                    } else {
                        konacnaOcjena = BigDecimal.valueOf(1);
                    }
                    System.out.println("Konačna ocjena studija studenta " + student.getIme() + " " + student.getPrezime() + " je " + konacnaOcjena);
                }
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
            if (obrazovnaUstanova instanceof VeleucilisteJave java) {
                for (Student student : obrazovnaUstanova.getStudenti()) {
                    List<Ispit> ispitiStudenta;
                    ispitiStudenta = java.filtrirajIspitePoStudentu(java.getIspiti(), student);
                    BigDecimal konacnaOcjena;
                    if (!student.isNegativanStudent()) {
                        konacnaOcjena = java.izracunajKonacnuOcjenuStudijaZaStudenta(ispitiStudenta, student.getOcjenaPismenogZavrsnogRada().getNumericki(), student.getOcjenaObraneZavrsnogRada().getNumericki());
                        konacnaOcjena = konacnaOcjena.setScale(0, RoundingMode.HALF_UP);
                    } else {
                        konacnaOcjena = BigDecimal.valueOf(1);
                    }
                    System.out.println("Konačna ocjena studija studenta " + student.getIme() + " " + student.getPrezime() + " je " + konacnaOcjena);
                }
                Student studentGodine;
                try {
                    studentGodine = java.odrediNajuspjesnijegStudentaNaGodini(LocalDate.now().getYear());
                    System.out.println("Najbolji student " + LocalDate.now().getYear() + ". godine je " + studentGodine.getIme() + " " + studentGodine.getPrezime() + " JMBAG: " + studentGodine.getJmbag());
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                }

            }
            System.out.println();
        }
        System.out.println("Sortirane obrazovne ustanove prema broju studenata:");
        obrazovneUstanove.getObrazovneUstanove().stream().sorted((o1, o2) ->
        {
            if (o1.getStudenti().size() != o2.getStudenti().size()) {
                return o1.getStudenti().size() - o2.getStudenti().size();
            } else {
                return o1.getNazivUstanove().compareTo(o2.getNazivUstanove());
            }
        }).forEach(ustanova -> System.out.println(ustanova.getNazivUstanove() + ": " + ustanova.getStudenti().size() + " studenta."));

        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat\\obrazovne-ustanove.dat"));) {
            out.writeObject(obrazovneUstanove);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sc.close();
    }
}

