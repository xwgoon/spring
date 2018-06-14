package com.myapp.service.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

//@Component
public class SimpleTask {

    @Async
//    @Scheduled(fixedDelay = 1000)
    public void doSomething() {
        System.out.println("enter doSomething(): " + Thread.currentThread());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("exit doSomething(): " + Thread.currentThread());
    }

    @Async("taskExecutor1")
    public Future<String> returnSomething(String arg) {
        System.out.println("enter returnSomething(): " + Thread.currentThread());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("exit returnSomething(): " + Thread.currentThread());
        return new AsyncResult<>(arg);
    }

}
