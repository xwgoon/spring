package config;

import com.myapp.data.model.Address;
import com.myapp.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
public class DataConfig {

//    @Autowired
//    private Environment environment;
//
//    @Value("${myapp.user.name:defaultName}")
//    private String userName;

//    @Bean
//    public User user0() {
//        return new User(environment.getProperty("myapp.user.name"));
//        return new User("user0");
//    }

//    @Bean(initMethod = "myInit", name = "user")
//    @Profile("default")
//    public User user0() {
//        return new User("user0");
//    }
//
//    @Bean(initMethod = "myInit", name = "user")
//    @Profile("user1")
//    public User user1() {
//        return new User("user1");
//    }
//
//    @Bean(initMethod = "myInit", name = "user")
//    @Profile("user2")
//    public User user2() {
//        return new User("user2");
//    }

//    @Bean
//    @Profile("addressTest")
//    public Address addressTest() {
//        return new Address("addressTest");
//    }

}
