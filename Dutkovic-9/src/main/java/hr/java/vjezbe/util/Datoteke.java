package hr.java.vjezbe.util;

import hr.java.vjezbe.entitet.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Datoteke {

    public static List<Profesor> dohvatiProfesore() {
        List<Profesor> profesori = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("dat//profesori.txt"))) {
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

    public static void dodajProfesora(Profesor profesor) {
        try (FileWriter fileWriter = new FileWriter("dat//profesori.txt", true)) {
            fileWriter.write("\n\n" + profesor.getId() + "\n");
            fileWriter.write(profesor.getSifra() + "\n");
            fileWriter.write(profesor.getIme() + "\n");
            fileWriter.write(profesor.getPrezime() + "\n");
            fileWriter.write(profesor.getTitula());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Student> dohvatiStudente() {
        List<Student> studenti = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("dat//studenti.txt"))) {
            while (scanner.hasNextLine()) {
                Long id = scanner.nextLong();
                scanner.nextLine();
                String ime = scanner.nextLine();
                String prezime = scanner.nextLine();
                String jmbag = scanner.nextLine();
                LocalDate datumRodjenja = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("dd.MM.yyyy."));

                Student student = new Student(id, ime, prezime, jmbag, datumRodjenja);
                studenti.add(student);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
        return studenti;
    }

    public static void dodajStudenta(Student student) {
        try (FileWriter fileWriter = new FileWriter("dat//studenti.txt", true)) {
            fileWriter.write("\n\n" + student.getId() + "\n");
            fileWriter.write(student.getIme() + "\n");
            fileWriter.write(student.getPrezime() + "\n");
            fileWriter.write(student.getJmbag() + "\n");
            fileWriter.write(student.getDatumRodenja().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")).toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Predmet> dohvatiPredmete() {
        List<Profesor> sviProfesori = dohvatiProfesore();
        List<Predmet> predmeti = new ArrayList<>();
        List<Student> listaSvihStudenta = dohvatiStudente();
        Map<Long, Student> sviStudenti = new HashMap<>();
        for (Student student : listaSvihStudenta){
            sviStudenti.put(student.getId(), student);
        }
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
                scanner.nextLine();
                Set<Student> studenti = new HashSet<>(idsToList(scanner.nextLine(), sviStudenti));

                Predmet predmet = new Predmet(id, sifra, naziv, brojEctsBodova, nositelj);
                predmet.setStudenti(studenti);
                predmeti.add(predmet);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
        return predmeti;
    }

    public static void dodajPredmet(Predmet predmet) {
        try (FileWriter fileWriter = new FileWriter("dat//predmeti.txt", true)) {
            fileWriter.write("\n\n" + predmet.getId() + "\n");
            fileWriter.write(predmet.getSifra() + "\n");
            fileWriter.write(predmet.getNaziv() + "\n");
            fileWriter.write(predmet.getBrojEctsBodova() + "\n");
            fileWriter.write(predmet.getNositelj().getId() + "\n");
            for (Student student : predmet.getStudenti()) {
                fileWriter.write(student.getId() + " ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Ispit> dohvatiIspite() {
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

    public static void dodajIspit(Ispit ispit) {
        try (FileWriter fileWriter = new FileWriter("dat//ispiti.txt", true)) {
            fileWriter.write("\n\n" + ispit.getId() + "\n");
            fileWriter.write(ispit.getPredmet().getId() + "\n");
            fileWriter.write(ispit.getStudent().getId() + "\n");
            fileWriter.write(ispit.getOcjena() + "\n");
            fileWriter.write(ispit.getDatumIVrijeme().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm")).toString() + "\n");
            fileWriter.write(ispit.getDvorana().zgrada() + "\n");
            fileWriter.write(ispit.getDvorana().naziv());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Ocjena intToOcjena(int intOcjena) throws IllegalStateException{
        return switch (intOcjena) {
            case 1 -> Ocjena.NEDOVOLJAN;
            case 2 -> Ocjena.DOVOLJAN;
            case 3 -> Ocjena.DOBAR;
            case 4 -> Ocjena.VRLO_DOBAR;
            case 5 -> Ocjena.IZVRSTAN;
            default -> throw new IllegalStateException("Unexpected value: " + intOcjena);
        };
    }

    public static <T> List<T> idsToList(String stringPodataka, Map<Long, T> sviPodaci) {
        String[] stringArrayPodataka = stringPodataka.trim().split("\\s+");
        List<T> podaci = new ArrayList<>();
        for (String podatak : stringArrayPodataka) {
            podaci.add(sviPodaci.get(Long.parseLong(podatak)));
        }
        return podaci;
    }


    public static <T extends Entitet> T getObjectById(Long id, List<T> objects) {
        return objects.stream().filter(o -> o.getId() == id).findAny().orElseThrow(() -> new RuntimeException(String.format("Nema objekta koji ima id: %d", id)));
    }

}
