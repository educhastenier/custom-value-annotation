package com.bonitasoft.watch.spring.custom_annotation.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import com.bonitasoft.watch.spring.custom_annotation.Deprecations;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest(properties = {"test.property=testValue"})
//@ExtendWith(MockitoExtension.class)
class DeprecationsAnnotationProcessorTest {

    @Autowired
    private PropertyDeprecationAnnotationProcessor processor;

    @Bean
    public TestBean testBean() {
        return new TestBean();
    }

    @Autowired
    TestBean testBean;

    @Test
    void should_PostProcessBeforeInitialization_set_BonitaProperty_value() throws Exception {
        // Create a test bean with a field annotated with @BonitaProperty
//        TestBean testBean = new TestBean();

//        // Mock the environment to return a value for the property
//        when(environment.getProperty("test.property")).thenReturn("testValue");

        // Process the bean
        processor.postProcessBeforeInitialization(testBean, "testBean");

        // Verify that the field value was set correctly
        Field field = testBean.getClass().getDeclaredField("property");
        field.setAccessible(true);
        assertEquals("testValue", field.get(testBean));
    }

    static class TestBean {
        @Value("${test.property}")
        @Deprecations("test.property")
        private String property;
    }
}