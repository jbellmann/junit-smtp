/**
 * Copyright (C) 2010-2012 Joerg Bellmann <joerg.bellmann@googlemail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.jbellmann.junit.smtp;

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

import de.jbellmann.junit.smtp.FileStorage;

/**
 * 
 * 
 * @author Joerg Bellmann
 *
 */
public class FileStorageWiserTest {

    private static final Logger log = LoggerFactory.getLogger(FileStorageWiserTest.class);

    private File mailStorageDirectory;

    private FileStorage wiser;

    @Before
    public void setUp() throws IOException {
        File tempFileDirectory = File.createTempFile("filestorageWiser_", ".tmp").getParentFile();
        mailStorageDirectory = new File(tempFileDirectory, UUID.randomUUID().toString());
        mailStorageDirectory.mkdirs();
        log.info("created directory for mail-storage : {}", mailStorageDirectory.getAbsolutePath());
        wiser = new FileStorage(mailStorageDirectory);
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
