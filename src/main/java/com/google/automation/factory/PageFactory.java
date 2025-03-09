package com.google.automation.factory;

import com.codeborne.selenide.Selenide;
import com.google.automation.pages.GoogleSearchPage;
import com.google.automation.pages.GoogleSearchResultsPage;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Фабрика для создания и инициализации страниц.
 * Обеспечивает единую точку доступа к страницам и инкапсулирует логику их создания.
 */
public class PageFactory {
    private static final Logger log = LoggerFactory.getLogger(PageFactory.class);

    @Step("Открыть главную страницу Google")
    public static GoogleSearchPage getGoogleSearchPage() {
        log.info("Создание и открытие главной страницы Google");
        return Selenide.open("/", GoogleSearchPage.class).closePopup().changeLanguageToEnglish();
    }

    @Step("Открыть главную страницу Google и выполнить поиск: {query}")
    public static GoogleSearchResultsPage searchOnGoogle(String query) {
        log.info("Выполнение поиска: {}", query);
        GoogleSearchPage searchPage = getGoogleSearchPage();
        return searchPage.search(query);
    }

    @Step("Перейти к поиску картинок с запросом: {query}")
    public static GoogleSearchResultsPage searchImagesOnGoogle(String query) {
        log.info("Поиск картинок: {}", query);
        GoogleSearchResultsPage resultsPage = searchOnGoogle(query);
        return resultsPage.switchToImagesTab();
    }

    @Step("Перейти к поиску новостей с запросом: {query}")
    public static GoogleSearchResultsPage searchNewsOnGoogle(String query) {
        log.info("Поиск новостей: {}", query);
        GoogleSearchResultsPage resultsPage = searchOnGoogle(query);
        return resultsPage.switchToNewsTab();
    }

    @Step("Перейти к поиску видео с запросом: {query}")
    public static GoogleSearchResultsPage searchVideosOnGoogle(String query) {
        log.info("Поиск видео: {}", query);
        GoogleSearchResultsPage resultsPage = searchOnGoogle(query);
        return resultsPage.switchToVideosTab();
    }
}