package com.myapp.web;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class TomcatServer {

    public static void main(String[] args) throws Exception {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8888);
        tomcat.setBaseDir(".");  //默认是"tomcat.8888"
        tomcat.getHost().setAppBase(".");  //默认是"webapps"
//        ((StandardHost)tomcat.getHost()).setWorkDir("dir");  //可设绝对路径，也可设相对路径（相对baseDir），也可不设（默认为baseDir + /work/Tomcat/localhost/app0）
        tomcat.getConnector();
        Context ctx = tomcat.addWebapp("/contextPath", "web/src/main/webapp");  //docBase可设绝对路径，也可设相对路径（相对appBaseFile）
//        ctx.setAllowCasualMultipartParsing(true);

        tomcat.start();

//        new Thread(() -> {
//            try {
//                Thread.sleep(60 * 1000);
//                tomcat.stop();
//            } catch (InterruptedException | LifecycleException e) {
//                e.printStackTrace();
//            }
//        }).start();

        tomcat.getServer().await();
    }
}
