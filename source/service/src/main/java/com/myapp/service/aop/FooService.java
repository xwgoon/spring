package com.myapp.service.aop;

public interface FooService {

    Foo getFoo(String name, int age);

    String getById(Long id);

    void insertFoo (String name) throws Exception;
}
