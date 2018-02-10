package com.myapp.web;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

public class Application {

    public static void main(String[] args) throws Exception {

//        ApplicationContext context = new ClassPathXmlApplicationContext("spring/application.xml");
//        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.getEnvironment().setActiveProfiles("user2");
//        context.register(AppConfig.class);
//        context.refresh();

//        Address address = context.getBean("address", Address.class);
//        address.setAddress("123");
//        System.out.println(address);

//        Validator validator = context.getBean("validator", Validator.class);
//        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(address);
//        System.out.println(constraintViolations);
//        System.out.println(constraintViolations.iterator().next().getMessage());


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
//        ExpressionParser parser = new SpelExpressionParser();
//        Expression exp = parser.parseExpression("'Hello World'.toUpperCase()");
//        Object message = exp.getValue();
//
//        System.out.println(message);

        // Create and set a calendar
//        GregorianCalendar c = new GregorianCalendar();
//        c.set(1856, 7, 9);
//

//        User user = new User("Tom");
//        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression("name?:'Unknown'");
//        expression = parser.parseExpression("name?.toUpperCase()");

//        EvaluationContext context = new StandardEvaluationContext(user);
//        String value = expression.getValue(context, String.class);

//        String value = expression.getValue(user, String.class);
//
//        System.out.println(value);
//
//        Society society = new Society();
//        EvaluationContext societyContext = new StandardEvaluationContext(society);
//        societyContext.setVariable("testVariable", "Nikola Tesla");
//        ExpressionParser parser = new SpelExpressionParser();
//
//        Expression expression = parser.parseExpression("ints.?[#this > 1]");
//        Integer[] ints = (Integer[]) expression.getValue(societyContext);
//        System.out.println(Arrays.toString(ints));
//
//        expression = parser.parseExpression("Members.?[Nationality == '中国']");
//        List inventors = (List) expression.getValue(societyContext);
////        expression = parser.parseExpression("Members.^[Nationality == '中国']");
////        expression = parser.parseExpression("Members.$[Nationality == '中国']");
////        Inventor inventors = (Inventor) expression.getValue(societyContext);
//        System.out.println(inventors);
//
//        expression = parser.parseExpression("officers.?[value > 1]");
////        expression = parser.parseExpression("officers.^[value > 1]");
////        expression = parser.parseExpression("officers.$[value > 1]");
//        Map officers = expression.getValue(societyContext, Map.class);
//        System.out.println(officers);
//
//        expression = parser.parseExpression("members.![nationality]");
////        expression = parser.parseExpression("officers.![key]");
//        List nationalities = expression.getValue(societyContext, List.class);
//        System.out.println(nationalities);
//
//        String str = parser.parseExpression("'My name is ' + #testVariable").getValue(societyContext, String.class);
//        str = parser.parseExpression("random number is #{T(java.lang.Math).random()}", new TemplateParserContext()).getValue(String.class);
//
//        System.out.println(str);
//
//        FooService foo = (FooService) context.getBean("fooService");
//        foo.getFoo("Pengo", 12);


//        sortList();
//        System.out.println();
//        sortArray();

//        System.out.println(System.getProperty("user.dir"));
//        System.out.println(new File("web/src/main/").getAbsolutePath());
//        System.out.println(new File(".").getAbsolutePath());
    }


}
