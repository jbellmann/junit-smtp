package de.jbellmann.junit.smtp;
import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.jbellmann.junit.smtp.CountingMimeMessageListener;
import de.jbellmann.junit.smtp.Smtp;

/**
 * Test for building an Smtp-Resource.
 * 
 * @author Joerg Bellmann
 *
 */
public class SmtpBuilderTest {
    
    @Rule
    public TemporaryFolder tempfolderFactory = new TemporaryFolder();
    
    @Test
    public void testSimplestBuilder(){
        Smtp smtp = Smtp.createDefault();
        Assert.assertNotNull(smtp);
    }
    
    @Test
    public void testBuilderWithDumpDirectory() throws IOException{
        File tempDirectory = tempfolderFactory.newFolder();
        tempDirectory.mkdirs();
        Smtp smtp = Smtp.createDefault().dumpTo(tempDirectory);
        Assert.assertNotNull(smtp);
    }
    
    @Test
    public void testBuilderWithPort(){
        Smtp smtp = Smtp.onPort(2600);
        Assert.assertNotNull(smtp);
    }
    
    @Test
    public void testBuilderWithAddingMimeMessageListener(){
        Smtp smtp = Smtp.createDefault().addMimeMessageListener(new CountingMimeMessageListener());
        Assert.assertNotNull(smtp);
    }

}
