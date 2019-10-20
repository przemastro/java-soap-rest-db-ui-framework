package com.gfieast.qa.tests.api;

import com.gfieast.qa.tests.abstracts.AbstractApiTest;
import com.gfieast.qa.utils.Configuration;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = "html:target/html-report",
        features = Configuration.API_FEATURES_PATH + "ExampleApi.feature",
        tags = "@ApiTest")
public class ExampleApiTest extends AbstractApiTest {
}