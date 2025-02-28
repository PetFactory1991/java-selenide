package com.google.automation.pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage<T extends BasePage<T>> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Step("Open URL: {url}")
    public T openUrl(String url) {
        log.info("Opening URL: {}", url);
        Selenide.open(url);
        Selenide.sleep(500);
        return (T) this;
    }

    @Step("Get page title")
    public String getTitle() {
        String title = Selenide.title();
        log.info("Page title: {}", title);
        return title;
    }

    @Step("Take screenshot: {screenshotName}")
    public T takeScreenshot(String screenshotName) {
        log.info("Taking screenshot: {}", screenshotName);
        Selenide.screenshot(screenshotName);
        return (T) this;
    }

    public abstract boolean isPageLoaded();
}