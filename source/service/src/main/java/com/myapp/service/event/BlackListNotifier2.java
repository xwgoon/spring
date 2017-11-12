package com.myapp.service.event;

import com.myapp.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;

public class BlackListNotifier2 {

    @Autowired
    private ApplicationContext context;

    @EventListener(condition = "#event.address == 'a'")
//    @Async
    public List<ApplicationEvent> processEvent1(BlackListEvent1 event) {
//        System.out.println("BlackListNotifier2.processEvent1 监听到 BlackListEvent1 事件，然后发布了 BlackListEvent2 事件");
//        return new BlackListEvent2(this, "d", "");

        System.out.println(context);

        System.out.println("BlackListNotifier2.processEvent1 监听到 BlackListEvent1 事件，然后发布了 BlackListEvent2 和 BlackListEvent3 事件");
        List<ApplicationEvent> events = new ArrayList<>();
        events.add(new BlackListEvent2(this, null, null));
        events.add(new BlackListEvent3(this, null, null));
        return events;
    }

    @EventListener(BlackListEvent2.class)
//    @EventListener({BlackListEvent1.class, BlackListEvent2.class})
//    @Order(2)
    public void processEvent2() {
        System.out.println("BlackListNotifier2.processEvent2 监听到 BlackListEvent2 事件");
    }

    @EventListener
    public void onUserCreated(EntityCreatedEvent<User> event) {
        System.out.println("BlackListNotifier2.onUserCreated 监听到 EntityCreatedEvent<User> 事件");
    }

    @EventListener
//    @Order(1)
    public void processEvent3(ApplicationEvent event) {
        System.out.println("BlackListNotifier2.processEvent3 监听到 " + event.getClass().getSimpleName() + " 事件");
    }

}
