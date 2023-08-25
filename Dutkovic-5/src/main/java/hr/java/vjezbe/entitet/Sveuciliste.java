package hr.java.vjezbe.entitet;

import java.util.ArrayList;
import java.util.List;

public abstract class Sveuciliste <T> extends ObrazovnaUstanova{

    List<T> lista = new ArrayList<>();

    public void dodajObrazovnuUstanovu(T o){
        this.lista.add(o);
    }

    public T dohvatiObrazovnuUstanovu(int indeks){
        return lista.get(indeks);
    }

    public List<T> getObrazovneUstanove() {
        return lista;
    }

    public Sveuciliste(String nazivUstanove, List<Predmet> predmeti, List<Profesor> profesori, List<Student> studenti, List<Ispit> ispiti, List<T> lista) {
        super(nazivUstanove, predmeti, profesori, studenti, ispiti);
        this.lista = lista;
    }
}
