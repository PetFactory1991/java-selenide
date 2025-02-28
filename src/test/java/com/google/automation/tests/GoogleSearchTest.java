package com.google.automation.tests;

import com.google.automation.base.BaseTest;
import com.google.automation.pages.GoogleSearchPage;
import com.google.automation.pages.GoogleSearchResultsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GoogleSearchTest extends BaseTest {

    @Test
    @Feature("Google Search")
    @Description("Проверка загрузки главной страницы Google")
    @Severity(SeverityLevel.BLOCKER)
    public void testGoogleSearchPageLoads() {
        log.info("Запуск теста: проверка загрузки главной страницы Google");
        GoogleSearchPage searchPage = new GoogleSearchPage().openSearchPage().closePopup();
        Assert.assertTrue(searchPage.isPageLoaded(), "Главная страница Google не загружена");
    }

    @Test
    @Feature("Google Search")
    @Description("Проверка выполнения поиска и отображения результатов")
    @Severity(SeverityLevel.CRITICAL)
    public void testGoogleSearch() {
        log.info("Запуск теста: проверка выполнения поиска");
        String searchQuery = "Selenide Java";
        GoogleSearchPage searchPage = new GoogleSearchPage().openSearchPage().closePopup();
        GoogleSearchResultsPage resultsPage = searchPage.search(searchQuery);
        Assert.assertTrue(resultsPage.isPageLoaded(), "Страница результатов поиска не загружена");
        Assert.assertTrue(resultsPage.getResultsCount() > 0, "Результаты поиска не найдены");
        Assert.assertEquals(resultsPage.getSearchQuery(), searchQuery, "Поисковый запрос не соответствует ожидаемому");
    }

    @Test
    @Feature("Google Search")
    @Description("Проверка содержимого результатов поиска")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchResultsContent() {
        log.info("Запуск теста: проверка содержимого результатов поиска");
        String searchQuery = "Selenium WebDriver";
        String expectedTextInResults = "Selenium";
        GoogleSearchPage searchPage = new GoogleSearchPage().openSearchPage().closePopup();
        GoogleSearchResultsPage resultsPage = searchPage.search(searchQuery);
        Assert.assertTrue(resultsPage.resultsContainText(expectedTextInResults), "Результаты поиска не содержат ожидаемый текст");
    }
}