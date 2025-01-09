package com.bonitasoft.watch.spring.custom_annotation;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyService {

//    @BonitaProperty("my.property", deprecated = {"old.my.property", "older.my.property"})
//    private String myProperty;

    @Value("${new.property}")
    @Deprecations({"old.my.property", "older.my.property"})
    private String newProperty;

    @Value("${boolean.property}")
//    @BonitaProperty("deprecated.boolean.property")
    private boolean booleanProperty;

    @Value("${int.property}")
    @Deprecations("deprecated.int.property")
    private int intProperty;

    @Value("${long.property}")
    @Deprecations("deprecated.long.property")
    private long longProperty;
//
//    @BonitaProperty
//    private String unnamedProperty;
//

    public MyService(@Value("${long.construct.parameter}")
                     @Deprecations("deprecated.long.construct.parameter") long longConstructParameter) {
        System.out.println("MyService constructor: "+longConstructParameter);
    }

    @PostConstruct
    public void printProperty() {
//        System.out.println("My Property: " + myProperty);
        System.out.println("New Property: " + newProperty);
        System.out.println("booleanProperty: " + booleanProperty);
        System.out.println("intProperty: " + intProperty);
        System.out.println("longProperty: " + longProperty);
    }
}