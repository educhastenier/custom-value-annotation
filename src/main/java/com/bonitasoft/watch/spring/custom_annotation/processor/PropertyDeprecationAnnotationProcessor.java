package com.bonitasoft.watch.spring.custom_annotation.processor;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import com.bonitasoft.watch.spring.custom_annotation.Deprecations;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertyDeprecationAnnotationProcessor implements BeanPostProcessor {

    private final Environment environment;

    public PropertyDeprecationAnnotationProcessor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            warnIfDeprecatedPropertyUsed(bean, field, beanName);
        }
        for (Constructor<?> constructor : bean.getClass().getConstructors()) {
            for (Parameter parameter : constructor.getParameters()) {
                warnIfDeprecatedPropertyUsed(bean, parameter, beanName);
            }
        }
        return bean;
    }

    private void warnIfDeprecatedPropertyUsed(Object bean, AnnotatedElement element, String beanName) {
        if (element.isAnnotationPresent(Deprecations.class)) {
            if (!element.isAnnotationPresent(Value.class)) {
                throw new BeansException("Annotation @BonitaProperty must be used in conjunction with @Value annotation (used in bean '" + beanName + "'of type '" + bean.getClass().getName() + "')") {
                };
            }
            for (String deprecatedPropertyName : element.getAnnotation(Deprecations.class).value()) {
                String resolvedValue = environment.getProperty(deprecatedPropertyName);
                if (resolvedValue != null) {
                    System.out.println("Warning: property '" + deprecatedPropertyName + "' is deprecated." +
                            "Please use '" + PropertyNameExtractor.extractPropertyName(element.getAnnotation(Value.class).value()) + "' instead");
                }
            }
        }
    }

}