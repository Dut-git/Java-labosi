package hr.java.vjezbe.entitet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sveuciliste <T extends ObrazovnaUstanova> implements Serializable {

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
}
