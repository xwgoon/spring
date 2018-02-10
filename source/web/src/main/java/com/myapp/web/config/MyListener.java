package com.myapp.web.config;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class MyListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        //请求退出StandardHostValve时执行
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        //请求进入StandardHostValve时执行
    }
}
