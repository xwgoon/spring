package com.myapp.web.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

//    @Autowired  //can not be autowired
    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println(applicationContext);
        System.out.println("【MyBeanFactoryPostProcessor.postProcessBeanFactory()】");
    }

    public MyBeanFactoryPostProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
