package hr.java.vjezbe.iznimke;

import java.io.Serial;

/**
 * Klasa za iznimku koja se baca kada student ima negativnu ocjenu pri odredivanju prosjeka
 */
public class NemoguceOdreditiProsjekStudentaException extends Exception{

    @Serial
    private static final long serialVersionUID = 7613445867888191836L;

    public NemoguceOdreditiProsjekStudentaException() {
    }

    public NemoguceOdreditiProsjekStudentaException(String message) {
        super(message);
    }

    public NemoguceOdreditiProsjekStudentaException(String message, Throwable cause) {
        super(message, cause);
    }

    public NemoguceOdreditiProsjekStudentaException(Throwable cause) {
        super(cause);
    }

    public NemoguceOdreditiProsjekStudentaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    

}
