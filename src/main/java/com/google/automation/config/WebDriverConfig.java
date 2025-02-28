package com.google.automation.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebDriverConfig {

    public static void configure() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadTimeout = 30000;
        Configuration.timeout = 10000;

        // Настройка headless режима с системной настройкой
        Configuration.headless = Boolean.parseBoolean(
                System.getProperty("headless", "false")  // По умолчанию видимый режим
        );
        Configuration.baseUrl = "https://www.google.com";

        ChromeOptions options = new ChromeOptions();

        // Базовые настройки
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--user-data-dir=/tmp/chrome-user-data-" + System.currentTimeMillis());
        options.addArguments("--window-size=1920,1080");

        // Маскировка автоматизации
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // Реалистичный User-Agent (избегаем HeadlessChrome в строке UA)
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        // Отключаем сохранение паролей и автозаполнение
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        // Применяем дополнительные возможности браузера
        Configuration.browserCapabilities = options;

        // Настройка Allure
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
                        .includeSelenideSteps(true));
    }
}