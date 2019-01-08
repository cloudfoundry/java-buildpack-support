/*
 * Copyright 2013-2019 the original author or authors.
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class CloudFoundryConsoleHandlerTest {

    private final PrintStream mockErr = mock(PrintStream.class);

    private PrintStream savedErr = System.err;

    @Before
    public void before() {
        this.savedErr = System.err;
    }

    @After
    public void after() {
        System.setErr(this.savedErr);
    }

    @Test
    public void test() {
        System.setErr(this.mockErr);
        new CloudFoundryConsoleHandler();
        verify(this.mockErr, times(0)).close();
    }

}
