/*
 * Copyright 2013-2022 the original author or authors.
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

package org.cloudfoundry.tomcat.logging.access;

import org.apache.catalina.Container;
import org.apache.catalina.LifecycleException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public final class CloudFoundryAccessLoggingValveTest {

    private final CloudFoundryAccessLoggingValve valve = new CloudFoundryAccessLoggingValve();

    private final PrintStream testStream = mock(PrintStream.class);

    @Test
    public void log() throws IOException, LifecycleException {
        Path tmpDir = Files.createTempDirectory("tomcat-access-logging-support-test");
        Path loggingDirectory = Paths.get(tmpDir.toString(), "do-not-create-this-directory");
        valve.setDirectory(loggingDirectory.toString());
        valve.setContainer(mock(Container.class));

        PrintStream stdout = System.out;
        try {
            System.setOut(testStream);

            valve.start();
            valve.log("Testing");
            verify(testStream).println("Testing");

        } finally {
            System.setOut(stdout);
            valve.stop();
        }

        assertFalse(loggingDirectory.toFile().exists(), "should not have created anything on disk");
    }

}
