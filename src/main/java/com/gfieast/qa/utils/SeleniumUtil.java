package com.gfieast.qa.utils;

import com.gfieast.qa.UiTest;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;
import static com.gfieast.qa.utils.Configuration.driver;

@Slf4j
public class SeleniumUtil {

    private SeleniumUtil() {
    }

    /**
     * Scrolls to element and clicks it.
     *
     * @param element Element to be clicked.
     */
    public static void click(WebElement element) {
        scrollToElement(element);
        element.click();
    }

    /**
     * Scrolls to element and double clicks it.
     *
     * @param element Element to be clicked.
     */
    public static void doubleClick(WebElement element) {
        scrollToElement(element);
        driver.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    /**
     * Scrolls to element.
     *
     * @param element Element to be clicked.
     */
    public static void scrollToElement(WebElement element) {
        driver.executeScript("arguments[0].scrollIntoView(true);", element);
        UiTest.sleep(250);
    }

    /**
     * Gets nth cell in table.
     *
     * @param table      Table to be searched in.
     * @param nthElement Index of element to be searched for.
     * @return Found element or null.
     */
    public static WebElement findTableData(WebElement table, int nthElement) {
        final WebElement tableBody = table.findElement(By.cssSelector("tBody"));
        return tableBody.findElement(By.cssSelector("tr:nth-of-type(" + nthElement + ")"));
    }

    /**
     * Finds table cell in given location and compares its content with expected value.
     *
     * @param table     Table to be searched in.
     * @param nthRow    Index of row to be searched for.
     * @param nthColumn Index of column to be searched in.
     * @param expected  Expected value.
     */
    public static void findTableDataAndCompare(WebElement table, int nthRow, int nthColumn, String expected) {
        String value = null;
        for (int failCount = 0; failCount < 20; failCount++) {
            try {
                final WebElement tableRow = findTableData(table, nthRow);
                final WebElement tableData = tableRow.findElement(By.cssSelector("td:nth-of-type(" + nthColumn + ")"));
                value = tableData.getText();
            } catch (Exception e) {
                log.error("Element error", e);
                continue;
            }
            if (value.equals(expected)) {
                break;
            }
            UiTest.sleep(50);
        }
        Assert.assertEquals(value, expected);
    }

    /**
     * Finds row with matching content.
     *
     * @param table   Table to be searched in.
     * @param rowText Content of searched row.
     * @return Found row or null.
     */
    private static WebElement matchRow(WebElement table, String rowText) {
        List<WebElement> rows = table.findElements(By.cssSelector("tBody tr"));
        for (WebElement row : rows) {
            try {
                log.info("Row text: " + row.getText());
                if (row.getText().contains(rowText)) {
                    log.info("Row matched");
                    return row;
                }
            } catch (Exception e) {
                log.error("Element error", e);
            }
        }
        return null;
    }

    /**
     * Finds row with matching content with 10 tries.
     *
     * @param table   Table to be searched in.
     * @param rowText Content of searched row.
     * @return Found row or null.
     */
    public static WebElement findTableRow(WebElement table, String rowText) {
        log.info("Searching for matching row for text: " + rowText);

        for (int failCount = 0; failCount < 10; failCount++) {
            WebElement matchedRow = matchRow(table, rowText);
            if (matchedRow != null) {
                return matchedRow;
            }
        }

        return null;
    }

    /**
     * Clears input and writes text in it.
     *
     * @param element Input to be cleared and written into.
     * @param text    Text to be written.
     */
    public static void clearAndSendKeys(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Counts rows in table.
     *
     * @param table Table in which rows will be counted.
     * @return Number of rows in table.
     */
    public static int countTableRows(WebElement table) {
        return table.findElements(By.cssSelector("tBody tr")).size();
    }


    public static WebElement waitForElementClickable(WebDriver driver, WebElement element, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Wait for element to be visible.
     *
     * @param driver Selenium WebDriver.
     * @param element Element to be clicked.
     * @param timeOutInSeconds Explicit wait.
     */
    public static WebElement waitForElementVisible(WebDriver driver, WebElement element, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be invisible.
     *
     * @param driver Selenium WebDriver.
     * @param element Element to be clicked.
     * @param timeOutInSeconds Explicit wait.
     */
    public static Boolean waitForElementInvisible(WebDriver driver, WebElement element, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Wait for element with defined text to be visible.
     *
     * @param driver Selenium WebDriver.
     * @param element Element to be clicked.
     * @param text Value of text to be validated.
     * @param timeOutInSeconds Explicit wait.
     */
    public static boolean waitForTextPresent(WebDriver driver, WebElement element, String text, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        return wait.until(ExpectedConditions.textToBePresentInElementValue(element, text));
    }

}

