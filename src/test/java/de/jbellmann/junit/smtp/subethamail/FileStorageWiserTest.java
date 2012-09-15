package de.jbellmann.junit.smtp.subethamail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.TooMuchDataException;

import de.jbellmann.junit.smtp.subethamail.FileStorageWiser;

/**
 * 
 * 
 * @author Joerg Bellmann
 *
 */
public class FileStorageWiserTest {

    private static final Logger log = LoggerFactory.getLogger(FileStorageWiserTest.class);

    private File mailStorageDirectory;

    private FileStorageWiser wiser;

    @Before
    public void setUp() throws IOException {
        File tempFileDirectory = File.createTempFile("filestorageWiser_", ".tmp").getParentFile();
        mailStorageDirectory = new File(tempFileDirectory, UUID.randomUUID().toString());
        mailStorageDirectory.mkdirs();
        log.info("created directory for mail-storage : {}", mailStorageDirectory.getAbsolutePath());
        wiser = new FileStorageWiser(mailStorageDirectory);
        wiser.setPort(25000);
        wiser.start();
    }

    @After
    public void tearDown() {
        if (wiser != null) {
            wiser.stop();
            wiser = null;
        }
    }

    @Test
    public void testFileStorageWiser() throws TooMuchDataException, IOException {
        wiser.deliver("tester@organisation.com", "manager@organisation.com", new ByteArrayInputStream("Mail-Message to Manager".getBytes()));
    }

}
