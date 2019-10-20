package com.gfieast.qa.pages;

import com.gfieast.qa.elements.TestPageElements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import static com.gfieast.qa.UiTest.sleep;

public class TestPage extends TestPageElements {

    public TestPage() {
        PageFactory.initElements(driver, TestPageElements.class);
    }

    public TestPage iClickCustomButton() {
        exampleButton.click();
        return this;
    }
}