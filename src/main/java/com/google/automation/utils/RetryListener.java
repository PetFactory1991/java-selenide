// src/main/java/com/google/automation/utils/RetryListener.java
package com.google.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer {
    private static final Logger log = LoggerFactory.getLogger(RetryListener.class);

    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
        log.debug("RetryAnalyzer установлен для метода: {}",
                testMethod != null ? testMethod.getName() : "null");
    }
}