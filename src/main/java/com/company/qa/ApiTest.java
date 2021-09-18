package com.company.qa;

import com.company.qa.utils.Configuration;
import com.company.qa.utils.DataUtil;
import io.cucumber.core.api.Scenario;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterClass;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
public class ApiTest {

    static {
        // Forces execution of static block in Configuration class
        Configuration.init();
        // Sleep to allow logger form previous tests to catch up
        sleep(25);
        log.info("API test");
    }

    public ApiTest() {
        // Unused yet
    }

    protected void beforeClass() {
        // Unused yet
    }

    @AfterClass
    protected void afterClass(Scenario scenario) {
        if (scenario.isFailed()) {
            final String timeStamp = new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(Calendar.getInstance().getTime());
            final String directoryName = scenario.getName().replace(" ", "_") + "_" + timeStamp;
            DataUtil.copyTo("temp", "dump/" + Configuration.getFailureDir() + "/" + directoryName);
        }
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log.error("Sleep error", e);
        }
    }

}