package com.company.qa.elements;

import com.company.qa.pages.CommonPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TestPageElements extends CommonPage {

    @FindBy(css = "button")
    public static WebElement exampleButton;

}