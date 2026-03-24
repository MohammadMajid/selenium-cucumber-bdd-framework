package com.seleniumcucumberbddframework.utlis;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

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
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrupted while waiting", e);
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

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(100));

        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeToWaitInSec))
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(NoSuchElementException.class);

        WebElement foo = wait.until(webDriver -> {
            WebElement element = webDriver.findElement(locator);
            if (element != null && element.isDisplayed()) {
                return element;
            }
            return null;
        });

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(DEFAULT_WAIT_TIME));
        return foo;
    }

    public WebElement textToBePresentInElementLocated(By by, String textToWait, int timeToWaitInSec){
        Boolean found = false;
        WebElement element = null;
        try {
            element = driver.findElement(by);
            found = fluentWait(timeToWaitInSec).until(ExpectedConditions.textToBePresentInElementLocated(by, textToWait));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        if(!found) {
            System.out.println("Element with the text '" + textToWait + "' not found");
            return  null;
        }

        return element;
    }



    public void waitForVisibilityOfElement(WebElement element){
        FluentWait<WebDriver> wait = fluentWait();
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    public void waitForVisibilityOfElement(By locator){
        WebElement element = driver.findElement(locator);
        waitForVisibilityOfElement(element);
    }

    public void waitForPageTitle(String title){
        FluentWait<WebDriver> wait = fluentWait();
        wait.until(ExpectedConditions.titleIs(title));
    }

    public void waitForPageTitleContains(String title){
        FluentWait<WebDriver> wait = fluentWait();
        wait.until(ExpectedConditions.titleContains(title));
    }

    public void waitForInvisibilityOfElement(By locator){
        FluentWait<WebDriver> wait = fluentWait();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForElementAttributeContains(WebElement element,String attributeName, String attributeValue){
        FluentWait<WebDriver> wait = fluentWait();
        wait.until(ExpectedConditions.attributeContains(element,attributeName,attributeValue));
    }
    public void waitForElementAttributeContains(By locator,String attributeName, String attributeValue){
        WebElement element = driver.findElement(locator);
        waitForElementAttributeContains(element,attributeName,attributeValue);
    }

    public void waitForElementTextToBe(By locator, String text){
        FluentWait<WebDriver> wait = fluentWait();
        wait.until(ExpectedConditions.textToBe(locator,text));
    }

    public FluentWait<WebDriver> fluentWait() {
        return fluentWait(DEFAULT_WAIT_TIME);
    }

    public FluentWait<WebDriver> fluentWait(int durationInSeconds) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(durationInSeconds))
                .pollingEvery(Duration.ofMillis(50))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .withMessage("Selenium TimeoutException");
    }


    public void scrollToElement(WebElement element){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        delayFor(3000);
    }

    public String getLastWindowHandle(){
        Set<String> winHdls = driver.getWindowHandles();
        return winHdls.toArray()[winHdls.size() - 1].toString();
    }

    public String getLastWIndowHandle(){
        return getLastWindowHandle();
    }

    public void switchToLastWindow(){
        driver.switchTo().window(getLastWindowHandle());
    }

    public void closeLastWindow(){
        switchToLastWindow();
        driver.close();
    }

    public void switchToWindow(String winTitle){
        switchToWindowMatching(
                webDriver -> webDriver.getTitle().contains(winTitle),
                "Window with the title contain '" + winTitle + "' was not found.");
    }

    public void switchToWindowByURL(String url){
        switchToWindowMatching(
                webDriver -> webDriver.getCurrentUrl().contains(url),
                "Window with the URL contain '" + url + "' was not found.");
    }

    public void switchToWindow(int winIndex){
        driver.switchTo().window(getWindowHandle(winIndex));
    }

    public void closeWindow(String title){
        switchToWindow(title);
        driver.close();
        switchToLastWindow();
    }

    public void closeWindow(int winIndex){
        switchToWindow(winIndex);
        driver.close();
        switchToLastWindow();
    }

    public void closeAllOpenWindowExceptCurrent(){
        String currentWindowHnd = driver.getWindowHandle();
        Set<String> windowList = driver.getWindowHandles();
        for(String window : windowList){
            if(!currentWindowHnd.contentEquals(window)){
                driver.switchTo().window(window);
                driver.close();
            }
        }
        driver.switchTo().window(currentWindowHnd);
    }

    private String getWindowHandle(int winIndex) {
        Set<String> winHdls = driver.getWindowHandles();

        if (winIndex >= 0 && winIndex < winHdls.size()) {
            return winHdls.toArray()[winIndex].toString();
        }

        throw new RuntimeException("Window with the index '" + winIndex + "' not found.");
    }

    private void switchToWindowMatching(Predicate<WebDriver> matcher, String errorMessage) {
        Set<String> winHdls = driver.getWindowHandles();
        Iterator<String> iterator = winHdls.iterator();
        while (iterator.hasNext()) {
            String win = iterator.next();
            driver.switchTo().window(win);
            if (matcher.test(driver)) {
                return;
            }
        }

        throw new RuntimeException(errorMessage);
    }
}
