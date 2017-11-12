package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DataConfig.class)
public class ServiceConfig {

    //    @Bean
//    @Scope("prototype")
//    public UserDao userDao() {
//        System.out.println("AppConfig.userDao()");
//        return new UserDao();
//    }
//
//    @Bean
//    public UserService userService() {
//        return new UserServiceImpl(userDao());
//    }
//
//    @Bean
//    public UserService userService1() {
//        return new UserServiceImpl(userDao());
//    }

}
