// src/main/java/com/google/automation/utils/JsActions.java
package com.google.automation.utils;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsActions {
    private static final Logger log = LoggerFactory.getLogger(JsActions.class);

    /**
     * Выполнить JavaScript на странице
     *
     * @param script JavaScript код
     * @param args   аргументы скрипта
     * @return результат выполнения скрипта
     */
    public static Object executeScript(String script, Object... args) {
        log.info("Выполнение JavaScript: {}", script);
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        return js.executeScript(script, args);
    }

    /**
     * Клик по элементу с помощью JavaScript
     *
     * @param element элемент
     */
    public static void jsClick(SelenideElement element) {
        log.info("JavaScript клик по элементу");
        executeScript("arguments[0].click();", element);
    }

    /**
     * Скролл страницы к элементу
     *
     * @param element элемент
     */
    public static void scrollToElement(SelenideElement element) {
        log.info("Скролл к элементу");
        executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        WaitUtils.smartSleep(500); // Дадим время на завершение анимации
    }

    /**
     * Выделить элемент на странице (для отладки)
     *
     * @param element элемент
     */
    public static void highlightElement(SelenideElement element) {
        log.info("Выделение элемента на странице");
        executeScript(
                "var originalStyle = arguments[0].getAttribute('style');" +
                        "arguments[0].setAttribute('style', originalStyle + '; border: 2px solid red; background: yellow;');" +
                        "setTimeout(function() { arguments[0].setAttribute('style', originalStyle); }, 2000);",
                element);
    }

    /**
     * Установить значение элемента напрямую через JavaScript
     *
     * @param element элемент
     * @param value   значение
     */
    public static void setValue(SelenideElement element, String value) {
        log.info("Установка значения элемента через JavaScript: {}", value);
        executeScript("arguments[0].value = arguments[1];", element, value);
    }
}