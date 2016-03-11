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

import org.apache.catalina.LifecycleState;

final class StandardStateParser implements StateParser {

    @Override
    public boolean isRunning(Object state) {
        if (state instanceof Integer) {
            return isRunning((Integer) state);
        } else if (state instanceof LifecycleState) {
            return isRunning((LifecycleState) state);
        }

        return false;
    }

    private boolean isRunning(Integer state) {
        return state == 1;
    }

    private boolean isRunning(LifecycleState state) {
        return state == LifecycleState.STARTED;
    }

}
