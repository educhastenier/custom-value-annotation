package com.bonitasoft.watch.spring.custom_annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class BonitaPropertyAnnotationProcessor implements BeanPostProcessor {

    @Autowired
    private Environment environment;


    TypeConverter typeConverter = new SimpleTypeConverter();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            extracted(bean, field);
        }
        final Constructor<?>[] constructors = bean.getClass().getConstructors();
        for (Constructor<?> constructor : constructors) {
            for (Parameter parameter : constructor.getParameters()) {
                if (parameter.isAnnotationPresent(Deprecations.class)) {
                    extracted(bean, parameter);
                }
            }
        }
        return bean;
    }

    private void extracted(Object bean, AnnotatedElement field) {
        if (field.isAnnotationPresent(Deprecations.class)) {
            if (!field.isAnnotationPresent(Value.class)) {
                throw new BeansException("Annotation @BonitaProperty must be used in conjunction with @Value annotation") {
                };
            }
            Deprecations deprecations = field.getAnnotation(Deprecations.class);
            String[] deprecated = deprecations.value();
//                if (propertyName.isEmpty()) {
//                    propertyName = bonitaProperty.name();
//                }
//                if (propertyName.isEmpty()) {
//                    throw new InvalidPropertyException(bean.getClass(), "unset property name", "@BonitaProperty 'name' or 'value' attribute is mandatory");
//                }
//                String[] deprecated = bonitaProperty.deprecated();

            String resolvedValue = null;
            for (String deprecatedPropertyName : deprecated) {
                resolvedValue = environment.getProperty(deprecatedPropertyName);
                if (resolvedValue != null) {
                    Value valueAnnotation = field.getAnnotation(Value.class);
                    System.out.println("Warning: property '" + deprecatedPropertyName + "' is deprecated. Please use '" + valueAnnotation.value() + "' instead");
                    if (field instanceof Parameter) {
                        throw new InvalidPropertyException(bean.getClass(), deprecatedPropertyName,
                                "Property '" + deprecatedPropertyName + "' has been renamed to '" + valueAnnotation.value() + "'. Please update your configuration and restart.");
                    }
                    break;
                }
            }
//                if (resolvedValue == null) {
//                    resolvedValue = environment.getProperty(propertyName);
//                }

            // Set the field value
            if (field instanceof Field) {
                setFieldValueIfApplicable(bean, (Field) field, resolvedValue);
            }
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public void setFieldValueIfApplicable(Object targetObject, Field field, String value) {
        if (value != null) {
            // Make the field accessible if it is private
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, targetObject, typeConverter.convertIfNecessary(value, field.getType()));
        }
    }

}