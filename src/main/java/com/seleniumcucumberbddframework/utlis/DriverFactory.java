package com.seleniumcucumberbddframework.utlis;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class DriverFactory {

    private static final String BROWSER_PROPERTY = "browser";
    private static final String HEADLESS_PROPERTY = "headless";

    public enum BrowserType{
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

    public static final String USERNAME = "";
    public static final String AUTOMATE_KEY = "";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";
    public static final String LOCAL_GRID_URL = "http://localhost:4444/wd/hub";

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
        if(browserName.equalsIgnoreCase(BrowserType.CHROME.toString())){
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver(buildChromeOptions());
        }
        else if(browserName.equalsIgnoreCase(BrowserType.FIREFOX.toString())){
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver(buildFirefoxOptions());
        }
        else if(browserName.equalsIgnoreCase(BrowserType.CLOUD_CHROME.toString())){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browser", "Chrome");
            caps.setCapability("browser_version", "64.0");
            caps.setCapability("os", "Windows");
            caps.setCapability("os_version", "7");
            caps.setCapability("resolution", "1920x1080");

            try {
                return new RemoteWebDriver(toUrl(URL), caps);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Unable to create BrowserStack Chrome driver", e);
            }
        } else if(browserName.equalsIgnoreCase(BrowserType.CLOUD_FIREFOX.toString())){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browser", "Firefox");
            caps.setCapability("browser_version", "64.0");
            caps.setCapability("os", "Windows");
            caps.setCapability("os_version", "7");
            caps.setCapability("resolution", "1920x1080");

            try {
                return new RemoteWebDriver(toUrl(URL), caps);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Unable to create BrowserStack Firefox driver", e);
            }
        }
        else if(browserName.equalsIgnoreCase(BrowserType.CLOUD_IE.toString())){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browser", "IE");
            caps.setCapability("browser_version", "11.0");
            caps.setCapability("os", "Windows");
            caps.setCapability("os_version", "7");
            caps.setCapability("resolution", "1920x1080");
            try {
                return new RemoteWebDriver(toUrl(URL), caps);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Unable to create BrowserStack IE driver", e);
            }
        }else if(browserName.equalsIgnoreCase(BrowserType.GRID_CHROME.toString())){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setPlatform(Platform.ANY);
            caps.setBrowserName("chrome");
            try {
                return new RemoteWebDriver(toUrl(LOCAL_GRID_URL), caps);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Unable to create grid Chrome driver", e);
            }
        }
        else if(browserName.equalsIgnoreCase(BrowserType.GRID_FIREFOX.toString())){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setPlatform(Platform.ANY);
            caps.setBrowserName("firefox");
            try {
                return new RemoteWebDriver(toUrl(LOCAL_GRID_URL), caps);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Unable to create grid Firefox driver", e);
            }
        }
        else if(browserName.equalsIgnoreCase(BrowserType.GRID_IE.toString())){
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setPlatform(Platform.ANY);
            caps.setBrowserName("internet explorer");
            try {
                return new RemoteWebDriver(toUrl(LOCAL_GRID_URL), caps);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Unable to create grid IE driver", e);
            }
        }
        throw new IllegalArgumentException("Unsupported browser: " + browserName);
    }

    private static URL toUrl(String value) throws MalformedURLException {
        return URI.create(value).toURL();
    }

    private String resolveBrowserName() {
        return System.getProperty(BROWSER_PROPERTY, BrowserType.CHROME.toString());
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
        driver.get().quit();
        driver.remove();
    }
}
