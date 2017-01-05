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

package com.gopivotal.cloudfoundry.tomcat.logging;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.ConsoleHandler;
import java.util.logging.StreamHandler;

/**
 * An extension of {@link ConsoleHandler} that outputs to {@link System#out}.
 */
public final class CloudFoundryConsoleHandler extends ConsoleHandler {

    /**
     * Creates a new instance setting the formatter and output stream
     */
    public CloudFoundryConsoleHandler() throws NoSuchFieldException, IllegalAccessException {
        super();
        saveStdErrFromIrresponsibleClose(this);
        setFormatter(new CloudFoundryFormatter());
        setOutputStream(System.out);
    }

    private static void saveStdErrFromIrresponsibleClose(CloudFoundryConsoleHandler target) throws
            NoSuchFieldException, IllegalAccessException {
        final Field output = StreamHandler.class.getDeclaredField("output");
        final Field writer = StreamHandler.class.getDeclaredField("writer");

        AccessController.doPrivileged(new PrivilegedAction<Void>() {

            @Override
            public Void run() {
                output.setAccessible(true);
                writer.setAccessible(true);
                return null;
            }
        });

        output.set(target, null);
        writer.set(target, null);
    }

}
