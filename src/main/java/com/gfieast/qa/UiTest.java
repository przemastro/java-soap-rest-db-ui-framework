package com.gfieast.qa;

import com.gfieast.qa.utils.Configuration;
import com.gfieast.qa.utils.DataUtil;
import com.gfieast.qa.utils.GeneratorUtil;
import com.gfieast.qa.utils.WebEventListener;
import io.cucumber.core.api.Scenario;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.core.har.Har;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.gfieast.qa.utils.Configuration.driver;

@Slf4j
public class UiTest {

    protected UiTest() {
        initializeProperties();
    }

    private void initializeProperties() {
        Configuration.init();
        log.info("UI test");
    }

    protected void afterClass(Scenario scenario) throws IOException {
        if (driver != null && scenario.isFailed()) {
            final String timeStamp = GeneratorUtil.generateTimeStamp();
            final String fileName = scenario.getName().replace(" ", "_") + "_" + timeStamp;
            final String destinationDir = System.getProperty("user.dir") +
                    "/dump/" + Configuration.getFailureDir() + "/" + fileName;

            saveScreenshot(scenario, destinationDir);
            saveBrowserLogs(destinationDir);
            if (Configuration.getProxy() != null) {
                saveNetworkTraffic(destinationDir);
            }
        } else {
            Configuration.setupDriver();
        }
        driver.manage().deleteAllCookies();
    }

    private void saveScreenshot(Scenario scenario, String destinationDir) {
        log.info("Saving screenshot");

        try {
            final byte[] screenshot = WebEventListener.takeScreenshot(driver);

            if (screenshot.length == 0) {
                return;
            }

            FileUtils.writeByteArrayToFile(new File(destinationDir + ".png"), screenshot);
            Allure.addByteAttachmentAsync("Screenshot", "image/png", () -> screenshot);
            scenario.embed(screenshot, "Screenshot");
        } catch (IOException e) {
            log.error("Screenshot error", e);
        }
    }

    private void saveBrowserLogs(String destinationDir) throws IOException {
        final Logs logs = driver.manage().logs();
        final List<LogEntry> logEntries = logs.get(LogType.BROWSER).getAll();

        if (!logEntries.isEmpty()) {
            log.info("Saving browser logs");

            StringBuilder logsStr = new StringBuilder(logEntries.toString());

            /* Removes square brackets */
            logsStr.deleteCharAt(0);
            logsStr.deleteCharAt(logsStr.length() - 1);
            final String logsFinal = logsStr.toString().replace(", ", "\n");

            DataUtil.writeTextToFile(destinationDir + ".log", logsFinal);
        }
    }

    private void saveNetworkTraffic(String destinationDir) {
        final Har har = Configuration.getProxy().getHar();

        if (!har.getLog().getEntries().isEmpty()) {
            log.info("Saving network traffic");
            try {
                har.writeTo(new File(destinationDir + ".har"));
            } catch (IOException e) {
                log.error("Failed to write to file", e);
            } finally {
                Configuration.getProxy().newHar();
            }
        }
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error("got interrupted!");
        }
    }

}