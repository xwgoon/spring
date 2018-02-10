package com.myapp.service.aop;

public class DefaultFooService implements FooService {

    @Override
    public Foo getFoo(String name, int age) {
        return new Foo(name, age);
    }
}
