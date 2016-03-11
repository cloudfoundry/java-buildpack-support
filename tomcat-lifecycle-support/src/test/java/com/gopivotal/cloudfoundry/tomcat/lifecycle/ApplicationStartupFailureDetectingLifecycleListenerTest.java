/*
 * Copyright 2013-2016 the original author or authors.
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

package com.gopivotal.cloudfoundry.tomcat.lifecycle;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.core.StandardContext;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Test {@link ApplicationStartupFailureDetectingLifecycleListener}
 */
public final class ApplicationStartupFailureDetectingLifecycleListenerTest {

    private final Context context = mock(Context.class);

    private final Container lifecycle = mock(Container.class);

    private final StandardContext standardContext = mock(StandardContext.class);

    private final Runtime runtime = mock(Runtime.class);

    private final ApplicationStartupFailureDetectingLifecycleListener lifecycleListener = new
            ApplicationStartupFailureDetectingLifecycleListener(this.runtime);

    @Test
    public void irrelevantEvent() {
        this.lifecycleListener.lifecycleEvent(new LifecycleEvent(this.lifecycle, Lifecycle.AFTER_DESTROY_EVENT, null));

        verifyZeroInteractions(this.runtime);
    }

    @Test
    public void nonStandardContext() {
        when(this.lifecycle.findChildren()).thenReturn(new Container[]{this.context});

        this.lifecycleListener.lifecycleEvent(new LifecycleEvent(this.lifecycle, Lifecycle.AFTER_START_EVENT, null));

        verifyZeroInteractions(this.runtime);
    }

    @Test
    public void started() {
        when(this.lifecycle.findChildren()).thenReturn(new Container[]{this.standardContext});
        when(this.standardContext.getState()).thenReturn(LifecycleState.STARTED);

        this.lifecycleListener.lifecycleEvent(new LifecycleEvent(this.lifecycle, Lifecycle.AFTER_START_EVENT, null));

        verifyZeroInteractions(this.runtime);
    }

    @Test
    public void nullApplicationName() {
        when(this.lifecycle.findChildren()).thenReturn(new Container[]{this.standardContext});
        when(this.standardContext.getState()).thenReturn(LifecycleState.STOPPED);

        this.lifecycleListener.lifecycleEvent(new LifecycleEvent(this.lifecycle, Lifecycle.AFTER_START_EVENT, null));

        verify(this.standardContext).getName();
    }

    @Test
    public void halt() {
        when(this.lifecycle.findChildren()).thenReturn(new Container[]{this.standardContext});
        when(this.standardContext.getState()).thenReturn(LifecycleState.STOPPED);
        when(this.standardContext.getDisplayName()).thenReturn("test-display-name");

        this.lifecycleListener.lifecycleEvent(new LifecycleEvent(this.lifecycle, Lifecycle.AFTER_START_EVENT, null));

        verify(this.runtime).halt(404);
    }

}
