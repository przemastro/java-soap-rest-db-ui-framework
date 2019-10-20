package com.gfieast.qa.tests.abstracts;

import com.gfieast.qa.utils.Configuration;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(glue = Configuration.STEPS_PATH)
public abstract class AbstractUiTest extends AbstractTest {

    protected void setup() {
        Configuration.setupDriver();
    }

}