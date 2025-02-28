package com.ultimateqa.automation.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverConfig {

    public static void configure() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadTimeout = 30000;
        Configuration.timeout = 10000;
        // В CI окружении рекомендуется использовать headless режим
        Configuration.headless = true;
        Configuration.baseUrl = "https://ultimateqa.com/automation";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        // Добавляем уникальную директорию для каждого запуска браузера
        options.addArguments("--user-data-dir=/tmp/chrome-user-data-" + System.currentTimeMillis());

        // Дополнительные опции для стабильной работы в AWS CodeBuild
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        // Применяем дополнительные возможности браузера
        Configuration.browserCapabilities = options;

        // Настройка Allure для Selenide
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
                        .includeSelenideSteps(true));
    }
}