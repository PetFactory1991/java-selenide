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
    @Description("Check loading of the Google homepage")
    @Severity(SeverityLevel.BLOCKER)
    public void testGoogleSearchPageLoads() {
        log.info("Starting test: check loading of the Google homepage");
        GoogleSearchPage searchPage = new GoogleSearchPage().openSearchPage().closePopup();
        Assert.assertTrue(searchPage.isPageLoaded(), "Google homepage is not loaded");
    }

    @Test
    @Feature("Google Search")
    @Description("Check search execution and display of results")
    @Severity(SeverityLevel.CRITICAL)
    public void testGoogleSearch() {
        log.info("Starting test: check search execution");
        String searchQuery = "Selenide Java";
        GoogleSearchPage searchPage = new GoogleSearchPage().openSearchPage().closePopup();
        GoogleSearchResultsPage resultsPage = searchPage.search(searchQuery);
        Assert.assertTrue(resultsPage.isPageLoaded(), "Search results page is not loaded");
        Assert.assertTrue(resultsPage.getResultsCount() > 0, "No search results found");
        Assert.assertEquals(resultsPage.getSearchQuery(), searchQuery, "Search query does not match the expected");
    }

    @Test
    @Feature("Google Search")
    @Description("Check the content of search results")
    @Severity(SeverityLevel.NORMAL)
    public void testSearchResultsContent() {
        log.info("Starting test: check the content of search results");
        String searchQuery = "Selenium WebDriver";
        String expectedTextInResults = "Selenium";
        GoogleSearchPage searchPage = new GoogleSearchPage().openSearchPage().closePopup();
        GoogleSearchResultsPage resultsPage = searchPage.search(searchQuery);
        Assert.assertTrue(resultsPage.resultsContainText(expectedTextInResults), "Search results do not contain the expected text");
    }
}