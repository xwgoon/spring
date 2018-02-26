package com.myapp.service.task;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;

public class MyTaskExecutor {

    private TaskExecutor taskExecutor;

    public MyTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void printMessages() {
        for (int i = 0; i < 25; i++) {
            taskExecutor.execute(new MessagePrinterTask("Message" + i));
        }
    }

    public void shutdown() {
        if (taskExecutor instanceof ExecutorConfigurationSupport) {
            ((ExecutorConfigurationSupport) taskExecutor).shutdown();
        }
    }

    private class MessagePrinterTask implements Runnable {

        private String message;

        MessagePrinterTask(String message) {
            this.message = message;
        }

        public void run() {
            System.out.println(Thread.currentThread() + ": " + message);
        }

    }
}
