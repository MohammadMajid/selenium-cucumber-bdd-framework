package com.seleniumcucumberbddframework.qa.junit;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = "@smoke",         /* smoke test only */
        //tags = { "@smoke,@debug" }, /* debug or smoke test */
        //monochr ome = true,
        features = "src/test/resources/features/EditAndDeleteEployee.feature",
        glue = {"com.seleniumcucumberbddframework"},
        //dryRun = true,
        plugin={
                "pretty:target/cucumber-results/junit/cucumber-pretty.txt",
                "html:target/cucumber-results/junit/cucumber-report.html",
                "json:target/cucumber-results/junit/cucumber-report.json",
                "junit:target/cucumber-results/junit/test-report.xml",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
public class BDDRunnerJUnit {
}
