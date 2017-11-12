package com.myapp.service.event;

import org.springframework.context.ApplicationEvent;

public class BlackListEvent3 extends ApplicationEvent {

    private final String address;
    private final String test;

    public BlackListEvent3(Object source, String address, String test) {
        super(source);
        this.address = address;
        this.test = test;
    }

    @Override
    public String toString() {
        return "BlackListEvent{" +
                "address='" + address + '\'' +
                ", test='" + test + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public String getTest() {
        return test;
    }
}
