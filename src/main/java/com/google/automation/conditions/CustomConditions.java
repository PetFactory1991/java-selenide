package com.google.automation.conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.CheckReturnValue;

public class CustomConditions {

    /**
     * Проверяет, что элемент стал кликабельным (visible и enabled)
     */
    public static WebElementCondition clickable = new WebElementCondition("clickable") {
        @Override
        @Nonnull
        @CheckReturnValue
        public CheckResult check(Driver driver, WebElement element) {
            try {
                boolean result = element.isDisplayed() && element.isEnabled();
                return new CheckResult(result, result ? null : "Element is not clickable");
            } catch (StaleElementReferenceException e) {
                return new CheckResult(false, "Element is stale");
            }
        }
    };

    /**
     * Проверяет, что текст элемента содержит ожидаемый текст (игнорируя регистр)
     */
    public static WebElementCondition textContainsIgnoreCase(String expectedText) {
        return new WebElementCondition("text contains ignore case '" + expectedText + "'") {
            @Override
            @Nonnull
            @CheckReturnValue
            public CheckResult check(Driver driver, WebElement element) {
                try {
                    String actualText = element.getText();
                    boolean result = actualText != null &&
                            actualText.toLowerCase().contains(expectedText.toLowerCase());
                    return new CheckResult(result,
                            result ? null : "Text does not contain: " + expectedText);
                } catch (StaleElementReferenceException e) {
                    return new CheckResult(false, "Element is stale");
                }
            }
        };
    }

    /**
     * Проверяет, что у элемента присутствует указанный класс в атрибуте class
     */
    public static WebElementCondition hasClass(String className) {
        return new WebElementCondition("has class '" + className + "'") {
            @Override
            @Nonnull
            @CheckReturnValue
            public CheckResult check(Driver driver, WebElement element) {
                try {
                    String classes = element.getAttribute("class");
                    boolean result = classes != null &&
                            (classes.equals(className) ||
                                    classes.contains(" " + className + " ") ||
                                    classes.startsWith(className + " ") ||
                                    classes.endsWith(" " + className));
                    return new CheckResult(result,
                            result ? null : "Element does not have class: " + className);
                } catch (StaleElementReferenceException e) {
                    return new CheckResult(false, "Element is stale");
                }
            }
        };
    }
}