package com.gfieast.qa.pages;

import com.gfieast.qa.UiTest;
import com.gfieast.qa.elements.CommonPageElements;
import com.gfieast.qa.utils.Configuration;
import com.gfieast.qa.utils.SeleniumUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.gfieast.qa.utils.Configuration.DEFAULT_WAIT_TIME;
import static com.gfieast.qa.utils.Configuration.driver;

@Slf4j
public class CommonPage extends CommonPageElements {

    protected WebDriver driver;

    private static final int WAIT_FOR_ELEMENT_CLICKABLE = 5;

    public CommonPage() {
        this.driver = Configuration.driver;
        PageFactory.initElements(driver, this);
    }

    protected void waitForSpinners() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
        List<WebElement> spinners = driver.findElements(By.cssSelector(".ui-table-loading-icon.pi-spin.pi.pi-spinner"));
        final long startTime = System.currentTimeMillis();
        for (WebElement spinner : spinners) {
            try {
                while (spinner.isDisplayed()) {
                    if (System.currentTimeMillis() - startTime > 10000) {
                        throw new RuntimeException("Spinners persisted for too long");
                    }
                    UiTest.sleep(100);
                }
            } catch (StaleElementReferenceException e) {
                log.error("Element error", e);
            }

        }
        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);

    }


    public void iClickCommonButton() {
        SeleniumUtil.click(commonButton);
    }


    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            log.error("got interr");
        }
    }

    public String verifyPageName(String pageTitle) {
        Assert.assertEquals(driver.getTitle(), pageTitle);
        return driver.getTitle();
    }
}