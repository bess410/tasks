package com.hotmail.bess410;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyExecutor {
    //TODO реализовать отслеживание циклических зависимостей
    private static ExecutorService executorService;
    private MyService service;

    public static ExecutorService getExecutor() {
        return executorService;
    }

    public MyExecutor(MyService service, int countThread) {
        this.service = service;
        executorService = Executors.newFixedThreadPool(countThread);
    }

    public void executeService() {
        service.runService();
    }
}