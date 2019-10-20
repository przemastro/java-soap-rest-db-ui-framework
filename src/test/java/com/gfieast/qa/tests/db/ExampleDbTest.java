package com.gfieast.qa.tests.db;

import com.gfieast.qa.tests.abstracts.AbstractDbTest;
import com.gfieast.qa.utils.Configuration;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = "html:target/html-report",
        features = Configuration.API_FEATURES_PATH + "ExampleDb.feature",
        tags = "@DBTest")
public class ExampleDbTest extends AbstractDbTest {
}