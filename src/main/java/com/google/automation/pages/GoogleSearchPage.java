package com.google.automation.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

public class GoogleSearchPage extends BasePage<GoogleSearchPage> {

    private final SelenideElement popup = $("button[id='L2AGLb']");
    private final SelenideElement searchBox = $("textarea[name='q']");
    private final SelenideElement searchButton = $("input[name='btnK']");
    private final SelenideElement googleLogo = $("img.lnXdpd");
    private final SelenideElement accountButton = $("a[aria-label*='Google Account']");

    @Step("Open 'Google search' page")
    public GoogleSearchPage openSearchPage() {
        log.info("Open 'Google search' page");
        return openUrl("/");
    }

    @Step("Close popup")
    public GoogleSearchPage closePopup() {
        log.info("Close popup");
        popup.shouldBe(visible).click();
        Selenide.sleep(500);
        return this;
    }

    @Step("Check if the page is loaded")
    @Override
    public boolean isPageLoaded() {
        boolean isLoaded = googleLogo.isDisplayed() && searchBox.isDisplayed();
        log.info("Check if the page is loaded: {}", isLoaded);
        return isLoaded;
    }

    @Step("Ввести поисковый запрос: {query}")
    public GoogleSearchPage enterSearchQuery(String query) {
        log.info("Ввод поискового запроса: {}", query);
        searchBox.shouldBe(visible).setValue(query);
        return this;
    }

    @Step("Выполнить поиск нажатием кнопки")
    public GoogleSearchResultsPage clickSearchButton() {
        log.info("Нажатие кнопки поиска");
        // Иногда кнопка поиска может быть не видна сразу, поэтому добавляем прокрутку
        searchBox.pressTab();
        searchButton.click();
        return page(GoogleSearchResultsPage.class);
    }

    @Step("Выполнить поиск нажатием Enter")
    public GoogleSearchResultsPage pressEnterToSearch() {
        log.info("Нажатие Enter для выполнения поиска");
        searchBox.sendKeys(Keys.ENTER);
        Selenide.sleep(500);
        return page(GoogleSearchResultsPage.class);
    }

    @Step("Выполнить поиск: {query}")
    public GoogleSearchResultsPage search(String query) {
        log.info("Выполнение поиска: {}", query);
        enterSearchQuery(query);
        return pressEnterToSearch();
    }

    @Step("Открыть страницу аккаунта Google")
    public void openGoogleAccount() {
        log.info("Открытие страницы аккаунта Google");
        accountButton.click();
    }
}