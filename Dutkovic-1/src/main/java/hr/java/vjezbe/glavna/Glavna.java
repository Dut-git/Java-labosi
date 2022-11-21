package hr.java.vjezbe.glavna;
import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Glavna {

    private static final int BROJ_PROFESORA = 2;
    private static final int BROJ_PREDMETA = 3;
    private static final int BROJ_STUDENTA = 2;
    private static final int BROJ_ISPITA = 1;

    public static Profesor kreirajProfesora(Scanner sc, int i){
        System.out.println("Unesite " + (i+1) + ". profesora:");
        System.out.print("Unesite sifru profesora: ");
        String sifra = sc.nextLine();
        System.out.print("Unesite ime profesora: ");
        String ime = sc.nextLine();
        System.out.print("Unesite prezime profesora: ");
        String prezime = sc.nextLine();
        System.out.print("Unesite titulu profesora: ");
        String titula = sc.nextLine();
        return new Profesor(ime, prezime, sifra, titula);
    }

    public static Predmet kreirajPredmet(Scanner sc, int i, Profesor[] profesori){
        System.out.println("Unesite " + (i+1) + ". predmet:");
        System.out.print("Unesite sifru predmeta: ");
        String sifra = sc.nextLine();
        System.out.print("Unesite naziv predmeta: ");
        String naziv = sc.nextLine();
        System.out.print("Unesite broj ECTS bodova za predmet '" + naziv + "': ");
        int brojEctsBodova = sc.nextInt(); sc.nextLine();
        System.out.println("Odaberite profesora:");
        for (int j = 0; j < BROJ_PROFESORA; j++) {
            System.out.println((j+1) + ". " + profesori[j].getIme() + " " + profesori[j].getPrezime());
        }
        System.out.print("Odabir >> ");
        int odabir = sc.nextInt(); sc.nextLine();
        System.out.print("Unesite broj studenata za predmet '" + naziv + "': ");
        int brojStudenata = sc.nextInt(); sc.nextLine();
        return new Predmet(sifra, naziv, brojEctsBodova, profesori[odabir - 1]);
    }

    public static Student kreirajStudenta(Scanner sc, int i){
        System.out.println("Unesite " + (i+1) + ". studenta:");
        System.out.print("Unesite ime studenta: ");
        String ime = sc.nextLine();
        System.out.print("Unesite prezime studenta: ");
        String prezime = sc.nextLine();
        System.out.print("Unesite datum rodenja studenta " + ime + " " + prezime + " u formatu (dd.MM.yyyy.): ");
        String vrijeme = sc.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
        LocalDate datumRodenja = LocalDate.parse(vrijeme, formatter);
        System.out.print("Unesite JMBAG studenta: ");
        String jmbag = sc.nextLine();
        return new Student(ime, prezime, jmbag, datumRodenja);
    }

    public static Ispit kreirajIspit(Scanner sc, int i, Student[] studenti, Predmet[] predmeti){
        System.out.println("Unesite " + (i+1) + ". ispitni rok:");
        System.out.println("Odaberite predmet:");
        for (int j = 0; j < BROJ_PREDMETA; j++) {
            System.out.println((j+1) + ". " + predmeti[j].getNaziv());
        }
        System.out.print("Odabir >> ");
        int odabirPredmeta = sc.nextInt(); sc.nextLine();
        System.out.println("Odaberite studenta:");
        for (int j = 0; j < BROJ_STUDENTA; j++) {
            System.out.println((j+1) + ". " + studenti[j].getIme() + " " + studenti[j].getPrezime());
        }
        System.out.print("Odabir >> ");
        int odabirStudenta = sc.nextInt(); sc.nextLine();
        System.out.print("Unesite ocjenu na ispitu (1-5): ");
        int ocjena = sc.nextInt(); sc.nextLine();
        System.out.print("Unesite datum i vrijeme ispita u formatu (dd.MM.yyyy.THH:mm): ");
        String datumIVrijemeIspita = sc.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm");
        LocalDateTime datumIVrijeme = LocalDateTime.parse(datumIVrijemeIspita, formatter);
        return new Ispit(predmeti[odabirPredmeta - 1], studenti[odabirStudenta - 1], ocjena, datumIVrijeme);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Profesor[] profesori = new Profesor[BROJ_PROFESORA];
        Predmet[] predmeti = new Predmet[BROJ_PREDMETA];
        Student[] studenti = new Student[BROJ_STUDENTA];
        Ispit[] ispiti = new Ispit[BROJ_ISPITA];

        for (int i = 0; i < BROJ_PROFESORA; i++){
            profesori[i] = kreirajProfesora(sc, i);
        }
        for (int i = 0; i < BROJ_PREDMETA; i++){
            predmeti[i] = kreirajPredmet(sc, i, profesori);
        }
        for (int i = 0; i < BROJ_STUDENTA; i++){
            studenti[i] = kreirajStudenta(sc, i);
        }
        for (int i = 0; i < BROJ_ISPITA; i++) {
            ispiti[i] = kreirajIspit(sc, i, studenti, predmeti);
        }

        for (int i = 0; i < BROJ_ISPITA; i++) {
            if(ispiti[i].getOcjena() == 5){
                System.out.println("Student " + ispiti[i].getStudent().getIme()
                + " " + ispiti[i].getStudent().getPrezime() + " je ostvario ocjenu 'izvrstan' na predmetu '"
                + ispiti[i].getPredmet().getNaziv() + "'");
            }
        }

    }
}
