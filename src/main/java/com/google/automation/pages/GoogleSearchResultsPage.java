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

    @Step("Check if the page is loaded")
    @Override
    public boolean isPageLoaded() {
        boolean isLoaded = resultBlock.isDisplayed() && searchBox.isDisplayed();
        log.info("Check if the page is loaded: {}", isLoaded);
        return isLoaded;
    }

    @Step("Get the number of search results")
    public int getResultsCount() {
        int count = searchResults.size();
        log.info("Number of search results: {}", count);
        return count;
    }

    @Step("Get the current search query")
    public String getSearchQuery() {
        String query = searchBox.getValue();
        log.info("Current search query: {}", query);
        return query;
    }

    @Step("Check if results contain text: {text}")
    public boolean resultsContainText(String text) {
        log.info("Checking if results contain text: {}", text);
        return searchResults.filter(visible)
                .stream()
                .anyMatch(element -> element.getText().contains(text));
    }

    @Step("Get the title of the first result")
    public String getFirstResultTitle() {
        SelenideElement firstResult = searchResults.first();
        String title = firstResult.$("h3").getText();
        log.info("Title of the first result: {}", title);
        return title;
    }

    @Step("Click on the first result")
    public void clickFirstResult() {
        log.info("Clicking on the first result");
        searchResults.first().$("h3").click();
    }

    @Step("Search again with query: {query}")
    public GoogleSearchResultsPage searchAgain(String query) {
        log.info("Searching again with query: {}", query);
        searchBox.shouldBe(visible).clear();
        searchBox.setValue(query);
        searchBox.pressEnter();
        return this;
    }
}