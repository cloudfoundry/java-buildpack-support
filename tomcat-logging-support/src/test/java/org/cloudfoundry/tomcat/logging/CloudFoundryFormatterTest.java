/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.tomcat.logging;

import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class CloudFoundryFormatterTest {

    private final CloudFoundryFormatter formatter = new CloudFoundryFormatter();

    @Test
    public void format() {
        LogRecord record = new LogRecord(Level.INFO, "Test Message");
        record.setLoggerName("short.logger");

        assertEquals("[CONTAINER] short.logger                                       INFO    Test Message\n",
                this.formatter.format(record));
    }

    @Test
    public void truncateLogger() {
        LogRecord record = new LogRecord(Level.INFO, "Test Message");
        record.setLoggerName("fantastic.extra.special.awesome.super.really.long.logger");

        assertEquals("[CONTAINER] tic.extra.special.awesome.super.really.long.logger INFO    Test Message\n",
                this.formatter.format(record));
    }

    @Test
    public void longLevel() {
        LogRecord record = new LogRecord(Level.WARNING, "Test Message");
        record.setLoggerName("short.logger");

        assertEquals("[CONTAINER] short.logger                                       WARNING Test Message\n",
                this.formatter.format(record));
    }

    @Test
    public void throwable() {
        LogRecord record = new LogRecord(Level.INFO, "Test Message");
        record.setLoggerName("short.logger");
        record.setThrown(new Throwable("Throwable Message"));

        assertTrue(this.formatter.format(record).split("\n").length > 1);
    }

    @Test
    public void parameter() {
        LogRecord record = new LogRecord(Level.INFO, "Test Message {0}");
        record.setLoggerName("short.logger");
        record.setParameters(new String[]{"parameter-1"});

        assertEquals("[CONTAINER] short.logger                                       INFO    Test Message " +
                "parameter-1\n", this.formatter.format(record));
    }

    @Test
    public void nullLoggerName() {
    	LogRecord record = new LogRecord(Level.INFO, "Message OK");

    	assertEquals("[CONTAINER] null                                               INFO    Message OK\n",
    			this.formatter.format(record));
    }

}
