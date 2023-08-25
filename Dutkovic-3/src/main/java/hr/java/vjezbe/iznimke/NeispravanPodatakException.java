package hr.java.vjezbe.iznimke;

import java.io.Serial;

/**
 * Klasa za iznimku koja se baca kada korisnik neispravno unese podatak
 */
public class NeispravanPodatakException extends Exception{

    @Serial
    private static final long serialVersionUID = -1725154265284093140L;

    public NeispravanPodatakException() {
    }

    public NeispravanPodatakException(String message) {
        super(message);
    }

    public NeispravanPodatakException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeispravanPodatakException(Throwable cause) {
        super(cause);
    }

    public NeispravanPodatakException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
