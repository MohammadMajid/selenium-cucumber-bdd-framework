package com.seleniumcucumberbddframework.utlis;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class DriverFactory {

    private static final String BROWSER_PROPERTY = "browser";
    private static final String EXECUTION_MODE_PROPERTY = "execution.mode";
    private static final String HEADLESS_PROPERTY = "headless";
    private static final String GRID_URL_PROPERTY = "grid.url";
    private static final String PLATFORM_NAME_PROPERTY = "platform.name";
    private static final String BROWSER_VERSION_PROPERTY = "browser.version";
    private static final String BROWSERSTACK_USERNAME_PROPERTY = "browserstack.username";
    private static final String BROWSERSTACK_ACCESS_KEY_PROPERTY = "browserstack.accessKey";
    private static final String BROWSERSTACK_URL_PROPERTY = "browserstack.url";

    public enum BrowserType {
        CHROME,
        FIREFOX,
        CLOUD_CHROME,
        CLOUD_FIREFOX,
        CLOUD_IE,
        GRID_CHROME,
        GRID_FIREFOX,
        GRID_IE
    }

    private static DriverFactory instance = null;

    public static final String DEFAULT_GRID_URL = "http://localhost:4444";
    public static final String DEFAULT_BROWSERSTACK_URL = "hub-cloud.browserstack.com/wd/hub";

    private DriverFactory() {
        //Do-nothing..Do not allow to initialize this class from outside
    }

    public static DriverFactory getInstance()
    {
        if(instance == null){
            instance = new DriverFactory();
        }
        return instance;
    }
    public static DriverFactory getInstance(String browserName)
    {
        System.out.println("Running browser: " + browserName);

        if(instance == null){
            instance = new DriverFactory();
        }

        instance.driver.set(instance.createWebDriver(browserName));
        return instance;
    }

    private WebDriver createWebDriver(String browserName) {
        if (browserName.equalsIgnoreCase(BrowserType.CHROME.toString())) {
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver(buildChromeOptions());
        } else if (browserName.equalsIgnoreCase(BrowserType.FIREFOX.toString())) {
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver(buildFirefoxOptions());
        } else if (browserName.equalsIgnoreCase(BrowserType.CLOUD_CHROME.toString())) {
            return createBrowserStackDriver("chrome");
        } else if (browserName.equalsIgnoreCase(BrowserType.CLOUD_FIREFOX.toString())) {
            return createBrowserStackDriver("firefox");
        } else if (browserName.equalsIgnoreCase(BrowserType.CLOUD_IE.toString())) {
            return createBrowserStackDriver("internet explorer");
        } else if (browserName.equalsIgnoreCase(BrowserType.GRID_CHROME.toString())) {
            return createGridDriver("chrome");
        } else if (browserName.equalsIgnoreCase(BrowserType.GRID_FIREFOX.toString())) {
            return createGridDriver("firefox");
        } else if (browserName.equalsIgnoreCase(BrowserType.GRID_IE.toString())) {
            return createGridDriver("internet explorer");
        }
        throw new IllegalArgumentException("Unsupported browser: " + browserName);
    }

    private static URL toUrl(String value) throws MalformedURLException {
        return URI.create(value).toURL();
    }

    private String resolveBrowserName() {
        String executionMode = System.getProperty(EXECUTION_MODE_PROPERTY, "local").trim().toLowerCase(Locale.ROOT);
        String browserName = System.getProperty(BROWSER_PROPERTY, BrowserType.CHROME.toString()).trim().toLowerCase(Locale.ROOT);

        if ("grid".equals(executionMode)) {
            if ("firefox".equals(browserName)) {
                return BrowserType.GRID_FIREFOX.toString();
            }
            if ("ie".equals(browserName) || "internet explorer".equals(browserName)) {
                return BrowserType.GRID_IE.toString();
            }
            return BrowserType.GRID_CHROME.toString();
        }

        if ("cloud".equals(executionMode) || "browserstack".equals(executionMode)) {
            if ("firefox".equals(browserName)) {
                return BrowserType.CLOUD_FIREFOX.toString();
            }
            if ("ie".equals(browserName) || "internet explorer".equals(browserName)) {
                return BrowserType.CLOUD_IE.toString();
            }
            return BrowserType.CLOUD_CHROME.toString();
        }

        if ("firefox".equals(browserName)) {
            return BrowserType.FIREFOX.toString();
        }
        return BrowserType.CHROME.toString();
    }

    private boolean isHeadlessEnabled() {
        return Boolean.parseBoolean(System.getProperty(HEADLESS_PROPERTY, "false"));
    }

    private ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        if (isHeadlessEnabled()) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        return options;
    }

    private FirefoxOptions buildFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();

        if (isHeadlessEnabled()) {
            options.addArguments("-headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        return options;
    }

    private WebDriver createGridDriver(String browserName) {
        try {
            return new RemoteWebDriver(toUrl(System.getProperty(GRID_URL_PROPERTY, DEFAULT_GRID_URL)), buildRemoteOptions(browserName));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Unable to create grid driver for browser: " + browserName, e);
        }
    }

    private WebDriver createBrowserStackDriver(String browserName) {
        String username = System.getProperty(BROWSERSTACK_USERNAME_PROPERTY, "").trim();
        String accessKey = System.getProperty(BROWSERSTACK_ACCESS_KEY_PROPERTY, "").trim();
        String browserStackUrl = System.getProperty(BROWSERSTACK_URL_PROPERTY, DEFAULT_BROWSERSTACK_URL).trim();

        if (username.isEmpty() || accessKey.isEmpty()) {
            throw new IllegalStateException("BrowserStack credentials are required. Set -Dbrowserstack.username and -Dbrowserstack.accessKey.");
        }

        String remoteUrl = "https://" + username + ":" + accessKey + "@" + browserStackUrl;

        try {
            return new RemoteWebDriver(toUrl(remoteUrl), buildRemoteOptions(browserName));
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Unable to create BrowserStack driver for browser: " + browserName, e);
        }
    }

    private org.openqa.selenium.MutableCapabilities buildRemoteOptions(String browserName) {
        if ("firefox".equalsIgnoreCase(browserName)) {
            FirefoxOptions options = buildFirefoxOptions();
            applyRemoteMetadata(options, "firefox");
            return options;
        }

        if ("internet explorer".equalsIgnoreCase(browserName) || "ie".equalsIgnoreCase(browserName)) {
            org.openqa.selenium.MutableCapabilities capabilities = new org.openqa.selenium.MutableCapabilities();
            capabilities.setCapability("browserName", "internet explorer");
            applyRemoteMetadata(capabilities, "internet explorer");
            return capabilities;
        }

        ChromeOptions options = buildChromeOptions();
        applyRemoteMetadata(options, "chrome");
        return options;
    }

    private void applyRemoteMetadata(org.openqa.selenium.MutableCapabilities capabilities, String browserName) {
        capabilities.setCapability("browserName", browserName);

        String platformName = System.getProperty(PLATFORM_NAME_PROPERTY, "").trim();
        if (!platformName.isEmpty()) {
            capabilities.setCapability("platformName", platformName);
        }

        String browserVersion = System.getProperty(BROWSER_VERSION_PROPERTY, "").trim();
        if (!browserVersion.isEmpty()) {
            capabilities.setCapability("browserVersion", browserVersion);
        }
    }

    ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() // thread local driver object for webdriver
    {
        @Override
        protected WebDriver initialValue()
        {
            return createWebDriver(resolveBrowserName());
        }
    };
    public WebDriver getDriver() // call this method to get the driver object and launch the browser
    {
        return driver.get();
    }
    public void removeDriver() // Quits the driver and closes the browser
    {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            currentDriver.quit();
        }
        driver.remove();
    }
}
