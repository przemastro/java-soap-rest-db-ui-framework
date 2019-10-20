package com.gfieast.qa.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
public class Configuration {

    public static final String FEATURES_PATH = "src/test/java/com/gfieast/qa/features/";
    public static final String SQL_SCRIPTS_PATH = "src/test/resources/SQLScripts/";
    public static final String STEPS_PATH = "com/gfieast/qa/steps/";
    public static final String UI_FEATURES_PATH = FEATURES_PATH + "ui/";
    public static final String API_FEATURES_PATH = FEATURES_PATH + "api/";
    public static final int DEFAULT_WAIT_TIME = 15;
    public static EventFiringWebDriver driver;
    private static Map<String, Object> configurationData;
    private static HashSet<String> browserArguments;
    private static String failureDir;
    private static String login;
    private static String password;
    private static BrowserMobProxy proxy;
    private static boolean headless;
    private static boolean noProxy;
    private static String ssl;


    /* Reads configuration variables, sets up logger, creates directories. */
    static {
        try {
            configurationData = DataUtil.readYamlFromFile("src/test/resources/functional-automated-tests.yml");
        } catch (IOException e) {
            log.error("Could not read configuration", e);
        }
        setupConfiguration();
        Configuration.setupLogger();
        RestAssured.urlEncodingEnabled = false;

        if (!noProxy) {
            proxy = setupProxy();
        }

        //noinspection ResultOfMethodCallIgnored
        new File("temp").mkdir();
        //noinspection ResultOfMethodCallIgnored
        new File("dump").mkdir();

        /* Closes browser on all tests end */
        Runtime.getRuntime().addShutdownHook(
                new Thread("app-shutdown-hook") {
                    @Override
                    public void run() {
                        exitBrowser();
                    }
                }
        );
    }

    private Configuration() {
    }

    /**
     * Used to force execution of static code block.
     */
    public static void init() {
        // Empty on purpose
    }

    private static void setupLogger() {
        BasicConfigurator.configure();

        Logger logger4j = Logger.getRootLogger();
        logger4j.setLevel(Level.toLevel("INFO"));
        log.info("Logger setup finished");
    }

    /**
     * Reads configuration YAML and parses it.
     */
    private static void setupConfiguration() {
        log.info("Configuration setup");
        for (final String key : configurationData.keySet()) {
            String value = System.getProperty(key);
            value = value == null ? get(key) : value;
            set(key, value);
        }

        login = get("login");
        password = get("password");
        String browserOptions = get("browser_options");
        browserArguments = new HashSet<>(Arrays.asList(browserOptions.split("\\s*,\\s*")));
        headless = browserArguments.contains("headless");
        noProxy = browserArguments.contains("no-proxy") || headless;
    }

    private static BrowserMobProxy setupProxy() {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        final CaptureType[] captureTypes = {
                CaptureType.REQUEST_HEADERS,
                CaptureType.REQUEST_CONTENT,
                CaptureType.RESPONSE_HEADERS,
                CaptureType.RESPONSE_CONTENT
        };
        final HashSet<CaptureType> enable = new HashSet<>(Arrays.asList(captureTypes));
        proxy.enableHarCaptureTypes(enable);
        return proxy;
    }

    private static Proxy setupSeleniumProxy(BrowserMobProxy proxy) {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        final String proxyStr = "localhost:" + proxy.getPort();
        seleniumProxy.setHttpProxy(proxyStr)
                .setSslProxy(proxyStr);

        return seleniumProxy;
    }

    private static LoggingPreferences getDriverLoggingPrefs() {
        LoggingPreferences loggingPrefs = new LoggingPreferences();
        loggingPrefs.enable(LogType.BROWSER, java.util.logging.Level.ALL);
        return loggingPrefs;
    }

    private static WebDriver setupIEDriver() {
        WebDriverManager.iedriver().setup();

        InternetExplorerOptions options = new InternetExplorerOptions();
        options.setCapability(
                InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true
        );
        options.setCapability(CapabilityType.LOGGING_PREFS, getDriverLoggingPrefs());
        if (!noProxy) {
            options.setProxy(setupSeleniumProxy(proxy));
        }

        return new InternetExplorerDriver(options);
    }

    private static WebDriver setupFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(headless);
        options.setCapability(CapabilityType.LOGGING_PREFS, getDriverLoggingPrefs());
        if (!noProxy) {
            options.setProxy(setupSeleniumProxy(proxy));
        }

        return new FirefoxDriver(options);
    }

    private static WebDriver setupChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        /* Removes message about browser being run by automated test */
        options.setExperimentalOption(
                "excludeSwitches", Collections.singletonList("enable-automation")
        );
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-extensions");
        options.setHeadless(headless);
        options.setCapability("goog:loggingPrefs", getDriverLoggingPrefs());
        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        if (!noProxy) {
            options.setProxy(setupSeleniumProxy(proxy));
        }

        return new ChromeDriver(options);
    }

    public static void setupDriver() {
        log.info("Driver setup");
        if (driver != null) {
            log.info("Browser already set upped");
            return;
        }

        WebDriver seleniumDriver;

        switch (get("browser").toLowerCase()) {
            case "internet explorer":
            case "ie":
                seleniumDriver = setupIEDriver();
                break;
            case "firefox":
                seleniumDriver = setupFirefoxDriver();
                break;
            case "chrome":
            default:
                seleniumDriver = setupChromeDriver();
                break;
        }

        setupDriverListener(seleniumDriver);

        if (!noProxy) {
            proxy.newHar();
            proxy.start();
        }

        if (browserArguments.contains("maximize")) {
            driver.manage().window().maximize();
        }
    }

    private static void setupDriverListener(WebDriver seleniumDriver) {
        driver = new EventFiringWebDriver(seleniumDriver);
        WebEventListener eventListener = new WebEventListener();
        driver.register(eventListener);
        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);
    }

    private static void exitBrowser() {
        try {
            driver.manage().deleteAllCookies();
            driver.close();
            driver.quit();
        } catch (NoSuchSessionException e) {
            log.error("Driver closed error", e);
        }
    }

    /**
     * Gets value from configurationData.
     *
     * @param key Key from which value is to be taken.
     * @return Value of given key.
     */
    public static String get(String key) {
        return configurationData.get(key).toString();
    }

    /**
     * Sets value in configurationData.
     *
     * @param key   Key for which value is to be set.
     * @param value Value to be set.
     */
    private static void set(String key, String value) {
        if (configurationData.containsKey(key)) {
            configurationData.put(key, value);
        } else {
            log.info("Key does not exists in configuration!");
        }
    }

    public static String getFailureDir() {
        return failureDir;
    }

    public static void setFailureDir(String failureDir) {
        Configuration.failureDir = failureDir;
    }

    public static String getLogin() {
        return login;
    }

    public static String getPassword() {
        return password;
    }

    public static BrowserMobProxy getProxy() {
        return proxy;
    }

    public static String getSSL() {
        return ssl;
    }
}