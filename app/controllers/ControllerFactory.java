package controllers;

import global.Global;

/**
 * This is a workaround for play 2.0 where you need a static call to
 * controller method in routes file. In play 2.1 this is no longer necessary.
 *
 * @author felixmueller
 */
public final class ControllerFactory {

    private ControllerFactory() {
        // to prevent instantiation, it's just a damn stupid factory
    }

    public static Application application() {
        return Global.getBean(Application.class);
    }
}
