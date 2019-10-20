package com.gfieast.qa.tests.abstracts;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(glue = {"com/santander/qa/steps/api", "com/santander/qa/steps/db"})
public abstract class AbstractApiTest extends AbstractTest {

    protected void setup() {
    }

}
