package de.jbellmann.junit.smtp;

import javax.mail.internet.MimeMessage;

/**
 * Defines an Listener to handle incoming {@link MimeMessage}s.
 * 
 * @author Joerg Bellmann
 *
 */
public interface MessageListener {
    
    public void onMessage(MimeMessage mimeMessage);

}
