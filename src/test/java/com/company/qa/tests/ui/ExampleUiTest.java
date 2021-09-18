package com.company.qa.tests.ui;

import com.company.qa.tests.abstracts.AbstractUiTest;
import com.company.qa.utils.Configuration;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = Configuration.UI_FEATURES_PATH + "ExampleUi.feature",
        plugin = "html:target/html-report",
        tags = "@UiTest")
public class ExampleUiTest extends AbstractUiTest {
}