package controllers;

import org.junit.Test;
import play.mvc.Result;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class ApplicationTest {

    @Test
    public void shouldSayHello() {
        running(fakeApplication(), new Runnable() {

            @Override
            public void run() {
                Result result = routeAndCall(fakeRequest(GET, "/"));
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentAsString(result)).isEqualToIgnoringCase("hello");
            }
        });
    }

    @Test
    public void shouldSayHelloToGivenUser() {
        running(fakeApplication(), new Runnable() {

            @Override
            public void run() {
                Result result = routeAndCall(fakeRequest(GET, "/personalized/felix"));
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentAsString(result)).isEqualToIgnoringCase("hello felix");
            }
        });
    }
}
