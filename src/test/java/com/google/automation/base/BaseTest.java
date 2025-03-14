// src/test/java/com/google/automation/base/BaseTest.java
package com.google.automation.base;

import com.codeborne.selenide.Selenide;
import com.google.automation.config.WebDriverConfig;
import com.google.automation.utils.RetryListener;
import com.google.automation.utils.TestListener;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({TestListener.class, RetryListener.class})
public class BaseTest {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeSuite
    public void setUpSuite() {
        log.info("Set up suite");
    }

    @BeforeMethod
    @Step("Set up test")
    public void setUp() {
        log.info("Set up test");
        WebDriverConfig.configure();
    }

    @AfterMethod
    @Step("Tear down test")
    public void tearDown() {
        log.info("Tear down test");
        Selenide.closeWebDriver();
    }
}