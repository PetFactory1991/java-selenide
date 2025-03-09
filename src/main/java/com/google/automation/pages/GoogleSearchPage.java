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

    @Step("Close popup what appears on the first visit")
    public GoogleSearchPage closePopup() {
        log.info("Close popup what appears on the first visit");
        popup.shouldBe(visible).click();
        Selenide.sleep(500);
        return this;
    }

    @Step("Change to English language")
    public GoogleSearchPage changeLanguageToEnglish() {
        log.info("Change to English language");
        $("div#SIvCob a").shouldBe(visible).click();
        return this;
    }

    @Step("Check if the page is loaded")
    @Override
    public boolean isPageLoaded() {
        boolean isLoaded = googleLogo.isDisplayed() && searchBox.isDisplayed();
        log.info("Check if the page is loaded: {}", isLoaded);
        return isLoaded;
    }

    @Step("Enter search query: {query}")
    public GoogleSearchPage enterSearchQuery(String query) {
        log.info("Enter search query: {}", query);
        searchBox.shouldBe(visible).setValue(query);
        return this;
    }

    @Step("Click search button")
    public GoogleSearchResultsPage clickSearchButton() {
        log.info("Click search button");
        searchBox.pressTab();
        searchButton.click();
        return page(GoogleSearchResultsPage.class);
    }

    @Step("Press Enter to search")
    public GoogleSearchResultsPage pressEnterToSearch() {
        log.info("Press Enter to search");
        searchBox.sendKeys(Keys.ENTER);
        Selenide.sleep(500);
        return page(GoogleSearchResultsPage.class);
    }

    @Step("Search: {query}")
    public GoogleSearchResultsPage search(String query) {
        log.info("Search: {}", query);
        enterSearchQuery(query);
        return pressEnterToSearch();
    }

    @Step("Open Google account")
    public void openGoogleAccount() {
        log.info("Open Google account");
        accountButton.click();
    }
}