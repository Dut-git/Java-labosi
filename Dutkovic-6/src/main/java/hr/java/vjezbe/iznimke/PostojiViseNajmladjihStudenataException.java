package hr.java.vjezbe.iznimke;

import java.io.Serial;

/**
 * Klasa za iznimku koja se baca kada ima vise studenata koji se kvalificiraju za nagradu rektora
 */
public class PostojiViseNajmladjihStudenataException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1324053465120359404L;

    public PostojiViseNajmladjihStudenataException() {
    }

    public PostojiViseNajmladjihStudenataException(String message) {
        super(message);
    }

    public PostojiViseNajmladjihStudenataException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostojiViseNajmladjihStudenataException(Throwable cause) {
        super(cause);
    }

    public PostojiViseNajmladjihStudenataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
