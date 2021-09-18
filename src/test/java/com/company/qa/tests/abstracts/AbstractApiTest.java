package com.company.qa.tests.abstracts;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(glue = {"com/company/qa/steps/api", "com/company/qa/steps/db"})
public abstract class AbstractApiTest extends AbstractTest {

    protected void setup() {
    }

}
