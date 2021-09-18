package com.company.qa.pages;

import com.company.qa.elements.TestPageElements;
import org.openqa.selenium.support.PageFactory;

public class TestPage extends TestPageElements {

    public TestPage() {
        PageFactory.initElements(driver, TestPageElements.class);
    }

    public TestPage iClickCustomButton() {
        exampleButton.click();
        return this;
    }
}