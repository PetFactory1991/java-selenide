// src/main/java/com/google/automation/utils/BrowserStateUtils.java
package com.google.automation.utils;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BrowserStateUtils {
    private static final Logger log = LoggerFactory.getLogger(BrowserStateUtils.class);

    /**
     * Получить все куки браузера
     *
     * @return набор куков
     */
    public static Set<Cookie> getAllCookies() {
        WebDriver driver = WebDriverRunner.getWebDriver();
        Set<Cookie> cookies = driver.manage().getCookies();
        log.info("Получено {} cookies", cookies.size());
        return cookies;
    }

    /**
     * Добавить куку
     *
     * @param name  имя куки
     * @param value значение куки
     */
    public static void addCookie(String name, String value) {
        log.info("Добавление cookie: {}={}", name, value);
        WebDriver driver = WebDriverRunner.getWebDriver();
        driver.manage().addCookie(new Cookie(name, value));
    }

    /**
     * Удалить куку по имени
     *
     * @param name имя куки
     */
    public static void deleteCookie(String name) {
        log.info("Удаление cookie: {}", name);
        WebDriver driver = WebDriverRunner.getWebDriver();
        driver.manage().deleteCookieNamed(name);
    }

    /**
     * Очистить все куки
     */
    public static void clearAllCookies() {
        log.info("Очистка всех cookies");
        WebDriver driver = WebDriverRunner.getWebDriver();
        driver.manage().deleteAllCookies();
    }

    /**
     * Сохранить значение в localStorage
     *
     * @param key   ключ
     * @param value значение
     */
    public static void setLocalStorageItem(String key, String value) {
        log.info("Сохранение в localStorage: {}={}", key, value);
        WebDriver driver = WebDriverRunner.getWebDriver();
        ((JavascriptExecutor) driver).executeScript(
                String.format("window.localStorage.setItem('%s', '%s');", key, value));
    }

    /**
     * Получить значение из localStorage
     *
     * @param key ключ
     * @return значение
     */
    public static String getLocalStorageItem(String key) {
        log.info("Получение из localStorage по ключу: {}", key);
        WebDriver driver = WebDriverRunner.getWebDriver();
        return (String) ((JavascriptExecutor) driver).executeScript(
                String.format("return window.localStorage.getItem('%s');", key));
    }

    /**
     * Удалить значение из localStorage
     *
     * @param key ключ
     */
    public static void removeLocalStorageItem(String key) {
        log.info("Удаление из localStorage по ключу: {}", key);
        WebDriver driver = WebDriverRunner.getWebDriver();
        ((JavascriptExecutor) driver).executeScript(
                String.format("window.localStorage.removeItem('%s');", key));
    }

    /**
     * Очистить весь localStorage
     */
    public static void clearLocalStorage() {
        log.info("Очистка всего localStorage");
        WebDriver driver = WebDriverRunner.getWebDriver();
        ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
    }
}