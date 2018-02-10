package com.myapp.web.config;

import com.myapp.data.model.User;
import org.springframework.beans.factory.FactoryBean;

public class MyFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        return new User(3L, "王五", "13612345678");
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
