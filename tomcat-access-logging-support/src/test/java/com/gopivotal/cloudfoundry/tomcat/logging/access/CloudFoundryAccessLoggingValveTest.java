/*
 * Copyright 2013-2014 the original author or authors.
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

import org.junit.Test;

import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test {@link com.gopivotal.cloudfoundry.tomcat.logging.access.CloudFoundryAccessLoggingValve}
 */
public final class CloudFoundryAccessLoggingValveTest {

    private final CloudFoundryAccessLoggingValve valve = new CloudFoundryAccessLoggingValve();

    private final PrintStream testStream = mock(PrintStream.class);

    @Test
    public void log() {
        PrintStream stdout = System.out;
        System.setOut(testStream);

        valve.log("Testing");
        verify(testStream).println("Testing");

        System.setOut(stdout);
    }

}
