// src/main/java/com/google/automation/utils/TestListener.java
package com.google.automation.utils;

import io.qameta.allure.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Запуск теста: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Тест успешно выполнен: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Тест завершился с ошибкой: {}", result.getName());
        log.error("Причина: {}", result.getThrowable().getMessage());

        // Автоматическое создание скриншота при падении теста
        String testName = result.getTestClass().getName() + "." + result.getName();
        ScreenshotUtils.takeScreenshotForAllure("Ошибка в тесте " + testName);

        // Сохранение логов в Allure
        saveTextLog("Ошибка в тесте " + testName + ": " + result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.info("Тест пропущен: {}", result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("Начало выполнения тестового набора: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Завершение тестового набора: {}", context.getName());
        log.info("Всего тестов: {}, Успешно: {}, Неудачно: {}, Пропущено: {}",
                context.getAllTestMethods().length,
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    @Attachment(value = "{0}", type = "text/plain")
    private String saveTextLog(String message) {
        return message;
    }
}