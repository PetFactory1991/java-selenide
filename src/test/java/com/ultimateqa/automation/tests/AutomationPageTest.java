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
    @Feature("Страница автоматизации")
    @Description("Проверка загрузки страницы автоматизации")
    @Severity(SeverityLevel.BLOCKER)
    public void testAutomationPageLoads() {
        log.info("Запуск теста: проверка загрузки страницы автоматизации");

        AutomationPage automationPage = new AutomationPage().openAutomationPage();

        Assert.assertTrue(automationPage.isPageLoaded(),
                "Страница автоматизации не загрузилась");

        String titleText = automationPage.getPageTitleText();
        Assert.assertTrue(titleText.contains("Automation Practice"),
                "Заголовок страницы не содержит ожидаемый текст");
    }
}