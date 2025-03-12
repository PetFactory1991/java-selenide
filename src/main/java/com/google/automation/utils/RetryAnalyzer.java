// src/main/java/com/google/automation/utils/RetryAnalyzer.java
package com.google.automation.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger log = LoggerFactory.getLogger(RetryAnalyzer.class);

    private int counter = 0;
    private static final int MAX_RETRY_COUNT = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess()) {
            if (counter < MAX_RETRY_COUNT) {
                counter++;
                log.warn("Повторный запуск теста: {} (попытка {}/{})",
                        result.getName(), counter, MAX_RETRY_COUNT);
                return true;
            }

            log.error("Тест не прошел после {} повторных попыток: {}",
                    MAX_RETRY_COUNT, result.getName());
        }
        return false;
    }
}