package config;

import org.springframework.context.annotation.*;

@Configuration
//@Import(ServiceConfig.class)
@ImportResource("classpath:spring/application.xml")
//@PropertySource("classpath:spring/prop.properties")
//@ComponentScan("com.myapp")
public class AppConfig {

}
