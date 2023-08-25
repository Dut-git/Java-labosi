package hr.java.vjezbe.entitet;

import java.io.Serializable;

/**
 * Sluzi za kreiranje dvorane u kojoj se pisao ispit
 */

public record Dvorana(String naziv, String zgrada) implements Serializable {
}
