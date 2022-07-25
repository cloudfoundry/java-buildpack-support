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

package org.cloudfoundry.tomcat.lifecycle;

import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class StandardStateParserTest {

    private final StandardStateParser stateParser = new StandardStateParser();

    @Test
    public void tomcat6Stopped() {
        assertFalse(this.stateParser.isRunning(0));
    }

    @Test
    public void tomcat6Started() {
        assertTrue(this.stateParser.isRunning(1));
    }

    @Test
    public void tomcat7Stopped() {
        assertFalse(this.stateParser.isRunning(LifecycleState.STARTING));
    }

    @Test
    public void tomcat7Started() {
        assertTrue(this.stateParser.isRunning(LifecycleState.STARTED));
    }

    @Test
    public void unknown() {
        assertFalse(this.stateParser.isRunning(""));
    }

}
