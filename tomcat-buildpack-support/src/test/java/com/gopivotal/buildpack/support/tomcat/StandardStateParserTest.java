package com.gopivotal.buildpack.support.tomcat;

import org.apache.catalina.LifecycleState;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
