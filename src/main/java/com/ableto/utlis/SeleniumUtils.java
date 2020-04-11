package com.ableto.utlis;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.util.concurrent.TimeUnit;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class SeleniumUtils {

    public static final int DEFAULT_WAIT_TIME = 10;

    protected WebDriver driver = DriverFactory.getInstance().getDriver();

    public SeleniumUtils(){
    }

    public void delayFor(int timeInMili){
        try {
            Thread.sleep(timeInMili);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void highlight(WebElement element) {
        for (int i = 0; i < 2; i++) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "border: 2px solid red;");
            delayFor(200);
            js.executeScript(
                    "arguments[0].setAttribute('style', arguments[1]);",
                    element, "");
            delayFor(200);
        }
    }

    public WebElement waitForElementDisplayed(final By locator, int timeToWaitInSec) {

        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(timeToWaitInSec, TimeUnit.SECONDS)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);

        WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                WebElement element = driver.findElement(locator);
                if (element != null && element.isDisplayed()) {
                    return element;
                }
                return null;
            }
        });

        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIME,TimeUnit.MILLISECONDS);
        return foo;
    }
}
