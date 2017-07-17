/*
 * Copyright 2013-2017 the original author or authors.
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

package com.gopivotal.cloudfoundry.tomcat.logging.access;

import org.apache.catalina.valves.AccessLogValve;

import java.io.CharArrayWriter;

/**
 * An extension of {@link org.apache.catalina.valves.AccessLogValve} that logs to {@link System#out}.
 */
public final class CloudFoundryAccessLoggingValve extends AccessLogValve {

    /**
     * Log a message
     *
     * @param message the message to log
     */
    public void log(String message) {
        System.out.println(message);
    }

    public void log(CharArrayWriter message) {
        System.out.println(message);
    }

}
