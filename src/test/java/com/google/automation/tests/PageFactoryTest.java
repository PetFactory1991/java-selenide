
package com.google.automation.tests;

import com.google.automation.base.BaseTest;
import com.google.automation.factory.PageFactory;
import com.google.automation.pages.GoogleSearchResultsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.google.automation.utils.ScreenshotUtils.takeScreenshot;
import static com.google.automation.utils.ScreenshotUtils.takeScreenshotForAllure;

public class PageFactoryTest extends BaseTest {

    @Test
    @Feature("Page Factory")
    @Description("Проверка работы Page Factory для стандартного поиска")
    @Severity(SeverityLevel.NORMAL)
    public void testPageFactorySearch() {
        log.info("Запуск теста с использованием Page Factory для стандартного поиска");
        String query = "Automation Framework";
        GoogleSearchResultsPage resultsPage = PageFactory.searchOnGoogle(query);
        Assert.assertTrue(resultsPage.isPageLoaded(), "Страница результатов не загрузилась");
        Assert.assertTrue(resultsPage.getResultsCount() > 0, "Результаты поиска не найдены");
    }

    @Test
    @Feature("Page Factory")
    @Description("Проверка работы Page Factory для поиска новостей")
    @Severity(SeverityLevel.NORMAL)
    public void testPageFactoryNewsSearch() {
        log.info("Запуск теста с использованием Page Factory для поиска новостей");
        String query = "Technology news";
        GoogleSearchResultsPage resultsPage = PageFactory.searchNewsOnGoogle(query);
        takeScreenshot("news_search_results");
        takeScreenshotForAllure("news_search_results");

        // Дополнительные проверки для новостного поиска можно добавить здесь
    }
}