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

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.core.StandardContext;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * This LifecycleListener shuts down Tomcat 6 or 7 if an application fails to start.
 * 
 * In Cloud Foundry, which supports only a single host with a single context, the listener should be added to the Host
 * element.
 */
public final class ApplicationStartupFailureDetectingLifecycleListener implements LifecycleListener {

    private static final int EXIT_CODE = 404;

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Runtime runtime;

    private final StateParser stateParser = new StandardStateParser();

    /**
     * Construct the listener with the system {@link Runtime}.
     */
    public ApplicationStartupFailureDetectingLifecycleListener() {
        this(Runtime.getRuntime());
    }

    /**
     * Construct the listener with the specified {@link Runtime}. This method is intended for use only in testing.
     *
     * @param runtime the {@link Runtime} to be used to halt Tomcat
     */
    ApplicationStartupFailureDetectingLifecycleListener(Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        if (event.getType() == Lifecycle.AFTER_START_EVENT) {
            Container lifecycle = (Container) event.getLifecycle();
            for (Container child : lifecycle.findChildren()) {
                if (child instanceof StandardContext) {
                    checkContext((StandardContext) child);
                }
            }
        }
    }

    private void checkContext(StandardContext context) {
        Object state = getState(context);

        if (!this.stateParser.isRunning(state)) {
            String applicationName = getApplicationName(context);

            this.logger.severe(String.format("Error: Application '%s' failed (state = %s): see Tomcat's logs for " +
                    "details. Halting Tomcat.", applicationName, state));

            System.err.flush();
            System.out.flush();
            this.runtime.halt(EXIT_CODE);
        }
    }

    private String getApplicationName(StandardContext context) {
        String displayName = context.getDisplayName();

        if (displayName == null) {
            return context.getName();
        }

        return displayName;
    }

    private Object getState(StandardContext context) {
        try {
            Method getStateMethod = StandardContext.class.getMethod("getState");
            return getStateMethod.invoke(context);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
