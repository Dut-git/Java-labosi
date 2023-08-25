package hr.java.vjezbe.baza;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.iznimke.BazaPodatakaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.sql.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BazaPodataka {

    private static final Logger logger = LoggerFactory.getLogger(BazaPodataka.class);

    public static List<Profesor> dohvatiProfesorePremaKriterijima(Profesor profesor) throws BazaPodatakaException {
        List<Profesor> listaProfesora = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {
            StringBuilder sqlUpit = new StringBuilder("SELECT * FROM PROFESOR WHERE 1 = 1");
            if (!Optional.ofNullable(profesor).isEmpty()) {
                if (Optional.ofNullable(profesor).map(Profesor::getId).isPresent()) {
                    sqlUpit.append(" AND ID = " + profesor.getId());
                }
                if (!Optional.ofNullable(profesor.getSifra()).map(String::isBlank).orElse(true)) {
                    sqlUpit.append(" AND SIFRA LIKE '%" + profesor.getSifra() + "%'");
                }
                if (!Optional.ofNullable(profesor.getIme()).map(String::isBlank).orElse(true)) {
                    sqlUpit.append(" AND IME LIKE '%" + profesor.getIme() + "%'");
                }
                if (!Optional.ofNullable(profesor.getPrezime()).map(String::isBlank).orElse(true)) {
                    sqlUpit.append(" AND PREZIME LIKE '%" + profesor.getPrezime() + "%'");
                }
                if (!Optional.ofNullable(profesor.getTitula()).map(String::isBlank).orElse(true)) {
                    sqlUpit.append(" AND TITULA LIKE '%" + profesor.getTitula() + "%'");
                }
            }
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String sifra = resultSet.getString("sifra");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String titula = resultSet.getString("titula");
                Profesor noviProfesor = new Profesor.ProfesorBuilder(id, ime, prezime).saSifrom(sifra).saTitulom(titula).build();
                listaProfesora.add(noviProfesor);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaProfesora;
    }

    public static void spremiNovogProfesora(Profesor profesor) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO PROFESOR(ime, prezime, sifra, titula) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, profesor.getIme());
            preparedStatement.setString(2, profesor.getPrezime());
            preparedStatement.setString(3, profesor.getSifra());
            preparedStatement.setString(4, profesor.getTitula());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static List<Student> dohvatiStudentePremaKriterijima(Student student) throws BazaPodatakaException {
        List<Student> listaStudent = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {
            StringBuilder sqlUpit = new StringBuilder("SELECT * FROM STUDENT WHERE 1 = 1");
            if (!Optional.ofNullable(student).isEmpty()) {
                if (Optional.ofNullable(student).map(Student::getId).isPresent()) {
                    sqlUpit.append(" AND ID = " + student.getId());
                }
                if (!Optional.ofNullable(student.getIme()).map(String::isBlank).orElse(true)) {
                    sqlUpit.append(" AND IME LIKE '%" + student.getIme() + "%'");
                }
                if (!Optional.ofNullable(student.getPrezime()).map(String::isBlank).orElse(true)) {
                    sqlUpit.append(" AND PREZIME LIKE '%" + student.getPrezime() + "%'");
                }
                if (!Optional.ofNullable(student.getJmbag()).map(String::isBlank).orElse(true)) {
                    sqlUpit.append(" AND JMBAG LIKE '%" + student.getJmbag() + "%'");
                }
                if (Optional.ofNullable(student).map(Student::getDatumRodenja).isPresent()) {
                    sqlUpit.append(" AND DATUM_RODENJA LIKE '%" + student.getDatumRodenja().toString() + "%'");
                }
            }
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String jmbag = resultSet.getString("jmbag");
                LocalDate datumRodenja = LocalDate.parse(resultSet.getString("datum_rodjenja"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Student noviStudent = new Student(id, ime, prezime, jmbag, datumRodenja);
                listaStudent.add(noviStudent);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaStudent;
    }

    public static void spremiNovogStudenta(Student student) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO STUDENT(ime, prezime, jmbag, datum_rodjenja) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, student.getIme());
            preparedStatement.setString(2, student.getPrezime());
            preparedStatement.setString(3, student.getJmbag());
            preparedStatement.setString(4, student.getDatumRodenja().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static List<Predmet> dohvatiPredmetePremaKriterijima(Predmet predmet) throws BazaPodatakaException {
        List<Predmet> listaPredmet = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {
            StringBuilder sqlUpit = new StringBuilder("SELECT * FROM PREDMET WHERE 1 = 1");
            if (!Optional.ofNullable(predmet).isEmpty()) {
                if (Optional.ofNullable(predmet).map(Predmet::getId).isPresent()) {
                    sqlUpit.append(" AND ID = " + predmet.getId());
                }
                if (!Optional.ofNullable(predmet.getSifra()).map(String::isBlank).orElse(true)) {
                    sqlUpit.append(" AND SIFRA LIKE '%" + predmet.getSifra() + "%'");
                }
                if (!Optional.ofNullable(predmet.getNaziv()).map(String::isBlank).orElse(true)) {
                    sqlUpit.append(" AND NAZIV LIKE '%" + predmet.getNaziv() + "%'");
                }
                if (Optional.ofNullable(predmet).map(Predmet::getBrojEctsBodova).isPresent()) {
                    sqlUpit.append(" AND BROJ_ECTS_BODOVA LIKE '%" + predmet.getBrojEctsBodova() + "%'");
                }
                if (Optional.ofNullable(predmet).map(Predmet::getNositelj).isPresent()) {
                    sqlUpit.append(" AND PROFESOR_ID LIKE '%" + predmet.getNositelj().getId() + "%'");
                }
            }
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String sifra = resultSet.getString("sifra");
                String naziv = resultSet.getString("naziv");
                Integer brojEctsBodova = resultSet.getInt("broj_ects_bodova");
                Long nositeljId = resultSet.getLong("profesor_id");
                Predmet noviPredmet = new Predmet(id, sifra, naziv, brojEctsBodova, getObjectById(nositeljId, dohvatiProfesore()));
                listaPredmet.add(noviPredmet);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaPredmet;
    }

    public static void spremiNoviPredmet(Predmet predmet) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO PREDMET(sifra, naziv, broj_ects_bodova, profesor_id) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, predmet.getSifra());
            preparedStatement.setString(2, predmet.getNaziv());
            preparedStatement.setInt(3, predmet.getBrojEctsBodova());
            preparedStatement.setLong(4, predmet.getNositelj().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static List<Ispit> dohvatiIspitePremaKriterijima(Ispit ispit) throws BazaPodatakaException {
        List<Ispit> listaIspit = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {
            StringBuilder sqlUpit = new StringBuilder("SELECT * FROM ISPIT WHERE 1 = 1");
            if (!Optional.ofNullable(ispit).isEmpty()) {
                if (Optional.ofNullable(ispit).map(Ispit::getId).isPresent()) {
                    sqlUpit.append(" AND ID = " + ispit.getId());
                }
                if (Optional.ofNullable(ispit).map(Ispit::getPredmet).isPresent()) {
                    sqlUpit.append(" AND PREDMET_ID LIKE '%" + ispit.getPredmet().getId() + "%'");
                }
                if (Optional.ofNullable(ispit).map(Ispit::getStudent).isPresent()) {
                    sqlUpit.append(" AND STUDENT_ID LIKE '%" + ispit.getStudent().getId() + "%'");
                }
                if (Optional.ofNullable(ispit).map(Ispit::getOcjena).isPresent()) {
                    sqlUpit.append(" AND OCJENA LIKE '%" + ispit.getOcjena().getNumericki() + "%'");
                }
                if (Optional.ofNullable(ispit).map(Ispit::getDatumIVrijeme).isPresent()) {
                    sqlUpit.append(" AND DATUM_I_VRIJEME LIKE '%" + ispit.getDatumIVrijeme().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "%'");
                }
            }
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long predmetId = resultSet.getLong("predmet_id");
                Long studentId = resultSet.getLong("student_id");
                Integer ocjena = resultSet.getInt("ocjena");
                String datumIVrijeme = resultSet.getString("datum_i_vrijeme");
                Ispit noviIspit = new Ispit(id, getObjectById(predmetId, dohvatiPredmete()), getObjectById(studentId, dohvatiStudente()), intToOcjena(ocjena), LocalDateTime.parse(datumIVrijeme, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), new Dvorana("Naziv dvorane", "Zgrada dvorane"));
                listaIspit.add(noviIspit);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaIspit;
    }

    public static void spremiNoviIspit(Ispit ispit) throws BazaPodatakaException {
        try (Connection veza = spajanjeNaBazu()) {
            PreparedStatement preparedStatement = veza.prepareStatement("INSERT INTO ISPIT(predmet_id, student_id, ocjena, datum_i_vrijeme) VALUES (?, ?, ?, ?)");
            preparedStatement.setLong(1, ispit.getPredmet().getId());
            preparedStatement.setLong(2, ispit.getStudent().getId());
            preparedStatement.setInt(3, ispit.getOcjena().getNumericki());
            preparedStatement.setString(4, ispit.getDatumIVrijeme().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static List<Predmet> dohvatiPredmete() throws BazaPodatakaException {
        List<Predmet> listaPredmet = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {
            StringBuilder sqlUpit = new StringBuilder("SELECT * FROM PREDMET ");
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String sifra = resultSet.getString("sifra");
                String naziv = resultSet.getString("naziv");
                Integer brojEctsBodova = resultSet.getInt("broj_ects_bodova");
                Long nositeljId = resultSet.getLong("profesor_id");
                Predmet noviPredmet = new Predmet(id, sifra, naziv, brojEctsBodova, getObjectById(nositeljId, dohvatiProfesore()));
                listaPredmet.add(noviPredmet);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaPredmet;
    }

    public static List<Student> dohvatiStudente() throws BazaPodatakaException {
        List<Student> listaStudent = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {
            StringBuilder sqlUpit = new StringBuilder("SELECT * FROM STUDENT ");
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String jmbag = resultSet.getString("jmbag");
                LocalDate datumRodenja = LocalDate.parse(resultSet.getString("datum_rodjenja"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Student noviStudent = new Student(id, ime, prezime, jmbag, datumRodenja);
                listaStudent.add(noviStudent);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaStudent;
    }

    public static List<Profesor> dohvatiProfesore() throws BazaPodatakaException {
        List<Profesor> listaProfesora = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {
            StringBuilder sqlUpit = new StringBuilder("SELECT * FROM PROFESOR ");
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String sifra = resultSet.getString("sifra");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String titula = resultSet.getString("titula");
                Profesor noviProfesor = new Profesor.ProfesorBuilder(id, ime, prezime).saSifrom(sifra).saTitulom(titula).build();
                listaProfesora.add(noviProfesor);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaProfesora;
    }

    public static List<Ispit> dohvatiIspite() throws BazaPodatakaException {
        List<Ispit> listaIspit = new ArrayList<>();
        try (Connection veza = spajanjeNaBazu()) {
            StringBuilder sqlUpit = new StringBuilder("SELECT * FROM ISPIT ");
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long predmetId = resultSet.getLong("predmet_id");
                Long studentId = resultSet.getLong("student_id");
                Integer ocjena = resultSet.getInt("ocjena");
                String datumIVrijeme = resultSet.getString("datum_i_vrijeme");
                Ispit noviIspit = new Ispit(id, getObjectById(predmetId, dohvatiPredmete()), getObjectById(studentId, dohvatiStudente()), intToOcjena(ocjena), LocalDateTime.parse(datumIVrijeme, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), new Dvorana("Naziv dvorane", "Zgrada dvorane"));
                listaIspit.add(noviIspit);
            }
        } catch (SQLException | IOException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
        return listaIspit;
    }

    public static Connection spajanjeNaBazu() throws SQLException, IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("dat//database.properties"));

        String urlBaze = properties.getProperty("urlBaze");
        String korisnickoIme = properties.getProperty("korisnickoIme");
        String lozinka = properties.getProperty("lozinka");

        return DriverManager.getConnection(urlBaze, korisnickoIme, lozinka);
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
