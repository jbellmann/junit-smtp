package de.jbellmann.junit.smtp.subethamail;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.TooMuchDataException;
import org.subethamail.wiser.Wiser;

/**
 * 
 * @author Joerg Bellmann
 *
 */
public class FileStorageWiser extends Wiser {

    private static final Logger log = LoggerFactory.getLogger(FileStorageWiser.class);
    
    private final File storageDirectory;
    private final String uuid;
    private final AtomicInteger counter = new AtomicInteger();
    
    public FileStorageWiser(File storageDirectory){
        super();
        this.uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        this.storageDirectory = storageDirectory;
        log.info("Mail-Storage-UUID {}", uuid);
    }

    @Override
    public void deliver(String from, String recipient, InputStream data) throws TooMuchDataException, IOException {
        log.debug("Delivering mail from " + from + " to " + recipient);

        String fileName = new StringBuilder(from).append("_to_").append(recipient).append("_").append(uuid).append("_").append(counter.incrementAndGet()).append(".txt").toString();
        File outputFile = new File(storageDirectory, fileName);
        outputFile.createNewFile();
        IOUtil.copy(new BufferedInputStream(data), new BufferedWriter(new FileWriter(outputFile)), 256);
    }

}
