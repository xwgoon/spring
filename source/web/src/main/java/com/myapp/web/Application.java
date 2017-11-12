package com.myapp.web;

import com.myapp.data.model.Address;
import com.myapp.data.model.User;
import com.myapp.service.event.BlackListEvent1;
import com.myapp.service.event.EventService;
import com.myapp.service.propertyEditor.DependsOnExoticType;
import config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Set;

//@Component
public class Application {

//    @Autowired
//    private static UserService userService;

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring/application.xml");
//        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.getEnvironment().setActiveProfiles("user2");
//        context.register(AppConfig.class);
//        context.refresh();

        Address address = context.getBean("address", Address.class);
//        address.setAddress("123");
        System.out.println(address);

        Validator validator = context.getBean("validator", Validator.class);
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);
        System.out.println(constraintViolations);
        System.out.println(constraintViolations.iterator().next().getMessage());


//        System.out.println(context.getBean("propertyEditorSample", DependsOnExoticType.class).getType());

//        Resource resource = context.getResource("classpath:i18n/exceptions_en_GB.properties");
//        System.out.println(resource.);

//        try {
//            Resource resource = null;
//            resource = new UrlResource("file:///ok.jpg");  //ok
//
//            resource = new ClassPathResource("com/myapp/web/Test.java");  //not work
//            resource = new ClassPathResource("i18n/exceptions_zh_CN.properties");  //ok
//
////            resource = new ServletContextResource("");
//
//            System.out.println(resource.exists());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        EventService eventService = context.getBean("eventService", EventService.class);
//        eventService.sendEmail("a", "你好！");

//        eventService.publishEntityCreatedEvent();


//        String message = context.getMessage("myapp.test", new Object[]{"1", "2"}, "'\'myapp.test\'' is not existed.", Locale.UK);
//        System.out.println(message);
//
//        message = context.getMessage("myapp.test", new Object[]{"1", "2"}, "'\'myapp.test\'' is not existed.", Locale.CHINA);
//        System.out.println(message);


//        User user = context.getBean("user0", User.class);
//        System.out.println(user.getResource());

//        System.out.println(context.getBean("user0", User.class));
//        System.out.println(context.getBean("user1", User.class));


//        System.out.println(context.getEnvironment().getProperty("myapp.user.name"));

//        System.out.println(context.getEnvironment().getPropertySources());
//        System.out.println();
//
//        System.out.println(context.getEnvironment().getSystemProperties());
//        System.out.println(System.getProperties());
//        System.out.println();
//
//        System.out.println(context.getEnvironment().getSystemEnvironment());
//        System.out.println(System.getenv());

//        Address address = context.getBean("addressTest", Address.class);
//        System.out.println(address);


//        UserService userService = context.getBean("userService", UserService.class);
//        User user = userService.findOneById(1L);
//        System.out.println(user);

//
//        Address address = context.getBean("address", Address.class);
//        System.out.println(address);
//
//        address.setAddress("人民公园");
//        System.out.println(address);

//        User user = context.getBean("user12", User.class);

//        System.out.println(System.getProperties());

//        context.getBean("test1", Test1.class).printAddress();

//        User user = context.getBean("myFactoryBean", User.class);
//        System.out.println(user);
//
//        MyFactoryBean myFactoryBean = context.getBean("&myFactoryBean", MyFactoryBean.class);
//        try {
//            System.out.println(myFactoryBean.getObject());
//            System.out.println(myFactoryBean.isSingleton());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
