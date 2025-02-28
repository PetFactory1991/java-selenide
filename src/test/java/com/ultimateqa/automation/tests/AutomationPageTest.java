package com.ultimateqa.automation.tests;

import com.ultimateqa.automation.base.BaseTest;
import com.ultimateqa.automation.pages.AutomationPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AutomationPageTest extends BaseTest {

    @Test
    @Feature("Automation page")
    @Description("Check if automation page loads")
    @Severity(SeverityLevel.BLOCKER)
    public void testAutomationPageLoads() {
        log.info("Running test: check if automation page loads");

        AutomationPage automationPage = new AutomationPage().openAutomationPage();

        Assert.assertTrue(automationPage.isPageLoaded(),
                "Automation page is not loaded");

        String titleText = automationPage.getPageTitleText();
        Assert.assertTrue(titleText.contains("Automation Practice"),
                "Automation page title is incorrect");
    }

    @Test
    @Feature("Complicated page")
    @Description("Open 'Big page with many elements' page")
    @Severity(SeverityLevel.BLOCKER)
    public void testOpenBigPageWithManyElements() {
        log.info("Running test: open 'Big page with many elements' page");

        var page = new AutomationPage().openAutomationPage()
                .openBigPageWithManyElements()
                .getSectionOfButtonsText();

        Assert.assertEquals(page.getText(), "Section of Buttons",
                "Text is incorrect");
    }
}