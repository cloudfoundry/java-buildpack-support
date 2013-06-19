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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.core.StandardContext;
import org.junit.Test;

/**
 * Test {@link ApplicationStartupFailureDetectingLifecycleListener}
 *
 */
public class ApplicationStartupFailureDetectingLifecycleListenerTests {

	private static final Object TEST_DATA = new Object();

	@Test
	public void testFailedApplication() {
		LifecycleListener listener = new ApplicationStartupFailureDetectingLifecycleListener();
		
		Container mockContainer = mock(Container.class);
		Container[] mockStandardContexts = new Container[1];
		mockStandardContexts[0] = mock(StandardContext.class);
		
		when(mockContainer.findChildren()).thenReturn(mockStandardContexts);
		LifecycleEvent event = new LifecycleEvent(mockContainer, Lifecycle.AFTER_START_EVENT, TEST_DATA);
		listener.lifecycleEvent(event);
	}

}
