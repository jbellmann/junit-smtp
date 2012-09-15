package de.jbellmann.junit.smtp;

import java.io.ByteArrayInputStream;

import org.junit.Rule;
import org.junit.Test;

import de.jbellmann.junit.smtp.Smtp;

public class SmtpRuleTest {

    /**
     * Default port is 2500.
     */
    @Rule
    public Smtp smtp = new Smtp();

    @Test
    public void testSmtp() throws InterruptedException {
        Thread.sleep(1000);
        smtp.deliver("from@test.de", "recipient@test.de", new ByteArrayInputStream("Mail-Message to from@test.de".getBytes()));
        Thread.sleep(1000);
    }

}
