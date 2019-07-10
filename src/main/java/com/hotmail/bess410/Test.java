package com.hotmail.bess410;

public class Test {
    public static void main(String[] args) {

        MyService service1 = new MyService("service1", 6000);
        MyService service2 = new MyService("service2", 5000);
        MyService service3 = new MyService("service3", 2000);
        MyService service4 = new MyService("service4", 500);

        service1.getChildren().add(service2);
        service2.getParents().add(service1);

        service2.getChildren().add(service3);
        service3.getParents().add(service2);

        service2.getChildren().add(service4);
        service4.getParents().add(service2);

        MyExecutor executor = new MyExecutor(service1,2);

        executor.executeService();
    }
}