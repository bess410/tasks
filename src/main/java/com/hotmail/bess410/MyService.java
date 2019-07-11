package com.hotmail.bess410;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

class MyService implements Runnable {
    private String name;
    private int timeExecution;

    private Set<MyService> parents = new HashSet<>();
    private Set<MyService> children = new HashSet<>();


    public MyService(String name, int timeExecution) {
        this.name = name;
        this.timeExecution = timeExecution;
    }

    public Set<MyService> getParents() {
        return parents;
    }

    public Set<MyService> getChildren() {
        return children;
    }

    @Override
    public void run() {
        System.out.println(name + " начал выполнение " + LocalDateTime.now());
        try {
            Thread.sleep(timeExecution);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " закончил выполнение");
        checkToStop();
        checkParentsToRun();
    }

    public void runService() {
        if (isReadyToRun()) {
            MyExecutor.getExecutor().execute(this);
        } else {
            children.forEach(MyService::runService);
        }
    }

    private void checkParentsToRun() {
        parents.forEach(parent -> parent.getChildren().remove(this));
        parents.stream()
                .filter(MyService::isReadyToRun)
                .forEach(MyService::runService);
    }

    private void checkToStop() {
        if (parents.isEmpty()) {
            MyExecutor.getExecutor().shutdown();
        }
    }

    private boolean isReadyToRun() {
        return children.isEmpty();
    }
}