package com.ultimateqa.automation.pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BasePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Step("Открыть URL: {url}")
    public BasePage openUrl(String url) {
        log.info("Открытие URL: {}", url);
        Selenide.open(url);
        return this;
    }

    @Step("Получить заголовок страницы")
    public String getTitle() {
        String title = Selenide.title();
        log.info("Получен заголовок страницы: {}", title);
        return title;
    }

    @Step("Эмуляция человеческого скролла")
    public void humanLikeScroll() {
        log.info("Выполнение эмуляции скролла");
        int scrollSteps = 5;
        int scrollDistance = 300;
        int delayMs = 500;

        for (int i = 0; i < scrollSteps; i++) {
            Selenide.executeJavaScript("window.scrollBy(0, arguments[0])", scrollDistance);
            Selenide.sleep(delayMs);
        }
    }

    @Step("Случайная задержка")
    public void randomDelay() {
        int delay = (int) (1000 + Math.random() * 2000);
        log.info("Случайная задержка: {} мс", delay);
        Selenide.sleep(delay);
    }
}