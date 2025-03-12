// src/main/java/com/google/automation/utils/WaitUtils.java
package com.google.automation.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementNotFound;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Supplier;

public class WaitUtils {
    private static final Logger log = LoggerFactory.getLogger(WaitUtils.class);
    private static final long DEFAULT_TIMEOUT_SECONDS = 10;
    private static final long POLL_INTERVAL_MILLIS = 500;

    /**
     * Ожидание полной загрузки страницы (document.readyState === 'complete')
     */
    public static void waitForPageToLoad() {
        waitForPageToLoad(DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Ожидание полной загрузки страницы с указанным таймаутом
     *
     * @param timeoutSeconds таймаут в секундах
     */
    public static void waitForPageToLoad(long timeoutSeconds) {
        log.info("Ожидание загрузки страницы, таймаут: {} секунд", timeoutSeconds);
        WebDriver driver = WebDriverRunner.getWebDriver();

        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds), Duration.ofMillis(POLL_INTERVAL_MILLIS))
                .until(webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
    }

    /**
     * Ожидание появления элемента с повторными попытками в случае ошибок
     *
     * @param elementSupplier функция, возвращающая элемент
     * @param timeoutSeconds  таймаут в секундах
     * @param retries         количество повторных попыток
     * @return элемент, если он появился
     */
    public static SelenideElement waitForElementWithRetry(Supplier<SelenideElement> elementSupplier,
                                                          long timeoutSeconds,
                                                          int retries) {
        log.info("Ожидание элемента с повторными попытками, таймаут: {} секунд, повторы: {}",
                timeoutSeconds, retries);

        int attempt = 0;
        while (attempt <= retries) {
            try {
                SelenideElement element = elementSupplier.get();
                element.should(com.codeborne.selenide.Condition.visible, Duration.ofSeconds(timeoutSeconds));
                return element;
            } catch (ElementNotFound e) {
                attempt++;
                if (attempt > retries) {
                    log.error("Элемент не найден после {} попыток", retries);
                    throw e;
                }
                log.warn("Попытка {}/{} не удалась, повторяем...", attempt, retries);
                Selenide.sleep(POLL_INTERVAL_MILLIS);
            }
        }

        // Если мы дошли до сюда, что-то пошло не так
        throw new NoSuchElementException("Element not found after " + retries + " retries");
    }

    /**
     * Умная задержка (использовать только в крайних случаях)
     *
     * @param millis время задержки в миллисекундах
     */
    public static void smartSleep(long millis) {
        log.info("Умная задержка: {} мс", millis);
        Selenide.sleep(millis);
    }
}