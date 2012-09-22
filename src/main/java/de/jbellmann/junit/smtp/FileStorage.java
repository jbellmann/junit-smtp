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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.wiser.Wiser;

import com.google.common.io.ByteStreams;

/**
 * Dumps a Mail in a File.
 * 
 * @author Joerg Bellmann
 *
 */
class FileStorage extends Wiser {

    private static final Logger LOG = LoggerFactory.getLogger(FileStorage.class);
    private static final int DEFAULT_BUFFER_SIZE = 256;
    private static final int EIGHT = 8;

    private final File storageDirectory;
    private final String uuid;
    private final AtomicInteger messageCounter = new AtomicInteger();

    private List<MailEventListener> mailEventListenerList = new LinkedList<MailEventListener>();

    public FileStorage(File storageDirectory) {
        super();
        this.uuid = UUID.randomUUID().toString().replace("-", "").substring(0, EIGHT);
        this.storageDirectory = storageDirectory;
        LOG.info("Mail-Storage-UUID {}", uuid);
    }

    @Override
    public void deliver(String from, String recipient, InputStream data) throws IOException {
        LOG.debug("Delivering mail from " + from + " to " + recipient);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(data);
        ByteStreams.copy(bis, baos);
        byte[] dataArray = baos.toByteArray();

        writeToStorage(from, recipient, dataArray);
        // 
        notifyListener(from, recipient, dataArray);
    }

    protected void notifyListener(String from, String recipient, byte[] data) throws IOException {
        MailEvent event = new MailEventImpl(from, recipient, data);
        for (MailEventListener listener : mailEventListenerList) {
            listener.onMailEvent(event);
        }
    }

    protected void writeToStorage(String from, String recipient, byte[] data) throws IOException {
        String fileName = new StringBuilder(from).append("_to_")
                                                .append(recipient)
                                                .append("_")
                                                .append(uuid)
                                                .append("_")
                                                .append(messageCounter.incrementAndGet())
                                                .append(".txt").toString();
        File outputFile = new File(storageDirectory, fileName);
        outputFile.createNewFile();
        IOUtil.copy(new ByteArrayInputStream(data), new BufferedWriter(new FileWriter(outputFile)), DEFAULT_BUFFER_SIZE);
    }

    void addMailEventListener(MailEventListener mailEventListener) {
        mailEventListenerList.add(mailEventListener);
    }

    /**
     * {@link MailEvent}-Implementation.
     * 
     * @author Joerg Bellmann
     *
     */
    class MailEventImpl implements MailEvent {

        private final String from;
        private final String recipient;
        private final byte[] data;

        MailEventImpl(String from, String recipient, byte[] data) {
            this.from = from;
            this.recipient = recipient;
            this.data = data;
        }

        @Override
        public String getFrom() {
            return this.from;
        }

        @Override
        public String getRecipient() {
            return this.recipient;
        }

        @Override
        public MimeMessage getMimeMessage() {
            try {
                return new MimeMessage(getSession(), new ByteArrayInputStream(data));
            } catch (MessagingException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

    }

}
