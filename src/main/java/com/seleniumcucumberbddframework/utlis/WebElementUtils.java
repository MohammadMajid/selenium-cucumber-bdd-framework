package com.seleniumcucumberbddframework.utlis;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class WebElementUtils extends SeleniumUtils {

    public WebElementUtils() {
        super();
    }

    public void typeText(WebElement element, String text){
        highlight(element);
        element.clear();
        element.sendKeys(text);
    }

    public void click(WebElement element){
        highlight(element);
        element.click();
    }
    public void click(By by){
        WebElement element = waitForElementDisplayed(by,SeleniumUtils.DEFAULT_WAIT_TIME);
        click(element);
    }

    protected boolean isElementDisplayed(WebElement element, Integer timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }
}
