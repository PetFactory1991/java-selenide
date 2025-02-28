package com.ultimateqa.automation.pages;

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


    @Step("Open 'Big page with many elements' page")
    public BigPageWithManyElements openBigPageWithManyElements() {
        log.info("Open 'Big page with many elements' page");
        bigPageWithManyElements.click();
        return page(BigPageWithManyElements.class);
    }

    @Step("Проверить, что страница автоматизации загружена")
    public boolean isPageLoaded() {
        boolean isLoaded = pageTitle.isDisplayed() && pageTitle.getText().contains("Automation Practice");
        log.info("Проверка загрузки страницы автоматизации: {}", isLoaded);
        return isLoaded;
    }

    @Step("Получить текст заголовка страницы")
    public String getPageTitleText() {
        String title = pageTitle.getText();
        log.info("Заголовок страницы автоматизации: {}", title);
        return title;
    }

    @Step()
    public AutomationPage openAutomationPage() {
        log.info("Open Automation page");
        open("/");
        return page(AutomationPage.class);
    }

    @Step("Open 'Fake landing page' page")
    public FakeLandingPage openFakeLandingPage() {
        log.info("Open 'Fake landing page' page");
        fakeLandingPage.click();
        return page(FakeLandingPage.class);
    }

    @Step("Open 'Fake pricing page' page")
    public FakePricingPage openFakePricingPage() {
        log.info("Open 'Fake pricing page' page");
        fakePricingPage.click();
        return page(FakePricingPage.class);
    }

    @Step("Open 'Fill out forms' page")
    public FillOutFormsPage openFillOutFormsPage() {
        log.info("Open 'Fill out forms' page");
        fillOutForms.click();
        return page(FillOutFormsPage.class);
    }

    @Step("Open 'Learn how to automate an application that evolves over time' page")
    public LearnHowToAutomateAnApplicationThatEvolvesOverTime openLearnHowToAutomateAnApplicationThatEvolvesOverTime() {
        log.info("Open 'Learn how to automate an application that evolves over time' page");
        learnHowToAutomateAnApplicationThatEvolvesOverTime.click();
        return page(LearnHowToAutomateAnApplicationThatEvolvesOverTime.class);
    }

    @Step("Open 'Login automation' page")
    public LoginAutomationPage openLoginAutomationPage() {
        log.info("Open 'Login automation' page");
        loginAutomation.click();
        return page(LoginAutomationPage.class);
    }

    @Step("Open 'Interactions with simple elements' page")
    public InteractionsWithSimpleElements openInteractionsWithSimpleElements() {
        log.info("Open 'Interactions with simple elements' page");
        interactionsWithSimpleElements.click();
        return page(InteractionsWithSimpleElements.class);
    }
}