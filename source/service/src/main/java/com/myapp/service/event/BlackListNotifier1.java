package com.myapp.service.event;

import org.springframework.context.ApplicationListener;

public class BlackListNotifier1 implements ApplicationListener<BlackListEvent1> {

    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    @Override
    public void onApplicationEvent(BlackListEvent1 event) {
        System.out.println("BlackListNotifier1 监听到 BlackListEvent1 事件");
//        System.out.println(event);
    }

}
