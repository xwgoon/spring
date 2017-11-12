package com.myapp.service.event;

import com.myapp.data.model.Address;
import com.myapp.data.model.User;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.List;

public class EventService implements ApplicationEventPublisherAware {

    private List<String> blackList;
    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void sendEmail(String address, String text) {
        if (blackList.contains(address)) {
            System.out.println(address + " 发布了 BlackListEvent1 事件");
            BlackListEvent1 event = new BlackListEvent1(this, address, text);
            publisher.publishEvent(event);
        } else {
            System.out.println(address + " 发布了 BlackListEvent2 事件");
            BlackListEvent2 event = new BlackListEvent2(this, address, text);
            publisher.publishEvent(event);
        }
    }

    public void publishEntityCreatedEvent() {
        publisher.publishEvent(new EntityCreatedEvent<>(new User()));
//        publisher.publishEvent(new EntityCreatedEvent<>(new Address()));
    }

    public void setBlackList(List<String> blackList) {
        this.blackList = blackList;
    }
}
