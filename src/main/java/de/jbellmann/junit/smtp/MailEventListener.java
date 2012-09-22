package de.jbellmann.junit.smtp;

/**
 * Defines an Listener to handle incoming {@link MailEvent}s.
 * 
 * @author Joerg Bellmann
 *
 */
public interface MailEventListener {

    void onMailEvent(MailEvent mailEvent);

}
