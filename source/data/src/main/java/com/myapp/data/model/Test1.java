package com.myapp.data.model;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Test1
//        implements
//        ApplicationContextAware
//        BeanNameAware
{

    @Autowired
    private ApplicationContext applicationContext;

//    public Test1() {
//    }

    //    @Autowired
//    public Test1(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }

    public void printAddress() {
        Address address = applicationContext.getBean("address", Address.class);
        System.out.println(address);
    }

//    public void init(){
//        System.out.println("【init】");
//    }

//    @Override
//    public void setBeanName(String name) {
//        System.out.println("Bean name: " + name);
//    }
}
