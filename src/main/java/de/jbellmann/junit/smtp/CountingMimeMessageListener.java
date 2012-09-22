package de.jbellmann.junit.smtp;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link MailEventListener} that counts incoming {@link MailEvent}s.
 * 
 * @author Joerg Bellmann
 *
 */
public class CountingMimeMessageListener implements MailEventListener {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void onMailEvent(MailEvent mimeMessage) {
        counter.incrementAndGet();
    }

    public int getMessageCount() {
        return counter.get();
    }

}
