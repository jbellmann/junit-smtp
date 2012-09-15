package de.jbellmann.junit.smtp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.rules.ExternalResource;
import org.subethamail.smtp.TooMuchDataException;

import de.jbellmann.junit.smtp.subethamail.FileStorageWiser;

/**
 * 
 * 
 * 
 * @author Joerg Bellmann
 *
 */
public class Smtp extends ExternalResource {

    public static final int DEFAULT_PORT = 2500;

    private int port = DEFAULT_PORT;
    private File targetDirectory;
    private FileStorageWiser wiser;

    public Smtp(int port, File targetDirectory) {
        assertPort(port);
        assertFile(targetDirectory);
        this.port = port;
        this.targetDirectory = targetDirectory;
    }

    public Smtp(File targetDirectory) {
        this(DEFAULT_PORT, targetDirectory);
    }

    public Smtp() {
        this(new File(System.getProperty("user.dir"), "wiserMails"));
    }
    
    public Smtp(int port){
        this(port, new File(System.getProperty("user.dir"), "wiserMails"));
    }

    private void assertFile(File targetDirectory) {
        if (targetDirectory == null) {
            throw new RuntimeException("targetDirectory should not be null");
        }
    }

    private void assertPort(int port) {
        if (port < 1 || port > 650000) {
            throw new RuntimeException("port should betwenn 0 an 650000");
        }
    }

    @Override
    protected void before() throws Throwable {
        createMailStore();
        wiser = new FileStorageWiser(targetDirectory);
        wiser.setPort(port);
        wiser.start();
    }

    @Override
    protected void after() {
        wiser.stop();
        wiser = null;
    }

    protected void createMailStore() {
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }
    }
    
    /**
     * This method is just for testing mail delivery.
     * 
     * 
     * @param from the from address
     * @param recipient the recipient address
     * @param data the raw data
     * @throws SmtpRuntimeException 
     */
    public void deliver(String from, String recipient, InputStream data ) throws SmtpRuntimeException {
        if(wiser != null){
            try {
                wiser.deliver(from, recipient, data);
            } catch (TooMuchDataException e) {
                throw new SmtpRuntimeException(e);
            } catch (IOException e) {
                throw new SmtpRuntimeException(e);
            }
        }
    }

}
