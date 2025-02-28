package com.ultimateqa.automation.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WebDriverConfig {

    public static void configure() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadTimeout = 30000;
        Configuration.timeout = 10000;
        Configuration.headless = false;
        Configuration.baseUrl = "https://ultimateqa.com/automation";

        ChromeOptions options = new ChromeOptions();

        // Создаем уникальную директорию для user-data-dir
        String uniqueDir = System.getProperty("java.io.tmpdir") + File.separator + "chrome-user-data-" + System.currentTimeMillis();
        options.addArguments("--user-data-dir=" + uniqueDir);

        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36");

        // Добавление параметра excludeSwitches для исключения "enable-automation"
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

        // Убираем info bar с сообщением "Chrome is being controlled by automated test software"
        options.setExperimentalOption("useAutomationExtension", false);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        Configuration.browserCapabilities = options;

        // Настройка Allure для Selenide
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
                        .includeSelenideSteps(true));
    }
}