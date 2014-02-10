package com.gopivotal.buildpack.support.tomcat;

/**
 * An abstraction for determining the current state of the Tomcat instance
 */
interface StateParser {

    /**
     * Whether the Tomcat instance is running
     *
     * @param state the state object to parse
     *
     * @return whether the Tomcat instance is running
     */
    boolean isRunning(Object state);
}
