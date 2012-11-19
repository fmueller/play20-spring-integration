package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import play.mvc.Controller;
import play.mvc.Result;
import services.HelloWorldService;
import services.PersonalizedHelloWorldService;

/**
 * It's important to mark the controller as a component to enable
 * component scanning and autowiring by Spring.
 *
 * @author felixmueller
 */
@Component
public class Application extends Controller {

    @Autowired
    private HelloWorldService helloWorldService;

    @Autowired
    private PersonalizedHelloWorldService personalizedHelloWorldService;

    public Result index() {
        return ok(helloWorldService.sayHello());
    }

    public Result helloTo(String name) {
        return ok(personalizedHelloWorldService.sayHelloTo(name));
    }
}
