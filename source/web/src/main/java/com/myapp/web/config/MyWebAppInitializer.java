package com.myapp.web.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * 使用java注册servlet，并且此servlet可与web.xml中注册的servlet同时存在。
 *
 * 1、基于xml或java的配置都可使用此方式。基于xml时使用XmlWebApplicationContext；基于java时使用AnnotationConfigWebApplicationContext
 */
public class MyWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {

//        XmlWebApplicationContext rootCtx = new XmlWebApplicationContext();
//        rootCtx.setConfigLocation("classpath:META-INF/spring/applicationContext.xml");
//        servletContext.addListener(new ContextLoaderListener(rootCtx));

//        XmlWebApplicationContext servletCtx = new XmlWebApplicationContext();
//        servletCtx.setConfigLocation("classpath:META-INF/spring/application.xml");
//        ServletRegistration.Dynamic appServlet = servletContext.addServlet("javaServlet", new DispatcherServlet(servletCtx));
//        appServlet.setLoadOnStartup(2);
//        appServlet.addMapping("/javaServletPath/*");

//        appServlet.setMultipartConfig();

    }
}

/**
 * 2、基于xml的简单方式
 */
//public class MyWebAppInitializer extends AbstractDispatcherServletInitializer {
//
//    @Override
//    protected WebApplicationContext createServletApplicationContext() {
//        XmlWebApplicationContext servletCtx = new XmlWebApplicationContext();
//        servletCtx.setConfigLocation("classpath:META-INF/spring/spring-mvc.xml");
//        return servletCtx;
//    }
//
//    @Override
//    protected String[] getServletMappings() {
//        return new String[]{"/app1/*"};
//    }
//
//    @Override
//    protected WebApplicationContext createRootApplicationContext() {
//        XmlWebApplicationContext rootCtx = new XmlWebApplicationContext();
//        rootCtx.setConfigLocation("classpath:META-INF/spring/service.xml");
//        return rootCtx;
//    }
//}

/**
 * 3、基于java的简单方式
 */
//public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//
//    @Override
//    protected Class<?>[] getRootConfigClasses() {
//        return new Class[0];
//    }
//
//    @Override
//    protected Class<?>[] getServletConfigClasses() {
//        return new Class[0];
//    }
//
//    @Override
//    protected String[] getServletMappings() {
//        return new String[]{"/app11/*"};
//    }
//}
