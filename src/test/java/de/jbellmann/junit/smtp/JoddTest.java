package de.jbellmann.junit.smtp;

import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;

import org.junit.Rule;
import org.junit.Test;

/**
 * Tests sending mails with the JODD-Framework.
 * 
 * http://jodd.org/
 * 
 * @author jbellmann
 *
 */
public class JoddTest {
    
    @Rule
    public Smtp smtp = Smtp.createDefault();
    
    @Test
    public void sendMailWithJodd() throws InterruptedException{
        Email email = Email.create()
                .from("joddTestFrom@jodd.org").to("joddTestRecipient@jodd.org")
                .subject("Hello!")
                .addText("A plain text message...");
        
        // this is not the server, it is as sessionfactory-abstraction
        SmtpServer smtpServer = new SmtpServer("localhost", smtp.getPort());
        SendMailSession session = smtpServer.createSession();
        session.open();
        session.sendMail(email);
        session.close();
        Thread.sleep(500);
    }

}
