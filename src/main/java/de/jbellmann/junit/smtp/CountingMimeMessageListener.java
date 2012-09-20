package de.jbellmann.junit.smtp;

import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.internet.MimeMessage;

/**
 * {@link MessageListener} that count {@link MimeMessage}s.
 * 
 * @author Joerg Bellmann
 *
 */
public class CountingMimeMessageListener implements MessageListener {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void onMessage(MimeMessage mimeMessage) {
        counter.incrementAndGet();
    }

    public int getMessageCount() {
        return counter.get();
    }

}
