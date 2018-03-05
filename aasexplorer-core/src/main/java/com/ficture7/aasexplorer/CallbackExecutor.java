package com.ficture7.aasexplorer;

import java.util.concurrent.Executor;

public interface CallbackExecutor extends Executor {
    Executor getCallbackExecutor();
}
