package com.google.automation.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class GoogleSearchResultsPage extends BasePage<GoogleSearchResultsPage> {

    private final SelenideElement searchBox = $("textarea[name='q']");
    private final SelenideElement resultBlock = $("div#search");
    private final ElementsCollection searchResults = $$("div.g");
    private final SelenideElement googleLogo = $("img.lnXdpd");

    private final SelenideElement allTab = $x("//div[text()='All']");
    private final SelenideElement imagesTab = $x("//div[text()='Images']");
    private final SelenideElement newsTab = $x("//div[text()='News']");
    private final SelenideElement videosTab = $x("//div[text()='Videos']");
    private final SelenideElement nextPageButton = $("a#pnnext");
    private final SelenideElement prevPageButton = $("a#pnprev");
    private final ElementsCollection paginationLinks = $$("table.AaVjTc td:not(.YyVfkd) a");

    // not correct locator
    private final SelenideElement notCorrectLocator = $x("div.non-existent-class");

    @Step("Check if the page is loaded")
    @Override
    public boolean isPageLoaded() {
        boolean isLoaded = resultBlock.isDisplayed() && searchBox.isDisplayed();
        log.info("Check if the page is loaded: {}", isLoaded);
        return isLoaded;
    }

    @Step("Get the number of search results")
    public int getResultsCount() {
        int count = searchResults.size();
        log.info("Number of search results: {}", count);
        return count;
    }

    @Step("Get the current search query")
    public String getSearchQuery() {
        String query = searchBox.getValue();
        log.info("Current search query: {}", query);
        return query;
    }

    @Step("Check if results contain text: {text}")
    public boolean resultsContainText(String text) {
        log.info("Checking if results contain text: {}", text);
        return searchResults.filter(visible)
                .stream()
                .anyMatch(element -> element.getText().contains(text));
    }

    @Step("Get the title of the first result")
    public String getFirstResultTitle() {
        SelenideElement firstResult = searchResults.first();
        String title = firstResult.$("h3").getText();
        log.info("Title of the first result: {}", title);
        return title;
    }

    @Step("Click on the first result")
    public void clickFirstResult() {
        log.info("Clicking on the first result");
        searchResults.first().$("h3").click();
    }

    @Step("Search again with query: {query}")
    public GoogleSearchResultsPage searchAgain(String query) {
        log.info("Searching again with query: {}", query);
        searchBox.shouldBe(visible).clear();
        searchBox.setValue(query);
        searchBox.pressEnter();
        return this;
    }

    @Step("Переключиться на вкладку Картинки")
    public GoogleSearchResultsPage switchToImagesTab() {
        log.info("Переключение на вкладку Картинки");
        imagesTab.shouldBe(visible).click();
        return this;
    }

    @Step("Переключиться на вкладку Новости")
    public GoogleSearchResultsPage switchToNewsTab() {
        log.info("Переключение на вкладку Новости");
        newsTab.shouldBe(visible).click();
        return this;
    }

    @Step("Переключиться на вкладку Видео")
    public GoogleSearchResultsPage switchToVideosTab() {
        log.info("Переключение на вкладку Видео");
        videosTab.shouldBe(visible).click();
        return this;
    }

    @Step("Перейти на следующую страницу результатов")
    public GoogleSearchResultsPage goToNextPage() {
        log.info("Переход на следующую страницу");
        if (nextPageButton.exists()) {
            nextPageButton.click();
            return this;
        }
        log.warn("Кнопка перехода на следующую страницу не найдена");
        throw new IllegalStateException("Кнопка перехода на следующую страницу не найдена");
    }

    @Step("Перейти на предыдущую страницу результатов")
    public GoogleSearchResultsPage goToPreviousPage() {
        log.info("Переход на предыдущую страницу");
        if (prevPageButton.exists()) {
            prevPageButton.click();
            return this;
        }
        log.warn("Кнопка перехода на предыдущую страницу не найдена");
        throw new IllegalStateException("Кнопка перехода на предыдущую страницу не найдена");
    }

    @Step("Перейти на страницу по номеру: {pageNumber}")
    public GoogleSearchResultsPage goToPage(int pageNumber) {
        log.info("Переход на страницу номер {}", pageNumber);
        // Google обычно показывает до 10 страниц пагинации
        if (pageNumber < 1 || pageNumber > 10) {
            throw new IllegalArgumentException("Номер страницы должен быть от 1 до 10");
        }

        // Если пытаемся перейти на первую страницу, а мы уже на ней
        if (pageNumber == 1 && !prevPageButton.exists()) {
            log.info("Уже находимся на первой странице");
            return this;
        }

        // Ищем ссылку с номером страницы
        for (SelenideElement link : paginationLinks) {
            if (link.getText().equals(String.valueOf(pageNumber))) {
                link.click();
                return this;
            }
        }

        log.warn("Ссылка на страницу {} не найдена", pageNumber);
        throw new IllegalStateException("Ссылка на страницу " + pageNumber + " не найдена");
    }

    @Step("Выполнить метод с невалидным локатором")
    public GoogleSearchResultsPage doActionWithInvalidLocator() {
        log.info("Выполнение действия с невалидным локатором");
        notCorrectLocator.shouldBe(visible).click();
        return this;
    }
}