package com.gopivotal.buildpack.support.tomcat;

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
