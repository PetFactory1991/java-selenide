// src/main/java/com/google/automation/config/WebDriverConfig.java (обновленный)
package com.google.automation.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebDriverConfig {
    private static final Logger log = LoggerFactory.getLogger(WebDriverConfig.class);

    public static void configure() {
        log.info("Настройка WebDriver с параметрами из EnvironmentConfig");

        org.fusesource.jansi.AnsiConsole.systemInstall();

        // Применяем настройки из конфигурации
        Configuration.browser = "chrome";
        Configuration.browserSize = EnvironmentConfig.getBrowserSize();
        Configuration.pageLoadTimeout = EnvironmentConfig.getTimeout();
        Configuration.timeout = EnvironmentConfig.getTimeout();
        Configuration.headless = EnvironmentConfig.isHeadless();
        Configuration.baseUrl = EnvironmentConfig.getBaseUrl();

        log.info("Используется окружение с параметрами: baseUrl={}, headless={}, timeout={}",
                Configuration.baseUrl, Configuration.headless, Configuration.timeout);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--user-data-dir=/tmp/chrome-user-data-" + System.currentTimeMillis());
        options.addArguments("--window-size=" + EnvironmentConfig.getBrowserSize().replace("x", ","));
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        Configuration.browserCapabilities = options;

        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
                        .includeSelenideSteps(true));
    }
}