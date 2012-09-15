package de.jbellmann.junit.smtp;

/**
 * A RuntimeException to wrap exceptions depending on a specific library.
 * 
 * @author Joerg Bellmann
 *
 */
@SuppressWarnings("serial")
public class SmtpRuntimeException extends RuntimeException {

    public SmtpRuntimeException(Throwable cause) {
        super(cause);
    }

}
