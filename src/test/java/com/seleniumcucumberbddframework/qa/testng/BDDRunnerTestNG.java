package com.seleniumcucumberbddframework.qa.testng;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

@CucumberOptions(
        tags = "@smoke",
        //monochrome = true,
        features = "src/test/resources/features/EditAndDeleteEployee.feature",
        glue = {"com.seleniumcucumberbddframework"},
        //dryRun = true,
        plugin={
                "pretty:target/cucumber-results/testng/cucumber-pretty.txt",
                "html:target/cucumber-results/testng/cucumber-report.html",
                "json:target/cucumber-results/testng/cucumber-report.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        }
)
public class BDDRunnerTestNG extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
