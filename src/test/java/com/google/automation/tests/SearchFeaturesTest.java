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

public class SearchFeaturesTest extends BaseTest {

    @Test
    @Feature("Расширенные функции поиска")
    @Description("Проверка переключения между страницами результатов")
    @Severity(SeverityLevel.NORMAL)
    public void testPagination() {
        log.info("Запуск теста: проверка пагинации");

        GoogleSearchPage searchPage = new GoogleSearchPage()
                .openSearchPage()
                .closePopup();

        GoogleSearchResultsPage resultsPage = searchPage.search("Java programming language");

        // Переход на вторую страницу
        resultsPage.goToNextPage();

        // Проверка, что на странице есть результаты
        Assert.assertTrue(resultsPage.getResultsCount() > 0,
                "На второй странице нет результатов поиска");

        // Проверка, что можно вернуться на первую страницу
        resultsPage.goToPreviousPage();

        // Проверка, что на странице есть результаты
        Assert.assertTrue(resultsPage.getResultsCount() > 0,
                "После возврата на первую страницу нет результатов поиска");
    }

    @Test
    @Feature("Расширенные функции поиска")
    @Description("Проверка переключения между вкладками поиска")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchTabs() {
        log.info("Запуск теста: проверка вкладок поиска");

        GoogleSearchPage searchPage = new GoogleSearchPage()
                .openSearchPage()
                .closePopup()
                .changeLanguageToEnglish();

        GoogleSearchResultsPage resultsPage = searchPage.search("Automation testing tools");

        // Переключение на вкладку Новости
        resultsPage.switchToNewsTab();

        // Переключение на вкладку Картинки
        resultsPage.switchToImagesTab();

        // Переключение на вкладку Видео
        resultsPage.switchToVideosTab();
    }
}