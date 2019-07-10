package com.hotmail.bess410;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) {
        MyExecutor executor = new MyExecutor(2);
        MyService service1 = new MyService(null, "service1", 6000);
        MyService service2 = new MyService(service1, "service2", 5000);
        MyService service3 = new MyService(service2, "service3", 2000);
        MyService service4 = new MyService(service2, "service4", 500);
        service1.addChild(service2);
        service2.addChild(service3);
        service2.addChild(service4);

        executor.executeService(service1);
    }
}


class MyService implements Runnable {
    private String name;
    private int timeExecution;
    private ExecutorService executorService;
    private boolean inProgress = true;

    private MyService parent;
    private Set<MyService> children = new HashSet<>();

    public MyService(MyService parent, String name, int timeExecution) {
        this.parent = parent;
        this.name = name;
        this.timeExecution = timeExecution;
    }

    public void addChild(MyService service) {
        children.add(service);
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
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
        parent.children.remove(this);
        parent.executeChildren();
    }

    public void executeChildren() {
        if (children.isEmpty()) {
            executorService.submit(this);
            setInProgress(false);
        } else {
            children.stream().filter(service -> service.inProgress)
                    .forEach(service -> {
                        service.setExecutorService(executorService);
                        if (service.children.isEmpty()) {
                            executorService.submit(service);
                            service.setInProgress(false);
                        } else {
                            service.executeChildren();
                        }
                    });
        }
    }


}

class MyExecutor {
    //TODO реализовать отслеживание циклических зависимостей

    private int countThread;

    public MyExecutor(int countThread) {
        this.countThread = countThread;
    }

    public void executeService(MyService service) {
        ExecutorService executorService = Executors.newFixedThreadPool(countThread);
        service.setExecutorService(executorService);
        service.executeChildren();
        //TODO как закончить выполнение программы
    }
}