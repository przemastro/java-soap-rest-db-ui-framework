package com.gfieast.qa.tests.abstracts;


import com.gfieast.qa.utils.Configuration;
import com.gfieast.qa.utils.GeneratorUtil;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(plugin = "html:target/html-report")
public abstract class AbstractTest extends AbstractTestNGCucumberTests {

    static {
        Configuration.setFailureDir(GeneratorUtil.generateTimeStamp());
    }

    AbstractTest() {
        setup();
    }

    abstract void setup();

}