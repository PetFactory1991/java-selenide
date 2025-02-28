package com.ultimateqa.automation.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class AutomationPage extends BasePage {

    private final SelenideElement pageTitle = $x("//h1").parent();
    private final SelenideElement bigPageWithManyElements = $("a[href*='complicated-page']");
    private final SelenideElement fakeLandingPage = $("a[href*='fake-landing-page']");
    private final SelenideElement fakePricingPage = $("a[href*='fake-pricing-page']");
    private final SelenideElement fillOutForms = $("a[href*='filling-out-forms']");
    private final SelenideElement learnHowToAutomateAnApplicationThatEvolvesOverTime = $("a[href*='sample-application-lifecycle-sprint-1']");
    private final SelenideElement loginAutomation = $("a[href*='sign_in']");
    private final SelenideElement interactionsWithSimpleElements = $("a[href*='simple-html-elements-for-automation']");


    @Step()
    public AutomationPage openAutomationPage() {
        logger.info("Open Automation page");
        open("/");
        return page(AutomationPage.class);
    }

    @Step("Open 'Big page with many elements' page")
    public BigPageWithManyElements openBigPageWithManyElements() {
        logger.info("Open 'Big page with many elements' page");
        return page(BigPageWithManyElements.class);
    }

    @Step("Проверить, что страница автоматизации загружена")
    @Override
    public boolean isPageLoaded() {
        boolean isLoaded = pageTitle.isDisplayed() && pageTitle.getText().contains("Automation Practice");
        logger.info("Проверка загрузки страницы автоматизации: {}", isLoaded);
        return isLoaded;
    }

    @Step("Получить текст заголовка страницы")
    public String getPageTitleText() {
        String title = pageTitle.getText();
        logger.info("Заголовок страницы автоматизации: {}", title);
        return title;
    }
}