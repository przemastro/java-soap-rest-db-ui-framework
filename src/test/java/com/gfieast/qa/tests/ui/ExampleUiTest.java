package com.gfieast.qa.tests.ui;

import com.gfieast.qa.tests.abstracts.AbstractUiTest;
import com.gfieast.qa.utils.Configuration;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = Configuration.UI_FEATURES_PATH + "ExampleUi.feature",
        plugin = "html:target/html-report",
        tags = "@UiTest")
public class ExampleUiTest extends AbstractUiTest {
}