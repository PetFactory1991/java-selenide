package com.google.automation.tests;

import com.google.automation.base.BaseTest;
import com.google.automation.pages.GoogleSearchPage;
import com.google.automation.pages.GoogleSearchResultsPage;
import com.google.automation.utils.DataProviders;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ParameterizedSearchTest extends BaseTest {

    @Test(dataProvider = "searchQueries", dataProviderClass = DataProviders.class)
    @Feature("Параметризованный поиск")
    @Description("Проверка поиска с различными запросами из CSV файла")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchWithDataFromCsv(String query, String expectedText) {
        log.info("Запуск теста поиска с запросом: {} (ожидаемый текст: {})", query, expectedText);

        GoogleSearchPage searchPage = new GoogleSearchPage()
                .openSearchPage()
                .closePopup();

        GoogleSearchResultsPage resultsPage = searchPage.search(query);

        Assert.assertTrue(resultsPage.isPageLoaded(),
                "Страница результатов не загрузилась для запроса: " + query);

        Assert.assertTrue(resultsPage.resultsContainText(expectedText),
                "Результаты не содержат ожидаемый текст: " + expectedText);
    }

    @Test(dataProvider = "simpleSearchQueries", dataProviderClass = DataProviders.class)
    @Feature("Параметризованный поиск")
    @Description("Проверка поиска с различными запросами из массива")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchWithSimpleData(String query, String expectedText) {
        log.info("Запуск теста поиска с запросом: {} (ожидаемый текст: {})", query, expectedText);

        GoogleSearchPage searchPage = new GoogleSearchPage()
                .openSearchPage()
                .closePopup();

        GoogleSearchResultsPage resultsPage = searchPage.search(query);

        Assert.assertTrue(resultsPage.isPageLoaded(),
                "Страница результатов не загрузилась для запроса: " + query);

        Assert.assertTrue(resultsPage.resultsContainText(expectedText),
                "Результаты не содержат ожидаемый текст: " + expectedText);
    }
}