package de.jbellmann.junit.smtp;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Joerg Bellmann
 *
 */
public class MimeMessageListenerTest {
    
    @Test
    public void testAddingMimeMessageListener() throws InterruptedException{
        CountingMimeMessageListener listener = new CountingMimeMessageListener();
        Smtp smtp = Smtp.createDefault().addMimeMessageListener(listener);
        smtp.deliverTestMail();
        Thread.sleep(2000);
        Assert.assertEquals(1, listener.getMessageCount());
    }
}