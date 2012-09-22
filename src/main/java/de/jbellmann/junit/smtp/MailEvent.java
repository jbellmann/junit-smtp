package de.jbellmann.junit.smtp;

import javax.mail.internet.MimeMessage;

/**
 * Notifies {@link MailEventListener} about incoming Mails.
 * 
 * @author Joerg Bellmann
 * 
 */
public interface MailEvent {

    String getFrom();

    String getRecipient();

    MimeMessage getMimeMessage();

}
