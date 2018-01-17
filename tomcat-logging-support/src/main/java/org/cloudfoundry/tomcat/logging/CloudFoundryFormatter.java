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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Tomcat logging formatter for running in Cloud Foundry
 */
public final class CloudFoundryFormatter extends Formatter {

    private static final Integer MAX_LENGTH = 50;

    private static final String FORMAT = String.format("[CONTAINER] %%-%ss %%-7s %%s%%s\n", MAX_LENGTH);

    @Override
    public String format(LogRecord record) {
        return String.format(FORMAT, getLogger(record), record.getLevel(), formatMessage(record), getException(record));
    }

    private String getException(LogRecord record) {
        StringWriter sw = new StringWriter();

        Throwable t = record.getThrown();
        if (t != null) {
            sw.append('\n');
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
        }

        return sw.toString();
    }

    private String getLogger(LogRecord record) {
        String loggerName = record.getLoggerName();

        if (loggerName != null && loggerName.length() > MAX_LENGTH) {
            return loggerName.substring(loggerName.length() - MAX_LENGTH);
        }

        return loggerName;
    }
}
