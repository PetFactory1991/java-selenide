// src/test/java/com/google/automation/tests/AdvancedFeaturesTest.java
package com.google.automation.tests;

import com.codeborne.selenide.Selenide;
import com.google.automation.base.BaseTest;
import com.google.automation.conditions.CustomConditions;
import com.google.automation.factory.PageFactory;
import com.google.automation.pages.GoogleSearchPage;
import com.google.automation.pages.GoogleSearchResultsPage;
import com.google.automation.utils.BrowserStateUtils;
import com.google.automation.utils.JsActions;
import com.google.automation.utils.WaitUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.$;

public class AdvancedFeaturesTest extends BaseTest {

    @Test
    @Feature("Продвинутые возможности")
    @Description("Демонстрация продвинутых возможностей фреймворка")
    @Severity(SeverityLevel.NORMAL)
    public void testAdvancedFeatures() {
        log.info("Запуск теста с продвинутыми возможностями");

        // Открываем Google с помощью PageFactory
        GoogleSearchPage searchPage = PageFactory.getGoogleSearchPage();

        // Добавляем данные в localStorage для демонстрации
        BrowserStateUtils.setLocalStorageItem("test-key", "test-value");

        // Используем JavaScript для установки значения
        JsActions.setValue($("textarea[name='q']"), "Selenide advanced features");

        // Используем JavaScript для клика
        JsActions.jsClick($("input[name='btnK']"));

        // Ожидаем загрузку страницы
        WaitUtils.waitForPageToLoad();

        // Получаем страницу результатов
        GoogleSearchResultsPage resultsPage = Selenide.page(GoogleSearchResultsPage.class);

        // Используем кастомное условие для проверки
        $("h3").should(CustomConditions.textContainsIgnoreCase("selenide"));

        // Подсвечиваем результаты для демонстрации
        JsActions.highlightElement($("#search"));

        // Проверяем значение в localStorage
        String value = BrowserStateUtils.getLocalStorageItem("test-key");
        Assert.assertEquals(value, "test-value", "Значение в localStorage не соответствует ожидаемому");

        // Очищаем localStorage
        BrowserStateUtils.removeLocalStorageItem("test-key");
    }
}