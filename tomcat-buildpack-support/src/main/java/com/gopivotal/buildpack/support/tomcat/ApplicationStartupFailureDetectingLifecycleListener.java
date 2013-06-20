/*
 * Copyright 2013 the original author or authors.
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

package com.gopivotal.buildpack.support.tomcat;

import java.lang.reflect.Method;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.core.StandardContext;

/**
 * This LifecycleListener shuts down Tomcat 6 or 7 if an application fails to start.
 * 
 * In Cloud Foundry, which supports only a single host with a single context, the listener
 * should be added to the Host element.
 * 
 */
public class ApplicationStartupFailureDetectingLifecycleListener implements LifecycleListener {

	/**
	 * @inheritDoc
	 */
	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		if (event.getType() == Lifecycle.AFTER_START_EVENT) {
			Container host = (Container) event.getLifecycle();
			Container[] contexts = host.findChildren();
			for (Container container : contexts) {
				if (container instanceof StandardContext) {
					checkContext((StandardContext) container);
				}
			}
		}
	}

	private void checkContext(StandardContext context) {
		Object state = null;
		try {
			Method getStateMethod = StandardContext.class.getMethod("getState");
			state = getStateMethod.invoke(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (tomcat6ApplicationNotRunning(state)	|| tomcat7ApplicationNotRunning(state)) {
			String message = "Error: Application " + context.getDisplayName() +
					" failed (state = "	+ state + "): shutting down Tomcat";
			System.err.println(message);
			throw new IllegalStateException(message);
		}
	}

	private boolean tomcat6ApplicationNotRunning(Object state) {
		return state instanceof Integer && (Integer) state != 1;
	}

	private boolean tomcat7ApplicationNotRunning(Object state) {
		// Avoid class loading errors in Tomcat 6 by checking for Integer first.
		return !(state instanceof Integer) && state instanceof LifecycleState
				&& (LifecycleState) state != LifecycleState.STARTED;
	}

}