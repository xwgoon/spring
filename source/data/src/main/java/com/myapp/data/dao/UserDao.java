package com.myapp.data.dao;

import com.myapp.data.model.User;
import org.springframework.stereotype.Repository;

//@Repository
public class UserDao {

    private static UserDao userDao = new UserDao();

    public static UserDao staticCreateMethod() {
        return userDao;
    }

    public UserDao instanceCreateMethod() {
        return userDao;
    }

    public User findOneById(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("张三");
        return user;
    }
}
