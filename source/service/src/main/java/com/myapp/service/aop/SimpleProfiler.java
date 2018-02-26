package com.myapp.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.Ordered;
import org.springframework.util.StopWatch;

public class SimpleProfiler implements Ordered {

    private int order;

    public Object profile(ProceedingJoinPoint call, String name) throws Throwable {
        StopWatch clock = new StopWatch("Profiling for '" + name + "'");
        try {
            clock.start(call.toShortString());
            return call.proceed();
        } finally {
            clock.stop();
            System.out.println(clock.prettyPrint());
        }
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
