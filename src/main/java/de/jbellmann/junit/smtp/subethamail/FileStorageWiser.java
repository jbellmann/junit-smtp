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
import org.subethamail.wiser.Wiser;

/**
 * 
 * @author Joerg Bellmann
 *
 */
public class FileStorageWiser extends Wiser {

    private static final Logger LOG = LoggerFactory.getLogger(FileStorageWiser.class);
    private static final int DEFAULT_BUFFER_SIZE = 256;
    private static final int EIGHT = 8;

    private final File storageDirectory;
    private final String uuid;
    private final AtomicInteger counter = new AtomicInteger();

    public FileStorageWiser(File storageDirectory) {
        super();
        this.uuid = UUID.randomUUID().toString().replace("-", "").substring(0, EIGHT);
        this.storageDirectory = storageDirectory;
        LOG.info("Mail-Storage-UUID {}", uuid);
    }

    @Override
    public void deliver(String from, String recipient, InputStream data) throws IOException {
        LOG.debug("Delivering mail from " + from + " to " + recipient);

        String fileName = new StringBuilder(from).append("_to_").append(recipient).append("_").append(uuid).append("_").append(counter.incrementAndGet())
                .append(".txt").toString();
        File outputFile = new File(storageDirectory, fileName);
        outputFile.createNewFile();
        IOUtil.copy(new BufferedInputStream(data), new BufferedWriter(new FileWriter(outputFile)), DEFAULT_BUFFER_SIZE);
    }

}
