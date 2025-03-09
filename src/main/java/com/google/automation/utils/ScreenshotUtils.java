// src/main/java/com/google/automation/utils/ScreenshotUtils.java
package com.google.automation.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOTS_DIR = "target/screenshots/";

    static {
        // Создаем директорию для скриншотов при загрузке класса
        try {
            Files.createDirectories(Paths.get(SCREENSHOTS_DIR));
        } catch (Exception e) {
            log.error("Не удалось создать директорию для скриншотов", e);
        }
    }

    /**
     * Делает скриншот страницы и сохраняет его в директории для скриншотов.
     *
     * @param name Имя скриншота
     * @return Путь к сохраненному файлу
     */
    public static String takeScreenshot(String name) {
        log.info("Создание скриншота: {}", name);
        try {
            File screenshotDir = new File(SCREENSHOTS_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String fileName = name + "_" + timestamp + ".png";
            String filePath = SCREENSHOTS_DIR + fileName;

            WebDriver driver = WebDriverRunner.getWebDriver();
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), Paths.get(filePath));

            log.info("Скриншот сохранен: {}", filePath);
            return filePath;
        } catch (Exception e) {
            log.error("Ошибка при создании скриншота: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Делает скриншот страницы и прикрепляет его к Allure-отчету.
     *
     * @param name Имя скриншота
     * @return Массив байтов с изображением
     */
    @Attachment(value = "{name}", type = "image/png")
    public static byte[] takeScreenshotForAllure(String name) {
        log.info("Создание скриншота для Allure: {}", name);
        try {
            return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (WebDriverException e) {
            log.error("Не удалось создать скриншот для Allure: {}", e.getMessage());
            return new byte[0];
        }
    }
}