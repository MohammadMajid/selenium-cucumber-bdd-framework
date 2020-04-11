package com.ableto.qa;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by Mohammad Majid on 4/11/2020
 */

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = { "@smoke" },         /* smoke test only */
        //tags = { "@smoke,@debug" }, /* debug or smoke test */
        //monochr ome = true,
        features = "src/test/resources/features/EditAndDeleteEployee.*.feature",
        glue = {"com.ableto.steps"},
        //dryRun = true,
        plugin={
                "pretty:target/cucumber-report/cucumber-pretty.txt",
                "html:target/cucumber-report",
                "json:target/cucumber-report/json/cucumber-report.json",
                "junit:target/cucumber-report/xml/test-report.xml"
        }
)
public class BDDRunner {
}
