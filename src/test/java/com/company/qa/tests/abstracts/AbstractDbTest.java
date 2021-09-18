package com.company.qa.tests.abstracts;


import io.cucumber.testng.CucumberOptions;

@CucumberOptions(glue = {"com/company/qa/steps/db"})
public abstract class AbstractDbTest extends AbstractTest {

    protected void setup() {
    }

}