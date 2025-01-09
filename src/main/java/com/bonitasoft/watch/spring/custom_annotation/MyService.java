package com.bonitasoft.watch.spring.custom_annotation;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyService {

    @Value("${my.property:${old.my.property:${older.my.property}}}")
    @Deprecations({"old.my.property", "older.my.property"})
    private String myProperty;

    @Value("${new.property}}")
    private String newProperty;

    @Value("${boolean.property:${deprecated.boolean.property}}")
    @Deprecations("deprecated.boolean.property")
    private boolean booleanProperty;

    @Value("${int.property:${deprecated.int.property}}")
    @Deprecations("deprecated.int.property")
    private int intProperty;

    @Value("${long.property:${deprecated.long.property}}")
    @Deprecations("deprecated.long.property")
    private long longProperty;

    @Value("${my.enum.flower:${my.old.enum.flower.name:ROSE}}")
    @Deprecations("my.old.enum.flower.name")
    private Flower flower;

    public MyService(@Value("${long.construct.parameter:${deprecated.long.construct.parameter}}")
                     @Deprecations("deprecated.long.construct.parameter") long longConstructParameter) {
        System.out.println("MyService constructor: " + longConstructParameter);
    }

    @PostConstruct
    public void printProperties() {
        System.out.println("My Property: " + myProperty);
        System.out.println("New Property: " + newProperty);
        System.out.println("booleanProperty: " + booleanProperty);
        System.out.println("intProperty: " + intProperty);
        System.out.println("longProperty: " + longProperty);
        System.out.println("flower: " + flower);
    }

    enum Flower {
        ROSE, LILY, TULIP
    }
}