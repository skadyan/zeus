package com.cumulativeminds.zeus;

import java.util.concurrent.Executor;

public class RunInCurrentThreadExecutor implements Executor {

    public static RunInCurrentThreadExecutor INSTANCE = new RunInCurrentThreadExecutor();

    private RunInCurrentThreadExecutor() {
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }

}
