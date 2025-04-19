// src/test/java/com/google/automation/tests/AllureCategoriesTest.java
package com.google.automation.tests;

import com.codeborne.selenide.WebDriverRunner;
import com.google.automation.base.BaseTest;
import com.google.automation.factory.PageFactory;
import com.google.automation.pages.GoogleSearchResultsPage;
import io.qameta.allure.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;

public class AllureCategoriesTest extends BaseTest {

    @Test
    @Feature("Аллюр категории")
    @Story("Логические ошибки")
    @Description("Тест с логической ошибкой для проверки категоризации в Allure")
    @Severity(SeverityLevel.NORMAL)
    public void testWithAssertionError() {
        log.info("Запуск теста с логической ошибкой");
        String query = "Selenide";

        GoogleSearchResultsPage resultsPage = PageFactory.searchOnGoogle(query);

        // Логическая ошибка - специально проверяем неверное условие
        Assert.assertEquals(resultsPage.getSearchQuery(), "Другой запрос",
                "Поисковый запрос должен был быть 'Другой запрос', но был '" + resultsPage.getSearchQuery() + "'");
    }

    @Test
    @Feature("Аллюр категории")
    @Story("Таймауты и стабильность")
    @Description("Тест со сломанным локатором для проверки категоризации в Allure")
    @Severity(SeverityLevel.NORMAL)
    public void testWithStabilityIssue() {
        log.info("Запуск теста с проблемой стабильности");

        // Ищем несуществующий элемент, чтобы вызвать ошибку Element not found
        String query = "Selenide framework";
        GoogleSearchResultsPage resultsPage = PageFactory.searchOnGoogle(query);

        try {
            // Используем заведомо некорректный локатор
            resultsPage.doActionWithInvalidLocator();
        } catch (Exception e) {
            // Делаем скриншот для отчета
            Allure.addAttachment("Скриншот в момент ошибки",
                    new ByteArrayInputStream(((TakesScreenshot) WebDriverRunner.getWebDriver())
                            .getScreenshotAs(OutputType.BYTES)));
            throw e; // Перебрасываем исключение для правильной категоризации
        }
    }
}