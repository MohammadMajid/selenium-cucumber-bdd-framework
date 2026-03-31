package com.seleniumcucumberbddframework.hook;

import com.seleniumcucumberbddframework.utlis.AllureEnvironmentWriter;
import com.seleniumcucumberbddframework.utlis.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.time.Duration;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

public class BeforeAfterHook extends StepBase{
    @Before
    public void setup() {
        driver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver().manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
        AllureEnvironmentWriter.write(driver());
    }

    @After
    public void tearDown(){
        DriverFactory.getInstance().removeDriver();
    }
}
