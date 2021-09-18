package com.company.qa.steps.ui;

import com.company.qa.pages.CommonPage;
import com.company.qa.pages.TestPage;
import com.company.qa.utils.Configuration;
import com.company.qa.UiTest;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;

import java.io.IOException;


public class Steps extends UiTest {

    private TestPage testPage = new TestPage();
    private CommonPage commonPage = new CommonPage();

    @After
    public void afterScenario(Scenario scenario) throws IOException {
        afterClass(scenario);
    }

    @Given("Jestem na stronie głównej {string}")
    public void iAmOnMainPage(String pageName) {
        Configuration.driver.get(Configuration.get("frontend_url"));
        commonPage.verifyPageName(pageName);
    }

    @Given("Kliknę na przycisk customowy")
    public void iClickCustomButton() {
        testPage.iClickCustomButton();
    }

    @Given("Kliknę na przycisk commonowy")
    public void iClickCommonButton() {
        commonPage.iClickCommonButton();
    }

    @Given("Sprawdzę czy na pewno jestem na głównej stornie")
    public void iAmReallyOnMainPage() {
    }
}