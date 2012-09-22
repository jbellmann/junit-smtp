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

import org.junit.rules.ExternalResource;

/**
 * Simple Rule for starting and stopping an Smtp-Server.
 * <br/>
 * Example:
 * <pre style="code">
 * class MailTest {
 *
 *  {@literal @}Rule
 *  public Smtp smtp = Smtp.createDefault();
 *
 *
 *  public void testYourMailSender() {
 *      int port = smtp.getPort();  // 2500
 *      // send a mail here
 *  }
 *
 * }
 * </pre>
 * 
 * @author Joerg Bellmann
 */
public class Smtp extends ExternalResource {

    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;

    public static final int DEFAULT_PORT = 2500;

    private int port = DEFAULT_PORT;
    private File targetDirectory;
    private FileStorage wiser;

    private Smtp(int port, File targetDirectory) {
        assertPort(port);
        assertFile(targetDirectory);
        this.port = port;
        this.targetDirectory = targetDirectory;
        createMailStore();
        wiser = new FileStorage(targetDirectory);
        wiser.setPort(port);
    }

    private Smtp(File targetDirectory) {
        this(DEFAULT_PORT, targetDirectory);
    }

    private Smtp() {
        this(new File(System.getProperty("user.dir"), "testMails"));
    }

    public Smtp(int port) {
        this(port, new File(System.getProperty("user.dir"), "testMails"));
    }

    private static void assertFile(File targetDirectory) {
        if (targetDirectory == null) {
            throw new RuntimeException("targetDirectory should not be null");
        }
    }

    private static void assertPort(int port) {
        if (port < MIN_PORT || port > MAX_PORT) {
            throw new RuntimeException("port should betwenn " + MIN_PORT + " and " + MAX_PORT);
        }
    }

    @Override
    protected void before() throws Throwable {
        if (wiser == null) {
            throw new NullPointerException("Wiser is null. We can not start anything.");
        }
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
    public void deliverTestMail() {
        if (wiser != null) {
            try {
                wiser.deliver("smtp.rule.test.from@test.de", "smtp.rule.test.recipient@test.de", new ByteArrayInputStream(
                        "This message was send by the Smtp itself as a test.".getBytes()));
            } catch (Exception e) {
                throw new SmtpRuntimeException(e);
            }
        }
    }

    /**
     * Simplest way to get an Smtp-Resource with defaults.
     * 
     * @return
     */
    public static Smtp createDefault() {
        return new Smtp();
    }

    /**
     * Create an Smtp-Resource and specify the directory for message dumps.
     * 
     * @param dumpDirectory
     * @return
     */
    public Smtp dumpTo(File dumpDirectory) {
        return new Smtp(port, dumpDirectory);
    }

    public static Smtp onPort(int port) {
        assertPort(port);
        return new Smtp(port);
    }

    public Smtp addMimeMessageListener(MailEventListener mailEventListener) {
        if (mailEventListener == null) {
            throw new RuntimeException("The Listener to add should not be null.");
        }
        this.wiser.addMailEventListener(mailEventListener);
        return this;
    }

    public int getPort() {
        return port;
    }
}
