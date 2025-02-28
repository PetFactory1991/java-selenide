package com.ultimateqa.automation.base;

import com.codeborne.selenide.Selenide;
import com.ultimateqa.automation.config.WebDriverConfig;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeSuite
    public void setUpSuite() {
        log.info("Настройка тестового набора");
    }

    @BeforeMethod
    @Step("Настройка теста")
    public void setUp() {
        log.info("Настройка теста");
        WebDriverConfig.configure();
    }

    @AfterMethod
    @Step("Завершение теста")
    public void tearDown() {
        log.info("Завершение теста");
        Selenide.closeWebDriver();
    }
}