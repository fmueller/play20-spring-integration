package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * So, you have spring which means autowiring services is no problem.
 * Because this service is marked as a component it will be discovered
 * and autowired automatically during component scanning by Spring.
 *
 * @author felixmueller
 */
@Component
public class PersonalizedHelloWorldService {

    @Autowired
    private HelloWorldService helloWorldService;

    public String sayHelloTo(String name) {
        return helloWorldService.sayHello() + " " + name;
    }
}
