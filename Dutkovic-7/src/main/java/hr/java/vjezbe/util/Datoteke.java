package hr.java.vjezbe.util;

import hr.java.vjezbe.entitet.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Datoteke {

    public static List<Profesor> dohvatiProfesore(){
        List<Profesor> profesori = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("dat\\profesori.txt"))) {
            while (sc.hasNextLine()) {
                Long id = sc.nextLong();
                sc.nextLine();

                String sifra = sc.nextLine();
                String ime = sc.nextLine();
                String prezime = sc.nextLine();
                String titula = sc.nextLine();

                Profesor profesor = new Profesor.ProfesorBuilder(id, ime, prezime).saSifrom(sifra).saTitulom(titula).build();
                profesori.add(profesor);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
        return profesori;
    }

    public static List<Student> dohvatiStudente(){
        List<Student> studenti = new ArrayList<>();
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
                studenti.add(student);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
        return studenti;
    }

    public static List<Predmet> dohvatiPredmete(){
        List<Profesor> sviProfesori = dohvatiProfesore();
        List<Predmet> predmeti = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("dat//predmeti.txt"))) {
            while (scanner.hasNextLine()) {
                Long id = scanner.nextLong();
                scanner.nextLine();

                String sifra = scanner.nextLine();
                String naziv = scanner.nextLine();
                int brojEctsBodova = scanner.nextInt();
                scanner.nextLine();
                Long odabir = scanner.nextLong();
                Profesor nositelj = getObjectById(odabir, sviProfesori);

                Predmet predmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj);
                predmeti.add(predmet);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
        return predmeti;
    }

    public static List<Ispit> dohvatiIspite(){
        List<Ispit> ispiti = new ArrayList<>();
        List<Predmet> sviPredmeti = dohvatiPredmete();
        List<Student> sviStudenti = dohvatiStudente();
        try (Scanner scanner = new Scanner(new File("dat//ispiti.txt"))) {
            while (scanner.hasNextLine()) {
                Long id = scanner.nextLong();
                scanner.nextLine();

                Long odabirPredmeta = scanner.nextLong();
                Predmet predmet = getObjectById(odabirPredmeta, sviPredmeti);
                scanner.nextLine();

                Long odabirStudenta = scanner.nextLong();
                Student student = getObjectById(odabirStudenta, sviStudenti);
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
                ispiti.add(ispit);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
        return ispiti;
    }

    private static Ocjena intToOcjena(int intOcjena) {
        return switch (intOcjena) {
            case 1 -> Ocjena.NEDOVOLJAN;
            case 2 -> Ocjena.DOVOLJAN;
            case 3 -> Ocjena.DOBAR;
            case 4 -> Ocjena.VRLO_DOBAR;
            case 5 -> Ocjena.IZVRSTAN;
            default -> throw new IllegalStateException("Unexpected value: " + intOcjena);
        };
    }

    private static <T> List<T> idsToList(String stringPodataka, Map<Long, T> sviPodaci) {
        String[] stringArrayPodataka = stringPodataka.trim().split("\\s+");
        List<T> podaci = new ArrayList<>();
        for (String podatak : stringArrayPodataka) {
            podaci.add(sviPodaci.get(Long.parseLong(podatak)));
        }
        return podaci;
    }

    private static <T extends Entitet> T getObjectById(Long id, List<T> objects){
        return objects.stream().filter(o -> o.getId() == id).findAny().orElseThrow(() -> new RuntimeException(String.format("Nema objekta koji ima id: %d", id)));
    }

}
