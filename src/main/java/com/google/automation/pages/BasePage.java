package com.google.automation.pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePage<T extends BasePage<T>> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Step("Открыть URL: {url}")
    public T openUrl(String url) {
        log.info("Открытие URL: {}", url);
        Selenide.open(url);
        Selenide.sleep(500);
        return (T) this;
    }

    @Step("Получить заголовок страницы")
    public String getTitle() {
        String title = Selenide.title();
        log.info("Получен заголовок страницы: {}", title);
        return title;
    }

    @Step("Сделать скриншот страницы")
    public T takeScreenshot(String screenshotName) {
        log.info("Создание скриншота: {}", screenshotName);
        Selenide.screenshot(screenshotName);
        return (T) this;
    }

    // Абстрактный метод, который должен быть реализован в каждой странице
    public abstract boolean isPageLoaded();
}