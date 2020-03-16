package com.hrpc.api;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author huoji
 */
public class BootStrap {

    public static void main(String[] args) {
        ApplicationContext application = new AnnotationConfigApplicationContext(SpringConfig.class);
        ((AnnotationConfigApplicationContext) application).start();
    }
}
