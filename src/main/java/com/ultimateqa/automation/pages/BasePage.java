package com.ultimateqa.automation.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

public class BasePage {

    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    // Общие элементы для всех страниц
    protected final SelenideElement header = $(".main-header");
    protected final SelenideElement footer = $(".footer");
    protected final SelenideElement logo = $(".logo");

    @Step("Открыть URL: {url}")
    public BasePage openUrl(String url) {
        logger.info("Открытие URL: {}", url);
        Selenide.open(url);
        return this;
    }

    @Step("Получить заголовок страницы")
    public String getTitle() {
        String title = Selenide.title();
        logger.info("Получен заголовок страницы: {}", title);
        return title;
    }

    @Step("Перейти на главную страницу")
    public void goToHomePage() {
        logger.info("Переход на главную страницу");
        logo.click();
    }

    @Step("Проверка что страница загружена")
    public boolean isPageLoaded() {
        boolean isLoaded = header.isDisplayed() && footer.isDisplayed();
        logger.info("Проверка загрузки страницы: {}", isLoaded);
        return isLoaded;
    }

    @Step("Эмуляция человеческого скролла")
    public void humanLikeScroll() {
        logger.info("Выполнение эмуляции скролла");
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
        logger.info("Случайная задержка: {} мс", delay);
        Selenide.sleep(delay);
    }
}