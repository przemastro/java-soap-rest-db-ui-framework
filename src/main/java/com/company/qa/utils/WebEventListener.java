package com.company.qa.utils;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;


@Slf4j
public class WebEventListener extends AbstractWebDriverEventListener {

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        log.info("Trying to navigate to: '" + url + "'");
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        log.info("Navigated to:'" + url + "'");
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        log.info("Trying to click on: " + element.toString());
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        log.info("Clicked on: " + element.toString());
    }

    @Override
    public void onException(Throwable error, WebDriver driver) {
        log.error("Selenium error occurred", error);
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        log.info("Trying to find Element " + by.toString());
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        log.info("Found Element " + by.toString());
    }

    public static byte[] takeScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}