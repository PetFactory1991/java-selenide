package com.google.automation.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class GoogleSearchResultsPage extends BasePage<GoogleSearchResultsPage> {

    private final SelenideElement searchBox = $("textarea[name='q']");
    private final SelenideElement resultBlock = $("div#search");
    private final ElementsCollection searchResults = $$("div.g");
    private final SelenideElement googleLogo = $("img.lnXdpd");

    @Step("Проверить, что страница результатов поиска загружена")
    @Override
    public boolean isPageLoaded() {
        boolean isLoaded = resultBlock.isDisplayed() && searchBox.isDisplayed();
        log.info("Проверка загрузки страницы результатов поиска: {}", isLoaded);
        return isLoaded;
    }

    @Step("Получить количество результатов поиска")
    public int getResultsCount() {
        int count = searchResults.size();
        log.info("Количество результатов поиска: {}", count);
        return count;
    }

    @Step("Получить текст поискового запроса")
    public String getSearchQuery() {
        String query = searchBox.getValue();
        log.info("Текущий поисковый запрос: {}", query);
        return query;
    }

    @Step("Проверить, содержат ли результаты поиска текст: {text}")
    public boolean resultsContainText(String text) {
        log.info("Проверка наличия текста '{}' в результатах поиска", text);
        return searchResults.filter(visible)
                .stream()
                .anyMatch(element -> element.getText().contains(text));
    }

    @Step("Получить заголовок первого результата")
    public String getFirstResultTitle() {
        SelenideElement firstResult = searchResults.first();
        String title = firstResult.$("h3").getText();
        log.info("Заголовок первого результата: {}", title);
        return title;
    }

    @Step("Кликнуть по первому результату поиска")
    public void clickFirstResult() {
        log.info("Клик по первому результату поиска");
        searchResults.first().$("h3").click();
    }

    @Step("Выполнить новый поиск: {query}")
    public GoogleSearchResultsPage searchAgain(String query) {
        log.info("Выполнение нового поиска: {}", query);
        searchBox.shouldBe(visible).clear();
        searchBox.setValue(query);
        searchBox.pressEnter();
        return this;
    }
}