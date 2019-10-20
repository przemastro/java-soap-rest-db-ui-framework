package com.gfieast.qa.tests.abstracts;


import io.cucumber.testng.CucumberOptions;

@CucumberOptions(glue = {"com/santander/qa/steps/db"})
public abstract class AbstractDbTest extends AbstractTest {

    protected void setup() {
    }

}