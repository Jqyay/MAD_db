package com.example.madguardians.database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Executor {
    // Singleton ExecutorService for reusability
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void executeTask(Runnable task) {
        executorService.execute(task);
    }

    public static void shutdownExecutor() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
