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

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.core.StandardContext;
import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link ApplicationStartupFailureDetectingLifecycleListener}
 *
 */
public class ApplicationStartupFailureDetectingLifecycleListenerTests {

	private static final Object TEST_DATA = new Object();
	
	LifecycleListener listener;
	Container mockContainer;
	StandardContext mockStandardContext;
	LifecycleEvent event;
	Runtime runtime;
	
	@Before
	public void setup() {
		this.mockContainer = mock(Container.class);
		this.mockStandardContext = mock(StandardContext.class);

		Container[] mockStandardContexts = new Container[1];
		mockStandardContexts[0] = this.mockStandardContext;
		when(this.mockStandardContext.getDisplayName()).thenReturn("test app");
		when(this.mockContainer.findChildren()).thenReturn(mockStandardContexts);
		this.event = new LifecycleEvent(this.mockContainer, Lifecycle.AFTER_START_EVENT, TEST_DATA);
		this.runtime = mock(Runtime.class);
		this.listener = new ApplicationStartupFailureDetectingLifecycleListener(this.runtime);
	}

	@Test
	public void testRunningApplicationInTomcat7() {
		when(this.mockStandardContext.getState()).thenReturn(LifecycleState.STARTED);
		this.listener.lifecycleEvent(this.event);
		verify(this.mockContainer).findChildren();
		verify(this.mockStandardContext).getState();
		verify(this.runtime, never()).halt(anyInt());
	}

	@Test
	public void testFailedApplicationInTomcat7() {
		when(this.mockStandardContext.getState()).thenReturn(LifecycleState.FAILED);
		this.listener.lifecycleEvent(this.event);
		verify(this.mockContainer).findChildren();
		verify(this.mockStandardContext).getState();
		verify(this.mockStandardContext).getDisplayName();
		verify(this.runtime).halt(404);
	}
	
	@Test
	public void testIrrelevantEvent() {
		when(this.mockStandardContext.getState()).thenReturn(LifecycleState.STARTED);
		this.listener.lifecycleEvent(new LifecycleEvent(this.mockContainer, Lifecycle.AFTER_DESTROY_EVENT, TEST_DATA));
		verify(this.mockContainer, never()).findChildren();
		verify(this.mockStandardContext, never()).getState();
		verify(this.runtime, never()).halt(anyInt());
	}

	@Test
	public void testGetStateException() {
		when(this.mockStandardContext.getState()).thenThrow(new RuntimeException());
		this.listener.lifecycleEvent(this.event);
		verify(this.mockContainer).findChildren();
		verify(this.mockStandardContext).getState();
		verify(this.runtime, never()).halt(anyInt());
	}

}
