package configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import services.HelloWorldService;

/**
 * Everybody loves annotation-based configuration. So, this is a really simple one.
 * It shows how you can use component scanning: for this example Spring
 * discovers the Application controller as a component and the PersonalizedHelloWorldService.
 * The normal HelloWorldService is being created by a bean method as you can see below.
 *
 * @author felixmueller
 */
@Configuration
@ComponentScan({"controllers", "services"})
public class SpringConfiguration {

    /**
     * You can use bean methods to create your beans
     * as here for the hello world service.
     */
    @Bean
    public HelloWorldService helloWorldService() {
        return new HelloWorldService();
    }
}
