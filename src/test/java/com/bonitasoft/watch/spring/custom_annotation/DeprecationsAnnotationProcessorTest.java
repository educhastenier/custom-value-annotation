package com.bonitasoft.watch.spring.custom_annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class DeprecationsAnnotationProcessorTest {

    @Mock
    private Environment environment;

    @InjectMocks
    private BonitaPropertyAnnotationProcessor processor;

    @Test
    void should_PostProcessBeforeInitialization_set_BonitaProperty_value() throws Exception {
        // Create a test bean with a field annotated with @BonitaProperty
        TestBean testBean = new TestBean();

        // Mock the environment to return a value for the property
        when(environment.getProperty("test.property")).thenReturn("testValue");

        // Process the bean
        processor.postProcessBeforeInitialization(testBean, "testBean");

        // Verify that the field value was set correctly
        Field field = TestBean.class.getDeclaredField("property");
        field.setAccessible(true);
        assertEquals("testValue", field.get(testBean));
    }

    static class TestBean {
        @Deprecations("test.property")
        private String property;
    }
}