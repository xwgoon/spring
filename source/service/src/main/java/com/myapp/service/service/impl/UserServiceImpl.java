package com.myapp.service.service.impl;

import com.myapp.data.dao.AddressDao;
import com.myapp.data.dao.UserDao;
import com.myapp.data.model.User;
import com.myapp.service.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

//    @Autowired
//    @Qualifier("userDao1Alias")
//    @Resource
    private UserDao userDao11;

    //    @Resource(name = "addressDao")
    private AddressDao addressDao;

    //    public UserServiceImpl() {
//    }
//
//    @Autowired(required = false)
    public UserServiceImpl(UserDao userDao) {
        this.userDao11 = userDao;
    }

    //
//    @Autowired(required = false)
//    public UserServiceImpl(UserDao userDao, AddressDao addressDao) {
//        this.userDao = userDao;
//        this.addressDao = addressDao;
//    }

    //    @Required
//    @Autowired
//    @Resource
    public void setUserDao11(UserDao userDao) {
        this.userDao11 = userDao;
    }

//        @Autowired
//    @Resource
    public void fun(UserDao userDao2) {
        this.userDao11 = userDao2;
    }

    @Override
    public User findOneById(Long id) {
        return userDao11.findOneById(id);
    }

    @Override
    public String toString() {
        return "UserServiceImpl{" +
                "userDao=" + userDao11 +
                ", addressDao=" + addressDao +
                '}';
    }
}
