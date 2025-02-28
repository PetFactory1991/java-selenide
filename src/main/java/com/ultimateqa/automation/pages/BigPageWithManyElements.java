package com.ultimateqa.automation.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class BigPageWithManyElements {

    private final SelenideElement sectionOfButtonsText = $("span#Section_of_Buttons");

    public SelenideElement getSectionOfButtonsText() {
        return sectionOfButtonsText;
    }
}
