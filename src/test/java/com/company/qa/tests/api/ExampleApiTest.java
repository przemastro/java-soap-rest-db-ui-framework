package com.company.qa.tests.api;

import com.company.qa.utils.Configuration;
import com.company.qa.tests.abstracts.AbstractApiTest;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = "html:target/html-report",
        features = Configuration.API_FEATURES_PATH + "ExampleApi.feature",
        tags = "@ApiTest")
public class ExampleApiTest extends AbstractApiTest {
}