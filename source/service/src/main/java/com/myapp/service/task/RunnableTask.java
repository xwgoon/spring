package com.myapp.service.task;

import java.util.Date;

public class RunnableTask implements Runnable {

    private String message;

    public RunnableTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println(new Date() + " enter run()");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(new Date() + " Runnable Task with " + message
                + " on thread " + Thread.currentThread().getName());
    }
}
