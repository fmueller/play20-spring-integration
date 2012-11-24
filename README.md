play20-spring-integration
=========================

**Small project that shows how to easily integrate spring into play 2.0.x applications.**

I saw [this demo](https://github.com/guillaumebort/play20-spring-demo) by Guillaume Bort on how to combine spring and play 2. The name of this repository suggests that is does work with play 2.0.x but it only works with play 2.1 because _managed controller classes instantiation_ is only available on play 2.1 branch. So, I show you how to integrate spring into your play 2.0.x application.

If you like XML files you can also try this module: [play-2.0-spring-module](https://github.com/wsargent/play-2.0-spring-module). I'm a fanboy of annotation-based configuration. So, I don't use this module. But it inspired my solution a lot.

**The goal is to autowire our controllers.**

## How does it work?

### Add spring dependencies
First of all, you have to add the spring dependency to your project by configuring `project/Build.scala` file.

```scala
import sbt._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "play20-spring-integration"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.springframework" % "spring-context" % "3.1.2.RELEASE"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
    // Add your own project settings here
  )
}
```

### Bootstrap the application context

To initiate a spring application context on the start of a play application you have to implement a `Global` object as shown below.

```java
package global;

import configurations.SpringConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import play.Application;
import play.GlobalSettings;

public class Global extends GlobalSettings {

    private static ApplicationContext applicationContext;

    @Override
    public void onStart(Application application) {
        applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
    }

    public static <T> T getBean(Class<T> beanClass) {
        if (applicationContext == null) {
            throw new IllegalStateException("application context is not initialized");
        }
        return applicationContext.getBean(beanClass);
    }
}
```

Through the `getBean` method we will later autowire our controllers. It is only a delegating method.

### Set up your Spring configuration

In this example the configuration is done via annotations. You could also use XML files.

```java
package configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import services.HelloWorldService;

@Configuration
@ComponentScan({"controllers", "services"})
public class SpringConfiguration {

    @Bean
    public HelloWorldService helloWorldService() {
        return new HelloWorldService();
    }
}
```
It demonstrates two points: component scanning for `controllers` and `services` package and the usage of explicit bean definition by `@Bean` annotation. All controllers and services are being automatically discovered and autowired if they are annotated as a component.

### Add some services

For this example we have two _really important_ services. The first one is a POJO with the most famous functionality in the developer world: hello world. :-)

```java
package services;

public class HelloWorldService {

    public String sayHello() {
        return "hello";
    }
}
```

The second one is annotated as a component to show that component scanning works and gets the first service injected.


```java
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonalizedHelloWorldService {

    @Autowired
    private HelloWorldService helloWorldService;

    public String sayHelloTo(String name) {
        return helloWorldService.sayHello() + " " + name;
    }
}
```

### Autowire controllers
You have to integrate the autowiring of controllers into the play request lifecycle. For that, you can use a delegating static controller factory (inspired by [play-2.0-spring-module](https://github.com/wsargent/play-2.0-spring-module)).

```java
package controllers;

import global.Global;

public final class ControllerFactory {

    private ControllerFactory() {
        // to prevent instantiation, it's just a damn stupid factory
    }

    public static Application application() {
        return Global.getBean(Application.class);
    }
}
```

For each controller you write such a static getter method. By calling the `getBean` method you force the autowiring of the controller. With this nifty trick you get autowired controllers. The `routes` file contains the new routes:

```scala
GET     /                           controllers.ControllerFactory.application.index()
GET     /personalized/:name         controllers.ControllerFactory.application.helloTo(name: String)
```

The `Application`controller is not static anymore and is annotated as a component. So, it can be autowired.

```java
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import play.mvc.Controller;
import play.mvc.Result;
import services.HelloWorldService;
import services.PersonalizedHelloWorldService;

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
```

Now you have fully-fledged spring integration in your play application. Make use of it!