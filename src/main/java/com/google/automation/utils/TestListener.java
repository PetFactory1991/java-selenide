// src/main/java/com/google/automation/utils/TestListener.java
package com.google.automation.utils;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

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

        // Создание скриншота при падении теста
        takeScreenshot("Ошибка в тесте " + result.getName());

        // Сохранение логов консоли
        getBrowserConsoleLogs();

        // Сохранение HTML страницы
        attachPageSource();

        // Сохранение стектрейса ошибки
        if (result.getThrowable() != null) {
            attachExceptionStackTrace(result.getThrowable());
        }

        // Сохранение логов в Allure
        saveTextLog("Ошибка в тесте " + result.getName() + ": " + result.getThrowable().getMessage());
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

    @Attachment(value = "{0}", type = "image/png")
    public byte[] takeScreenshot(String name) {
        log.info("Создание скриншота: {}", name);
        if (!WebDriverRunner.hasWebDriverStarted()) {
            return new byte[0];
        }
        return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Console logs", type = "text/plain")
    public String getBrowserConsoleLogs() {
        log.info("Получение логов консоли браузера");
        if (!WebDriverRunner.hasWebDriverStarted()) {
            return "WebDriver не запущен!";
        }

        StringBuilder logs = new StringBuilder();
        try {
            LogEntries entries = WebDriverRunner.getWebDriver().manage().logs().get(LogType.BROWSER);
            for (LogEntry entry : entries) {
                logs.append(new Date(entry.getTimestamp())).append(" ")
                        .append(entry.getLevel()).append(" ")
                        .append(entry.getMessage()).append("\n");
            }
        } catch (Exception e) {
            logs.append("Ошибка получения логов: ").append(e.getMessage());
        }
        return logs.toString();
    }

    @Attachment(value = "Page source", type = "text/html")
    public String attachPageSource() {
        log.info("Сохранение HTML страницы");
        return WebDriverRunner.hasWebDriverStarted()
                ? WebDriverRunner.getWebDriver().getPageSource()
                : "WebDriver не запущен!";
    }

    @Attachment(value = "Stack trace", type = "text/plain")
    public String attachExceptionStackTrace(Throwable throwable) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        throwable.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }

    @Attachment(value = "{0}", type = "text/plain")
    private String saveTextLog(String message) {
        return message;
    }
}